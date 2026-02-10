package dz.sh.trc.hyflo.flow.intelligence.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * View model DTO representing per-pipeline coverage status within a slot.
 * 
 * NOTE: Intentionally does NOT extend GenericDTO because:
 * - Represents an aggregated view/projection, not a persistent entity
 * - Combines data from FlowReading + Pipeline + ValidationStatus + Employee
 * - Read-only view for monitoring dashboard
 * 
 * PATTERN: Nested DTOs for all related entities
 * - Eliminates denormalized fields (pipelineCode, pipelineName strings)
 * - Provides full objects with translations
 * - Enables frontend to navigate/link to related entities
 * 
 * @see dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService#getSlotCoverage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Per-pipeline coverage status with workflow state and actions")
public class PipelineCoverageDTO {
    
    // ========== PIPELINE CONTEXT (ID + nested DTO) ==========
    
    @Schema(description = "Pipeline ID", example = "5")
    private Long pipelineId;
    
    @Schema(description = "Pipeline details (code, name, type, translations)")
    private PipelineDTO pipeline;
    
    // ========== READING CONTEXT (ID + nested DTO) ==========
    
    @Schema(description = "Flow reading ID (null if NOT_RECORDED)", example = "123")
    private Long readingId;
    
    @Schema(description = "Validation status details (code, translations)")
    private ValidationStatusDTO validationStatus;
    
    // ========== TIMESTAMPS ==========
    
    @Schema(description = "When reading was recorded", example = "2026-02-04T06:15:00")
    private LocalDateTime recordedAt;
    
    @Schema(description = "When reading was validated", example = "2026-02-04T08:30:00")
    private LocalDateTime validatedAt;
    
    // ========== ACTOR CONTEXT (nested DTOs) ==========
    
    @Schema(description = "Employee who recorded the reading")
    private EmployeeDTO recordedBy;
    
    @Schema(description = "Employee who validated the reading")
    private EmployeeDTO validatedBy;
    
    // ========== WORKFLOW STATE ==========
    
    @Schema(description = "Available workflow actions for current user", 
            example = "[\"EDIT\", \"SUBMIT\"]")
    private List<String> availableActions;
    
    @Schema(description = "Can current user edit this reading", example = "true")
    private Boolean canEdit;
    
    @Schema(description = "Can current user submit this reading", example = "true")
    private Boolean canSubmit;
    
    @Schema(description = "Can current user validate this reading", example = "false")
    private Boolean canValidate;
    
    @Schema(description = "Is this reading overdue for the slot deadline", example = "false")
    private Boolean isOverdue;
}
