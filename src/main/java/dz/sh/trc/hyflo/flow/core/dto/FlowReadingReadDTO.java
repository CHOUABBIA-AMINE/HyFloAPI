/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 - Phase 2: removed fromEntity() — mapper owns mapping
 *                             Added readingSlot and workflow fields for full v2 query contract
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

import dz.sh.trc.hyflo.platform.kernel.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Read DTO for an operational flow reading")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowReadingReadDTO extends GenericDTO<FlowReading> {

    @Schema(description = "Date of the reading", example = "2026-03-20")
    private LocalDate readingDate;

    @Schema(description = "Measured flow volume in m\u00b3", example = "12500.5000")
    private BigDecimal volumeM3;

    @Schema(description = "Measured flow volume in MSCF", example = "441.4500")
    private BigDecimal volumeMscf;

    @Schema(description = "Inlet pressure in bar", example = "68.5000")
    private BigDecimal inletPressureBar;

    @Schema(description = "Outlet pressure in bar", example = "65.2000")
    private BigDecimal outletPressureBar;

    @Schema(description = "Temperature in Celsius", example = "22.3000")
    private BigDecimal temperatureCelsius;

    @Schema(description = "Operator notes")
    private String notes;

    @Schema(description = "Submission timestamp")
    private LocalDateTime submittedAt;

    @Schema(description = "Validation timestamp")
    private LocalDateTime validatedAt;

    @Schema(description = "ID of the parent pipeline")
    private Long pipelineId;

    @Schema(description = "Code of the parent pipeline", example = "GZ1-HASSI-ARZEW")
    private String pipelineCode;

    @Schema(description = "ID of the validation status")
    private Long validationStatusId;

    @Schema(description = "Code of the validation status", example = "APPROVED")
    private String validationStatusCode;

    @Schema(description = "ID of the reading slot")
    private Long readingSlotId;

    @Schema(description = "Code of the reading slot", example = "SLOT_06H")
    private String readingSlotCode;

    @Schema(description = "ID of the workflow instance")
    private Long workflowInstanceId;

    @Schema(description = "Current workflow state code", example = "SUBMITTED")
    private String workflowStateCode;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by FlowReadingMapper.

    @Override
    public FlowReading toEntity() {
        throw new UnsupportedOperationException("Use FlowReadingMapper for write operations");
    }

    @Override
    public void updateEntity(FlowReading entity) {
        throw new UnsupportedOperationException("Use FlowReadingMapper for update operations");
    }
}
