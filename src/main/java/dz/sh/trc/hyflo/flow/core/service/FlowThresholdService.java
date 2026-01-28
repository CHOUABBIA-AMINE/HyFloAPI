/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowThresholdService extends GenericService<FlowThreshold, FlowThresholdDTO, Long> {

    private final FlowThresholdRepository flowThresholdRepository;

    @Override
    protected JpaRepository<FlowThreshold, Long> getRepository() {
        return flowThresholdRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowThreshold";
    }

    @Override
    protected FlowThresholdDTO toDTO(FlowThreshold entity) {
        return FlowThresholdDTO.fromEntity(entity);
    }

    @Override
    protected FlowThreshold toEntity(FlowThresholdDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowThreshold entity, FlowThresholdDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowThresholdDTO create(FlowThresholdDTO dto) {
        log.info("Creating flow threshold: pipelineId={}", 
                 dto.getPipelineId());
        
        if (flowThresholdRepository.existsByPipelineId(dto.getPipelineId())) {
            throw new BusinessValidationException("Flow threshold for this pipeline and product combination already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowThresholdDTO update(Long id, FlowThresholdDTO dto) {
        log.info("Updating flow threshold with ID: {}", id);
        
        if (flowThresholdRepository.existsByPipelineIdAndIdNot(
                dto.getPipelineId(), id)) {
            throw new BusinessValidationException("Flow threshold for this pipeline and product combination already exists");
        }
        
        return super.update(id, dto);
    }

    public List<FlowThresholdDTO> getAll() {
        log.debug("Getting all flow thresholds without pagination");
        return flowThresholdRepository.findAll().stream()
                .map(FlowThresholdDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowThresholdDTO> findByPipeline(Long pipelineId) {
        log.debug("Finding flow thresholds by pipeline id: {}", pipelineId);
        return flowThresholdRepository.findByPipelineId(pipelineId).stream()
                .map(FlowThresholdDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowThresholdDTO> findByActive(Boolean active) {
        log.debug("Finding flow thresholds by active status: {}", active);
        return flowThresholdRepository.findByActive(active).stream()
                .map(FlowThresholdDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowThresholdDTO> findByPipelineAndActive(Long pipelineId, Boolean active) {
        log.debug("Finding flow thresholds by pipeline {} and active status: {}", pipelineId, active);
        return flowThresholdRepository.findByPipelineIdAndActive(pipelineId, active).stream()
                .map(FlowThresholdDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<FlowThresholdDTO> findActiveByPipeline(Long pipelineId) {
        log.debug("Finding active flow threshold by pipeline {}", pipelineId);
        return flowThresholdRepository.findActiveByPipeline(pipelineId)
                .map(FlowThresholdDTO::fromEntity);
    }

    public Page<FlowThresholdDTO> findAllActive(Pageable pageable) {
        log.debug("Finding all active flow thresholds (paginated)");
        return executeQuery(p -> flowThresholdRepository.findAllActive(p), pageable);
    }

    public Page<FlowThresholdDTO> findActiveByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("Finding active flow thresholds by pipeline: {}", pipelineId);
        return executeQuery(p -> flowThresholdRepository.findActiveByPipeline(pipelineId, p), pageable);
    }
}
