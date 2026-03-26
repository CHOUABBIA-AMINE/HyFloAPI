/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastQueryService
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Replaces   : FlowForecastService (GenericService-based, legacy)
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowForecastReadDto;
import dz.sh.trc.hyflo.flow.core.mapper.FlowForecastMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowForecastQueryService {

    private final FlowForecastRepository flowForecastRepository;

    public FlowForecastReadDto findById(Long id) {
        return flowForecastRepository.findById(id)
                .map(FlowForecastMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("FlowForecast", id));
    }

    public Page<FlowForecastReadDto> findAll(Pageable pageable) {
        return flowForecastRepository.findAll(pageable).map(FlowForecastMapper::toReadDto);
    }

    public List<FlowForecastReadDto> findAll() {
        return flowForecastRepository.findAll().stream().map(FlowForecastMapper::toReadDto).toList();
    }

    public List<FlowForecastReadDto> findByForecastDate(LocalDate forecastDate) {
        return flowForecastRepository.findByForecastDate(forecastDate)
                .stream().map(FlowForecastMapper::toReadDto).toList();
    }

    public List<FlowForecastReadDto> findByDateRange(LocalDate start, LocalDate end) {
        return flowForecastRepository.findByForecastDateBetween(start, end)
                .stream().map(FlowForecastMapper::toReadDto).toList();
    }

    public List<FlowForecastReadDto> findByInfrastructure(Long infrastructureId) {
        return flowForecastRepository.findByInfrastructureId(infrastructureId)
                .stream().map(FlowForecastMapper::toReadDto).toList();
    }

    public List<FlowForecastReadDto> findByProduct(Long productId) {
        return flowForecastRepository.findByProductId(productId)
                .stream().map(FlowForecastMapper::toReadDto).toList();
    }

    public List<FlowForecastReadDto> findByOperationType(Long operationTypeId) {
        return flowForecastRepository.findByOperationTypeId(operationTypeId)
                .stream().map(FlowForecastMapper::toReadDto).toList();
    }

    public Page<FlowForecastReadDto> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate start, LocalDate end, Pageable pageable) {
        return flowForecastRepository.findByInfrastructureAndDateRange(infrastructureId, start, end, pageable)
                .map(FlowForecastMapper::toReadDto);
    }

    public Page<FlowForecastReadDto> findByProductAndOperationTypeAndDateRange(
            Long productId, Long operationTypeId, LocalDate start, LocalDate end, Pageable pageable) {
        return flowForecastRepository.findByProductAndOperationTypeAndDateRange(
                        productId, operationTypeId, start, end, pageable)
                .map(FlowForecastMapper::toReadDto);
    }

    public Page<FlowForecastReadDto> findCompleted(LocalDate start, LocalDate end, Pageable pageable) {
        return flowForecastRepository.findCompletedByDateRange(start, end, pageable)
                .map(FlowForecastMapper::toReadDto);
    }

    public Page<FlowForecastReadDto> findPending(Pageable pageable) {
        return flowForecastRepository.findPending(LocalDate.now(), pageable)
                .map(FlowForecastMapper::toReadDto);
    }
}
