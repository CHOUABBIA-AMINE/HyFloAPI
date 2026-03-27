/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationCommandServiceImpl
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : Phase 4/5 bridge — Commit 37
 *
 *  @Type       : Class
 *  @Layer      : Service (Command Implementation)
 *  @Package    : Flow / Core
 *
 *  Implementation of the FlowOperationCommandService contract.
 *  Uses FlowOperationMapper for all entity ↔ DTO conversions.
 *  No DTO.fromEntity() calls in this class.
 *
 *  Workflow logic (approve/reject) extracted from legacy
 *  FlowOperationService and correctly placed here.
 *
 *  Commit 26.2 — post-Phase 3 corrective
 *  Commit 37   — create/update migrated from FlowOperationDTO
 *                to FlowOperationCommandDTO.
 *                create() no longer reads validationStatusId from DTO:
 *                PENDING status is resolved internally via
 *                ValidationStatusRepository.findByCode("PENDING").
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowOperationCommandDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowOperationMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowOperation;
import dz.sh.trc.hyflo.flow.core.repository.FlowOperationRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowOperationCommandService;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.flow.workflow.repository.WorkflowInstanceRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command service implementation for {@link FlowOperation}.
 *
 * <h3>Mapping strategy</h3>
 * All entity ↔ DTO conversions go through {@link FlowOperationMapper}.
 * The legacy {@link dz.sh.trc.hyflo.flow.core.dto.FlowOperationDTO#fromEntity} is
 * never called here.
 *
 * <h3>Write invariants</h3>
 * <ul>
 *   <li>Uniqueness: date + infrastructure + product + type must be unique per create</li>
 *   <li>State guard: only PENDING operations may be updated or deleted</li>
 *   <li>Workflow update: approve/reject update the linked WorkflowInstance if present</li>
 *   <li>Status purity: PENDING status resolved from DB on create; never set from operator DTO</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowOperationCommandServiceImpl implements FlowOperationCommandService {

    private final FlowOperationRepository flowOperationRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkflowInstanceRepository workflowInstanceRepository;

    // =====================================================================
    // CREATE
    // =====================================================================

    @Override
    public FlowOperationReadDTO create(FlowOperationCommandDTO dto) {
        log.info("[FlowOperationCommandService] create: date={}, infraId={}, productId={}, typeId={}",
                dto.getOperationDate(), dto.getInfrastructureId(),
                dto.getProductId(), dto.getTypeId());

        if (flowOperationRepository.existsByOperationDateAndInfrastructureIdAndProductIdAndTypeId(
                dto.getOperationDate(), dto.getInfrastructureId(),
                dto.getProductId(), dto.getTypeId())) {
            throw new BusinessValidationException(
                    "Flow operation for this date, infrastructure, product, and type combination already exists");
        }

        // Resolve PENDING status server-side — never accepted from operator input
        ValidationStatus pendingStatus = validationStatusRepository.findByCode("PENDING")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus", "PENDING"));

        FlowOperation entity = FlowOperationMapper.toNewEntity(
                dto.getOperationDate(),
                dto.getVolume(),
                dto.getNotes(),
                dto.getInfrastructureId(),
                dto.getProductId(),
                dto.getTypeId(),
                dto.getRecordedById(),
                pendingStatus.getId());

        FlowOperation saved = flowOperationRepository.save(entity);
        log.info("[FlowOperationCommandService] created id={}", saved.getId());
        return FlowOperationMapper.toReadDTO(saved);
    }

    // =====================================================================
    // UPDATE
    // =====================================================================

    @Override
    public FlowOperationReadDTO update(Long id, FlowOperationCommandDTO dto) {
        log.info("[FlowOperationCommandService] update id={}", id);

        FlowOperation entity = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));

        guardPendingOnly(entity, "update");

        if (dto.getOperationDate() != null) entity.setOperationDate(dto.getOperationDate());
        if (dto.getVolume() != null)        entity.setVolume(dto.getVolume());
        if (dto.getNotes() != null)         entity.setNotes(dto.getNotes());

        // FK updates from command DTO (operator-controlled fields only)
        if (dto.getInfrastructureId() != null) {
            dz.sh.trc.hyflo.network.core.model.Infrastructure infra =
                new dz.sh.trc.hyflo.network.core.model.Infrastructure();
            infra.setId(dto.getInfrastructureId());
            entity.setInfrastructure(infra);
        }
        if (dto.getProductId() != null) {
            dz.sh.trc.hyflo.network.common.model.Product product =
                new dz.sh.trc.hyflo.network.common.model.Product();
            product.setId(dto.getProductId());
            entity.setProduct(product);
        }
        if (dto.getTypeId() != null) {
            dz.sh.trc.hyflo.flow.type.model.OperationType type =
                new dz.sh.trc.hyflo.flow.type.model.OperationType();
            type.setId(dto.getTypeId());
            entity.setType(type);
        }

        FlowOperation saved = flowOperationRepository.save(entity);
        return FlowOperationMapper.toReadDTO(saved);
    }

    // =====================================================================
    // DELETE
    // =====================================================================

    @Override
    public void delete(Long id) {
        log.info("[FlowOperationCommandService] delete id={}", id);

        FlowOperation entity = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));

        guardPendingOnly(entity, "delete");
        flowOperationRepository.delete(entity);
        log.info("[FlowOperationCommandService] deleted id={}", id);
    }

    // =====================================================================
    // APPROVE (validate)
    // =====================================================================

    @Override
    public FlowOperationReadDTO approve(Long id, Long validatorId) {
        log.info("[FlowOperationCommandService] approve id={}, validatorId={}", id, validatorId);

        FlowOperation operation = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));

        if (!"PENDING".equalsIgnoreCase(operation.getValidationStatus().getCode())) {
            throw new BusinessValidationException(
                    String.format("Cannot approve operation in %s status. Only PENDING operations can be approved.",
                            operation.getValidationStatus().getCode()));
        }

        Employee validator = employeeRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", validatorId));

        ValidationStatus approvedStatus = validationStatusRepository.findByCode("VALIDATED")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus", "VALIDATED"));

        operation.setValidationStatus(approvedStatus);
        operation.setValidatedBy(validator);
        operation.setValidatedAt(LocalDateTime.now());

        // Update linked WorkflowInstance if present
        if (operation.getWorkflowInstance() != null) {
            WorkflowInstance wf = operation.getWorkflowInstance();
            wf.setLastActor(validator);
            workflowInstanceRepository.save(wf);
        }

        FlowOperation saved = flowOperationRepository.save(operation);
        log.info("[FlowOperationCommandService] approved id={}", id);
        return FlowOperationMapper.toReadDTO(saved);
    }

    // =====================================================================
    // REJECT
    // =====================================================================

    @Override
    public FlowOperationReadDTO reject(Long id, Long validatorId, String rejectionReason) {
        log.info("[FlowOperationCommandService] reject id={}, validatorId={}", id, validatorId);

        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new BusinessValidationException("Rejection reason is mandatory");
        }

        FlowOperation operation = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));

        if (!"PENDING".equalsIgnoreCase(operation.getValidationStatus().getCode())) {
            throw new BusinessValidationException(
                    String.format("Cannot reject operation in %s status. Only PENDING operations can be rejected.",
                            operation.getValidationStatus().getCode()));
        }

        Employee validator = employeeRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", validatorId));

        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus", "REJECTED"));

        operation.setValidationStatus(rejectedStatus);
        operation.setValidatedBy(validator);
        operation.setValidatedAt(LocalDateTime.now());

        String updatedNotes = (operation.getNotes() != null ? operation.getNotes() + "\n\n" : "")
                + "[REJECTED] " + rejectionReason;
        operation.setNotes(updatedNotes.length() > 500 ? updatedNotes.substring(0, 500) : updatedNotes);

        // Update linked WorkflowInstance if present
        if (operation.getWorkflowInstance() != null) {
            WorkflowInstance wf = operation.getWorkflowInstance();
            wf.setLastActor(validator);
            wf.setComment(rejectionReason);
            workflowInstanceRepository.save(wf);
        }

        FlowOperation saved = flowOperationRepository.save(operation);
        log.info("[FlowOperationCommandService] rejected id={}", id);
        return FlowOperationMapper.toReadDTO(saved);
    }

    // =====================================================================
    // Internal guards
    // =====================================================================

    private void guardPendingOnly(FlowOperation operation, String action) {
        if (operation.getValidationStatus() == null
                || !"PENDING".equalsIgnoreCase(operation.getValidationStatus().getCode())) {
            String code = operation.getValidationStatus() != null
                    ? operation.getValidationStatus().getCode() : "null";
            throw new BusinessValidationException(
                    String.format("Cannot %s operation in %s status. Only PENDING operations can be %sed.",
                            action, code, action));
        }
    }
}
