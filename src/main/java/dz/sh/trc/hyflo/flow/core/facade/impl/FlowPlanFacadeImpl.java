/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanFacadeImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Facade / Impl
 *  @Package    : Flow / Core / Facade / Impl
 *
 *  @Description: Implements FlowPlanFacade.
 *                Provides cross-module read-only access to FlowPlan data
 *                for the flow.intelligence layer.
 *                Uses FlowPlanRepository and maps to FlowPlanFacadeDTO.
 *                No entity types exposed outside flow.core.
 *
 **/

package dz.sh.trc.hyflo.flow.core.facade.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.facade.FlowPlanFacade;
import dz.sh.trc.hyflo.flow.core.model.FlowPlan;
import dz.sh.trc.hyflo.flow.core.repository.FlowPlanRepository;
import dz.sh.trc.hyflo.intelligence.dto.facade.FlowPlanFacadeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowPlanFacadeImpl implements FlowPlanFacade {

    private final FlowPlanRepository flowPlanRepository;

    @Override
    public List<FlowPlanFacadeDTO> findByPipelineAndDate(Long pipelineId, LocalDate date) {
        log.debug("FlowPlanFacadeImpl.findByPipelineAndDate pipelineId={} date={}", pipelineId, date);
        return flowPlanRepository.findByPipelineIdAndPlanDate(pipelineId, date)
                .stream().map(this::toFacadeDTO).collect(Collectors.toList());
    }

    @Override
    public List<FlowPlanFacadeDTO> findByPipelineAndDateRange(
            Long pipelineId, LocalDate from, LocalDate to) {
        log.debug("FlowPlanFacadeImpl.findByPipelineAndDateRange pipelineId={} from={} to={}",
                pipelineId, from, to);
        return flowPlanRepository.findByPipelineIdAndPlanDateBetween(pipelineId, from, to)
                .stream().map(this::toFacadeDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<FlowPlanFacadeDTO> findLatestApprovedByPipelineAndDate(
            Long pipelineId, LocalDate date) {
        log.debug("FlowPlanFacadeImpl.findLatestApprovedByPipelineAndDate pipelineId={} date={}",
                pipelineId, date);
        return flowPlanRepository.findLatestApprovedByPipelineAndDate(pipelineId, date)
                .map(this::toFacadeDTO);
    }

    @Override
    public List<FlowPlanFacadeDTO> findApprovedByPipeline(Long pipelineId) {
        log.debug("FlowPlanFacadeImpl.findApprovedByPipeline pipelineId={}", pipelineId);
        return flowPlanRepository.findApprovedByPipeline(pipelineId, Pageable.unpaged())
                .stream().map(this::toFacadeDTO).collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Mapper
    // -------------------------------------------------------------------------

    private FlowPlanFacadeDTO toFacadeDTO(FlowPlan entity) {
        return FlowPlanFacadeDTO.builder()
                .id(entity.getId())
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .planDate(entity.getPlanDate())
                .scenarioCode(entity.getScenarioCode())
                .plannedVolumeM3(entity.getPlannedVolumeM3())
                .plannedVolumeMscf(entity.getPlannedVolumeMscf())
                .plannedInletPressureBar(entity.getPlannedInletPressureBar())
                .plannedOutletPressureBar(entity.getPlannedOutletPressureBar())
                .plannedTemperatureCelsius(entity.getPlannedTemperatureCelsius())
                .statusCode(entity.getStatus() != null ? entity.getStatus().getCode() : null)
                .approvedAt(entity.getApprovedAt())
                .build();
    }
}
