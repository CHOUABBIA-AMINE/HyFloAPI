/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperationService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-31-2026 - Added validate and reject methods
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowOperationDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowOperation;
import dz.sh.trc.hyflo.flow.core.repository.FlowOperationRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowOperationService extends GenericService<FlowOperation, FlowOperationDTO, Long> {

    private final FlowOperationRepository flowOperationRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<FlowOperation, Long> getRepository() {
        return flowOperationRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowOperation";
    }

    @Override
    protected FlowOperationDTO toDTO(FlowOperation entity) {
        return FlowOperationDTO.fromEntity(entity);
    }

    @Override
    protected FlowOperation toEntity(FlowOperationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowOperation entity, FlowOperationDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowOperationDTO create(FlowOperationDTO dto) {
        log.info("Creating flow operation: date={}, infrastructureId={}, productId={}, typeId={}", 
                 dto.getOperationDate(), dto.getInfrastructureId(), dto.getProductId(), dto.getTypeId());
        
        if (flowOperationRepository.existsByOperationDateAndInfrastructureIdAndProductIdAndTypeId(
                dto.getOperationDate(), dto.getInfrastructureId(), dto.getProductId(), dto.getTypeId())) {
            throw new BusinessValidationException(
                "Flow operation for this date, infrastructure, product, and type combination already exists");
        }
        
        return super.create(dto);
    }

    public List<FlowOperationDTO> getAll() {
        log.debug("Getting all flow operations without pagination");
        return flowOperationRepository.findAll().stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowOperationDTO> findByDate(LocalDate date) {
        log.debug("Finding flow operations by date: {}", date);
        return flowOperationRepository.findByOperationDate(date).stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowOperationDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Finding flow operations by date range: {} to {}", startDate, endDate);
        return flowOperationRepository.findByOperationDateBetween(startDate, endDate).stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowOperationDTO> findByInfrastructure(Long infrastructureId) {
        log.debug("Finding flow operations by infrastructure id: {}", infrastructureId);
        return flowOperationRepository.findByInfrastructureId(infrastructureId).stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowOperationDTO> findByProduct(Long productId) {
        log.debug("Finding flow operations by product id: {}", productId);
        return flowOperationRepository.findByProductId(productId).stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowOperationDTO> findByType(Long typeId) {
        log.debug("Finding flow operations by type id: {}", typeId);
        return flowOperationRepository.findByTypeId(typeId).stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowOperationDTO> findByValidationStatus(Long validationStatusId) {
        log.debug("Finding flow operations by validation status id: {}", validationStatusId);
        return flowOperationRepository.findByValidationStatusId(validationStatusId).stream()
                .map(FlowOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FlowOperationDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow operations by infrastructure {} and date range: {} to {}", 
                  infrastructureId, startDate, endDate);
        return executeQuery(p -> flowOperationRepository.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, p), pageable);
    }

    public Page<FlowOperationDTO> findByProductAndTypeAndDateRange(
            Long productId, Long typeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow operations by product {}, type {} and date range: {} to {}", 
                  productId, typeId, startDate, endDate);
        return executeQuery(p -> flowOperationRepository.findByProductAndTypeAndDateRange(
                productId, typeId, startDate, endDate, p), pageable);
    }

    public Page<FlowOperationDTO> findByValidationStatusAndDateRange(
            Long statusId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow operations by validation status {} and date range: {} to {}", 
                  statusId, startDate, endDate);
        return executeQuery(p -> flowOperationRepository.findByValidationStatusAndDateRange(
                statusId, startDate, endDate, p), pageable);
    }

    /**
     * Validates a PENDING flow operation.
     * Changes status from PENDING to VALIDATED.
     * 
     * @param id Flow operation ID to validate
     * @param validatorId Employee ID performing the validation
     * @return Updated flow operation DTO
     * @throws ResourceNotFoundException if operation or validator not found
     * @throws BusinessValidationException if operation is not in PENDING status
     */
    @Transactional
    public FlowOperationDTO validate(Long id, Long validatorId) {
        log.info("Validating flow operation: id={}, validatorId={}", id, validatorId);
        
        // Fetch operation
        FlowOperation operation = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));
        
        // Check if operation is in PENDING status
        if (!"PENDING".equalsIgnoreCase(operation.getValidationStatus().getCode())) {
            throw new BusinessValidationException(
                String.format("Cannot validate operation in %s status. Only PENDING operations can be validated.",
                        operation.getValidationStatus().getCode()));
        }
        
        // Fetch validator employee
        Employee validator = employeeRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", validatorId));
        
        // Fetch VALIDATED status
        ValidationStatus validatedStatus = validationStatusRepository.findByCode("VALIDATED")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus", "VALIDATED"));
        
        // Update operation
        operation.setValidationStatus(validatedStatus);
        operation.setValidatedBy(validator);
        operation.setValidatedAt(LocalDateTime.now());
        
        // Save and return
        FlowOperation saved = flowOperationRepository.save(operation);
        log.info("Flow operation validated successfully: id={}", id);
        return FlowOperationDTO.fromEntity(saved);
    }

    /**
     * Rejects a PENDING flow operation.
     * Changes status from PENDING to REJECTED and adds rejection reason to notes.
     * 
     * @param id Flow operation ID to reject
     * @param validatorId Employee ID performing the rejection
     * @param rejectionReason Reason for rejection
     * @return Updated flow operation DTO
     * @throws ResourceNotFoundException if operation or validator not found
     * @throws BusinessValidationException if operation is not in PENDING status or reason is empty
     */
    @Transactional
    public FlowOperationDTO reject(Long id, Long validatorId, String rejectionReason) {
        log.info("Rejecting flow operation: id={}, validatorId={}", id, validatorId);
        
        // Validate rejection reason
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new BusinessValidationException("Rejection reason is mandatory");
        }
        
        // Fetch operation
        FlowOperation operation = flowOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlowOperation", id));
        
        // Check if operation is in PENDING status
        if (!"PENDING".equalsIgnoreCase(operation.getValidationStatus().getCode())) {
            throw new BusinessValidationException(
                String.format("Cannot reject operation in %s status. Only PENDING operations can be rejected.",
                        operation.getValidationStatus().getCode()));
        }
        
        // Fetch validator employee
        Employee validator = employeeRepository.findById(validatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", validatorId));
        
        // Fetch REJECTED status
        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus", "REJECTED"));
        
        // Update operation with rejection details
        operation.setValidationStatus(rejectedStatus);
        operation.setValidatedBy(validator);
        operation.setValidatedAt(LocalDateTime.now());
        
        // Append rejection reason to notes
        String updatedNotes = (operation.getNotes() != null ? operation.getNotes() + "\n\n" : "") +
                              "[REJECTED] " + rejectionReason;
        operation.setNotes(updatedNotes.length() > 500 ? updatedNotes.substring(0, 500) : updatedNotes);
        
        // Save and return
        FlowOperation saved = flowOperationRepository.save(operation);
        log.info("Flow operation rejected successfully: id={}", id);
        return FlowOperationDTO.fromEntity(saved);
    }
}
