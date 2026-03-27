/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyService
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.service to flow.intelligence.service
 *                             Updated to use flow.intelligence.repository and flow.intelligence.dto
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.intelligence.repository.FlowAnomalyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAnomalyService {

    private final FlowAnomalyRepository flowAnomalyRepository;

    public FlowAnomalyReadDTO getById(Long id) {
        return flowAnomalyRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowAnomaly not found: " + id));
    }

    public Page<FlowAnomalyReadDTO> getAll(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return flowAnomalyRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)))
                .map(this::toDTO);
    }

    public List<FlowAnomalyReadDTO> getAll() {
        return flowAnomalyRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long count() {
        return flowAnomalyRepository.count();
    }

    public Page<FlowAnomalyReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return flowAnomalyRepository.findAll(pageable).map(this::toDTO);
        }
        return flowAnomalyRepository.searchByAnyField(query, pageable).map(this::toDTO);
    }

    public List<FlowAnomalyReadDTO> getByReadingId(Long readingId) {
        log.debug("getByReadingId({})", readingId);
        return flowAnomalyRepository.findByReadingId(readingId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<FlowAnomalyReadDTO> getByPipelineSegmentId(Long segmentId) {
        log.debug("getByPipelineSegmentId({})", segmentId);
        return flowAnomalyRepository.findByPipelineSegmentId(segmentId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private FlowAnomalyReadDTO toDTO(FlowAnomaly entity) {
        return FlowAnomalyReadDTO.builder()
                .id(entity.getId())
                .anomalyType(entity.getAnomalyType())
                .severityScore(entity.getSeverityScore())
                .confidenceScore(entity.getConfidenceScore())
                .modelName(entity.getModelName())
                .explanation(entity.getExplanation())
                .detectedAt(entity.getDetectedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getCode() : null)
                .build();
    }
}
