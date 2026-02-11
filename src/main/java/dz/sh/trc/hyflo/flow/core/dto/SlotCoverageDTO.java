/**
 * @Author: CHOUABBIA Amine
 * @Name: SlotCoverageDTO
 * @CreatedOn: 2026-02-11
 * @Type: DTO
 * @Layer: DTO
 * @Package: Flow / Core
 * @Description: Complete slot coverage response with all pipelines and their reading status
 */

package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotCoverageDTO {
    
    private LocalDate date;
    private Integer slotNumber;
    private Long structureId;
    private String structureName;
    
    private List<PipelineCoverageItemDTO> pipelineCoverage;
    private CoverageSummaryDTO summary;
    
    // User permissions for this slot
    private Boolean canCreateReadings;
    private Boolean canEditReadings;
    private Boolean canSubmitReadings;
    private Boolean canApproveReadings;
    private Boolean canRejectReadings;
}
