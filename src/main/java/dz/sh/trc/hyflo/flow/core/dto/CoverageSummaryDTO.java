/**
 * @Author: CHOUABBIA Amine
 * @Name: CoverageSummaryDTO
 * @CreatedOn: 2026-02-11
 * @Type: DTO
 * @Layer: DTO
 * @Package: Flow / Core
 * @Description: Coverage statistics summary
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
public class CoverageSummaryDTO {
    
    private Integer totalPipelines;
    private Integer notRecorded;
    private Integer draft;
    private Integer submitted;
    private Integer approved;
    private Integer rejected;
    
    // Completion percentage (0-100)
    private Double completionRate;
}
