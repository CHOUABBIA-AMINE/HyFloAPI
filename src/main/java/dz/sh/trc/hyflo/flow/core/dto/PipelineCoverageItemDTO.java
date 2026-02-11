/**
 * @Author: CHOUABBIA Amine
 * @Name: PipelineCoverageItemDTO
 * @CreatedOn: 2026-02-11
 * @Type: DTO
 * @Layer: DTO
 * @Package: Flow / Core
 * @Description: Individual pipeline coverage with reading status and permissions
 */

package dz.sh.trc.hyflo.flow.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineCoverageItemDTO {
    
    private Long pipelineId;
    private String pipelineCode;
    private String pipelineName;
    private String productName;
    
    // Reading status: NOT_RECORDED, DRAFT, SUBMITTED, APPROVED, REJECTED
    private String status;
    
    // The actual reading (null if NOT_RECORDED)
    private FlowReadingDTO reading;
    
    // Pipeline-level permissions
    private Boolean canCreate;
    private Boolean canEdit;
    private Boolean canSubmit;
    private Boolean canApprove;
    private Boolean canReject;
}
