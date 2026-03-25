/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.3: marked deprecated before Phase 4
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
import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowCoreReadMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.core.repository.FlowAnomalyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>TRANSITIONAL — do not bind new code to this class.</b>
 *
 * <p>FlowAnomaly belongs to the intelligence domain. This generic service
 * is a temporary placeholder kept for controller compatibility only.
 *
 * <p>Phase 4 will replace this with a dedicated anomaly command/query service
 * pair inside the intelligence module. Do NOT add business logic here.
 *
 * @deprecated since v2-phase3 — will be replaced by intelligence domain
 *             command/query services in Phase 4. Scheduled for removal
 *             during controller migration.
 */
@Deprecated(since = "v2-phase3", forRemoval = true)
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAnomalyService extends GenericService<FlowAnomaly, FlowAnomalyReadDto, Long> {

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
    protected FlowAnomalyReadDto toDTO(FlowAnomaly entity) {
        return FlowCoreReadMapper.toDto(entity);
    }

    @Override
    protected FlowAnomaly toEntity(FlowAnomalyReadDto dto) {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly creation");
    }

    @Override
    protected void updateEntityFromDTO(FlowAnomaly entity, FlowAnomalyReadDto dto) {
        throw new UnsupportedOperationException("Use intelligence engine for anomaly updates");
    }

    @Override
    protected Page<FlowAnomalyReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return flowAnomalyRepository.searchByAnyField(query, pageable).map(FlowCoreReadMapper::toDto);
    }

    /** @deprecated use intelligence query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowAnomalyReadDto> getByReadingId(Long readingId) {
        log.debug("Getting anomalies for reading ID: {}", readingId);
        return flowAnomalyRepository.findByReadingId(readingId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }

    /** @deprecated use intelligence query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowAnomalyReadDto> getByPipelineSegmentId(Long segmentId) {
        log.debug("Getting anomalies for segment ID: {}", segmentId);
        return flowAnomalyRepository.findByPipelineSegmentId(segmentId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }
}
