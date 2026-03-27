/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdQueryService
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.service → flow.common.service
 *                             DTO import updated to flow.common.dto.FlowThresholdDTO
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.FlowThresholdDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Query contract for FlowThreshold read operations.
 * Returns only FlowThresholdDTO — never raw entities.
 */
public interface FlowThresholdQueryService {

    FlowThresholdDTO getThresholdById(Long id);
    Page<FlowThresholdDTO> getAllThresholds(int page, int size, String sortBy, String sortDirection);
    Page<FlowThresholdDTO> getActiveThresholds(int page, int size);
    List<FlowThresholdDTO> getThresholdsByPipelineId(Long pipelineId);
    Optional<FlowThresholdDTO> getActiveThresholdByPipelineId(Long pipelineId);
    List<FlowThresholdDTO> getActiveThresholdsByStructureId(Long structureId);
    List<FlowThresholdDTO> searchByPipelineCode(String pipelineCode);
    List<FlowThresholdDTO> searchByPipelineCodePattern(String pattern);
    List<FlowThresholdDTO> getAllActiveThresholds();
    List<FlowThresholdDTO> getAllInactiveThresholds();
    List<Long> getPipelinesWithoutThresholds();
    List<Long> getPipelinesWithoutActiveThresholds();
    long countPipelinesWithoutThresholds();
    long countActiveThresholds();
    boolean hasActiveThreshold(Long pipelineId);
}
