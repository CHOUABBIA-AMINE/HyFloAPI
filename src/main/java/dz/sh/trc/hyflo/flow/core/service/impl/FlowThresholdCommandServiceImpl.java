/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdCommandServiceImpl
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation (Command)
 *  @Package    : Flow / Core
 *
 *  @Description: Implements FlowThresholdCommandService.
 *                Handles all write operations for FlowThreshold:
 *                create, update, delete, activate, deactivate.
 *
 *                Business invariant: only one active threshold per pipeline.
 *                Activation of a new threshold atomically deactivates
 *                the previously active threshold for the same pipeline.
 *
 *                Range validation: pressureMin < pressureMax,
 *                temperatureMin < temperatureMax,
 *                flowRateMin < flowRateMax,
 *                containedVolumeMin < containedVolumeMax.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command implementation for FlowThreshold write operations.
 *
 * Business invariant: only one active threshold per pipeline.
 * All activation operations atomically deactivate the existing active threshold.
 *
 * Phase 5 — Commit 35 (impl added 03-26-2026)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowThresholdCommandServiceImpl implements dz.sh.trc.hyflo.flow.core.service.FlowThresholdCommandService {

    private final FlowThresholdRepository thresholdRepository;
    private final PipelineRepository      pipelineRepository;

    // ----------------------------------------------------------------
    //  CREATE
    // ----------------------------------------------------------------

    @Override
    public FlowThresholdDTO createThreshold(FlowThresholdDTO dto) {
        log.info("createThreshold for pipelineId={}, active={}", dto.getPipelineId(), dto.getActive());

        validateRanges(dto);

        Pipeline pipeline = pipelineRepository.findById(dto.getPipelineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pipeline not found: " + dto.getPipelineId()));

        // Business invariant: if creating as active, deactivate current active threshold
        if (Boolean.TRUE.equals(dto.getActive())) {
            deactivateCurrentActiveForPipeline(dto.getPipelineId());
        }

        FlowThreshold entity = new FlowThreshold();
        entity.setPipeline(pipeline);
        applyDtoToEntity(dto, entity);

        FlowThreshold saved = thresholdRepository.save(entity);
        log.info("createThreshold: saved id={}", saved.getId());
        return FlowThresholdDTO.fromEntity(saved);
    }

    // ----------------------------------------------------------------
    //  UPDATE
    // ----------------------------------------------------------------

    @Override
    public FlowThresholdDTO updateThreshold(Long id, FlowThresholdDTO dto) {
        log.info("updateThreshold id={}", id);

        validateRanges(dto);

        FlowThreshold entity = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));

        // If the update activates an inactive threshold, deactivate the current active one
        boolean wasInactive = !Boolean.TRUE.equals(entity.getActive());
        boolean nowActive   = Boolean.TRUE.equals(dto.getActive());
        if (wasInactive && nowActive) {
            deactivateCurrentActiveForPipeline(entity.getPipeline().getId());
        }

        applyDtoToEntity(dto, entity);
        FlowThreshold saved = thresholdRepository.save(entity);
        log.info("updateThreshold: saved id={}", saved.getId());
        return FlowThresholdDTO.fromEntity(saved);
    }

    // ----------------------------------------------------------------
    //  DELETE
    // ----------------------------------------------------------------

    @Override
    public void deleteThreshold(Long id) {
        log.warn("deleteThreshold id={} — hard delete", id);
        if (!thresholdRepository.existsById(id)) {
            throw new ResourceNotFoundException("FlowThreshold not found: " + id);
        }
        thresholdRepository.deleteById(id);
    }

    // ----------------------------------------------------------------
    //  ACTIVATE
    // ----------------------------------------------------------------

    @Override
    public FlowThresholdDTO activateThreshold(Long id) {
        log.info("activateThreshold id={}", id);

        FlowThreshold entity = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));

        if (Boolean.TRUE.equals(entity.getActive())) {
            // Already active — idempotent return
            log.debug("activateThreshold id={} — already active, no-op", id);
            return FlowThresholdDTO.fromEntity(entity);
        }

        deactivateCurrentActiveForPipeline(entity.getPipeline().getId());
        entity.setActive(true);
        FlowThreshold saved = thresholdRepository.save(entity);
        log.info("activateThreshold: id={} activated", id);
        return FlowThresholdDTO.fromEntity(saved);
    }

    // ----------------------------------------------------------------
    //  DEACTIVATE
    // ----------------------------------------------------------------

    @Override
    public FlowThresholdDTO deactivateThreshold(Long id) {
        log.info("deactivateThreshold id={}", id);

        FlowThreshold entity = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));

        if (!Boolean.TRUE.equals(entity.getActive())) {
            // Already inactive — idempotent return
            log.debug("deactivateThreshold id={} — already inactive, no-op", id);
            return FlowThresholdDTO.fromEntity(entity);
        }

        entity.setActive(false);
        FlowThreshold saved = thresholdRepository.save(entity);
        log.info("deactivateThreshold: id={} deactivated", id);
        return FlowThresholdDTO.fromEntity(saved);
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    /**
     * Deactivate the currently active threshold for a pipeline, if one exists.
     * Called before activating a new threshold to enforce the single-active invariant.
     */
    private void deactivateCurrentActiveForPipeline(Long pipelineId) {
        thresholdRepository.findByPipelineIdAndActiveTrue(pipelineId)
                .ifPresent(existing -> {
                    log.debug("deactivateCurrentActiveForPipeline pipelineId={}: "
                            + "deactivating existing active threshold id={}",
                            pipelineId, existing.getId());
                    existing.setActive(false);
                    thresholdRepository.save(existing);
                });
    }

    /**
     * Validate that min < max for all four physical parameter pairs.
     *
     * @throws BusinessValidationException if any pair has min >= max
     */
    private void validateRanges(FlowThresholdDTO dto) {
        if (dto.getPressureMin() != null && dto.getPressureMax() != null
                && dto.getPressureMin().compareTo(dto.getPressureMax()) >= 0) {
            throw new BusinessValidationException(
                    "pressureMin must be strictly less than pressureMax");
        }
        if (dto.getTemperatureMin() != null && dto.getTemperatureMax() != null
                && dto.getTemperatureMin().compareTo(dto.getTemperatureMax()) >= 0) {
            throw new BusinessValidationException(
                    "temperatureMin must be strictly less than temperatureMax");
        }
        if (dto.getFlowRateMin() != null && dto.getFlowRateMax() != null
                && dto.getFlowRateMin().compareTo(dto.getFlowRateMax()) >= 0) {
            throw new BusinessValidationException(
                    "flowRateMin must be strictly less than flowRateMax");
        }
        if (dto.getContainedVolumeMin() != null && dto.getContainedVolumeMax() != null
                && dto.getContainedVolumeMin().compareTo(dto.getContainedVolumeMax()) >= 0) {
            throw new BusinessValidationException(
                    "containedVolumeMin must be strictly less than containedVolumeMax");
        }
    }

    /**
     * Copy all mutable DTO fields into the entity.
     * Does NOT touch the pipeline association — managed separately.
     */
    private void applyDtoToEntity(FlowThresholdDTO dto, FlowThreshold entity) {
        entity.setPressureMin(dto.getPressureMin());
        entity.setPressureMax(dto.getPressureMax());
        entity.setTemperatureMin(dto.getTemperatureMin());
        entity.setTemperatureMax(dto.getTemperatureMax());
        entity.setFlowRateMin(dto.getFlowRateMin());
        entity.setFlowRateMax(dto.getFlowRateMax());
        entity.setContainedVolumeMin(dto.getContainedVolumeMin());
        entity.setContainedVolumeMax(dto.getContainedVolumeMax());
        entity.setAlertTolerance(dto.getAlertTolerance());
        entity.setActive(dto.getActive() != null ? dto.getActive() : Boolean.FALSE);
    }
}
