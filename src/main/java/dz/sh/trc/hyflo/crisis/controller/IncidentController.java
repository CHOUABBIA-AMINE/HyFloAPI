/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentController
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Removed GenericController inheritance; inject IIncidentQueryService
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.crisis.dto.query.IncidentReadDto;
import dz.sh.trc.hyflo.crisis.service.IIncidentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for Incident — read-only endpoints.
 */
@RestController
@RequestMapping("/crisis/incident")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Incidents", description = "APIs for querying hydrocarbon transport incidents")
@SecurityRequirement(name = "bearer-auth")
public class IncidentController {

    private final IIncidentQueryService incidentService;

    // ========== READ ==========

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get incident by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incident found",
                content = @Content(schema = @Schema(implementation = IncidentReadDto.class))),
        @ApiResponse(responseCode = "404", description = "Incident not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<IncidentReadDto> getById(@PathVariable Long id) {
        log.debug("GET /crisis/incident/{}", id);
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all incidents (paginated)")
    public ResponseEntity<Page<IncidentReadDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /crisis/incident?page={}&size={}", page, size);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(incidentService.getAll(pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all incidents (unpaginated)")
    public ResponseEntity<List<IncidentReadDto>> getAllUnpaginated() {
        log.debug("GET /crisis/incident/all");
        return ResponseEntity.ok(incidentService.getAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Search incidents")
    public ResponseEntity<Page<IncidentReadDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /crisis/incident/search?q={}", q);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(incidentService.searchByQuery(q, pageable));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Count incidents")
    public ResponseEntity<Long> count() {
        log.debug("GET /crisis/incident/count");
        return ResponseEntity.ok(incidentService.count());
    }

    // ========== CUSTOM QUERIES ==========

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all active incidents")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active incidents retrieved"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<List<IncidentReadDto>> getActiveIncidents() {
        log.debug("GET /crisis/incident/active");
        return ResponseEntity.ok(incidentService.getActiveIncidents());
    }

    @GetMapping("/segment/{segmentId}")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get incidents by pipeline segment ID")
    public ResponseEntity<List<IncidentReadDto>> getBySegmentId(
            @Parameter(description = "Segment ID", required = true) @PathVariable Long segmentId) {
        log.debug("GET /crisis/incident/segment/{}", segmentId);
        return ResponseEntity.ok(incidentService.getByPipelineSegmentId(segmentId));
    }
}
