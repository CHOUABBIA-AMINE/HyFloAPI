/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertQueryService
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-26-2026 — remove unused private buildPageable()
 *
 *  @Type       : Class
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Replaces   : FlowAlertService (GenericService-based, legacy)
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowAlertMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
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
public class FlowAlertQueryService {

    private final FlowAlertRepository flowAlertRepository;

    public FlowAlertReadDTO findById(Long id) {
        return flowAlertRepository.findById(id)
                .map(FlowAlertMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowAlert", id));
    }

    public Page<FlowAlertReadDTO> findAll(Pageable pageable) {
        return flowAlertRepository.findAll(pageable).map(FlowAlertMapper::toReadDTO);
    }

    public List<FlowAlertReadDTO> findAll() {
        return flowAlertRepository.findAll().stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    public List<FlowAlertReadDTO> findByThreshold(Long thresholdId) {
        return flowAlertRepository.findByThresholdId(thresholdId)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    public List<FlowAlertReadDTO> findByFlowReading(Long flowReadingId) {
        return flowAlertRepository.findByFlowReadingId(flowReadingId)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    public List<FlowAlertReadDTO> findByStatus(Long statusId) {
        return flowAlertRepository.findByStatusId(statusId)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    public List<FlowAlertReadDTO> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return flowAlertRepository.findByAlertTimestampBetween(start, end)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    public List<FlowAlertReadDTO> findByNotificationSent(Boolean sent) {
        return flowAlertRepository.findByNotificationSent(sent)
                .stream().map(FlowAlertMapper::toReadDTO).toList();
    }

    public Page<FlowAlertReadDTO> findUnacknowledged(Pageable pageable) {
        return flowAlertRepository.findUnacknowledged(pageable).map(FlowAlertMapper::toReadDTO);
    }

    public Page<FlowAlertReadDTO> findUnresolved(Pageable pageable) {
        return flowAlertRepository.findUnresolved(pageable).map(FlowAlertMapper::toReadDTO);
    }

    public Page<FlowAlertReadDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        return flowAlertRepository.findUnresolvedByPipeline(pipelineId, pageable).map(FlowAlertMapper::toReadDTO);
    }

    public Page<FlowAlertReadDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowAlertRepository.findByStatusAndTimeRange(statusId, start, end, pageable)
                .map(FlowAlertMapper::toReadDTO);
    }

    public Page<FlowAlertReadDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowAlertRepository.findByPipelineAndTimeRange(pipelineId, start, end, pageable)
                .map(FlowAlertMapper::toReadDTO);
    }
}
