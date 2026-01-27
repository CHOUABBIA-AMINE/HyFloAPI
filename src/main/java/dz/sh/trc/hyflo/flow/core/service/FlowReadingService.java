/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-27-2026 - Added validate and reject methods
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

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
import dz.sh.trc.hyflo.exception.EntityNotFoundException;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingService extends GenericService<FlowReading, FlowReadingDTO, Long> {

    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<FlowReading, Long> getRepository() {
        return flowReadingRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowReading";
    }

    @Override
    protected FlowReadingDTO toDTO(FlowReading entity) {
        return FlowReadingDTO.fromEntity(entity);
    }

    @Override
    protected FlowReading toEntity(FlowReadingDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowReadingDTO create(FlowReadingDTO dto) {
        log.info("Creating flow reading: pipelineId={}, recordedAt={}", 
                 dto.getPipelineId(), dto.getRecordedAt());
        
        if (flowReadingRepository.existsByPipelineIdAndRecordedAt(
                dto.getPipelineId(), dto.getRecordedAt())) {
            throw new BusinessValidationException(
                "Flow reading for this pipeline and timestamp already exists");
        }
        
        return super.create(dto);
    }

    public List<FlowReadingDTO> getAll() {
        log.debug("Getting all flow readings without pagination");
        return flowReadingRepository.findAll().stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByPipeline(Long pipelineId) {
        log.debug("Finding flow readings by pipeline id: {}", pipelineId);
        return flowReadingRepository.findByPipelineId(pipelineId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow readings by time range: {} to {}", startTime, endTime);
        return flowReadingRepository.findByRecordedAtBetween(startTime, endTime).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow readings by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return flowReadingRepository.findByPipelineIdAndRecordedAtBetween(
                pipelineId, startTime, endTime).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByValidationStatus(Long validationStatusId) {
        log.debug("Finding flow readings by validation status id: {}", validationStatusId);
        return flowReadingRepository.findByValidationStatusId(validationStatusId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FlowReadingDTO> findByPipelineAndTimeRangePaginated(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow readings (paginated) by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndTimeRange(
                pipelineId, startTime, endTime, p), pageable);
    }

    public Page<FlowReadingDTO> findByPipelineAndValidationStatus(
            Long pipelineId, Long statusId, Pageable pageable) {
        log.debug("Finding flow readings by pipeline {} and validation status {}", 
                  pipelineId, statusId);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndValidationStatus(
                pipelineId, statusId, p), pageable);
    }

    public Page<FlowReadingDTO> findLatestByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("Finding latest flow readings for pipeline: {}", pipelineId);
        return executeQuery(p -> flowReadingRepository.findLatestByPipeline(pipelineId, p), pageable);
    }

    // ========== VALIDATION WORKFLOW METHODS ==========

    /**
     * Validate a flow reading
     * Updates the reading status to VALIDATED and records validator information
     * 
     * @param id Reading ID
     * @param validatedById Employee ID of the validator
     * @return Updated reading DTO with VALIDATED status
     * @throws EntityNotFoundException if reading, status, or employee not found
     */
    @Transactional
    public FlowReadingDTO validate(Long id, Long validatedById) {
        log.info("Validating flow reading {} by employee {}", id, validatedById);
        
        // Find the reading
        FlowReading reading = flowReadingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Flow reading with ID %d not found", id)));
        
        // Find VALIDATED status
        ValidationStatus validatedStatus = validationStatusRepository.findByCode("VALIDATED")
                .orElseThrow(() -> new EntityNotFoundException(
                        "VALIDATED status not found in database. Please configure validation statuses."));
        
        // Find validator employee
        Employee validator = employeeRepository.findById(validatedById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Employee with ID %d not found", validatedById)));
        
        // Update reading
        reading.setValidationStatus(validatedStatus);
        reading.setValidatedBy(validator);
        reading.setValidatedAt(LocalDateTime.now());
        
        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Successfully validated flow reading {} by employee {} ({})", 
                 id, validator.getRegistrationNumber(), 
                 validator.getFirstNameLt() + " " + validator.getLastNameLt());
        
        return FlowReadingDTO.fromEntity(saved);
    }

    /**
     * Reject a flow reading
     * Updates the reading status to REJECTED and records rejection information
     * 
     * @param id Reading ID
     * @param rejectedById Employee ID of the rejector
     * @param rejectionReason Reason for rejection
     * @return Updated reading DTO with REJECTED status
     * @throws EntityNotFoundException if reading, status, or employee not found
     */
    @Transactional
    public FlowReadingDTO reject(Long id, Long rejectedById, String rejectionReason) {
        log.info("Rejecting flow reading {} by employee {} with reason: {}", 
                 id, rejectedById, rejectionReason);
        
        // Find the reading
        FlowReading reading = flowReadingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Flow reading with ID %d not found", id)));
        
        // Find REJECTED status
        ValidationStatus rejectedStatus = validationStatusRepository.findByCode("REJECTED")
                .orElseThrow(() -> new EntityNotFoundException(
                        "REJECTED status not found in database. Please configure validation statuses."));
        
        // Find rejector employee
        Employee rejector = employeeRepository.findById(rejectedById)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Employee with ID %d not found", rejectedById)));
        
        // Update reading
        reading.setValidationStatus(rejectedStatus);
        reading.setValidatedBy(rejector);
        reading.setValidatedAt(LocalDateTime.now());
        
        // Append rejection reason to notes
        String existingNotes = reading.getNotes();
        String rejectionNote = String.format("\n\n=== REJECTION ===\nRejected by: %s %s (%s)\nDate: %s\nReason: %s",
                rejector.getFirstNameLt(), 
                rejector.getLastNameLt(),
                rejector.getRegistrationNumber(),
                LocalDateTime.now(),
                rejectionReason);
        reading.setNotes(existingNotes != null ? existingNotes + rejectionNote : rejectionNote.trim());
        
        FlowReading saved = flowReadingRepository.save(reading);
        log.info("Successfully rejected flow reading {} by employee {} ({})", 
                 id, rejector.getRegistrationNumber(), 
                 rejector.getFirstNameLt() + " " + rejector.getLastNameLt());
        
        return FlowReadingDTO.fromEntity(saved);
    }
}
