/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanQueryServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Query Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Query implementation for FlowPlan read operations.
 *                All reads use FlowPlanMapper. No entity types exposed.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowPlanReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowPlanMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowPlanRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowPlanQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowPlanQueryServiceImpl implements FlowPlanQueryService {

    private final FlowPlanRepository flowPlanRepository;

    @Override
    public FlowPlanReadDTO getById(Long id) {
        log.debug("getById: {}", id);
        return flowPlanRepository.findById(id)
                .map(FlowPlanMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowPlan not found: " + id));
    }

    @Override
    public Page<FlowPlanReadDTO> getAll(Pageable pageable) {
        log.debug("getAll pageable={}", pageable);
        return flowPlanRepository.findAll(pageable)
                .map(FlowPlanMapper::toReadDTO);
    }

    @Override
    public List<FlowPlanReadDTO> getByPipeline(Long pipelineId) {
        log.debug("getByPipeline: {}", pipelineId);
        return flowPlanRepository.findByPipelineId(pipelineId)
                .stream().map(FlowPlanMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public Page<FlowPlanReadDTO> getByPipelinePaged(Long pipelineId, Pageable pageable) {
        log.debug("getByPipelinePaged: {} pageable={}", pipelineId, pageable);
        return flowPlanRepository.findByPipelineId(pipelineId, pageable)
                .map(FlowPlanMapper::toReadDTO);
    }

    @Override
    public List<FlowPlanReadDTO> getByPipelineAndDate(Long pipelineId, LocalDate date) {
        log.debug("getByPipelineAndDate: {} date={}", pipelineId, date);
        return flowPlanRepository.findByPipelineIdAndPlanDate(pipelineId, date)
                .stream().map(FlowPlanMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowPlanReadDTO> getByPipelineAndDateRange(
            Long pipelineId, LocalDate from, LocalDate to) {
        log.debug("getByPipelineAndDateRange: {} from={} to={}", pipelineId, from, to);
        return flowPlanRepository.findByPipelineIdAndPlanDateBetween(pipelineId, from, to)
                .stream().map(FlowPlanMapper::toReadDTO).collect(Collectors.toList());
    }

    @Override
    public Page<FlowPlanReadDTO> getApprovedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("getApprovedByPipeline: {} pageable={}", pipelineId, pageable);
        return flowPlanRepository.findApprovedByPipeline(pipelineId, pageable)
                .map(FlowPlanMapper::toReadDTO);
    }

    @Override
    public Optional<FlowPlanReadDTO> getLatestApproved(Long pipelineId, LocalDate date) {
        log.debug("getLatestApproved: {} date={}", pipelineId, date);
        return flowPlanRepository.findLatestApprovedByPipelineAndDate(pipelineId, date)
                .map(FlowPlanMapper::toReadDTO);
    }

    @Override
    public Page<FlowPlanReadDTO> getByScenario(String scenarioCode, Pageable pageable) {
        log.debug("getByScenario: {} pageable={}", scenarioCode, pageable);
        return flowPlanRepository.findByScenarioCode(scenarioCode, pageable)
                .map(FlowPlanMapper::toReadDTO);
    }
}
