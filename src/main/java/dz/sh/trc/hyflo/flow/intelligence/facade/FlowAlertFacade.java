/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertFacade
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade (Service)
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Implements IFlowAlertFacade.
 *                Provides cross-module read access to FlowAlert data
 *                without exposing JPA entity types outside this module.
 *
 *                FlowAlert has no dedicated severity field.
 *                Both severityCode and statusCode are derived from
 *                FlowAlert.status.code (AlertStatus entity).
 *                This is the honest representation of the current data model.
 *
 *                Mapping note for findByPipelineAndTimeRange:
 *                  The repository exposes a Page<FlowAlert> query scoped to pipeline.
 *                  Pageable.unpaged() is used to retrieve all records and convert
 *                  to the List<FlowAlertFacadeDTO> required by the interface contract.
 *
 *  @Fix        : Resolves Spring boot startup failure:
 *                "required a bean of type IFlowAlertFacade that could not be found"
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.intelligence.dto.facade.FlowAlertFacadeDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.impl.IFlowAlertFacade;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowAlert;
import dz.sh.trc.hyflo.flow.intelligence.repository.FlowAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade bean exposing FlowAlert data within the flow/intelligence module.
 *
 * Registered as a Spring {@code @Service} so it can be injected as
 * {@code IFlowAlertFacade} into PipelineIntelligenceService.
 *
 * Placed in flow/intelligence/facade because FlowAlert and its repository
 * are owned by the flow/intelligence module.
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
    public List<FlowAlertFacadeDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end) {
        log.debug("FlowAlertFacade.findByPipelineAndTimeRange pipelineId={}, start={}, end={}",
                pipelineId, start, end);
        return alertRepository
                .findByPipelineAndTimeRange(pipelineId, start, end, Pageable.unpaged())
                .getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FlowAlertFacadeDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("FlowAlertFacade.findUnresolvedByPipeline pipelineId={}, pageable={}",
                pipelineId, pageable);
        return alertRepository
                .findUnresolvedByPipeline(pipelineId, pageable)
                .map(this::toDTO);
    }

    @Override
    public List<FlowAlertFacadeDTO> findByThreshold(Long thresholdId) {
        log.debug("FlowAlertFacade.findByThreshold thresholdId={}", thresholdId);
        return alertRepository.findByThresholdId(thresholdId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlowAlertFacadeDTO> findByFlowReading(Long flowReadingId) {
        log.debug("FlowAlertFacade.findByFlowReading flowReadingId={}", flowReadingId);
        return alertRepository.findByFlowReadingId(flowReadingId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ================================================================
    //  PRIVATE MAPPING
    // ================================================================

    /**
     * Map FlowAlert entity to FlowAlertFacadeDTO.
     *
     * severityCode — FlowAlert has no dedicated severity field.
     *                Mapped from status.code (AlertStatus) as the best
     *                available approximation. PipelineIntelligenceService
     *                checks for "CRITICAL", "HIGH", "MEDIUM" values,
     *                which should be stored in AlertStatus.code.
     *
     * statusCode   — Same source: AlertStatus.code.
     *                Both fields receive the same value intentionally
     *                until a dedicated Severity entity is added to FlowAlert.
     *
     * thresholdId  — from threshold.id (FlowThreshold in flow.common)
     * pipelineId   — from threshold.pipeline.id (transitive FK)
     */
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
                .severityCode(statusCode)   // no dedicated severity field — using status.code
                .statusCode(statusCode)
                .message(alert.getMessage())
                .build();
    }
}
