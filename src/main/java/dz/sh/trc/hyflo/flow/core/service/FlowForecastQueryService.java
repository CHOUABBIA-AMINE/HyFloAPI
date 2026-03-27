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
import dz.sh.trc.hyflo.flow.core.dto.FlowForecastReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowForecastMapper;
import dz.sh.trc.hyflo.flow.core.repository.FlowForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public FlowForecastReadDTO findById(Long id) {
        return flowForecastRepository.findById(id)
                .map(FlowForecastMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("FlowForecast", id));
    }

    public Page<FlowForecastReadDTO> findAll(Pageable pageable) {
        return flowForecastRepository.findAll(pageable).map(FlowForecastMapper::toReadDTO);
    }

    public List<FlowForecastReadDTO> findAll() {
        return flowForecastRepository.findAll().stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    public List<FlowForecastReadDTO> findByForecastDate(LocalDate forecastDate) {
        return flowForecastRepository.findByForecastDate(forecastDate)
                .stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    public List<FlowForecastReadDTO> findByDateRange(LocalDate start, LocalDate end) {
        return flowForecastRepository.findByForecastDateBetween(start, end)
                .stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    public List<FlowForecastReadDTO> findByInfrastructure(Long infrastructureId) {
        return flowForecastRepository.findByInfrastructureId(infrastructureId)
                .stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    public List<FlowForecastReadDTO> findByProduct(Long productId) {
        return flowForecastRepository.findByProductId(productId)
                .stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    public List<FlowForecastReadDTO> findByOperationType(Long operationTypeId) {
        return flowForecastRepository.findByOperationTypeId(operationTypeId)
                .stream().map(FlowForecastMapper::toReadDTO).toList();
    }

    public Page<FlowForecastReadDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate start, LocalDate end, Pageable pageable) {
        return flowForecastRepository.findByInfrastructureAndDateRange(infrastructureId, start, end, pageable)
                .map(FlowForecastMapper::toReadDTO);
    }

    public Page<FlowForecastReadDTO> findByProductAndOperationTypeAndDateRange(
            Long productId, Long operationTypeId, LocalDate start, LocalDate end, Pageable pageable) {
        return flowForecastRepository.findByProductAndOperationTypeAndDateRange(
                        productId, operationTypeId, start, end, pageable)
                .map(FlowForecastMapper::toReadDTO);
    }

    public Page<FlowForecastReadDTO> findCompleted(LocalDate start, LocalDate end, Pageable pageable) {
        return flowForecastRepository.findCompletedByDateRange(start, end, pageable)
                .map(FlowForecastMapper::toReadDTO);
    }

    public Page<FlowForecastReadDTO> findPending(Pageable pageable) {
        return flowForecastRepository.findPending(LocalDate.now(), pageable)
                .map(FlowForecastMapper::toReadDTO);
    }
}
