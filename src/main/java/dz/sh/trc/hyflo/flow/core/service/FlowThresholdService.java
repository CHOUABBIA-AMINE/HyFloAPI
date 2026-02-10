/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowThresholdService
 * 	@CreatedOn	: 02-03-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Service
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 * 	@Description:
 * 	Service for managing pipeline operating thresholds.
 * 	Handles CRUD operations and threshold lifecycle management.
 * 	Enforces business rules: only one active threshold per pipeline.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FlowThresholdService {
    
    private final FlowThresholdRepository flowThresholdRepository;
    private final PipelineRepository pipelineRepository;
    
    // ========== CREATE ==========
    
    /**
     * Create a new threshold for a pipeline.
     * Validates that min < max for all parameters.
     * Deactivates existing active threshold if new one is active.
     *
     * @param dto Threshold data transfer object
     * @return Created threshold DTO
     * @throws ResourceNotFoundException if pipeline not found
     * @throws BusinessException if validation fails
     */
    public FlowThresholdDTO createThreshold(FlowThresholdDTO dto) {
        
        log.debug("Creating threshold for pipeline ID: {}", dto.getPipelineId());
        
        // Validate pipeline exists
        Pipeline pipeline = pipelineRepository.findById(dto.getPipelineId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Pipeline not found: " + dto.getPipelineId()));
        
        // Validate threshold ranges
        validateThresholdRanges(dto);
        
        // If creating an active threshold, deactivate existing active threshold
        if (Boolean.TRUE.equals(dto.getActive())) {
            Optional<FlowThreshold> existingActive = flowThresholdRepository
                .findByPipelineIdAndActiveTrue(dto.getPipelineId());
            
            if (existingActive.isPresent()) {
                log.info("Deactivating existing active threshold {} for pipeline {}", 
                    existingActive.get().getId(), pipeline.getCode());
                
                FlowThreshold existing = existingActive.get();
                existing.setActive(false);
                flowThresholdRepository.save(existing);
            }
        }
        
        // Convert DTO to entity and set pipeline
        FlowThreshold threshold = dto.toEntity();
        threshold.setPipeline(pipeline);
        
        FlowThreshold saved = flowThresholdRepository.save(threshold);
        
        log.info("Created threshold {} for pipeline {} (active={})", 
            saved.getId(), pipeline.getCode(), saved.getActive());
        
        return FlowThresholdDTO.fromEntity(saved);
    }
    
    // ========== READ ==========
    
    /**
     * Get threshold by ID
     *
     * @param id Threshold identifier
     * @return Threshold DTO
     * @throws ResourceNotFoundException if not found
     */
    @Transactional(readOnly = true)
    public FlowThresholdDTO getThresholdById(Long id) {
        
        FlowThreshold threshold = flowThresholdRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Threshold not found: " + id));
        
        return FlowThresholdDTO.fromEntity(threshold);
    }
    
    /**
     * Get active threshold for a pipeline
     *
     * @param pipelineId Pipeline identifier
     * @return Optional threshold DTO
     */
    @Transactional(readOnly = true)
    public Optional<FlowThresholdDTO> getActiveThresholdByPipelineId(Long pipelineId) {
        
        return flowThresholdRepository
            .findByPipelineIdAndActiveTrue(pipelineId)
            .map(FlowThresholdDTO::fromEntity);
    }
    
    /**
     * Get all thresholds for a pipeline (active and inactive)
     *
     * @param pipelineId Pipeline identifier
     * @return List of threshold DTOs
     */
    @Transactional(readOnly = true)
    public List<FlowThresholdDTO> getThresholdsByPipelineId(Long pipelineId) {
        
        return flowThresholdRepository.findByPipelineId(pipelineId)
            .stream()
            .map(FlowThresholdDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all active thresholds for a structure (station, terminal, manifold)
     *
     * @param structureId Structure identifier
     * @return List of threshold DTOs
     */
    @Transactional(readOnly = true)
    public List<FlowThresholdDTO> getActiveThresholdsByStructureId(Long structureId) {
        
        return flowThresholdRepository.findActiveByStructureId(structureId)
            .stream()
            .map(FlowThresholdDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all thresholds with pagination
     *
     * @param page Page number (0-indexed)
     * @param size Page size
     * @param sortBy Sort field (default: pipeline.code)
     * @param sortDirection Sort direction (ASC/DESC)
     * @return Page of threshold DTOs
     */
    @Transactional(readOnly = true)
    public Page<FlowThresholdDTO> getAllThresholds(
        int page, 
        int size, 
        String sortBy, 
        String sortDirection
    ) {
        
        Sort sort = Sort.by(
            "DESC".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
            sortBy != null ? sortBy : "id"
        );
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return flowThresholdRepository.findAllWithPagination(pageable)
            .map(FlowThresholdDTO::fromEntity);
    }
    
    /**
     * Get all active thresholds with pagination
     *
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return Page of active threshold DTOs
     */
    @Transactional(readOnly = true)
    public Page<FlowThresholdDTO> getActiveThresholds(int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return flowThresholdRepository.findActiveWithPagination(pageable)
            .map(FlowThresholdDTO::fromEntity);
    }
    
    /**
     * Get pipelines without any threshold configuration
     *
     * @return List of pipeline IDs lacking thresholds
     */
    @Transactional(readOnly = true)
    public List<Long> getPipelinesWithoutThresholds() {
        
        return flowThresholdRepository.findPipelinesWithoutThresholds();
    }
    
    /**
     * Get pipelines without active threshold configuration
     *
     * @return List of pipeline IDs lacking active thresholds
     */
    @Transactional(readOnly = true)
    public List<Long> getPipelinesWithoutActiveThresholds() {
        
        return flowThresholdRepository.findPipelinesWithoutActiveThresholds();
    }
    
    /**
     * Get count of pipelines without thresholds
     *
     * @return Count of pipelines lacking thresholds
     */
    @Transactional(readOnly = true)
    public long countPipelinesWithoutThresholds() {
        
        return flowThresholdRepository.countPipelinesWithoutThresholds();
    }
    
    /**
     * Search thresholds by pipeline code
     *
     * @param pipelineCode Pipeline code (exact match)
     * @return List of threshold DTOs
     */
    @Transactional(readOnly = true)
    public List<FlowThresholdDTO> searchByPipelineCode(String pipelineCode) {
        
        return flowThresholdRepository.findByPipelineCode(pipelineCode)
            .stream()
            .map(FlowThresholdDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Search thresholds by pipeline code pattern
     *
     * @param pattern Pipeline code pattern (e.g., "OZ%")
     * @return List of threshold DTOs
     */
    @Transactional(readOnly = true)
    public List<FlowThresholdDTO> searchByPipelineCodePattern(String pattern) {
        
        return flowThresholdRepository.findByPipelineCodeLike(pattern)
            .stream()
            .map(FlowThresholdDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all active thresholds (no pagination)
     *
     * @return List of all active threshold DTOs
     */
    @Transactional(readOnly = true)
    public List<FlowThresholdDTO> getAllActiveThresholds() {
        
        return flowThresholdRepository.findByActiveTrue()
            .stream()
            .map(FlowThresholdDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all inactive thresholds
     *
     * @return List of all inactive threshold DTOs
     */
    @Transactional(readOnly = true)
    public List<FlowThresholdDTO> getAllInactiveThresholds() {
        
        return flowThresholdRepository.findByActiveFalse()
            .stream()
            .map(FlowThresholdDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Count active thresholds
     *
     * @return Number of active thresholds
     */
    @Transactional(readOnly = true)
    public long countActiveThresholds() {
        
        return flowThresholdRepository.countByActiveTrue();
    }
    
    /**
     * Check if pipeline has an active threshold
     *
     * @param pipelineId Pipeline identifier
     * @return true if active threshold exists
     */
    @Transactional(readOnly = true)
    public boolean hasActiveThreshold(Long pipelineId) {
        
        return flowThresholdRepository.existsByPipelineIdAndActiveTrue(pipelineId);
    }
    
    // ========== UPDATE ==========
    
    /**
     * Update an existing threshold
     *
     * @param id Threshold identifier
     * @param dto Updated threshold data
     * @return Updated threshold DTO
     * @throws ResourceNotFoundException if threshold not found
     * @throws BusinessException if validation fails
     */
    public FlowThresholdDTO updateThreshold(Long id, FlowThresholdDTO dto) {
        
        log.debug("Updating threshold ID: {}", id);
        
        FlowThreshold threshold = flowThresholdRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Threshold not found: " + id));
        
        // Validate threshold ranges
        validateThresholdRanges(dto);
        
        // If activating this threshold, deactivate other active thresholds for same pipeline
        if (Boolean.TRUE.equals(dto.getActive()) && !threshold.getActive()) {
            Optional<FlowThreshold> existingActive = flowThresholdRepository
                .findByPipelineIdAndActiveTrue(threshold.getPipeline().getId());
            
            if (existingActive.isPresent() && !existingActive.get().getId().equals(id)) {
                log.info("Deactivating existing active threshold {} for pipeline {}", 
                    existingActive.get().getId(), threshold.getPipeline().getCode());
                
                FlowThreshold existing = existingActive.get();
                existing.setActive(false);
                flowThresholdRepository.save(existing);
            }
        }
        
        // Update entity using DTO's updateEntity method
        dto.updateEntity(threshold);
        
        FlowThreshold updated = flowThresholdRepository.save(threshold);
        
        log.info("Updated threshold {} for pipeline {} (active={})", 
            updated.getId(), updated.getPipeline().getCode(), updated.getActive());
        
        return FlowThresholdDTO.fromEntity(updated);
    }
    
    /**
     * Activate a threshold (deactivates other active thresholds for same pipeline)
     *
     * @param id Threshold identifier
     * @return Updated threshold DTO
     * @throws ResourceNotFoundException if threshold not found
     */
    public FlowThresholdDTO activateThreshold(Long id) {
        
        log.debug("Activating threshold ID: {}", id);
        
        FlowThreshold threshold = flowThresholdRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Threshold not found: " + id));
        
        // If already active, return as is
        if (threshold.getActive()) {
            log.debug("Threshold {} already active", id);
            return FlowThresholdDTO.fromEntity(threshold);
        }
        
        // Deactivate existing active threshold for this pipeline
        Optional<FlowThreshold> existingActive = flowThresholdRepository
            .findByPipelineIdAndActiveTrue(threshold.getPipeline().getId());
        
        if (existingActive.isPresent()) {
            log.info("Deactivating existing active threshold {} for pipeline {}", 
                existingActive.get().getId(), threshold.getPipeline().getCode());
            
            FlowThreshold existing = existingActive.get();
            existing.setActive(false);
            flowThresholdRepository.save(existing);
        }
        
        // Activate this threshold
        threshold.setActive(true);
        FlowThreshold activated = flowThresholdRepository.save(threshold);
        
        log.info("Activated threshold {} for pipeline {}", 
            activated.getId(), activated.getPipeline().getCode());
        
        return FlowThresholdDTO.fromEntity(activated);
    }
    
    /**
     * Deactivate a threshold
     *
     * @param id Threshold identifier
     * @return Updated threshold DTO
     * @throws ResourceNotFoundException if threshold not found
     */
    public FlowThresholdDTO deactivateThreshold(Long id) {
        
        log.debug("Deactivating threshold ID: {}", id);
        
        FlowThreshold threshold = flowThresholdRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Threshold not found: " + id));
        
        // If already inactive, return as is
        if (!threshold.getActive()) {
            log.debug("Threshold {} already inactive", id);
            return FlowThresholdDTO.fromEntity(threshold);
        }
        
        threshold.setActive(false);
        FlowThreshold deactivated = flowThresholdRepository.save(threshold);
        
        log.info("Deactivated threshold {} for pipeline {}", 
            deactivated.getId(), deactivated.getPipeline().getCode());
        
        return FlowThresholdDTO.fromEntity(deactivated);
    }
    
    // ========== DELETE ==========
    
    /**
     * Delete a threshold
     * WARNING: This is a hard delete. Consider deactivation instead for audit trail.
     *
     * @param id Threshold identifier
     * @throws ResourceNotFoundException if threshold not found
     */
    public void deleteThreshold(Long id) {
        
        log.debug("Deleting threshold ID: {}", id);
        
        FlowThreshold threshold = flowThresholdRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Threshold not found: " + id));
        
        String pipelineCode = threshold.getPipeline().getCode();
        
        flowThresholdRepository.delete(threshold);
        
        log.warn("DELETED threshold {} for pipeline {} - no audit trail", 
            id, pipelineCode);
    }
    
    // ========== VALIDATION ==========
    
    /**
     * Validate threshold ranges (min < max for all parameters)
     *
     * @param dto Threshold DTO
     * @throws BusinessException if validation fails
     */
    private void validateThresholdRanges(FlowThresholdDTO dto) {
        
        List<String> errors = new ArrayList<>();
        
        // Pressure validation
        if (dto.getPressureMin() != null && dto.getPressureMax() != null) {
            if (dto.getPressureMin().compareTo(dto.getPressureMax()) >= 0) {
                errors.add("Pressure minimum (" + dto.getPressureMin() + 
                    ") must be less than maximum (" + dto.getPressureMax() + ")");
            }
        }
        
        // Temperature validation
        if (dto.getTemperatureMin() != null && dto.getTemperatureMax() != null) {
            if (dto.getTemperatureMin().compareTo(dto.getTemperatureMax()) >= 0) {
                errors.add("Temperature minimum (" + dto.getTemperatureMin() + 
                    ") must be less than maximum (" + dto.getTemperatureMax() + ")");
            }
        }
        
        // Flow rate validation
        if (dto.getFlowRateMin() != null && dto.getFlowRateMax() != null) {
            if (dto.getFlowRateMin().compareTo(dto.getFlowRateMax()) >= 0) {
                errors.add("Flow rate minimum (" + dto.getFlowRateMin() + 
                    ") must be less than maximum (" + dto.getFlowRateMax() + ")");
            }
        }
        
        // Contained volume validation
        if (dto.getContainedVolumeMin() != null && dto.getContainedVolumeMax() != null) {
            if (dto.getContainedVolumeMin().compareTo(dto.getContainedVolumeMax()) >= 0) {
                errors.add("Contained volume minimum (" + dto.getContainedVolumeMin() + 
                    ") must be less than maximum (" + dto.getContainedVolumeMax() + ")");
            }
        }
        
        // Alert tolerance validation
        if (dto.getAlertTolerance() != null) {
            if (dto.getAlertTolerance().compareTo(BigDecimal.ZERO) < 0) {
                errors.add("Alert tolerance cannot be negative");
            }
            
            if (dto.getAlertTolerance().compareTo(BigDecimal.valueOf(50)) > 0) {
                errors.add("Alert tolerance cannot exceed 50%");
            }
        }
        
        if (!errors.isEmpty()) {
            String errorMessage = "Threshold validation failed: " + String.join("; ", errors);
            log.error(errorMessage);
            throw new BusinessValidationException(errorMessage);
        }
    }
}
