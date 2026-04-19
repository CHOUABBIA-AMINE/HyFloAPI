package dz.sh.trc.hyflo.core.system.auth.service.implementation;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.system.auth.dto.request.ChangePasswordRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.request.LoginRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.request.RefreshTokenRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.request.RegisterRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.response.AuthResponse;
import dz.sh.trc.hyflo.core.system.auth.model.RefreshToken;
import dz.sh.trc.hyflo.core.system.auth.repository.RefreshTokenRepository;
import dz.sh.trc.hyflo.core.system.auth.service.AuthService;
import dz.sh.trc.hyflo.core.system.security.model.User;
import dz.sh.trc.hyflo.core.system.security.repository.UserRepository;
import dz.sh.trc.hyflo.exception.base.ErrorCode;
import dz.sh.trc.hyflo.exception.business.BusinessException;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import dz.sh.trc.hyflo.platform.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ReferenceResolver referenceResolver;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.debug("Authenticating user {}", request.username());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(user);
        
        // Invalidate old refresh token
        refreshTokenRepository.deleteByUser(user);
        
        // Create new refresh token
        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenString)
                .expiryDate(Instant.now().plusMillis(604800000L)) // 7 days
                .build();
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken(), "Bearer", 3600L);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.debug("Registering new user {}", request.username());
        
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.AUTH_USERNAME_ALREADY_EXISTS, "Username already in use", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.AUTH_EMAIL_ALREADY_EXISTS, "Email already in use", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .employee(referenceResolver.resolve(request.employeeId(), Employee.class))
                .build();

        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user);
        
        String refreshTokenString = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenString)
                .expiryDate(Instant.now().plusMillis(604800000L)) // 7 days
                .build();
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken(), "Bearer", 3600L);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.debug("Refreshing token");
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_REFRESH_TOKEN_INVALID, "Invalid refresh token", HttpStatus.UNAUTHORIZED));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BusinessException(ErrorCode.AUTH_TOKEN_EXPIRED, "Refresh token expired. Please sign in again", HttpStatus.UNAUTHORIZED);
        }

        User user = refreshToken.getUser();
        String accessToken = jwtUtil.generateAccessToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), "Bearer", 3600L);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        log.debug("Logging out user");
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> refreshTokenRepository.deleteByUser(token.getUser()));
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        log.debug("Changing password for user id {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_USER_NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS, "Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        
        refreshTokenRepository.deleteByUser(user);
    }
}
