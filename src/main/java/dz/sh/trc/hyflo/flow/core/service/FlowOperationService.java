/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationService
 *  @CreatedOn  : 01-23-2026
 *  @UpdatedOn  : 03-26-2026 — H3: Marked @Deprecated — superseded by FlowOperationCommandService / FlowOperationQueryService
 *
 *  @Type       : Class
 *  @Layer      : Service (LEGACY)
 *  @Package    : Flow / Core
 *
 *  @Deprecated : This monolithic service is superseded by the command/query split:
 *                - FlowOperationCommandService (writes)
 *                - FlowOperationQueryService (reads)
 *                Retained temporarily because FlowOperationController (v1) still injects it.
 *                Will be deleted when v1 controller is removed in a future phase.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowOperationMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowOperation;
import dz.sh.trc.hyflo.flow.core.repository.FlowOperationRepository;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @deprecated Superseded by {@link FlowOperationCommandService} and {@link FlowOperationQueryService}.
 *             This class is retained only for backward compatibility with {@code FlowOperationController} (v1).
 *             Do not use in new code. Schedule for removal when v1 controller is retired.
 */
@Deprecated(since = "v2", forRemoval = true)
@Service
@Transactional(readOnly = true)
@Slf4j
@SuppressWarnings({"squid:S1133", "java:S1133"})
public class FlowOperationService extends GenericService<FlowOperation, FlowOperationDTO, Long> {

    private final FlowOperationRepository flowOperationRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final EmployeeRepository employeeRepository;

    public FlowOperationService(
            FlowOperationRepository flowOperationRepository,
            ValidationStatusRepository validationStatusRepository,
            EmployeeRepository employeeRepository) {
        super(flowOperationRepository, FlowOperationMapper::toDto, FlowOperationMapper::toEntity, "FlowOperation");
        this.flowOperationRepository = flowOperationRepository;
        this.validationStatusRepository = validationStatusRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<FlowOperationDTO> findByDate(LocalDate date) {
        return flowOperationRepository.findByOperationDate(date)
                .stream().map(FlowOperationMapper::toDto).toList();
    }

    public List<FlowOperationDTO> findByDateRange(LocalDate start, LocalDate end) {
        return flowOperationRepository.findByOperationDateBetween(start, end)
                .stream().map(FlowOperationMapper::toDto).toList();
    }

    public List<FlowOperationDTO> findByInfrastructure(Long infrastructureId) {
        return flowOperationRepository.findByInfrastructureId(infrastructureId)
                .stream().map(FlowOperationMapper::toDto).toList();
    }

    public List<FlowOperationDTO> findByProduct(Long productId) {
        return flowOperationRepository.findByProductId(productId)
                .stream().map(FlowOperationMapper::toDto).toList();
    }

    public List<FlowOperationDTO> findByType(Long typeId) {
        return flowOperationRepository.findByOperationTypeId(typeId)
                .stream().map(FlowOperationMapper::toDto).toList();
    }

    public List<FlowOperationDTO> findByValidationStatus(Long statusId) {
        return flowOperationRepository.findByValidationStatusId(statusId)
                .stream().map(FlowOperationMapper::toDto).toList();
    }

    public Page<FlowOperationDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate start, LocalDate end, Pageable pageable) {
        return flowOperationRepository
                .findByInfrastructureIdAndOperationDateBetween(infrastructureId, start, end, pageable)
                .map(FlowOperationMapper::toDto);
    }

    @Transactional
    public FlowOperationDTO validate(Long id, Long validatorId) {
        FlowOperation operation = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation not found: " + id));
        Employee validator = employeeRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + validatorId));
        ValidationStatus validatedStatus = validationStatusRepository.findByCode("VALIDATED")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus VALIDATED not found"));
        operation.setValidationStatus(validatedStatus);
        operation.setValidatedBy(validator);
        return FlowOperationMapper.toDto(flowOperationRepository.save(operation));
    }

    @Transactional
    public FlowOperationDTO reject(Long id, Long validatorId, String rejectionReason) {
        FlowOperation operation = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation not found: " + id));
        Employee validator = employeeRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + validatorId));
        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus REJECTED not found"));
        operation.setValidationStatus(rejectedStatus);
        operation.setValidatedBy(validator);
        if (rejectionReason != null && !rejectionReason.isBlank()) {
            String note = "\n=== REJECTED: " + rejectionReason + " ===";
            operation.setNotes(operation.getNotes() != null ? operation.getNotes() + note : note.trim());
        }
        return FlowOperationMapper.toDto(flowOperationRepository.save(operation));
    }
}
