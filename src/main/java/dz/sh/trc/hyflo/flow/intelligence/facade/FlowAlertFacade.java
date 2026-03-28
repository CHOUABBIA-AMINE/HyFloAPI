/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacade
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — CRITICAL refactor: was impl class, now interface.
 *                             Impl moved to facade.impl.FlowAlertFacadeImpl
 *
 *  @Type       : Interface
 *  @Layer      : Facade
 *  @Package    : Flow / Intelligence / Facade
 *
 *  @Description: Cross-module facade interface for FlowAlert data access.
 *                Implemented by FlowAlertFacadeImpl in facade.impl.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDTO;

/**
 * Facade interface giving flow/intelligence read-only access to FlowAlert data.
 * Implemented by FlowAlertFacadeImpl in facade.impl.
 */
public interface FlowAlertFacade {

    List<FlowAlertFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end);

    Page<FlowAlertFacadeDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable);

    List<FlowAlertFacadeDTO> findByThreshold(Long thresholdId);

    List<FlowAlertFacadeDTO> findByFlowReading(Long flowReadingId);
}
