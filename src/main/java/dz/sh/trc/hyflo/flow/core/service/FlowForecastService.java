/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowForecastService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowForecastDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowForecast;
import dz.sh.trc.hyflo.flow.core.repository.FlowForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowForecastService extends GenericService<FlowForecast, FlowForecastDTO, Long> {

    private final FlowForecastRepository flowForecastRepository;

    @Override
    protected JpaRepository<FlowForecast, Long> getRepository() {
        return flowForecastRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowForecast";
    }

    @Override
    protected FlowForecastDTO toDTO(FlowForecast entity) {
        return FlowForecastDTO.fromEntity(entity);
    }

    @Override
    protected FlowForecast toEntity(FlowForecastDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowForecast entity, FlowForecastDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowForecastDTO create(FlowForecastDTO dto) {
        log.info("Creating flow forecast: forecastDate={}, infrastructureId={}, productId={}, operationTypeId={}", 
                 dto.getForecastDate(), dto.getInfrastructureId(), dto.getProductId(), dto.getOperationTypeId());
        
        if (flowForecastRepository.existsByForecastDateAndInfrastructureIdAndProductIdAndOperationTypeId(
                dto.getForecastDate(), dto.getInfrastructureId(), dto.getProductId(), dto.getOperationTypeId())) {
            throw new BusinessValidationException(
                "Flow forecast for this date, infrastructure, product, and operation type combination already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowForecastDTO update(Long id, FlowForecastDTO dto) {
        log.info("Updating flow forecast with ID: {}", id);
        
        if (flowForecastRepository.existsByForecastDateAndInfrastructureIdAndProductIdAndOperationTypeIdAndIdNot(
                dto.getForecastDate(), dto.getInfrastructureId(), dto.getProductId(), dto.getOperationTypeId(), id)) {
            throw new BusinessValidationException(
                "Flow forecast for this date, infrastructure, product, and operation type combination already exists");
        }
        
        return super.update(id, dto);
    }

    public List<FlowForecastDTO> getAll() {
        log.debug("Getting all flow forecasts without pagination");
        return flowForecastRepository.findAll().stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowForecastDTO> findByForecastDate(LocalDate forecastDate) {
        log.debug("Finding flow forecasts by forecast date: {}", forecastDate);
        return flowForecastRepository.findByForecastDate(forecastDate).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowForecastDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Finding flow forecasts by date range: {} to {}", startDate, endDate);
        return flowForecastRepository.findByForecastDateBetween(startDate, endDate).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowForecastDTO> findByInfrastructure(Long infrastructureId) {
        log.debug("Finding flow forecasts by infrastructure id: {}", infrastructureId);
        return flowForecastRepository.findByInfrastructureId(infrastructureId).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowForecastDTO> findByProduct(Long productId) {
        log.debug("Finding flow forecasts by product id: {}", productId);
        return flowForecastRepository.findByProductId(productId).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowForecastDTO> findByOperationType(Long operationTypeId) {
        log.debug("Finding flow forecasts by operation type id: {}", operationTypeId);
        return flowForecastRepository.findByOperationTypeId(operationTypeId).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FlowForecastDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow forecasts by infrastructure {} and date range: {} to {}", 
                  infrastructureId, startDate, endDate);
        return executeQuery(p -> flowForecastRepository.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, p), pageable);
    }

    public Page<FlowForecastDTO> findByProductAndOperationTypeAndDateRange(
            Long productId, Long operationTypeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow forecasts by product {}, operation type {} and date range: {} to {}", 
                  productId, operationTypeId, startDate, endDate);
        return executeQuery(p -> flowForecastRepository.findByProductAndOperationTypeAndDateRange(
                productId, operationTypeId, startDate, endDate, p), pageable);
    }

    public Page<FlowForecastDTO> findCompleted(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding completed flow forecasts (with actual volumes) by date range: {} to {}", 
                  startDate, endDate);
        return executeQuery(p -> flowForecastRepository.findCompletedByDateRange(
                startDate, endDate, p), pageable);
    }

    public Page<FlowForecastDTO> findPending(Pageable pageable) {
        log.debug("Finding pending flow forecasts (future dates without actual volumes)");
        return executeQuery(p -> flowForecastRepository.findPending(LocalDate.now(), p), pageable);
    }
}
