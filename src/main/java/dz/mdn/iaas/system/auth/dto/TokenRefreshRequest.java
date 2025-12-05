/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: TokenRefreshRequest
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Authentication
 *
 **/

package dz.mdn.raas.system.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}
