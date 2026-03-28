/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacadeImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Intelligence / Facade / Impl
 *
 *  @Description: Implements FlowAlertFacade interface.
 *                Provides cross-module read access to FlowAlert data.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.FlowAlertFacade;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowAlert;
import dz.sh.trc.hyflo.flow.intelligence.repository.FlowAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAlertFacadeImpl implements FlowAlertFacade {

    private final FlowAlertRepository alertRepository;

    @Override
    public List<FlowAlertFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowAlertFacadeImpl.findByPipelineAndTimeRange pipelineId={}", pipelineId);
        return alertRepository
                .findByPipelineAndTimeRange(pipelineId, start, end, Pageable.unpaged())
                .getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FlowAlertFacadeDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("FlowAlertFacadeImpl.findUnresolvedByPipeline pipelineId={}", pipelineId);
        return alertRepository.findUnresolvedByPipeline(pipelineId, pageable).map(this::toDTO);
    }

    @Override
    public List<FlowAlertFacadeDTO> findByThreshold(Long thresholdId) {
        log.debug("FlowAlertFacadeImpl.findByThreshold thresholdId={}", thresholdId);
        return alertRepository.findByThresholdId(thresholdId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowAlertFacadeDTO> findByFlowReading(Long flowReadingId) {
        log.debug("FlowAlertFacadeImpl.findByFlowReading flowReadingId={}", flowReadingId);
        return alertRepository.findByFlowReadingId(flowReadingId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private FlowAlertFacadeDTO toDTO(FlowAlert alert) {
        String statusCode = alert.getStatus() != null ? alert.getStatus().getCode() : null;
        return FlowAlertFacadeDTO.builder()
                .id(alert.getId())
                .pipelineId(alert.getThreshold() != null
                        && alert.getThreshold().getPipeline() != null
                        ? alert.getThreshold().getPipeline().getId() : null)
                .thresholdId(alert.getThreshold() != null
                        ? alert.getThreshold().getId() : null)
                .flowReadingId(alert.getFlowReading() != null
                        ? alert.getFlowReading().getId() : null)
                .alertTimestamp(alert.getAlertTimestamp())
                .severityCode(statusCode)
                .statusCode(statusCode)
                .message(alert.getMessage())
                .build();
    }
}
