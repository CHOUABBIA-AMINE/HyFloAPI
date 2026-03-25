/**
 *
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : FlowForecastService
 *  @CreatedOn  : 01-23-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 26.3: marked deprecated before Phase 4
 *
 *  @Type       : Class
 *  @Layer      : Service (TRANSITIONAL GENERIC — DO NOT EXTEND)
 *  @Package    : Flow / Core
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
import dz.sh.trc.hyflo.flow.core.dto.FlowForecastDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowForecast;
import dz.sh.trc.hyflo.flow.core.repository.FlowForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>TRANSITIONAL — do not bind new code to this class.</b>
 *
 * <p>FlowForecast belongs to the intelligence domain. This generic service
 * is a temporary placeholder kept for controller compatibility only.
 *
 * <p>Phase 4 will replace this with a dedicated forecasting command/query
 * service pair inside the intelligence module, designed to support time-series
 * forecasting and AI model result ingestion.
 * Do NOT add business logic here.
 *
 * <p><b>Note:</b> This class still uses {@code FlowForecastDTO.fromEntity()}
 * in read paths. This is a known pre-Phase-4 limitation. Phase 4 will
 * introduce a {@code ForecastResultMapper} and proper read DTO.
 *
 * @deprecated since v2-phase3 — will be replaced by intelligence domain
 *             forecasting command/query services in Phase 4. Scheduled
 *             for removal during controller migration.
 */
@Deprecated(since = "v2-phase3", forRemoval = true)
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

    /** @deprecated use intelligence forecasting command service */
    @Override
    @Transactional
    @Deprecated(since = "v2-phase3", forRemoval = true)
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

    /** @deprecated use intelligence forecasting command service */
    @Override
    @Transactional
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public FlowForecastDTO update(Long id, FlowForecastDTO dto) {
        log.info("Updating flow forecast with ID: {}", id);

        if (flowForecastRepository.existsByForecastDateAndInfrastructureIdAndProductIdAndOperationTypeIdAndIdNot(
                dto.getForecastDate(), dto.getInfrastructureId(), dto.getProductId(), dto.getOperationTypeId(), id)) {
            throw new BusinessValidationException(
                    "Flow forecast for this date, infrastructure, product, and operation type combination already exists");
        }

        return super.update(id, dto);
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowForecastDTO> getAll() {
        log.debug("Getting all flow forecasts without pagination");
        return flowForecastRepository.findAll().stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowForecastDTO> findByForecastDate(LocalDate forecastDate) {
        log.debug("Finding flow forecasts by forecast date: {}", forecastDate);
        return flowForecastRepository.findByForecastDate(forecastDate).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowForecastDTO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Finding flow forecasts by date range: {} to {}", startDate, endDate);
        return flowForecastRepository.findByForecastDateBetween(startDate, endDate).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowForecastDTO> findByInfrastructure(Long infrastructureId) {
        log.debug("Finding flow forecasts by infrastructure id: {}", infrastructureId);
        return flowForecastRepository.findByInfrastructureId(infrastructureId).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowForecastDTO> findByProduct(Long productId) {
        log.debug("Finding flow forecasts by product id: {}", productId);
        return flowForecastRepository.findByProductId(productId).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public List<FlowForecastDTO> findByOperationType(Long operationTypeId) {
        log.debug("Finding flow forecasts by operation type id: {}", operationTypeId);
        return flowForecastRepository.findByOperationTypeId(operationTypeId).stream()
                .map(FlowForecastDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowForecastDTO> findByInfrastructureAndDateRange(
            Long infrastructureId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow forecasts by infrastructure {} and date range: {} to {}",
                infrastructureId, startDate, endDate);
        return executeQuery(p -> flowForecastRepository.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, p), pageable);
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowForecastDTO> findByProductAndOperationTypeAndDateRange(
            Long productId, Long operationTypeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding flow forecasts by product {}, operation type {} and date range: {} to {}",
                productId, operationTypeId, startDate, endDate);
        return executeQuery(p -> flowForecastRepository.findByProductAndOperationTypeAndDateRange(
                productId, operationTypeId, startDate, endDate, p), pageable);
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowForecastDTO> findCompleted(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Finding completed flow forecasts by date range: {} to {}", startDate, endDate);
        return executeQuery(p -> flowForecastRepository.findCompletedByDateRange(
                startDate, endDate, p), pageable);
    }

    /** @deprecated use intelligence forecasting query service */
    @Deprecated(since = "v2-phase3", forRemoval = true)
    public Page<FlowForecastDTO> findPending(Pageable pageable) {
        log.debug("Finding pending flow forecasts (future dates without actual volumes)");
        return executeQuery(p -> flowForecastRepository.findPending(LocalDate.now(), p), pageable);
    }
}
