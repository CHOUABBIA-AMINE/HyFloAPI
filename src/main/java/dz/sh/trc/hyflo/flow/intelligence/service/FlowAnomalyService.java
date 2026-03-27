/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyService
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.service → flow.intelligence.service
 *
 *  @Type       : Class
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<FlowAnomalyReadDTO> getAll(Pageable pageable) {
        return flowAnomalyRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<FlowAnomalyReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
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

    private FlowAnomalyReadDTO toDTO(FlowAnomaly a) {
        return FlowAnomalyReadDTO.builder()
                .id(a.getId())
                .anomalyType(a.getAnomalyType())
                .severityScore(a.getSeverityScore())
                .confidenceScore(a.getConfidenceScore())
                .modelName(a.getModelName())
                .explanation(a.getExplanation())
                .detectedAt(a.getDetectedAt())
                .readingId(a.getReading() != null ? a.getReading().getId() : null)
                .derivedReadingId(a.getDerivedReading() != null ? a.getDerivedReading().getId() : null)
                .pipelineSegmentId(a.getPipelineSegment() != null ? a.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(a.getPipelineSegment() != null ? a.getPipelineSegment().getCode() : null)
                .build();
    }
}
