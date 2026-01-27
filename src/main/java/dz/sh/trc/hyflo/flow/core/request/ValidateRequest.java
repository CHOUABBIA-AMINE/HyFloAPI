/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: ValidateRequest
 * 	@CreatedOn	: 01-27-2026
 * 	@UpdatedOn	: 01-27-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Request DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: Request body for validating a flow reading
 *
 **/

package dz.sh.trc.hyflo.flow.core.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRequest {
    
    @NotNull(message = "Validated by ID is required")
    private Long validatedById;
    
}