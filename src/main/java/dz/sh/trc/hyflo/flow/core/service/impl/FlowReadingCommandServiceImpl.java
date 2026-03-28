/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingCommandServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Command Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Command implementation for FlowReading write operations.
 *                Uses Phase 2 FlowReadingMapper exclusively.
 *                No DTO.fromEntity() calls. No raw entity in return types.
 *
 *  Phase 3 — Commit 17
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowReadingCommandDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingCommandService;
import dz.sh.trc.hyflo.flow.workflow.service.ReadableTargetValidationService;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for FlowReading write operations.
 *
 * Uses Phase 2 FlowReadingMapper exclusively.
 * No DTO.fromEntity() calls. No raw entity in return types.
 *
 * Phase 3 — Commit 17
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowReadingCommandServiceImpl implements FlowReadingCommandService {

    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final PipelineRepository pipelineRepository;
    private final ReadableTargetValidationService readableTargetValidationService;

    @Override
    public FlowReadingReadDTO createReading(FlowReadingCommandDTO command) {
        log.info("Creating direct flow reading for pipeline ID: {}", command.getPipelineId());

        // Validate pipeline exists (network ownership)
        pipelineRepository.findById(command.getPipelineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pipeline not found: " + command.getPipelineId()));

        // Validate readable target if additional target specified
        readableTargetValidationService.validateCommandTarget(command);

        FlowReading entity = FlowReadingMapper.toEntity(command);

        FlowReading saved = flowReadingRepository.save(entity);
        log.debug("Saved FlowReading ID: {}", saved.getId());
        return FlowReadingMapper.toReadDTO(saved);
    }

    @Override
    public FlowReadingReadDTO updateReading(Long id, FlowReadingCommandDTO command) {
        log.info("Updating flow reading ID: {}", id);

        FlowReading existing = flowReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowReading not found: " + id));

        // Guard: cannot update an approved reading
        if (existing.getValidationStatus() != null
                && "APPROVED".equalsIgnoreCase(existing.getValidationStatus().getCode())) {
            throw new IllegalStateException(
                    "Cannot update an approved FlowReading (ID=" + id + "). Reject and resubmit instead.");
        }

        FlowReadingMapper.updateEntity(command, existing);
        FlowReading saved = flowReadingRepository.save(existing);
        return FlowReadingMapper.toReadDTO(saved);
    }

    @Override
    public void deleteReading(Long id) {
        log.info("Deleting flow reading ID: {}", id);

        FlowReading existing = flowReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowReading not found: " + id));

        if (existing.getValidationStatus() != null
                && "APPROVED".equalsIgnoreCase(existing.getValidationStatus().getCode())) {
            throw new IllegalStateException(
                    "Cannot delete an approved FlowReading (ID=" + id + ").");
        }

        flowReadingRepository.deleteById(id);
        log.info("Deleted FlowReading ID: {}", id);
    }

    @Override
    public FlowReadingReadDTO submitForWorkflow(Long id, Long submittedById) {
        log.info("Submitting FlowReading ID: {} to workflow by employee: {}", id, submittedById);

        FlowReading reading = flowReadingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowReading not found: " + id));

        Employee submitter = employeeRepository.findById(submittedById)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + submittedById));

        reading.setRecordedBy(submitter);
        reading.setSubmittedAt(LocalDateTime.now());

        // Transition validation status to SUBMITTED
        validationStatusRepository.findByCode("SUBMITTED").ifPresent(reading::setValidationStatus);

        FlowReading saved = flowReadingRepository.save(reading);
        log.info("FlowReading ID: {} submitted to workflow", saved.getId());
        return FlowReadingMapper.toReadDTO(saved);
    }
}
