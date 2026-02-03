package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.exception.WorkflowException;
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.PipelineCoverageDTO;
import dz.sh.trc.hyflo.flow.core.dto.ReadingSubmitRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.ReadingValidationRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.SlotCoverageRequestDTO;
import dz.sh.trc.hyflo.flow.core.dto.SlotCoverageResponseDTO;
import dz.sh.trc.hyflo.flow.core.helper.ValidationStatusHelper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.repository.SlotCoverageProjection;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.repository.StructureRepository;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FlowReadingWorkflowService {
    
    private final FlowReadingRepository flowReadingRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final PipelineRepository pipelineRepository;
    private final ReadingSlotRepository readingSlotRepository;
    private final StructureRepository structureRepository;
    private final EmployeeRepository employeeRepository;
    
    // ❌ NO SecurityService needed
    
    /**
     * Get slot coverage - all pipelines with reading status
     */
    public SlotCoverageResponseDTO getSlotCoverage(SlotCoverageRequestDTO request) {
        
        // Query coverage
        List<SlotCoverageProjection> projections = flowReadingRepository
            .findSlotCoverage(
                request.getReadingDate(),
                request.getSlotId(),
                request.getStructureId()
            );
        
        // Load metadata
        Structure structure = structureRepository.findById(request.getStructureId())
            .orElseThrow(() -> new ResourceNotFoundException("Structure not found"));
        
        ReadingSlot slot = readingSlotRepository.findById(request.getSlotId())
            .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        
        // Build pipeline coverage list
        List<PipelineCoverageDTO> pipelines = projections.stream()
            .map(this::buildPipelineCoverage)
            .collect(Collectors.toList());
        
        // Calculate statistics by status code
        Map<String, Long> statusCounts = pipelines.stream()
            .collect(Collectors.groupingBy(
                PipelineCoverageDTO::getWorkflowStatus,
                Collectors.counting()
            ));
        
        int totalPipelines = pipelines.size();
        int missingCount = statusCounts.getOrDefault(ValidationStatusHelper.NOT_RECORDED, 0L).intValue();
        int recordedCount = totalPipelines - missingCount;
        
        return SlotCoverageResponseDTO.builder()
            .readingDate(request.getReadingDate())
            .slot(mapSlotToDTO(slot))
            .structure(mapStructureToDTO(structure))
            .totalPipelines(totalPipelines)
            .recordedCount(recordedCount)
            .submittedCount(statusCounts.getOrDefault(ValidationStatusHelper.SUBMITTED, 0L).intValue())
            .approvedCount(statusCounts.getOrDefault(ValidationStatusHelper.APPROVED, 0L).intValue())
            .rejectedCount(statusCounts.getOrDefault(ValidationStatusHelper.REJECTED, 0L).intValue())
            .missingCount(missingCount)
            .completionPercentage(calculateCompletion(recordedCount, totalPipelines))
            .isSlotComplete(flowReadingRepository.isSlotComplete(
                request.getReadingDate(), request.getSlotId(), request.getStructureId()))
            .pipelines(pipelines)
            .build();
    }
    
    /**
     * Submit or update reading
     */
    public void submitReading(ReadingSubmitRequestDTO request) {
        
        // ✅ Get employee from request
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found: " + request.getEmployeeId()));
        
        // Find or create reading
        FlowReading reading = findOrCreateReading(
            request.getPipelineId(),
            request.getReadingDate(),
            request.getSlotId(),
            employee
        );
        
        // Determine current and target status codes
        String currentStatusCode = ValidationStatusHelper.getStatusCode(reading);
        String targetStatusCode = Boolean.TRUE.equals(request.getSubmitImmediately()) ?
            ValidationStatusHelper.SUBMITTED : ValidationStatusHelper.DRAFT;
        
        // Update reading data
        reading.setPressure(request.getPressure());
        reading.setTemperature(request.getTemperature());
        reading.setFlowRate(request.getFlowRate());
        reading.setContainedVolume(request.getContainedVolume());
        reading.setNotes(request.getNotes());
        reading.setRecordedAt(LocalDateTime.now());
        reading.setRecordedBy(employee); // ✅ Set from request
        
        // Set validation status
        ValidationStatus status = validationStatusRepository
            .findByCode(targetStatusCode)
            .orElseThrow(() -> new IllegalStateException(
                "ValidationStatus '" + targetStatusCode + "' not configured"));
        
        reading.setValidationStatus(status);
        
        flowReadingRepository.save(reading);
        
        log.info("Reading {} transitioned from {} to {} by employee {}", 
            reading.getId(), currentStatusCode, targetStatusCode, employee.getRegistrationNumber());
    }
    
    /**
     * Validate reading (approve or reject)
     */
    public void validateReading(ReadingValidationRequestDTO request) {
        
        // ✅ Get employee from request
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found: " + request.getEmployeeId()));
        
        // Lock reading
        FlowReading reading = flowReadingRepository
            .findByIdForUpdate(request.getReadingId())
            .orElseThrow(() -> new ResourceNotFoundException("Reading not found"));
        
        // Check current state
        String currentStatusCode = ValidationStatusHelper.getStatusCode(reading);
        if (!ValidationStatusHelper.SUBMITTED.equals(currentStatusCode)) {
            throw new WorkflowException("Only submitted readings can be validated");
        }
        
        // Determine target status
        String targetStatusCode;
        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            targetStatusCode = ValidationStatusHelper.APPROVED;
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            targetStatusCode = ValidationStatusHelper.REJECTED;
        } else {
            throw new WorkflowException("Invalid action: " + request.getAction());
        }
        
        // Update validation status
        ValidationStatus status = validationStatusRepository
            .findByCode(targetStatusCode)
            .orElseThrow(() -> new IllegalStateException(
                "ValidationStatus '" + targetStatusCode + "' not configured"));
        
        reading.setValidationStatus(status);
        reading.setValidatedAt(LocalDateTime.now());
        reading.setValidatedBy(employee); // ✅ Set from request
        
        // For rejection, append reason to notes
        if (ValidationStatusHelper.REJECTED.equals(targetStatusCode)) {
            if (!StringUtils.hasText(request.getComments())) {
                throw new WorkflowException("Rejection reason is required");
            }
            String existingNotes = reading.getNotes() != null ? reading.getNotes() : "";
            reading.setNotes(existingNotes + "\n[REJECTED by " + 
                employee.getFirstNameLt() + " " + employee.getLastNameLt() + 
                "] " + request.getComments());
        }
        
        flowReadingRepository.save(reading);
        
        log.info("Reading {} validated: {} by employee {}", 
            reading.getId(), targetStatusCode, employee.getRegistrationNumber());
    }
    
    // === HELPER METHODS ===
    
    private PipelineCoverageDTO buildPipelineCoverage(SlotCoverageProjection proj) {
        
        String statusCode = proj.getValidationStatusCode();
        if (statusCode == null) {
            statusCode = ValidationStatusHelper.NOT_RECORDED;
        }
        
        // ❌ No role checking here - client determines available actions
        List<String> actions = new ArrayList<>();
        
        return PipelineCoverageDTO.builder()
            .pipelineId(proj.getPipelineId())
            .pipelineCode(proj.getPipelineCode())
            .pipelineName(proj.getPipelineName())
            .readingId(proj.getReadingId())
            .workflowStatus(statusCode)
            .workflowStatusDisplay(ValidationStatusHelper.getDisplayName(statusCode))
            .recordedAt(proj.getRecordedAt())
            .validatedAt(proj.getValidatedAt())
            .recordedByName(proj.getRecordedByName())
            .validatedByName(proj.getValidatedByName())
            .availableActions(actions) // Client determines this
            .canEdit(null) // Client determines this
            .canSubmit(null) // Client determines this
            .canValidate(null) // Client determines this
            .isOverdue(false)
            .build();
    }
    
    private FlowReading findOrCreateReading(
        Long pipelineId, 
        java.time.LocalDate date, 
        Long slotId,
        Employee employee
    ) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
            .orElseThrow(() -> new ResourceNotFoundException("Pipeline not found"));
        
        ReadingSlot slot = readingSlotRepository.findById(slotId)
            .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        
        return flowReadingRepository
            .findByPipelineAndDateAndSlot(pipelineId, date, slotId)
            .orElseGet(() -> {
                FlowReading newReading = new FlowReading();
                newReading.setPipeline(pipeline);
                newReading.setReadingDate(date);
                newReading.setReadingSlot(slot);
                newReading.setRecordedBy(employee);
                return newReading;
            });
    }
    
    private double calculateCompletion(int recorded, int total) {
        if (total == 0) return 0.0;
        return (recorded * 100.0) / total;
    }
    
    private ReadingSlotDTO mapSlotToDTO(ReadingSlot slot) {
        return ReadingSlotDTO.fromEntity(slot);
    }
    
    private StructureDTO mapStructureToDTO(Structure structure) {
        return StructureDTO.fromEntity(structure);
    }
}
