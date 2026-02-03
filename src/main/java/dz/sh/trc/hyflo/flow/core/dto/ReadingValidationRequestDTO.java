package dz.sh.trc.hyflo.flow.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingValidationRequestDTO {
    
	@NotNull(message = "Reading ID is required")
    private Long readingId;
    
    @NotNull(message = "Validation action is required")
    private String action; // "APPROVE" or "REJECT"
    
    @NotNull(message = "Validator employee ID is required")
    private Long employeeId; // ✅ ADDED: Employee performing validation
    
    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments; // Required if REJECT
}
