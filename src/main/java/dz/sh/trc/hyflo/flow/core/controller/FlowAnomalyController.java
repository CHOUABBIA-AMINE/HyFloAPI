/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowAnomalyController
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: Phase 4/5 bridge — Commit 36.3
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowAnomalyService;
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
 * REST Controller for FlowAnomaly.
 *
 * @deprecated Since HyFlo v2 Phase 4.
 *             Backing services are deprecated and lack CQRS separation.
 *             A dedicated FlowAnomalyV2Controller will be introduced in Phase 5
 *             backed by proper intelligence service interfaces.
 *             Scheduled for removal in Phase 8 cleanup.
 */
@Deprecated(since = "v2-phase4", forRemoval = true)
@RestController
@RequestMapping("/flow/core/anomaly")
@Slf4j
@Tag(name = "Flow Anomalies", description = "APIs for querying flow anomalies detected by the intelligence engine")
@SecurityRequirement(name = "bearer-auth")
public class FlowAnomalyController extends GenericController<FlowAnomalyReadDTO, Long> {

    private final FlowAnomalyService flowAnomalyService;

    public FlowAnomalyController(FlowAnomalyService flowAnomalyService) {
        super(flowAnomalyService, "FlowAnomaly");
        this.flowAnomalyService = flowAnomalyService;
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomaly by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Anomaly found", content = @Content(schema = @Schema(implementation = FlowAnomalyReadDTO.class))),
        @ApiResponse(responseCode = "404", description = "Anomaly not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<FlowAnomalyReadDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all anomalies (paginated)")
    public ResponseEntity<Page<FlowAnomalyReadDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all anomalies (unpaginated)")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search anomalies")
    public ResponseEntity<Page<FlowAnomalyReadDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Count anomalies")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<FlowAnomalyReadDTO> searchByQuery(String query, Pageable pageable) {
        return flowAnomalyService.searchByQuery(query, pageable);
    }

    @GetMapping("/reading/{readingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomalies by reading ID")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getByReadingId(
            @Parameter(description = "Reading ID", required = true) @PathVariable Long readingId) {
        log.debug("GET /flow/core/anomaly/reading/{}", readingId);
        return success(flowAnomalyService.getByReadingId(readingId));
    }

    @GetMapping("/segment/{segmentId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get anomalies by pipeline segment ID")
    public ResponseEntity<List<FlowAnomalyReadDTO>> getBySegmentId(
            @Parameter(description = "Segment ID", required = true) @PathVariable Long segmentId) {
        log.debug("GET /flow/core/anomaly/segment/{}", segmentId);
        return success(flowAnomalyService.getByPipelineSegmentId(segmentId));
    }
}
