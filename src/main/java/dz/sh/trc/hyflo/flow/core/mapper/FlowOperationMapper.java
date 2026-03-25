/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 *  Static utility mapper owning ALL conversion logic between
 *  FlowOperation entity and its v2 DTOs.
 *  DTOs are pure data carriers and contain no mapping methods.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowOperation;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;

/**
 * Static utility mapper for FlowOperation entity ↔ v2 DTOs.
 *
 * This class is the single owner of all FlowOperation mapping logic.
 * No DTO in the flow.core package may contain fromEntity() or toEntity()
 * calls for FlowOperation after Phase 3.
 */
public final class FlowOperationMapper {

    private FlowOperationMapper() {}

    // =====================================================================
    // entity → FlowOperationReadDto
    // =====================================================================

    /**
     * Map a {@link FlowOperation} entity to a v2 read DTO.
     * All FK projections are safe null-guarded.
     */
    public static FlowOperationReadDto toReadDto(FlowOperation entity) {
        if (entity == null) return null;

        return FlowOperationReadDto.builder()
                .id(entity.getId())
                .operationDate(entity.getOperationDate())
                .volume(entity.getVolume())
                .recordedAt(entity.getRecordedAt())
                .validatedAt(entity.getValidatedAt())
                .notes(entity.getNotes())

                .infrastructureId(entity.getInfrastructure() != null
                        ? entity.getInfrastructure().getId() : null)
                .infrastructureCode(entity.getInfrastructure() != null
                        ? entity.getInfrastructure().getCode() : null)

                .productId(entity.getProduct() != null
                        ? entity.getProduct().getId() : null)
                .productCode(entity.getProduct() != null
                        ? entity.getProduct().getCode() : null)

                .typeId(entity.getType() != null
                        ? entity.getType().getId() : null)
                .typeCode(entity.getType() != null
                        ? entity.getType().getCode() : null)

                .recordedById(entity.getRecordedBy() != null
                        ? entity.getRecordedBy().getId() : null)
                .validatedById(entity.getValidatedBy() != null
                        ? entity.getValidatedBy().getId() : null)

                .validationStatusId(entity.getValidationStatus() != null
                        ? entity.getValidationStatus().getId() : null)
                .validationStatusCode(entity.getValidationStatus() != null
                        ? entity.getValidationStatus().getCode() : null)

                .workflowInstanceId(entity.getWorkflowInstance() != null
                        ? entity.getWorkflowInstance().getId() : null)
                .workflowStateCode(entity.getWorkflowInstance() != null
                        && entity.getWorkflowInstance().getCurrentState() != null
                        ? entity.getWorkflowInstance().getCurrentState().getCode() : null)

                .build();
    }

    // =====================================================================
    // FlowOperationDTO (legacy command) → new FlowOperation entity
    // =====================================================================

    /**
     * Build a new {@link FlowOperation} entity from legacy command fields.
     * Used by {@code FlowOperationCommandServiceImpl.create()} to avoid
     * dependency on {@code FlowOperationDTO.toEntity()}.
     *
     * @param operationDate   date of the operation
     * @param volume          volume moved
     * @param notes           optional notes
     * @param infrastructureId FK to Infrastructure
     * @param productId        FK to Product
     * @param typeId           FK to OperationType
     * @param recordedById     FK to Employee (recorder)
     * @param validationStatusId FK to ValidationStatus
     */
    public static FlowOperation toNewEntity(
            java.time.LocalDate operationDate,
            java.math.BigDecimal volume,
            String notes,
            Long infrastructureId,
            Long productId,
            Long typeId,
            Long recordedById,
            Long validationStatusId) {

        FlowOperation entity = new FlowOperation();
        entity.setOperationDate(operationDate);
        entity.setVolume(volume);
        entity.setNotes(notes);
        entity.setRecordedAt(java.time.LocalDateTime.now());

        if (infrastructureId != null) {
            Infrastructure infra = new Infrastructure();
            infra.setId(infrastructureId);
            entity.setInfrastructure(infra);
        }
        if (productId != null) {
            Product product = new Product();
            product.setId(productId);
            entity.setProduct(product);
        }
        if (typeId != null) {
            OperationType type = new OperationType();
            type.setId(typeId);
            entity.setType(type);
        }
        if (recordedById != null) {
            Employee emp = new Employee();
            emp.setId(recordedById);
            entity.setRecordedBy(emp);
        }
        if (validationStatusId != null) {
            ValidationStatus status = new ValidationStatus();
            status.setId(validationStatusId);
            entity.setValidationStatus(status);
        }
        return entity;
    }
}
