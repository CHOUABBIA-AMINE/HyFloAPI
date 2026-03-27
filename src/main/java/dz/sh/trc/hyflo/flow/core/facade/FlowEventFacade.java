/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventFacade
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-26-2026 — fix: Severity/EventStatus have no getCode();
 *                             use getDesignationFr() instead
 *
 *  @Type       : Class
 *  @Layer      : Facade (Service)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements IFlowEventFacade from flow/intelligence.
 *                Provides cross-module read access to FlowEvent data
 *                without exposing flow/core entities outside their module.
 *
 *                Maps FlowEvent entities to FlowEventFacadeDTO.
 *                Owned by flow/core because it accesses flow/core repositories
 *                and entities.
 *
 *                Note: FlowEvent.infrastructure links to the owning infrastructure
 *                entity (which for pipeline events is the pipeline itself).
 *                The facade parameter pipelineId is used directly as
 *                infrastructureId for the query delegation.
 *
 *                Mapping note:
 *                - Severity has no code field: designationFr used as severityCode.
 *                - EventStatus has no code field: designationFr used as eventTypeCode.
 *                - AlertStatus (separate class) does have a code field.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade;

import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowEventFacadeDTO;
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
    public List<FlowEventFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowEventFacade.findByPipelineAndTimeRange pipelineId={}, start={}, end={}",
                pipelineId, start, end);
        return eventRepository
                .findByInfrastructureIdAndEventTimestampBetween(pipelineId, start, end)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowEventFacadeDTO> findByFlowReading(Long flowReadingId) {
        log.debug("FlowEventFacade.findByFlowReading flowReadingId={}", flowReadingId);
        return eventRepository.findByRelatedReadingId(flowReadingId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ================================================================
    //  PRIVATE MAPPING
    // ================================================================

    /**
     * Map FlowEvent entity to FlowEventFacadeDTO.
     *
     * severityCode  — Severity entity has no code field; mapped from designationFr
     *                 (the required non-null field). Example: "Critique", "Élevé".
     *
     * eventTypeCode — EventStatus entity has no code field; mapped from designationFr.
     *                 Example: "Ouvert", "Fermé".
     *
     * pipelineId    — mapped from infrastructure.id (pipeline-scoped events).
     */
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
