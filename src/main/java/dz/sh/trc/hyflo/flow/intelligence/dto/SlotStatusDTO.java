/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: SlotStatusDTO
 * 	@CreatedOn	: 02-07-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO containing status and measurements for a specific reading slot
 */
@Schema(description = "Status and measurements for a specific reading slot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusDTO {
    
    @Schema(description = "Reading slot ID", example = "1")
    private Long slotId;
    
    @Schema(description = "Slot code", example = "SLOT-01")
    private String slotCode;
    
    @Schema(description = "Slot start time", example = "00:00")
    private LocalTime startTime;
    
    @Schema(description = "Slot end time", example = "02:00")
    private LocalTime endTime;
    
    @Schema(description = "Slot designation (multilingual)", example = "00:00 - 02:00")
    private String designation;
    
    @Schema(description = "Display order for UI sorting", example = "1")
    private Integer displayOrder;
    
    // Reading information
    @Schema(description = "Flow reading ID if recorded", example = "123")
    private Long readingId;
    
    @Schema(description = "Validation status", example = "APPROVED", 
            allowableValues = {"NOT_RECORDED", "DRAFT", "SUBMITTED", "APPROVED", "REJECTED"})
    private String validationStatus;
    
    @Schema(description = "Timestamp when reading was recorded", example = "2026-02-07T08:30:00")
    private LocalDateTime recordedAt;
    
    @Schema(description = "Timestamp when reading was validated", example = "2026-02-07T09:15:00")
    private LocalDateTime validatedAt;
    
    @Schema(description = "Name of employee who recorded the reading", example = "John Doe")
    private String recorderName;
    
    @Schema(description = "Name of employee who validated the reading", example = "Jane Smith")
    private String validatorName;
    
    // Measurements
    @Schema(description = "Pressure measurement in bar", example = "85.5")
    private BigDecimal pressure;
    
    @Schema(description = "Temperature measurement in Celsius", example = "45.2")
    private BigDecimal temperature;
    
    @Schema(description = "Flow rate in cubic meters per hour", example = "1875.0")
    private BigDecimal flowRate;
    
    @Schema(description = "Contained volume in cubic meters", example = "3750.0")
    private BigDecimal containedVolume;
    
    // Status flags
    @Schema(description = "Whether this slot is past deadline", example = "false")
    private Boolean isOverdue;
    
    @Schema(description = "Whether reading has business warnings", example = "false")
    private Boolean hasWarnings;
    
    @Schema(description = "Additional notes or warnings", example = "Pressure slightly above threshold")
    private String notes;
}
