/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdCommandServiceImpl
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.service.impl → flow.common.service.impl
 *                             All imports updated to flow.common.* (model, repository, dto, service)
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation (Command)
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service.impl;

import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.common.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.common.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.flow.common.service.FlowThresholdCommandService;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowThresholdCommandServiceImpl implements FlowThresholdCommandService {

    private final FlowThresholdRepository thresholdRepository;
    private final PipelineRepository      pipelineRepository;

    @Override
    public FlowThresholdDTO createThreshold(FlowThresholdDTO dto) {
        log.info("createThreshold for pipelineId={}, active={}", dto.getPipelineId(), dto.getActive());
        validateRanges(dto);
        Pipeline pipeline = pipelineRepository.findById(dto.getPipelineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pipeline not found: " + dto.getPipelineId()));
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

    @Override
    public FlowThresholdDTO updateThreshold(Long id, FlowThresholdDTO dto) {
        log.info("updateThreshold id={}", id);
        validateRanges(dto);
        FlowThreshold entity = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));
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

    @Override
    public void deleteThreshold(Long id) {
        log.warn("deleteThreshold id={} — hard delete", id);
        if (!thresholdRepository.existsById(id)) {
            throw new ResourceNotFoundException("FlowThreshold not found: " + id);
        }
        thresholdRepository.deleteById(id);
    }

    @Override
    public FlowThresholdDTO activateThreshold(Long id) {
        log.info("activateThreshold id={}", id);
        FlowThreshold entity = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));
        if (Boolean.TRUE.equals(entity.getActive())) {
            return FlowThresholdDTO.fromEntity(entity);
        }
        deactivateCurrentActiveForPipeline(entity.getPipeline().getId());
        entity.setActive(true);
        FlowThreshold saved = thresholdRepository.save(entity);
        return FlowThresholdDTO.fromEntity(saved);
    }

    @Override
    public FlowThresholdDTO deactivateThreshold(Long id) {
        log.info("deactivateThreshold id={}", id);
        FlowThreshold entity = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowThreshold not found: " + id));
        if (!Boolean.TRUE.equals(entity.getActive())) {
            return FlowThresholdDTO.fromEntity(entity);
        }
        entity.setActive(false);
        FlowThreshold saved = thresholdRepository.save(entity);
        return FlowThresholdDTO.fromEntity(saved);
    }

    private void deactivateCurrentActiveForPipeline(Long pipelineId) {
        thresholdRepository.findByPipelineIdAndActiveTrue(pipelineId)
                .ifPresent(existing -> {
                    existing.setActive(false);
                    thresholdRepository.save(existing);
                });
    }

    private void validateRanges(FlowThresholdDTO dto) {
        if (dto.getPressureMin() != null && dto.getPressureMax() != null
                && dto.getPressureMin().compareTo(dto.getPressureMax()) >= 0) {
            throw new BusinessValidationException("pressureMin must be strictly less than pressureMax");
        }
        if (dto.getTemperatureMin() != null && dto.getTemperatureMax() != null
                && dto.getTemperatureMin().compareTo(dto.getTemperatureMax()) >= 0) {
            throw new BusinessValidationException("temperatureMin must be strictly less than temperatureMax");
        }
        if (dto.getFlowRateMin() != null && dto.getFlowRateMax() != null
                && dto.getFlowRateMin().compareTo(dto.getFlowRateMax()) >= 0) {
            throw new BusinessValidationException("flowRateMin must be strictly less than flowRateMax");
        }
        if (dto.getContainedVolumeMin() != null && dto.getContainedVolumeMax() != null
                && dto.getContainedVolumeMin().compareTo(dto.getContainedVolumeMax()) >= 0) {
            throw new BusinessValidationException("containedVolumeMin must be strictly less than containedVolumeMax");
        }
    }

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
