package dz.sh.trc.hyflo.flow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineCoverageDTO {
    
    private Long pipelineId;
    private String pipelineCode;
    private String pipelineName;
    
    private Long readingId;
    private String workflowStatus; // "NOT_RECORDED", "DRAFT", "SUBMITTED", "APPROVED", "REJECTED"
    private String workflowStatusDisplay; // "Not Recorded", "Draft", etc.
    private LocalDateTime recordedAt;
    private LocalDateTime validatedAt;
    
    private String recordedByName;
    private String validatedByName;
    
    private List<String> availableActions; // ["EDIT", "SUBMIT"]
    
    private Boolean canEdit;
    private Boolean canSubmit;
    private Boolean canValidate;
    private Boolean isOverdue;
}
