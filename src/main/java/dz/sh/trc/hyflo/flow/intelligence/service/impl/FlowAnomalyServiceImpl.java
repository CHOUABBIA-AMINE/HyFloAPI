/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service.impl;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.intelligence.repository.FlowAnomalyRepository;
import dz.sh.trc.hyflo.flow.intelligence.service.FlowAnomalyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAnomalyServiceImpl implements FlowAnomalyService {

    private final FlowAnomalyRepository flowAnomalyRepository;

    @Override
    public Page<FlowAnomalyReadDTO> getAll(Pageable pageable) {
        return flowAnomalyRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<FlowAnomalyReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return flowAnomalyRepository.searchByAnyField(query, pageable).map(this::toDTO);
    }

    @Override
    public List<FlowAnomalyReadDTO> getByReadingId(Long readingId) {
        log.debug("getByReadingId({})", readingId);
        return flowAnomalyRepository.findByReadingId(readingId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
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
