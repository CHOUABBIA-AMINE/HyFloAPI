package dz.sh.trc.hyflo.flow.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSubmitRequestDTO {
    
    private Long readingId; // null for new
    
    @NotNull(message = "Pipeline ID is required")
    private Long pipelineId;
    
    @NotNull(message = "Reading date is required")
    private LocalDate readingDate;
    
    @NotNull(message = "Slot ID is required")
    private Long slotId;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId; // ✅ ADDED: Operator recording the reading
    
    @NotNull(message = "Pressure is required")
    @PositiveOrZero(message = "Pressure must be positive or zero")
    private BigDecimal pressure;
    
    @NotNull(message = "Temperature is required")
    private BigDecimal temperature;
    
    @NotNull(message = "Flow rate is required")
    @PositiveOrZero(message = "Flow rate must be positive or zero")
    private BigDecimal flowRate;
    
    @PositiveOrZero(message = "Contained volume must be positive or zero")
    private BigDecimal containedVolume;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
    
    private Boolean submitImmediately; // true = SUBMITTED, false = DRAFT
}
