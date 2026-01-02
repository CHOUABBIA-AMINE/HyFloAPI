/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: RefreshTokenService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Authentication
 *
 **/

package dz.sh.trc.hyflo.system.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.system.auth.model.RefreshToken;
import dz.sh.trc.hyflo.system.auth.repository.RefreshTokenRepository;
import dz.sh.trc.hyflo.system.security.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user) {
    	RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(new RefreshToken());
    	    
	    refreshToken.setUser(user);
	    refreshToken.setToken(UUID.randomUUID().toString());
	    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));

	    return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return token;
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
