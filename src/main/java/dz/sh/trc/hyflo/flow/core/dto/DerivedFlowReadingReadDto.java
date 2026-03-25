/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: DerivedFlowReadingReadDto
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
 * Read DTO for exposing derived flow readings anchored to pipeline segments.
 */
@Schema(description = "Read DTO for derived flow readings")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DerivedFlowReadingReadDto {

    @Schema(description = "Technical identifier of the derived reading")
    private Long id;

    @Schema(description = "Reference date of this derived reading")
    private LocalDate readingDate;

    @Schema(description = "Derived pressure in bar")
    private BigDecimal pressure;

    @Schema(description = "Derived temperature in degrees Celsius")
    private BigDecimal temperature;

    @Schema(description = "Derived flow rate in m³/h or bpd")
    private BigDecimal flowRate;

    @Schema(description = "Derived contained volume in cubic meters")
    private BigDecimal containedVolume;

    @Schema(description = "Timestamp when this derived reading was calculated")
    private LocalDateTime calculatedAt;

    @Schema(description = "Identifier of the direct reading this derived reading is based on")
    private Long sourceReadingId;

    @Schema(description = "Identifier of the pipeline segment this reading is associated with")
    private Long pipelineSegmentId;

    @Schema(description = "Identifier of the validation status reference (if any)")
    private Long validationStatusId;

    @Schema(description = "Identifier of the quality flag reference (if any)")
    private Long qualityFlagId;

    @Schema(description = "Identifier of the data source reference (if any)")
    private Long dataSourceId;

    @Schema(description = "Identifier of the reading slot reference")
    private Long readingSlotId;
}
