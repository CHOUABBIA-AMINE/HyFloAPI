/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventQueryService
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Replaces   : FlowEventService (GenericService-based, legacy)
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowEventReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowEventMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventQueryService {

    private final FlowEventRepository flowEventRepository;

    public FlowEventReadDto findById(Long id) {
        return flowEventRepository.findById(id)
                .map(FlowEventMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("FlowEvent", id));
    }

    public Page<FlowEventReadDto> findAll(Pageable pageable) {
        return flowEventRepository.findAll(pageable).map(FlowEventMapper::toReadDto);
    }

    public List<FlowEventReadDto> findAll() {
        return flowEventRepository.findAll().stream().map(FlowEventMapper::toReadDto).toList();
    }

    public List<FlowEventReadDto> findByInfrastructure(Long infrastructureId) {
        return flowEventRepository.findByInfrastructureId(infrastructureId)
                .stream().map(FlowEventMapper::toReadDto).toList();
    }

    public List<FlowEventReadDto> findBySeverity(Long severityId) {
        return flowEventRepository.findBySeverityId(severityId)
                .stream().map(FlowEventMapper::toReadDto).toList();
    }

    public List<FlowEventReadDto> findByStatus(Long statusId) {
        return flowEventRepository.findByStatusId(statusId)
                .stream().map(FlowEventMapper::toReadDto).toList();
    }

    public List<FlowEventReadDto> findByImpactOnFlow(Boolean impactOnFlow) {
        return flowEventRepository.findByImpactOnFlow(impactOnFlow)
                .stream().map(FlowEventMapper::toReadDto).toList();
    }

    public List<FlowEventReadDto> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return flowEventRepository.findByEventTimestampBetween(start, end)
                .stream().map(FlowEventMapper::toReadDto).toList();
    }

    public Page<FlowEventReadDto> findByInfrastructureAndTimeRange(
            Long infrastructureId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository.findByInfrastructureAndTimeRange(infrastructureId, start, end, pageable)
                .map(FlowEventMapper::toReadDto);
    }

    public Page<FlowEventReadDto> findBySeverityAndTimeRange(
            Long severityId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository.findBySeverityAndTimeRange(severityId, start, end, pageable)
                .map(FlowEventMapper::toReadDto);
    }

    public Page<FlowEventReadDto> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository.findByStatusAndTimeRange(statusId, start, end, pageable)
                .map(FlowEventMapper::toReadDto);
    }

    public Page<FlowEventReadDto> findWithImpactOnFlowByTimeRange(
            LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository.findWithImpactOnFlowByTimeRange(start, end, pageable)
                .map(FlowEventMapper::toReadDto);
    }

    public Page<FlowEventReadDto> globalSearch(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return findAll(pageable);
        }
        return flowEventRepository.searchByAnyField(searchTerm.trim(), pageable)
                .map(FlowEventMapper::toReadDto);
    }
}
