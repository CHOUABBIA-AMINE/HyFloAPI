package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View model DTO for slot coverage aggregation.
 * 
 * NOTE: Intentionally does NOT extend GenericDTO because:
 * - Represents an aggregated view across multiple entities
 * - Read-only response from monitoring queries
 * 
 * PATTERN: Uses nested DTOs for slot and structure context
 * 
 * @see dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService#getSlotCoverage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Slot coverage summary with per-pipeline status")
public class SlotCoverageResponseDTO {
    
    // ========== CONTEXT ==========
    
    @Schema(description = "Reading date", example = "2026-02-04")
    private LocalDate readingDate;
    
    @Schema(description = "Reading slot details (time range, translations)")
    private ReadingSlotDTO slot;
    
    @Schema(description = "Structure details (station/terminal info, translations)")
    private StructureDTO structure;
    
    // ========== METADATA ==========
    
    @Schema(description = "When this coverage snapshot was generated", example = "2026-02-04T15:30:00")
    private LocalDateTime generatedAt;
    
    @Schema(description = "Deadline for this slot (readingDate + slot.endTime)", example = "2026-02-04T08:00:00")
    private LocalDateTime slotDeadline;
    
    // ========== AGGREGATE STATISTICS ==========
    
    @Schema(description = "Total pipelines in this structure", example = "25")
    private Integer totalPipelines;
    
    @Schema(description = "Pipelines with any reading (DRAFT/SUBMITTED/APPROVED/REJECTED)", example = "23")
    private Integer recordedCount;
    
    @Schema(description = "Pipelines with submitted readings awaiting validation", example = "15")
    private Integer submittedCount;
    
    @Schema(description = "Pipelines with approved readings", example = "20")
    private Integer approvedCount;
    
    @Schema(description = "Pipelines with rejected readings", example = "3")
    private Integer rejectedCount;
    
    @Schema(description = "Pipelines without any reading", example = "2")
    private Integer missingCount;
    
    // ========== COMPLETION METRICS ==========
    
    @Schema(description = "Recording completion percentage (recorded/total)", example = "92.0")
    private Double recordingCompletionPercentage;
    
    @Schema(description = "Validation completion percentage ((approved+rejected)/total)", example = "92.0")
    private Double validationCompletionPercentage;
    
    @Schema(description = "Is slot fully complete (all pipelines APPROVED)", example = "false")
    private Boolean isSlotComplete;
    
    // ========== DETAIL DATA ==========
    
    @Schema(description = "Per-pipeline coverage details")
    private List<PipelineCoverageDTO> pipelines;
}
