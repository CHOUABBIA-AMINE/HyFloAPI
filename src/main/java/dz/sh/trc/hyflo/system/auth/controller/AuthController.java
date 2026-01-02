/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Authentication
 *
 **/

package dz.sh.trc.hyflo.system.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.system.auth.dto.LoginRequest;
import dz.sh.trc.hyflo.system.auth.dto.LoginResponse;
import dz.sh.trc.hyflo.system.auth.dto.RegisterRequest;
import dz.sh.trc.hyflo.system.auth.dto.TokenRefreshRequest;
import dz.sh.trc.hyflo.system.auth.dto.TokenRefreshResponse;
import dz.sh.trc.hyflo.system.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        authenticationService.logout(authentication.getName());
        return ResponseEntity.ok().build();
    }
}
