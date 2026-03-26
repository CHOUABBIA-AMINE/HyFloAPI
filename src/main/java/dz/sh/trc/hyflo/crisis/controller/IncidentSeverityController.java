/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentSeverityController
 *  @CreatedOn  : 03-26-2026
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

import dz.sh.trc.hyflo.crisis.dto.query.IncidentSeverityReadDto;
import dz.sh.trc.hyflo.crisis.service.IIncidentSeverityQueryService;
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

@RestController
@RequestMapping("/crisis/incident-severity")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Incident Severities", description = "Reference APIs for incident severity levels")
@SecurityRequirement(name = "bearer-auth")
public class IncidentSeverityController {

    private final IIncidentSeverityQueryService severityService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get severity by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severity found",
                content = @Content(schema = @Schema(implementation = IncidentSeverityReadDto.class))),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<IncidentSeverityReadDto> getById(@PathVariable Long id) {
        log.debug("GET /crisis/incident-severity/{}", id);
        return ResponseEntity.ok(severityService.getById(id));
    }

    @GetMapping("/by-code/{code}")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get severity by code")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Severity found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<IncidentSeverityReadDto> getByCode(
            @Parameter(description = "Severity code, e.g. P1", required = true)
            @PathVariable String code) {
        log.debug("GET /crisis/incident-severity/by-code/{}", code);
        return ResponseEntity.ok(severityService.getByCode(code));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all severities (paginated)")
    public ResponseEntity<Page<IncidentSeverityReadDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "rank") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /crisis/incident-severity?page={}&size={}", page, size);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(severityService.getAll(pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all severities ordered by rank (unpaginated)")
    public ResponseEntity<List<IncidentSeverityReadDto>> getAllOrderedByRank() {
        log.debug("GET /crisis/incident-severity/all");
        return ResponseEntity.ok(severityService.getAllOrderedByRank());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Search severities by code or label")
    public ResponseEntity<Page<IncidentSeverityReadDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "rank") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /crisis/incident-severity/search?q={}", q);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(severityService.searchByQuery(q, pageable));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Count all severity levels")
    public ResponseEntity<Long> count() {
        log.debug("GET /crisis/incident-severity/count");
        return ResponseEntity.ok(severityService.count());
    }
}
