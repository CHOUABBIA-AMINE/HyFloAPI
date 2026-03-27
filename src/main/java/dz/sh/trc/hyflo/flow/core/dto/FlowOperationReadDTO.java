/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationReadDTO
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Record / DTO
 *  @Layer      : DTO (Read / Query)
 *  @Package    : Flow / Core
 *
 *  v2 read DTO for FlowOperation.
 *  Pure data carrier — no mapping logic, no fromEntity, no toEntity.
 *  Mapping is owned by {@link dz.sh.trc.hyflo.flow.core.mapper.FlowOperationMapper}.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Read DTO for {@link dz.sh.trc.hyflo.flow.core.model.FlowOperation}.
 *
 * Used exclusively as a response type from v2 query services.
 * Write operations use the legacy {@link FlowOperationDTO} for
 * backward compatibility until Phase 4 controller migration.
 */
@Schema(description = "Flow operation read DTO — v2 query response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowOperationReadDTO {

    @Schema(description = "Unique identifier")
    private Long id;

    @Schema(description = "Date of the flow operation", example = "2026-01-22")
    private LocalDate operationDate;

    @Schema(description = "Volume of product moved (m³ or barrels)", example = "25000.50")
    private BigDecimal volume;

    @Schema(description = "Timestamp when this operation was recorded")
    private LocalDateTime recordedAt;

    @Schema(description = "Timestamp when this operation was validated")
    private LocalDateTime validatedAt;

    @Schema(description = "Notes or comments about this operation")
    private String notes;

    // --- FK projections ---

    @Schema(description = "Infrastructure ID where the operation occurred")
    private Long infrastructureId;

    @Schema(description = "Infrastructure code/name label")
    private String infrastructureCode;

    @Schema(description = "Product ID (crude oil, gas, condensate, etc.)")
    private Long productId;

    @Schema(description = "Product code label")
    private String productCode;

    @Schema(description = "Operation type ID")
    private Long typeId;

    @Schema(description = "Operation type code label")
    private String typeCode;

    @Schema(description = "Employee ID who recorded this operation")
    private Long recordedById;

    @Schema(description = "Employee ID who validated this operation")
    private Long validatedById;

    @Schema(description = "Current validation status ID")
    private Long validationStatusId;

    @Schema(description = "Current validation status code (PENDING, APPROVED, REJECTED)")
    private String validationStatusCode;

    @Schema(description = "Linked workflow instance ID (null if no workflow opened yet)")
    private Long workflowInstanceId;

    @Schema(description = "Current workflow state code (null if no workflow opened yet)")
    private String workflowStateCode;
}
