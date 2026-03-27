/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationCommandDTO
 *  @CreatedOn  : Phase 5 — Commit 36
 *
 *  @Type       : Class
 *  @Layer      : DTO (Command)
 *  @Package    : Flow / Core / Command
 *
 *  @Description: Write-only DTO for FlowOperation command endpoints.
 *                Contains ONLY the fields that an operator supplies when
 *                recording or updating a hydrocarbon transport flow operation.
 *
 *                Intentional exclusions (vs FlowOperationDTO):
 *                  - No nested read DTOs (InfrastructureDTO, ProductDTO, etc.)
 *                  - No entity conversion methods (toEntity, updateEntity, fromEntity)
 *                  - validationStatusId  — managed by workflow engine, not operator
 *                  - validatedAt         — set by supervisor approval action
 *                  - validatedById       — set by supervisor approval action
 *                  - recordedAt          — set server-side at creation time
 *
 *                Used by:
 *                  FlowOperationCommandService.create(FlowOperationCommandDTO)     (Commit 37)
 *                  FlowOperationCommandService.update(Long, FlowOperationCommandDTO) (Commit 37)
 *
 *  Phase 5 — Commit 36
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Write-only command DTO for creating or updating a FlowOperation.
 *
 * Contains only the fields an operator provides when recording a
 * hydrocarbon transport flow operation. Workflow status, validator
 * identity, and server-managed timestamps are excluded.
 *
 * Phase 5 — Commit 36
 */
@Schema(description = "Command DTO for recording or updating a hydrocarbon transport flow operation. "
                    + "Write-only: contains only operator-supplied fields. "
                    + "Validation status and approval fields are managed by the workflow engine.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowOperationCommandDTO {

    @Schema(
        description = "Date of the flow operation. Must not be in the future.",
        example = "2026-03-25",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operation date is required")
    @PastOrPresent(message = "Operation date cannot be in the future")
    private LocalDate operationDate;

    @Schema(
        description = "Volume of product transported in cubic meters or barrels.",
        example = "25000.50",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    private BigDecimal volume;

    @Schema(
        description = "ID of the infrastructure (station, terminal, pipeline) where this operation occurred.",
        example = "101",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Infrastructure is required")
    private Long infrastructureId;

    @Schema(
        description = "ID of the hydrocarbon product type being transported.",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Product is required")
    private Long productId;

    @Schema(
        description = "ID of the operation type (production, transport, consumption, injection, etc.).",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operation type is required")
    private Long typeId;

    @Schema(
        description = "ID of the operator employee recording this operation.",
        example = "234",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Recording employee is required")
    private Long recordedById;

    @Schema(
        description = "Optional operational notes or observations.",
        example = "Normal operation, no anomalies detected.",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
