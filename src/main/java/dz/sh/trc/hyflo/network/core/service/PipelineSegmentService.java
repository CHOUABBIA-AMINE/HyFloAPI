/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineSegmentService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
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
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineSegmentService extends GenericService<PipelineSegment, PipelineSegmentDTO, Long> {

    private final PipelineSegmentRepository pipelineSegmentRepository;
    private final PipelineRepository pipelineRepository;

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
        
        if (dto.getPipelineId() != null) {
            Pipeline pipeline = pipelineRepository.findById(dto.getPipelineId())
                    .orElseThrow(() -> new RuntimeException("Pipeline not found with id: " + dto.getPipelineId()));
            entity.setPipeline(pipeline);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(PipelineSegment entity, PipelineSegmentDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getPipelineId() != null) {
            Pipeline pipeline = pipelineRepository.findById(dto.getPipelineId())
                    .orElseThrow(() -> new RuntimeException("Pipeline not found with id: " + dto.getPipelineId()));
            entity.setPipeline(pipeline);
        }
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

    public List<PipelineSegmentDTO> findByPipeline(Long pipelineId) {
        log.debug("Finding pipeline segments by pipeline id: {}", pipelineId);
        return pipelineSegmentRepository.findByPipelineId(pipelineId).stream()
                .map(PipelineSegmentDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
