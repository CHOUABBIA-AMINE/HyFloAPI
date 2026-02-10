package dz.sh.trc.hyflo.flow.intelligence.dto.monitoring;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;

import java.time.LocalDate;

/**
 * Query DTO for requesting slot coverage data.
 * 
 * NOTE: Intentionally does NOT extend GenericDTO because:
 * - Represents query parameters, not a persistent entity
 * - Used to filter/aggregate data across multiple entities
 * 
 * PATTERN: Dual representation (ID + nested DTO)
 * - ID fields: Used by backend for queries
 * - Nested DTOs: Populated by backend for display in UI headers/breadcrumbs
 * 
 * REFACTORED: Moved from flow.core.dto.command to flow.intelligence.dto.monitoring
 * Rationale: This is an analytics/monitoring DTO, not a CRUD operation DTO.
 *           Belongs in intelligence module alongside other monitoring DTOs.
 * 
 * @see dz.sh.trc.hyflo.flow.intelligence.service.PipelineIntelligenceService
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request parameters for slot coverage query")
public class SlotCoverageRequestDTO {
    
    // ========== TEMPORAL CONTEXT ==========
    
    @NotNull(message = "Reading date is required")
    @PastOrPresent(message = "Date cannot be in future")
    @Schema(description = "Date to query coverage for", example = "2026-02-04", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate readingDate;
    
    // ========== SLOT CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Slot is required")
    @Schema(description = "Reading slot ID", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long slotId;
    
    @Schema(description = "Reading slot details (time range, translations) - populated by backend")
    private ReadingSlotDTO slot;
    
    // ========== STRUCTURE CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Structure is required")
    @Schema(description = "Structure (station/terminal) ID", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long structureId;
    
    @Schema(description = "Structure details (code, name, translations) - populated by backend")
    private StructureDTO structure;
    
    // ========== FILTERS ==========
    
    @Schema(description = "Filter by validation status", example = "SUBMITTED", 
            allowableValues = {"NOT_RECORDED", "DRAFT", "SUBMITTED", "APPROVED", "REJECTED"})
    private String statusFilter;
    
    @Schema(description = "Show only pipelines with incomplete readings", example = "false")
    private Boolean showOnlyIncomplete;
}
