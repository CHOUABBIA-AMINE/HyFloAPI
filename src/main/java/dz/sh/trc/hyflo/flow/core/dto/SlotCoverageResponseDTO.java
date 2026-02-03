package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDate;
import java.util.List;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotCoverageResponseDTO {
    
    private LocalDate readingDate;
    private ReadingSlotDTO slot;
    private StructureDTO structure;
    
    private Integer totalPipelines;
    private Integer recordedCount;
    private Integer submittedCount;
    private Integer approvedCount;
    private Integer rejectedCount;
    private Integer missingCount;
    
    private Double completionPercentage;
    private Boolean isSlotComplete;
    
    private List<PipelineCoverageDTO> pipelines;
}
