/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAlertQueryService
 *  @CreatedOn  : 03-26-2026
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
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowAlertMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
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
public class FlowAlertQueryService {

    private final FlowAlertRepository flowAlertRepository;

    public FlowAlertReadDto findById(Long id) {
        return flowAlertRepository.findById(id)
                .map(FlowAlertMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("FlowAlert", id));
    }

    public Page<FlowAlertReadDto> findAll(Pageable pageable) {
        return flowAlertRepository.findAll(pageable).map(FlowAlertMapper::toReadDto);
    }

    public List<FlowAlertReadDto> findAll() {
        return flowAlertRepository.findAll().stream().map(FlowAlertMapper::toReadDto).toList();
    }

    public List<FlowAlertReadDto> findByThreshold(Long thresholdId) {
        return flowAlertRepository.findByThresholdId(thresholdId)
                .stream().map(FlowAlertMapper::toReadDto).toList();
    }

    public List<FlowAlertReadDto> findByFlowReading(Long flowReadingId) {
        return flowAlertRepository.findByFlowReadingId(flowReadingId)
                .stream().map(FlowAlertMapper::toReadDto).toList();
    }

    public List<FlowAlertReadDto> findByStatus(Long statusId) {
        return flowAlertRepository.findByStatusId(statusId)
                .stream().map(FlowAlertMapper::toReadDto).toList();
    }

    public List<FlowAlertReadDto> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return flowAlertRepository.findByAlertTimestampBetween(start, end)
                .stream().map(FlowAlertMapper::toReadDto).toList();
    }

    public List<FlowAlertReadDto> findByNotificationSent(Boolean sent) {
        return flowAlertRepository.findByNotificationSent(sent)
                .stream().map(FlowAlertMapper::toReadDto).toList();
    }

    public Page<FlowAlertReadDto> findUnacknowledged(Pageable pageable) {
        return flowAlertRepository.findUnacknowledged(pageable).map(FlowAlertMapper::toReadDto);
    }

    public Page<FlowAlertReadDto> findUnresolved(Pageable pageable) {
        return flowAlertRepository.findUnresolved(pageable).map(FlowAlertMapper::toReadDto);
    }

    public Page<FlowAlertReadDto> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        return flowAlertRepository.findUnresolvedByPipeline(pipelineId, pageable).map(FlowAlertMapper::toReadDto);
    }

    public Page<FlowAlertReadDto> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowAlertRepository.findByStatusAndTimeRange(statusId, start, end, pageable)
                .map(FlowAlertMapper::toReadDto);
    }

    public Page<FlowAlertReadDto> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return flowAlertRepository.findByPipelineAndTimeRange(pipelineId, start, end, pageable)
                .map(FlowAlertMapper::toReadDto);
    }

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }
}
