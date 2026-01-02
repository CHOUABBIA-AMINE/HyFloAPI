/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: LoginRequest
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Authentication
 *
 **/

package dz.sh.trc.hyflo.system.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
