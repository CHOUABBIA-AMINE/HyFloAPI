/**
 * 
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingWorkflowService
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Extracted from FlowReadingService (SRP refactoring)
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Workflow
 * 
 * 	@Description: Slot coverage monitoring service - provides operational dashboard data
 */

package dz.sh.trc.hyflo.flow.workflow.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.ReadingSlotService;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.PipelineCoverageDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.monitoring.SlotCoverageResponseDTO;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.service.StructureService;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SlotCoverageService {
    
    private final PipelineRepository pipelineRepository;
    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotService readingSlotService;
    private final StructureService structureService;
    
    /**
     * Get complete slot coverage for a specific date, slot, and structure.
     * 
     * This is the MAIN API for the operational dashboard.
     * Returns all pipelines managed by the structure with their reading status.
     */
    public SlotCoverageResponseDTO getSlotCoverage(LocalDate date, Integer slotNumber, Long structureId) {
        log.info("Getting slot coverage for date={}, slot={}, structure={}", date, slotNumber, structureId);
        
        // Get structure details
        StructureDTO structureDTO = structureService.getById(structureId);
        
        // Get slot details
        ReadingSlotDTO slotDTO = readingSlotService.getById(slotNumber.longValue());
        
        // Get all pipelines for this structure
        List<Pipeline> pipelines = pipelineRepository.findByManagerId(structureId);
        
        if (pipelines.isEmpty()) {
            log.warn("No pipelines found for structure {}", structureId);
        }
        
        // Get all readings for this date/slot/structure
        List<FlowReading> readings = flowReadingRepository
            .findByReadingDateAndReadingSlotIdAndStructureId(date, slotNumber.longValue(), structureId);
        
        // Create a map of pipelineId -> reading for quick lookup
        Map<Long, FlowReading> readingMap = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getPipeline().getId(),
                reading -> reading,
                (existing, replacement) -> replacement // Keep last if duplicates
            ));
        
        // Build pipeline coverage items
        List<PipelineCoverageDTO> pipelineCoverage = pipelines.stream()
            .map(pipeline -> buildPipelineCoverageItem(pipeline, readingMap.get(pipeline.getId())))
            .collect(Collectors.toList());
        
        // Calculate statistics
        int totalPipelines = pipelineCoverage.size();
        int recordedCount = (int) pipelineCoverage.stream()
            .filter(p -> p.getReadingId() != null)
            .count();
        int submittedCount = (int) pipelineCoverage.stream()
            .filter(p -> p.getValidationStatus() != null && "SUBMITTED".equals(p.getValidationStatus().getCode()))
            .count();
        int approvedCount = (int) pipelineCoverage.stream()
            .filter(p -> p.getValidationStatus() != null && "APPROVED".equals(p.getValidationStatus().getCode()))
            .count();
        int rejectedCount = (int) pipelineCoverage.stream()
            .filter(p -> p.getValidationStatus() != null && "REJECTED".equals(p.getValidationStatus().getCode()))
            .count();
        int missingCount = totalPipelines - recordedCount;
        
        // Calculate completion percentages
        double recordingCompletion = totalPipelines > 0 ? (recordedCount * 100.0 / totalPipelines) : 0.0;
        double validationCompletion = totalPipelines > 0 ? ((approvedCount + rejectedCount) * 100.0 / totalPipelines) : 0.0;
        boolean isComplete = totalPipelines > 0 && totalPipelines == approvedCount;
        
        // Calculate slot deadline
        LocalDateTime slotDeadline = date.atTime(slotDTO.getEndTime());
        
        // Build response
        return SlotCoverageResponseDTO.builder()
            .readingDate(date)
            .slot(slotDTO)
            .structure(structureDTO)
            .generatedAt(LocalDateTime.now())
            .slotDeadline(slotDeadline)
            .totalPipelines(totalPipelines)
            .recordedCount(recordedCount)
            .submittedCount(submittedCount)
            .approvedCount(approvedCount)
            .rejectedCount(rejectedCount)
            .missingCount(missingCount)
            .recordingCompletionPercentage(recordingCompletion)
            .validationCompletionPercentage(validationCompletion)
            .isSlotComplete(isComplete)
            .pipelines(pipelineCoverage)
            .build();
    }
    
    /**
     * Build a single pipeline coverage item
     */
    private PipelineCoverageDTO buildPipelineCoverageItem(Pipeline pipeline, FlowReading reading) {
        PipelineCoverageDTO.PipelineCoverageDTOBuilder builder = PipelineCoverageDTO.builder()
            .pipelineId(pipeline.getId())
            .pipeline(convertPipelineToDTO(pipeline));
        
        if (reading != null) {
            builder
                .readingId(reading.getId())
                .validationStatus(convertValidationStatusToDTO(reading.getValidationStatus()))
                .recordedAt(reading.getRecordedAt())
                .validatedAt(reading.getValidatedAt())
                .recordedBy(convertEmployeeToDTO(reading.getRecordedBy()))
                .validatedBy(convertEmployeeToDTO(reading.getValidatedBy()));
            
            // Set permissions based on status
            String statusCode = reading.getValidationStatus() != null ? reading.getValidationStatus().getCode() : "DRAFT";
            builder
                .canEdit("DRAFT".equals(statusCode) || "REJECTED".equals(statusCode))
                .canSubmit("DRAFT".equals(statusCode))
                .canValidate("SUBMITTED".equals(statusCode))
                .isOverdue(false); // TODO: Calculate based on slot deadline
        } else {
            // No reading exists
            builder
                .canEdit(true)
                .canSubmit(false)
                .canValidate(false)
                .isOverdue(false);
        }
        
        return builder.build();
    }
    
    /**
     * Convert Pipeline entity to DTO
     */
    private PipelineDTO convertPipelineToDTO(Pipeline pipeline) {
        if (pipeline == null) return null;
        // TODO: Use proper mapper or service
        return PipelineDTO.builder()
            .id(pipeline.getId())
            .code(pipeline.getCode())
            .name(pipeline.getName())
            .build();
    }
    
    /**
     * Convert ValidationStatus entity to DTO
     */
    private ValidationStatusDTO convertValidationStatusToDTO(dz.sh.trc.hyflo.flow.common.model.ValidationStatus status) {
        if (status == null) return null;
        // TODO: Use proper mapper or service
        return ValidationStatusDTO.builder()
            .id(status.getId())
            .code(status.getCode())
            .build();
    }
    
    /**
     * Convert Employee entity to DTO
     */
    private EmployeeDTO convertEmployeeToDTO(dz.sh.trc.hyflo.general.organization.model.Employee employee) {
        if (employee == null) return null;
        // TODO: Use proper mapper or service
        return EmployeeDTO.builder()
            .id(employee.getId())
            .firstNameLt(employee.getFirstNameLt())
            .lastNameLt(employee.getLastNameLt())
            .build();
    }
}
