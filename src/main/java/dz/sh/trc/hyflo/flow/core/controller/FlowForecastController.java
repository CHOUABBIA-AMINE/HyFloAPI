/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowForecastController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowForecastDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowForecastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flow/core/forecast")
@Tag(name = "Flow Forecast Management", description = "APIs for managing and analyzing flow forecasts and predictions")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class FlowForecastController extends GenericController<FlowForecastDTO, Long> {

    private final FlowForecastService flowForecastService;
    
    public FlowForecastController(FlowForecastService flowForecastService) {
        super(flowForecastService, "FlowForecast");
        this.flowForecastService = flowForecastService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get flow forecast by ID", description = "Retrieves a single flow forecast by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow forecast found", content = @Content(schema = @Schema(implementation = FlowForecastDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flow forecast not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<FlowForecastDTO> getById(
            @Parameter(description = "Flow forecast ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all flow forecasts (paginated)", description = "Retrieves a paginated list of all flow forecasts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow forecasts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all flow forecasts (unpaginated)", description = "Retrieves all flow forecasts without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow forecasts retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create flow forecast", description = "Creates a new flow forecast")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Flow forecast created successfully", content = @Content(schema = @Schema(implementation = FlowForecastDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:WRITE')")
    public ResponseEntity<FlowForecastDTO> create(
            @Parameter(description = "Flow forecast data", required = true) 
            @Valid @RequestBody FlowForecastDTO dto) {
        FlowForecastDTO created = flowForecastService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update flow forecast", description = "Updates an existing flow forecast")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow forecast updated successfully", content = @Content(schema = @Schema(implementation = FlowForecastDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Flow forecast not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:WRITE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:WRITE')")
    public ResponseEntity<FlowForecastDTO> update(
            @Parameter(description = "Flow forecast ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated flow forecast data", required = true) @Valid @RequestBody FlowForecastDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete flow forecast", description = "Deletes a flow forecast permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Flow forecast deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Flow forecast not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Flow forecast ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search flow forecasts", description = "Searches flow forecasts by notes and other criteria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if flow forecast exists", description = "Checks if a flow forecast with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Flow forecast ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count flow forecasts", description = "Returns the total number of flow forecasts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow forecast count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/date/{date}")
    @Operation(summary = "Get forecasts by date", description = "Retrieves all forecasts for a specific date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByForecastDate(
            @Parameter(description = "Forecast date (ISO 8601)", required = true, example = "2026-06-15") 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET /flow/core/forecast/date/{} - Getting forecasts by date", date);
        return ResponseEntity.ok(flowForecastService.findByForecastDate(date));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get forecasts by date range", description = "Retrieves forecasts within a specified date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByDateRange(
            @Parameter(description = "Start date (ISO 8601)", required = true, example = "2026-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (ISO 8601)", required = true, example = "2026-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/core/forecast/date-range - Getting forecasts from {} to {}", startDate, endDate);
        return ResponseEntity.ok(flowForecastService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    @Operation(summary = "Get forecasts by infrastructure", description = "Retrieves all forecasts for a specific infrastructure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByInfrastructure(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") 
            @PathVariable Long infrastructureId) {
        log.info("GET /flow/core/forecast/infrastructure/{} - Getting forecasts by infrastructure", infrastructureId);
        return ResponseEntity.ok(flowForecastService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get forecasts by product", description = "Retrieves all forecasts for a specific product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByProduct(
            @Parameter(description = "Product ID", required = true, example = "1") 
            @PathVariable Long productId) {
        log.info("GET /flow/core/forecast/product/{} - Getting forecasts by product", productId);
        return ResponseEntity.ok(flowForecastService.findByProduct(productId));
    }

    @GetMapping("/operation-type/{operationTypeId}")
    @Operation(summary = "Get forecasts by operation type", description = "Retrieves all forecasts for a specific operation type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Operation type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<List<FlowForecastDTO>> getByOperationType(
            @Parameter(description = "Operation type ID", required = true, example = "1") 
            @PathVariable Long operationTypeId) {
        log.info("GET /flow/core/forecast/operation-type/{} - Getting forecasts by operation type", operationTypeId);
        return ResponseEntity.ok(flowForecastService.findByOperationType(operationTypeId));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending forecasts", description = "Retrieves all forecasts that are still pending (not yet occurred)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pending forecasts retrieved"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getPending(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/forecast/pending - Getting pending forecasts");
        return ResponseEntity.ok(flowForecastService.findPending(
                buildPageable(page, size, "forecastDate", "asc")));
    }

    @GetMapping("/completed")
    @Operation(summary = "Get completed forecasts", description = "Retrieves forecasts that have occurred within a date range, sorted by accuracy")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Completed forecasts retrieved"),
        @ApiResponse(responseCode = "400", description = "Invalid date range"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getCompleted(
            @Parameter(description = "Start date (ISO 8601)", required = true, example = "2026-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (ISO 8601)", required = true, example = "2026-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size) {
        log.info("GET /flow/core/forecast/completed - Getting completed forecasts from {} to {}", startDate, endDate);
        return ResponseEntity.ok(flowForecastService.findCompleted(
                startDate, endDate, buildPageable(page, size, "accuracy", "desc")));
    }

    @GetMapping("/infrastructure/{infrastructureId}/date-range")
    @Operation(summary = "Get forecasts by infrastructure and date range", description = "Retrieves forecasts for an infrastructure within a date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Forecasts retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date range"),
        @ApiResponse(responseCode = "404", description = "Infrastructure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_FORECAST:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('FLOW_FORECAST:READ')")
    public ResponseEntity<Page<FlowForecastDTO>> getByInfrastructureAndDateRange(
            @Parameter(description = "Infrastructure ID", required = true, example = "1") @PathVariable Long infrastructureId,
            @Parameter(description = "Start date (ISO 8601)", required = true, example = "2026-01-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (ISO 8601)", required = true, example = "2026-12-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "forecastDate") @RequestParam(defaultValue = "forecastDate") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("GET /flow/core/forecast/infrastructure/{}/date-range - Getting forecasts from {} to {}", 
                 infrastructureId, startDate, endDate);
        return ResponseEntity.ok(flowForecastService.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }
}
