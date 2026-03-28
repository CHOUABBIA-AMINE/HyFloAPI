/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanQueryService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Description: Query contract for FlowPlan read operations.
 *                All methods return FlowPlanReadDTO or Page/List thereof.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.core.dto.FlowPlanReadDTO;

/**
 * Query contract for FlowPlan read operations.
 */
public interface FlowPlanQueryService {

    FlowPlanReadDTO getById(Long id);

    Page<FlowPlanReadDTO> getAll(Pageable pageable);

    List<FlowPlanReadDTO> getByPipeline(Long pipelineId);

    Page<FlowPlanReadDTO> getByPipelinePaged(Long pipelineId, Pageable pageable);

    List<FlowPlanReadDTO> getByPipelineAndDate(Long pipelineId, LocalDate date);

    List<FlowPlanReadDTO> getByPipelineAndDateRange(Long pipelineId, LocalDate from, LocalDate to);

    Page<FlowPlanReadDTO> getApprovedByPipeline(Long pipelineId, Pageable pageable);

    Optional<FlowPlanReadDTO> getLatestApproved(Long pipelineId, LocalDate date);

    Page<FlowPlanReadDTO> getByScenario(String scenarioCode, Pageable pageable);
}
