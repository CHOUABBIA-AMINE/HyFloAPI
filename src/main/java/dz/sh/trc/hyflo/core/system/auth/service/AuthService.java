/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: Interface
 *	@Layer		: Service
 *	@Package	: Core / System / Auth
 *
 **/

package dz.sh.trc.hyflo.core.system.auth.service;

import dz.sh.trc.hyflo.core.system.auth.dto.request.ChangePasswordRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.request.LoginRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.request.RefreshTokenRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.request.RegisterRequest;
import dz.sh.trc.hyflo.core.system.auth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);

    void changePassword(Long userId, ChangePasswordRequest request);
}
