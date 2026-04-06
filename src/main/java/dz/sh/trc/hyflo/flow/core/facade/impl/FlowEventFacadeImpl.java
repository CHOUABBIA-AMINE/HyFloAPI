/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacadeImpl
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: renamed from FlowEventFacade, moved to facade.impl,
 *                             implements facade.FlowEventFacade interface
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Core / Facade / Impl
 *
 *  @Description: Implements FlowEventFacade interface.
 *                Provides cross-module read access to FlowEvent data.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.facade.FlowEventFacade;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.intelligence.dto.facade.FlowEventFacadeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventFacadeImpl implements FlowEventFacade {

    private final FlowEventRepository eventRepository;

    @Override
    public List<FlowEventFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowEventFacadeImpl.findByPipelineAndTimeRange pipelineId={}", pipelineId);
        return eventRepository
                .findByInfrastructureIdAndEventTimestampBetween(pipelineId, start, end)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowEventFacadeDTO> findByFlowReading(Long flowReadingId) {
        log.debug("FlowEventFacadeImpl.findByFlowReading flowReadingId={}", flowReadingId);
        return eventRepository.findByRelatedReadingId(flowReadingId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private FlowEventFacadeDTO toDTO(FlowEvent event) {
        return FlowEventFacadeDTO.builder()
                .id(event.getId())
                .pipelineId(event.getInfrastructure() != null
                        ? event.getInfrastructure().getId() : null)
                .flowReadingId(event.getRelatedReading() != null
                        ? event.getRelatedReading().getId() : null)
                .eventTimestamp(event.getEventTimestamp())
                .severityCode(event.getSeverity() != null
                        ? event.getSeverity().getDesignationFr() : null)
                .eventTypeCode(event.getStatus() != null
                        ? event.getStatus().getDesignationFr() : null)
                .description(event.getDescription())
                .build();
    }
}
