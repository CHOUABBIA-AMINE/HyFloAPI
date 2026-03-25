/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyService
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
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
 * Service for FlowAnomaly entities.
 */
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

    public List<FlowAnomalyReadDto> getByReadingId(Long readingId) {
        log.debug("Getting anomalies for reading ID: {}", readingId);
        return flowAnomalyRepository.findByReadingId(readingId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }

    public List<FlowAnomalyReadDto> getByPipelineSegmentId(Long segmentId) {
        log.debug("Getting anomalies for segment ID: {}", segmentId);
        return flowAnomalyRepository.findByPipelineSegmentId(segmentId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }
}
