/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertQueryServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl (Query)
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.service.impl;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.intelligence.dto.FlowAlertReadDTO;
import dz.sh.trc.hyflo.intelligence.mapper.FlowAlertMapper;
import dz.sh.trc.hyflo.intelligence.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.intelligence.service.FlowAlertQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAlertQueryServiceImpl implements FlowAlertQueryService {

    private final FlowAlertRepository flowAlertRepository;

    @Override
    public FlowAlertReadDTO findById(Long id) {
        return flowAlertRepository.findById(id)
                .map(FlowAlertMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowAlert", id));
    }

    @Override
    public Page<FlowAlertReadDTO> findAll(Pageable pageable) {
        return flowAlertRepository.findAll(pageable).map(FlowAlertMapper::toReadDTO);
    }

    @Override
    public List<FlowAlertReadDTO> findAll() {
        return flowAlertRepository.findAll().stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowAlertReadDTO> findByThreshold(Long thresholdId) {
        return flowAlertRepository.findByThresholdId(thresholdId)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowAlertReadDTO> findByFlowReading(Long flowReadingId) {
        return flowAlertRepository.findByFlowReadingId(flowReadingId)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowAlertReadDTO> findByStatus(Long statusId) {
        return flowAlertRepository.findByStatusId(statusId)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowAlertReadDTO> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return flowAlertRepository.findByAlertTimestampBetween(start, end)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    @Override
    public List<FlowAlertReadDTO> findByNotificationSent(Boolean sent) {
        return flowAlertRepository.findByNotificationSent(sent)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    @Override
    public Page<FlowAlertReadDTO> findUnacknowledged(Pageable pageable) {
        return flowAlertRepository.findUnacknowledged(pageable).map(FlowAlertMapper::toReadDTO);
    }

    @Override
    public Page<FlowAlertReadDTO> findUnresolved(Pageable pageable) {
        return flowAlertRepository.findUnresolved(pageable).map(FlowAlertMapper::toReadDTO);
    }

    @Override
    public Page<FlowAlertReadDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        return flowAlertRepository.findUnresolvedByPipeline(pipelineId, pageable)
                .map(FlowAlertMapper::toReadDTO);
    }

    @Override
    public Page<FlowAlertReadDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowAlertRepository.findByStatusAndTimeRange(statusId, start, end, pageable)
                .map(FlowAlertMapper::toReadDTO);
    }

    @Override
    public Page<FlowAlertReadDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowAlertRepository.findByPipelineAndTimeRange(pipelineId, start, end, pageable)
                .map(FlowAlertMapper::toReadDTO);
    }
}
