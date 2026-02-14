/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegmentService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-14-2026 13:32 - Enhanced logging for findByPipeline
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.core.dto.PipelineSegmentDTO;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineSegmentService extends GenericService<PipelineSegment, PipelineSegmentDTO, Long> {

    private final PipelineSegmentRepository pipelineSegmentRepository;

    @Override
    protected JpaRepository<PipelineSegment, Long> getRepository() {
        return pipelineSegmentRepository;
    }

    @Override
    protected String getEntityName() {
        return "PipelineSegment";
    }

    @Override
    protected PipelineSegmentDTO toDTO(PipelineSegment entity) {
        return PipelineSegmentDTO.fromEntity(entity);
    }

    @Override
    protected PipelineSegment toEntity(PipelineSegmentDTO dto) {
        PipelineSegment entity = dto.toEntity();
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(PipelineSegment entity, PipelineSegmentDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public PipelineSegmentDTO create(PipelineSegmentDTO dto) {
        log.info("Creating pipeline segment: code={}", dto.getCode());
        
        if (pipelineSegmentRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Pipeline segment with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public PipelineSegmentDTO update(Long id, PipelineSegmentDTO dto) {
        log.info("Updating pipeline segment with ID: {}", id);
        
        if (pipelineSegmentRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Pipeline segment with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<PipelineSegmentDTO> getAll() {
        log.debug("Getting all pipeline segments without pagination");
        return pipelineSegmentRepository.findAll().stream()
                .map(PipelineSegmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PipelineSegmentDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for pipeline segments with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> pipelineSegmentRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    /**
     * Find all pipeline segments belonging to a specific pipeline.
     * 
     * This method includes comprehensive logging to help debug filtering issues.
     * 
     * @param pipelineId The ID of the parent pipeline
     * @return List of segments belonging to the specified pipeline
     */
    public List<PipelineSegmentDTO> findByPipeline(Long pipelineId) {
        log.info("🔍 Finding pipeline segments for pipeline ID: {}", pipelineId);
        
        // Execute repository query
        List<PipelineSegment> segments = pipelineSegmentRepository.findByPipelineId(pipelineId);
        log.info("📦 Repository returned {} segments for pipeline {}", segments.size(), pipelineId);
        
        // Detailed logging for debugging
        if (log.isDebugEnabled()) {
            segments.forEach(seg -> {
                Long segmentPipelineId = seg.getPipeline() != null ? seg.getPipeline().getId() : null;
                log.debug("  → Segment ID={}, Code={}, PipelineID={}", 
                    seg.getId(), 
                    seg.getCode(), 
                    segmentPipelineId
                );
                
                // Warn about data integrity issues
                if (segmentPipelineId == null) {
                    log.warn("⚠️ Segment {} has NULL pipeline reference!", seg.getCode());
                } else if (!segmentPipelineId.equals(pipelineId)) {
                    log.error("🚨 DATA INTEGRITY ERROR: Segment {} (ID={}) has pipelineId={} but query was for pipelineId={}!",
                        seg.getCode(), seg.getId(), segmentPipelineId, pipelineId);
                }
            });
        }
        
        // Convert to DTOs
        List<PipelineSegmentDTO> dtos = segments.stream()
                .map(PipelineSegmentDTO::fromEntity)
                .collect(Collectors.toList());
        
        log.info("✅ Returning {} segment DTOs for pipeline {}", dtos.size(), pipelineId);
        
        return dtos;
    }
}
