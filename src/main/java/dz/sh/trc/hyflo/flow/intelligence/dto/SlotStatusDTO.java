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
 * Detailed status of a single reading slot
 */
@Schema(description = "Status and data for a specific 2-hour reading slot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusDTO {
    
    // Slot metadata
    @Schema(description = "Slot identifier", example = "1")
    private Long slotId;
    
    @Schema(description = "Slot code", example = "SLOT_02")
    private String slotCode;
    
    @Schema(description = "Slot start time", example = "02:00:00")
    private LocalTime startTime;
    
    @Schema(description = "Slot end time", example = "04:00:00")
    private LocalTime endTime;
    
    @Schema(description = "Slot designation in French", example = "02h00 - 04h00")
    private String designation;
    
    @Schema(description = "Display order (1-12)", example = "2")
    private Integer displayOrder;
    
    // Recording status
    @Schema(description = "Reading record ID if exists", example = "1523")
    private Long readingId;
    
    @Schema(description = "Validation status", example = "APPROVED",
            allowableValues = {"NOT_RECORDED", "DRAFT", "SUBMITTED", "APPROVED", "REJECTED"})
    private String validationStatus;
    
    @Schema(description = "Timestamp when reading was recorded", example = "2026-02-07T03:45:00")
    private LocalDateTime recordedAt;
    
    @Schema(description = "Timestamp when reading was validated", example = "2026-02-07T09:15:00")
    private LocalDateTime validatedAt;
    
    // Operator information
    @Schema(description = "Name of operator who recorded reading", example = "Mohammed BENALI")
    private String recorderName;
    
    @Schema(description = "Name of validator who approved reading", example = "Fatima KHELIF")
    private String validatorName;
    
    // Measurement data
    @Schema(description = "Pressure reading in bar", example = "75.5")
    private BigDecimal pressure;
    
    @Schema(description = "Temperature reading in °C", example = "22.3")
    private BigDecimal temperature;
    
    @Schema(description = "Flow rate in m³/h", example = "1850.75")
    private BigDecimal flowRate;
    
    @Schema(description = "Contained volume in m³", example = "3700.50")
    private BigDecimal containedVolume;
    
    // Status flags
    @Schema(description = "Indicates if slot is past deadline without approved reading", example = "false")
    private Boolean isOverdue;
    
    @Schema(description = "Indicates if reading has warnings or notes", example = "false")
    private Boolean hasWarnings;
    
    @Schema(description = "Optional notes or observations", example = "Slight pressure fluctuation observed")
    private String notes;
}
