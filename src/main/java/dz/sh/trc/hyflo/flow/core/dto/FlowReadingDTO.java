/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingDTO
 *  @CreatedOn  : 03-26-2026 — Backward-compatibility bridge
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : Flow / Core
 *
 *  @Description: Backward-compatible read DTO that still carries fromEntity()
 *                for the intelligence facade and dashboard DTOs that were
 *                written against this class before the v2 migration split it
 *                into FlowReadingReadDto + FlowReadingMapper.
 *
 *                Callers that already use FlowReadingReadDto via the mapper
 *                are the authoritative v2 path. This class is kept as a
 *                compilation bridge and should be migrated away from over time.
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Flow reading DTO (bridge — prefer FlowReadingReadDto for new code)")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowReadingDTO {

    @Schema(description = "Record identifier")
    private Long id;

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

    // ----------------------------------------------------------------
    //  fromEntity — kept for FlowReadingFacade and dashboard DTOs
    //  that predate the FlowReadingMapper v2 migration.
    // ----------------------------------------------------------------

    public static FlowReadingDTO fromEntity(FlowReading entity) {
        if (entity == null) return null;
        return FlowReadingDTO.builder()
                .id(entity.getId())
                .readingDate(entity.getReadingDate())
                .volumeM3(entity.getVolumeM3())
                .volumeMscf(entity.getVolumeMscf())
                .inletPressureBar(entity.getInletPressureBar())
                .outletPressureBar(entity.getOutletPressureBar())
                .temperatureCelsius(entity.getTemperatureCelsius())
                .notes(entity.getNotes())
                .submittedAt(entity.getSubmittedAt())
                .validatedAt(entity.getValidatedAt())
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .validationStatusId(entity.getValidationStatus() != null ? entity.getValidationStatus().getId() : null)
                .validationStatusCode(entity.getValidationStatus() != null ? entity.getValidationStatus().getCode() : null)
                .workflowInstanceId(entity.getWorkflowInstance() != null ? entity.getWorkflowInstance().getId() : null)
                .build();
    }
}
