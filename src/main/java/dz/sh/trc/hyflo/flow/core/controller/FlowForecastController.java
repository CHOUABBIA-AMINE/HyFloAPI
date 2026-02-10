/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowForecastController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowForecastDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowForecastService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/forecast")
@Slf4j
public class FlowForecastController extends GenericController<FlowForecastDTO, Long> {

    private final FlowForecastService flowForecastService;
    
    public FlowForecastController(FlowForecastService flowForecastService) {
        super(flowForecastService, "FlowForecast");
        this.flowForecastService = flowForecastService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<FlowForecastDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:WRITE')")
    public ResponseEntity<FlowForecastDTO> create(@Valid @RequestBody FlowForecastDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:WRITE')")
    public ResponseEntity<FlowForecastDTO> update(@PathVariable Long id, @Valid @RequestBody FlowForecastDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByForecastDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET /flow/core/forecast/date/{} - Getting forecasts by date", date);
        return ResponseEntity.ok(flowForecastService.findByForecastDate(date));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/core/forecast/date-range - Getting forecasts from {} to {}", startDate, endDate);
        return ResponseEntity.ok(flowForecastService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByInfrastructure(@PathVariable Long infrastructureId) {
        log.info("GET /flow/core/forecast/infrastructure/{} - Getting forecasts by infrastructure", infrastructureId);
        return ResponseEntity.ok(flowForecastService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByProduct(@PathVariable Long productId) {
        log.info("GET /flow/core/forecast/product/{} - Getting forecasts by product", productId);
        return ResponseEntity.ok(flowForecastService.findByProduct(productId));
    }

    @GetMapping("/operation-type/{operationTypeId}")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByOperationType(@PathVariable Long operationTypeId) {
        log.info("GET /flow/core/forecast/operation-type/{} - Getting forecasts by operation type", operationTypeId);
        return ResponseEntity.ok(flowForecastService.findByOperationType(operationTypeId));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/forecast/pending - Getting pending forecasts");
        return ResponseEntity.ok(flowForecastService.findPending(
                buildPageable(page, size, "forecastDate", "asc")));
    }

    @GetMapping("/completed")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getCompleted(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/forecast/completed - Getting completed forecasts from {} to {}", startDate, endDate);
        return ResponseEntity.ok(flowForecastService.findCompleted(
                startDate, endDate, buildPageable(page, size, "accuracy", "desc")));
    }

    @GetMapping("/infrastructure/{infrastructureId}/date-range")
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getByInfrastructureAndDateRange(
            @PathVariable Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "forecastDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("GET /flow/core/forecast/infrastructure/{}/date-range - Getting forecasts from {} to {}", 
                 infrastructureId, startDate, endDate);
        return ResponseEntity.ok(flowForecastService.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }
}
