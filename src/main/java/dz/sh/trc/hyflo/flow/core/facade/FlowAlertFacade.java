/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacade
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade (Service)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowAlertFacade from flow/intelligence.
 *                Provides cross-module read access to FlowAlert data
 *                without exposing flow/core entities outside their module.
 *
 *                Maps FlowAlert entities to FlowAlertFacadeDto.
 *                Owned by flow/core because it accesses flow/core repositories
 *                and entities.
 *
 *                Note: FlowAlert has no severity field — severityCode is
 *                mapped from the alert status.code as a best-effort projection.
 *                A dedicated severity field can be added to FlowAlert in a
 *                future schema iteration.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDto;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowAlertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade bean exposing FlowAlert data to flow/intelligence module.
 *
 * Registered as a Spring @Service so it can be injected as IFlowAlertFacade
 * into PipelineIntelligenceService.
 *
 * Placed in flow/core/facade because it owns FlowAlert repository access.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAlertFacade implements IFlowAlertFacade {

    private final FlowAlertRepository alertRepository;

    // ----------------------------------------------------------------
    //  IFlowAlertFacade contract
    // ----------------------------------------------------------------

    @Override
    public List<FlowAlertFacadeDto> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowAlertFacade.findByPipelineAndTimeRange pipelineId={}, start={}, end={}",
                pipelineId, start, end);
        return alertRepository
                .findByPipelineAndTimeRange(pipelineId, start, end, Pageable.unpaged())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FlowAlertFacadeDto> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("FlowAlertFacade.findUnresolvedByPipeline pipelineId={}", pipelineId);
        Page<FlowAlert> page = alertRepository.findUnresolvedByPipeline(pipelineId, pageable);
        List<FlowAlertFacadeDto> dtos = page.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public List<FlowAlertFacadeDto> findByThreshold(Long thresholdId) {
        log.debug("FlowAlertFacade.findByThreshold thresholdId={}", thresholdId);
        return alertRepository.findByThresholdId(thresholdId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowAlertFacadeDto> findByFlowReading(Long flowReadingId) {
        log.debug("FlowAlertFacade.findByFlowReading flowReadingId={}", flowReadingId);
        return alertRepository.findByFlowReadingId(flowReadingId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ================================================================
    //  PRIVATE MAPPING
    // ================================================================

    /**
     * Map FlowAlert entity to FlowAlertFacadeDto.
     *
     * severityCode: FlowAlert has no dedicated severity field.
     * We map status.code as severityCode for PipelineIntelligenceService
     * health computation (CRITICAL check). A future schema addition
     * of FlowAlert.severity would replace this mapping.
     */
    private FlowAlertFacadeDto toDto(FlowAlert alert) {
        return FlowAlertFacadeDto.builder()
                .id(alert.getId())
                .pipelineId(alert.getThreshold() != null && alert.getThreshold().getPipeline() != null
                        ? alert.getThreshold().getPipeline().getId() : null)
                .thresholdId(alert.getThreshold() != null ? alert.getThreshold().getId() : null)
                .flowReadingId(alert.getFlowReading() != null ? alert.getFlowReading().getId() : null)
                .alertTimestamp(alert.getAlertTimestamp())
                .severityCode(alert.getStatus() != null ? alert.getStatus().getCode() : null)
                .statusCode(alert.getStatus() != null ? alert.getStatus().getCode() : null)
                .message(alert.getMessage())
                .build();
    }
}
