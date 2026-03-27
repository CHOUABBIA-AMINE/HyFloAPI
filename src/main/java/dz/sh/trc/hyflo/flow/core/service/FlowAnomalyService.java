/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.3: marked deprecated before Phase 4
 *  @UpdatedOn  : 03-26-2026 — Task 1: searchByQuery protected → public, remove @Override
 *
 *  @Type       : Class
 *  @Layer      : Service (TRANSITIONAL GENERIC — DO NOT EXTEND)
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowCoreReadMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.core.repository.FlowAnomalyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Deprecated(since = "v2-phase3", forRemoval = true)
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAnomalyService extends GenericService<FlowAnomaly, FlowAnomalyReadDTO, Long> {

    private final FlowAnomalyRepository flowAnomalyRepository;

    @Override
    protected JpaRepository<FlowAnomaly, Long> getRepository() {
        return flowAnomalyRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowAnomaly";
    }

    @Override
    protected FlowAnomalyReadDTO toDTO(FlowAnomaly entity) {
        return FlowCoreReadMapper.toDTO(entity);
    }

    @Override
    protected FlowAnomaly toEntity(FlowAnomalyReadDTO dto) {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly creation");
    }

    @Override
    protected void updateEntityFromDTO(FlowAnomaly entity, FlowAnomalyReadDTO dto) {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly updates");
    }

    public Page<FlowAnomalyReadDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return flowAnomalyRepository.searchByAnyField(query, pageable).map(FlowCoreReadMapper::toDTO);
    }

    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowAnomalyReadDTO> getByReadingId(Long readingId) {
        log.debug("Getting anomalies for reading ID: {}", readingId);
        return flowAnomalyRepository.findByReadingId(readingId)
                .stream().map(FlowCoreReadMapper::toDTO).collect(Collectors.toList());
    }

    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowAnomalyReadDTO> getByPipelineSegmentId(Long segmentId) {
        log.debug("Getting anomalies for segment ID: {}", segmentId);
        return flowAnomalyRepository.findByPipelineSegmentId(segmentId)
                .stream().map(FlowCoreReadMapper::toDTO).collect(Collectors.toList());
    }
}
