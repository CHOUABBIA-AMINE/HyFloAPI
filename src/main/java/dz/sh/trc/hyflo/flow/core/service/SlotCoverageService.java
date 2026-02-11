/**
 * @Author: CHOUABBIA Amine
 * @Name: SlotCoverageService
 * @CreatedOn: 2026-02-11
 * @Type: Service
 * @Layer: Service
 * @Package: Flow / Core
 * @Description: Slot coverage monitoring service - provides operational dashboard data
 */

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.CoverageSummaryDTO;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.dto.PipelineCoverageItemDTO;
import dz.sh.trc.hyflo.flow.core.dto.SlotCoverageDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
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
    private final FlowReadingService flowReadingService;
    
    /**
     * Get complete slot coverage for a specific date, slot, and structure.
     * 
     * This is the MAIN API for the operational dashboard.
     * Returns all pipelines managed by the structure with their reading status.
     */
    public SlotCoverageDTO getSlotCoverage(LocalDate date, Integer slotNumber, Long structureId) {
        log.info("Getting slot coverage for date={}, slot={}, structure={}", date, slotNumber, structureId);
        
        // Get all pipelines for this structure
        List<Pipeline> pipelines = pipelineRepository.findByStructureId(structureId);
        
        if (pipelines.isEmpty()) {
            log.warn("No pipelines found for structure {}", structureId);
        }
        
        // Get all readings for this date/slot/structure
        List<FlowReading> readings = flowReadingRepository
            .findByReadingDateAndReadingSlotIdAndStructureId(date, slotNumber, structureId);
        
        // Create a map of pipelineId -> reading for quick lookup
        Map<Long, FlowReading> readingMap = readings.stream()
            .collect(Collectors.toMap(
                reading -> reading.getPipeline().getId(),
                reading -> reading,
                (existing, replacement) -> replacement // Keep last if duplicates
            ));
        
        // Build pipeline coverage items
        List<PipelineCoverageItemDTO> pipelineCoverage = pipelines.stream()
            .map(pipeline -> buildPipelineCoverageItem(pipeline, readingMap.get(pipeline.getId())))
            .collect(Collectors.toList());
        
        // Calculate summary
        CoverageSummaryDTO summary = calculateSummary(pipelineCoverage);
        
        // Build response
        return SlotCoverageDTO.builder()
            .date(date)
            .slotNumber(slotNumber)
            .structureId(structureId)
            .structureName(pipelines.isEmpty() ? null : pipelines.get(0).getStructure().getName())
            .pipelineCoverage(pipelineCoverage)
            .summary(summary)
            .canCreateReadings(true)  // TODO: Implement permission logic
            .canEditReadings(true)
            .canSubmitReadings(true)
            .canApproveReadings(true)
            .canRejectReadings(true)
            .build();
    }
    
    /**
     * Build a single pipeline coverage item
     */
    private PipelineCoverageItemDTO buildPipelineCoverageItem(Pipeline pipeline, FlowReading reading) {
        String status;
        FlowReadingDTO readingDTO = null;
        
        if (reading == null) {
            status = "NOT_RECORDED";
        } else {
            // Convert reading to DTO
            readingDTO = flowReadingService.convertToDTO(reading);
            status = reading.getValidationStatus() != null 
                ? reading.getValidationStatus().getCode() 
                : "DRAFT";
        }
        
        return PipelineCoverageItemDTO.builder()
            .pipelineId(pipeline.getId())
            .pipelineCode(pipeline.getCode())
            .pipelineName(pipeline.getName())
            .productName(pipeline.getProduct() != null ? pipeline.getProduct().getName() : null)
            .status(status)
            .reading(readingDTO)
            .canCreate(status.equals("NOT_RECORDED"))
            .canEdit(status.equals("DRAFT") || status.equals("REJECTED"))
            .canSubmit(status.equals("DRAFT"))
            .canApprove(status.equals("SUBMITTED"))
            .canReject(status.equals("SUBMITTED"))
            .build();
    }
    
    /**
     * Calculate coverage summary statistics
     */
    private CoverageSummaryDTO calculateSummary(List<PipelineCoverageItemDTO> pipelineCoverage) {
        int total = pipelineCoverage.size();
        int notRecorded = 0;
        int draft = 0;
        int submitted = 0;
        int approved = 0;
        int rejected = 0;
        
        for (PipelineCoverageItemDTO item : pipelineCoverage) {
            switch (item.getStatus()) {
                case "NOT_RECORDED": notRecorded++; break;
                case "DRAFT": draft++; break;
                case "SUBMITTED": submitted++; break;
                case "APPROVED": approved++; break;
                case "REJECTED": rejected++; break;
            }
        }
        
        // Completion = (approved + submitted) / total
        double completionRate = total > 0 ? ((double)(approved + submitted) / total) * 100 : 0;
        
        return CoverageSummaryDTO.builder()
            .totalPipelines(total)
            .notRecorded(notRecorded)
            .draft(draft)
            .submitted(submitted)
            .approved(approved)
            .rejected(rejected)
            .completionRate(completionRate)
            .build();
    }
}
