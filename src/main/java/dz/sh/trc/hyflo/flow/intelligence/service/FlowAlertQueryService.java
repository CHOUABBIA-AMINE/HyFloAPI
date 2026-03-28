/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertQueryService
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: converted from @Service class to interface
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAlertReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface FlowAlertQueryService {

    FlowAlertReadDTO findById(Long id);

    Page<FlowAlertReadDTO> findAll(Pageable pageable);

    List<FlowAlertReadDTO> findAll();

    List<FlowAlertReadDTO> findByThreshold(Long thresholdId);

    List<FlowAlertReadDTO> findByFlowReading(Long flowReadingId);

    List<FlowAlertReadDTO> findByStatus(Long statusId);

    List<FlowAlertReadDTO> findByTimeRange(LocalDateTime start, LocalDateTime end);

    List<FlowAlertReadDTO> findByNotificationSent(Boolean sent);

    Page<FlowAlertReadDTO> findUnacknowledged(Pageable pageable);

    Page<FlowAlertReadDTO> findUnresolved(Pageable pageable);

    Page<FlowAlertReadDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable);

    Page<FlowAlertReadDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<FlowAlertReadDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
