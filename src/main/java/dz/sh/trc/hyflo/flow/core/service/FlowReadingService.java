/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowReadingService
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowCoreReadMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for FlowReading entities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingService extends GenericService<FlowReading, FlowReadingReadDto, Long> {

    private final FlowReadingRepository flowReadingRepository;

    @Override
    protected JpaRepository<FlowReading, Long> getRepository() {
        return flowReadingRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowReading";
    }

    @Override
    protected FlowReadingReadDto toDTO(FlowReading entity) {
        return FlowCoreReadMapper.toDto(entity);
    }

    @Override
    protected FlowReading toEntity(FlowReadingReadDto dto) {
        // Write operations via dedicated command service in Phase 2.
        throw new UnsupportedOperationException("Use FlowReadingCommandService for write operations");
    }

    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingReadDto dto) {
        throw new UnsupportedOperationException("Use FlowReadingCommandService for update operations");
    }

    @Override
    protected Page<FlowReadingReadDto> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAll(pageable);
        }
        return flowReadingRepository.searchByAnyField(query, pageable).map(FlowCoreReadMapper::toDto);
    }

    public List<FlowReadingReadDto> getByPipelineId(Long pipelineId) {
        log.debug("Getting flow readings for pipeline ID: {}", pipelineId);
        return flowReadingRepository.findByPipelineId(pipelineId)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }

    public List<FlowReadingReadDto> getByDateRange(LocalDate from, LocalDate to) {
        log.debug("Getting flow readings from {} to {}", from, to);
        return flowReadingRepository.findByReadingDateBetween(from, to)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }

    public List<FlowReadingReadDto> getByPipelineAndDateRange(Long pipelineId, LocalDate from, LocalDate to) {
        log.debug("Getting flow readings for pipeline {} from {} to {}", pipelineId, from, to);
        return flowReadingRepository.findByPipelineIdAndReadingDateBetween(pipelineId, from, to)
                .stream().map(FlowCoreReadMapper::toDto).collect(Collectors.toList());
    }
}
