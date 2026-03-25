/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DerivedFlowReadingReadDto
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 - Phase 2: upgraded to full v2 query contract
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for derived flow readings anchored to pipeline segments")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DerivedFlowReadingReadDto {

    @Schema(description = "Technical identifier of the derived reading")
    private Long id;

    @Schema(description = "Reference date of this derived reading")
    private LocalDate readingDate;

    @Schema(description = "Derived pressure in bar")
    private BigDecimal pressure;

    @Schema(description = "Derived temperature in degrees Celsius")
    private BigDecimal temperature;

    @Schema(description = "Derived flow rate in m\u00b3/h or bpd")
    private BigDecimal flowRate;

    @Schema(description = "Derived contained volume in cubic meters")
    private BigDecimal containedVolume;

    @Schema(description = "Timestamp when this derived reading was calculated")
    private LocalDateTime calculatedAt;

    @Schema(description = "Calculation method or model used", example = "LINEAR_INTERPOLATION_V1")
    private String calculationMethod;

    // --- Provenance ---

    @Schema(description = "ID of the source raw FlowReading")
    private Long sourceReadingId;

    @Schema(description = "Reading date of the source raw FlowReading")
    private LocalDate sourceReadingDate;

    // --- Segment context ---

    @Schema(description = "ID of the pipeline segment")
    private Long pipelineSegmentId;

    @Schema(description = "Code of the pipeline segment", example = "GZ1-SEG-04")
    private String pipelineSegmentCode;

    // --- Reference data IDs ---

    @Schema(description = "ID of the validation status")
    private Long validationStatusId;

    @Schema(description = "Code of the validation status", example = "APPROVED")
    private String validationStatusCode;

    @Schema(description = "ID of the data source")
    private Long dataSourceId;

    @Schema(description = "ID of the reading slot")
    private Long readingSlotId;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by DerivedFlowReadingMapper.
}
