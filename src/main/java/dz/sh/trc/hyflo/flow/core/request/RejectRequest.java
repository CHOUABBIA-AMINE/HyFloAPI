/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: RejectRequest
 * 	@CreatedOn	: 01-27-2026
 * 	@UpdatedOn	: 01-27-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Request DTO
 * 	@Package	: Flow / Core
 *
 * 	@Description: Request body for rejecting a flow reading
 *
 **/

package dz.sh.trc.hyflo.flow.core.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectRequest {
    
    @NotNull(message = "Rejected by ID is required")
    private Long rejectedById;
    
    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;
    
}