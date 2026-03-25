/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: FlowReadingReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for exposing flow readings without leaking JPA entities.
 */
@Schema(description = "Read DTO for flow readings")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FlowReadingReadDto {

    @Schema(description = "Technical identifier of the reading")
    private Long id;

    @Schema(description = "Reading date")
    private LocalDate readingDate;

    @Schema(description = "Pressure measurement in bar")
    private BigDecimal pressure;

    @Schema(description = "Temperature measurement in degrees Celsius")
    private BigDecimal temperature;

    @Schema(description = "Flow rate in m³/h or bpd")
    private BigDecimal flowRate;

    @Schema(description = "Total contained volume in cubic meters")
    private BigDecimal containedVolume;

    @Schema(description = "Timestamp when this reading was recorded")
    private LocalDateTime recordedAt;

    @Schema(description = "Timestamp when this reading was validated")
    private LocalDateTime validatedAt;

    @Schema(description = "Additional notes about this reading")
    private String notes;

    @Schema(description = "Identifier of the employee who recorded this reading")
    private Long recordedById;

    @Schema(description = "Identifier of the employee who validated this reading")
    private Long validatedById;

    @Schema(description = "Identifier of the validation status reference")
    private Long validationStatusId;

    @Schema(description = "Identifier of the pipeline where this reading was taken")
    private Long pipelineId;

    @Schema(description = "Identifier of the reading slot reference")
    private Long readingSlotId;

    @Schema(description = "Identifier of the workflow instance governing this reading (if any)")
    private Long workflowInstanceId;
}
