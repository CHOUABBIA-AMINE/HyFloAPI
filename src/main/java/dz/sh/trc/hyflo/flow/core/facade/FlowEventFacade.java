/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacade
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade (Service)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowEventFacade from flow/intelligence.
 *                Provides cross-module read access to FlowEvent data
 *                without exposing flow/core entities outside their module.
 *
 *                Maps FlowEvent entities to FlowEventFacadeDto.
 *                Owned by flow/core because it accesses flow/core repositories
 *                and entities.
 *
 *                Note: FlowEvent.infrastructure links to the owning infrastructure
 *                entity (which for pipeline events is the pipeline itself).
 *                The facade parameter pipelineId is used directly as
 *                infrastructureId for the query delegation.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowEventFacadeDto;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowEventFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade bean exposing FlowEvent data to flow/intelligence module.
 *
 * Registered as a Spring @Service so it can be injected as IFlowEventFacade
 * into PipelineIntelligenceService.
 *
 * Placed in flow/core/facade because it owns FlowEvent repository access.
 *
 * Infrastructure note: FlowEvent.infrastructure.id is used as the pipeline
 * correlation key. In the current model, events are attached to infrastructure
 * entities (Pipeline, Station, etc.). The pipelineId parameter passed in
 * matches the infrastructure PK when the event source is a pipeline.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventFacade implements IFlowEventFacade {

    private final FlowEventRepository eventRepository;

    // ----------------------------------------------------------------
    //  IFlowEventFacade contract
    // ----------------------------------------------------------------

    @Override
    public List<FlowEventFacadeDto> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowEventFacade.findByPipelineAndTimeRange pipelineId={}, start={}, end={}",
                pipelineId, start, end);
        // FlowEvent.infrastructure.id corresponds to the owning entity PK.
        // For pipeline-scoped events, pipelineId == infrastructureId.
        return eventRepository
                .findByInfrastructureIdAndEventTimestampBetween(pipelineId, start, end)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowEventFacadeDto> findByFlowReading(Long flowReadingId) {
        log.debug("FlowEventFacade.findByFlowReading flowReadingId={}", flowReadingId);
        return eventRepository.findByRelatedReadingId(flowReadingId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ================================================================
    //  PRIVATE MAPPING
    // ================================================================

    /**
     * Map FlowEvent entity to FlowEventFacadeDto.
     *
     * severityCode: mapped from severity.code if severity is present.
     * eventTypeCode: mapped from status.code as a best-effort classification.
     * pipelineId: mapped from infrastructure.id (pipeline-scoped events).
     */
    private FlowEventFacadeDto toDto(FlowEvent event) {
        return FlowEventFacadeDto.builder()
                .id(event.getId())
                .pipelineId(event.getInfrastructure() != null
                        ? event.getInfrastructure().getId() : null)
                .flowReadingId(event.getRelatedReading() != null
                        ? event.getRelatedReading().getId() : null)
                .eventTimestamp(event.getEventTimestamp())
                .severityCode(event.getSeverity() != null
                        ? event.getSeverity().getCode() : null)
                .eventTypeCode(event.getStatus() != null
                        ? event.getStatus().getCode() : null)
                .description(event.getDescription())
                .build();
    }
}
