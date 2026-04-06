/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventQueryServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl (Query)
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowEventReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowEventMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowEventQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventQueryServiceImpl implements FlowEventQueryService {

    private final FlowEventRepository flowEventRepository;

    @Override
    public FlowEventReadDTO findById(Long id) {
        return flowEventRepository.findById(id)
                .map(FlowEventMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowEvent", id));
    }

    @Override
    public Page<FlowEventReadDTO> findAll(Pageable pageable) {
        return flowEventRepository.findAll(pageable).map(FlowEventMapper::toReadDTO);
    }

    @Override
    public List<FlowEventReadDTO> findAll() {
        return flowEventRepository.findAll().stream().map(FlowEventMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowEventReadDTO> findByInfrastructure(Long infrastructureId) {
        return flowEventRepository.findByInfrastructureId(infrastructureId)
                .stream().map(FlowEventMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowEventReadDTO> findBySeverity(Long severityId) {
        return flowEventRepository.findBySeverityId(severityId)
                .stream().map(FlowEventMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowEventReadDTO> findByStatus(Long statusId) {
        return flowEventRepository.findByStatusId(statusId)
                .stream().map(FlowEventMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowEventReadDTO> findByImpactOnFlow(Boolean impactOnFlow) {
        return flowEventRepository.findByImpactOnFlow(impactOnFlow)
                .stream().map(FlowEventMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowEventReadDTO> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return flowEventRepository.findByEventTimestampBetween(start, end)
                .stream().map(FlowEventMapper::toReadDTO).toList();
    }

    @Override
    public Page<FlowEventReadDTO> findByInfrastructureAndTimeRange(
            Long infrastructureId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository
                .findByInfrastructureAndTimeRange(infrastructureId, start, end, pageable)
                .map(FlowEventMapper::toReadDTO);
    }

    @Override
    public Page<FlowEventReadDTO> findBySeverityAndTimeRange(
            Long severityId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository
                .findBySeverityAndTimeRange(severityId, start, end, pageable)
                .map(FlowEventMapper::toReadDTO);
    }

    @Override
    public Page<FlowEventReadDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository
                .findByStatusAndTimeRange(statusId, start, end, pageable)
                .map(FlowEventMapper::toReadDTO);
    }

    @Override
    public Page<FlowEventReadDTO> findWithImpactOnFlowByTimeRange(
            LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowEventRepository
                .findWithImpactOnFlowByTimeRange(start, end, pageable)
                .map(FlowEventMapper::toReadDTO);
    }

    @Override
    public Page<FlowEventReadDTO> globalSearch(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return findAll(pageable);
        }
        return flowEventRepository.searchByAnyField(searchTerm.trim(), pageable)
                .map(FlowEventMapper::toReadDTO);
    }
}
