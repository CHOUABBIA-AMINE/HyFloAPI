package dz.sh.trc.hyflo.flow.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotCoverageRequestDTO {
    
    @NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Date cannot be in future")
    private LocalDate readingDate;
    
    @NotNull(message = "Slot is required")
    private Long slotId;
    
    @NotNull(message = "Structure is required")
    private Long structureId;
    
    private String statusFilter; // "SUBMITTED", "APPROVED", etc.
    private Boolean showOnlyIncomplete;
}
