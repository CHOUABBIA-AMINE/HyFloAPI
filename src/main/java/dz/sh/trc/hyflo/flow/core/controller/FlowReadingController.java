/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowReadingController
 * 	@CreatedOn	: 03-25-2026
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
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.service.FlowReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for FlowReading.
 */
@RestController
@RequestMapping("/flow/core/reading")
@Slf4j
@Tag(name = "Flow Readings", description = "APIs for querying operational flow readings")
@SecurityRequirement(name = "bearer-auth")
public class FlowReadingController extends GenericController<FlowReadingReadDto, Long> {

    private final FlowReadingService flowReadingService;

    public FlowReadingController(FlowReadingService flowReadingService) {
        super(flowReadingService, "FlowReading");
        this.flowReadingService = flowReadingService;
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow reading by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow reading found", content = @Content(schema = @Schema(implementation = FlowReadingReadDto.class))),
        @ApiResponse(responseCode = "404", description = "Flow reading not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<FlowReadingReadDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all flow readings (paginated)")
    public ResponseEntity<Page<FlowReadingReadDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all flow readings (unpaginated)")
    public ResponseEntity<List<FlowReadingReadDto>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search flow readings")
    public ResponseEntity<Page<FlowReadingReadDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Count flow readings")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<FlowReadingReadDto> searchByQuery(String query, Pageable pageable) {
        return flowReadingService.searchByQuery(query, pageable);
    }

    @GetMapping("/pipeline/{pipelineId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow readings by pipeline ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow readings retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<FlowReadingReadDto>> getByPipelineId(
            @Parameter(description = "Pipeline ID", required = true) @PathVariable Long pipelineId) {
        log.debug("GET /flow/core/reading/pipeline/{} - Getting readings by pipeline", pipelineId);
        return success(flowReadingService.getByPipelineId(pipelineId));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow readings by date range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow readings retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<FlowReadingReadDto>> getByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /flow/core/reading/date-range - from={} to={}", from, to);
        return success(flowReadingService.getByDateRange(from, to));
    }

    @GetMapping("/pipeline/{pipelineId}/date-range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow readings by pipeline ID and date range")
    public ResponseEntity<List<FlowReadingReadDto>> getByPipelineAndDateRange(
            @PathVariable Long pipelineId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /flow/core/reading/pipeline/{}/date-range from={} to={}", pipelineId, from, to);
        return success(flowReadingService.getByPipelineAndDateRange(pipelineId, from, to));
    }
}
