/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentImpactController
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Crisis / Core
 *
 **/

package dz.sh.trc.hyflo.crisis.core.controller;

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

import dz.sh.trc.hyflo.crisis.core.dto.query.IncidentImpactReadDTO;
import dz.sh.trc.hyflo.crisis.core.service.IIncidentImpactQueryService;
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
@RequestMapping("/crisis/incident-impact")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Incident Impacts", description = "APIs for querying incident impact assessments")
@SecurityRequirement(name = "bearer-auth")
public class IncidentImpactController {

    private final IIncidentImpactQueryService impactService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get incident impact by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Impact record found",
                content = @Content(schema = @Schema(implementation = IncidentImpactReadDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<IncidentImpactReadDTO> getById(@PathVariable Long id) {
        log.debug("GET /crisis/incident-impact/{}", id);
        return ResponseEntity.ok(impactService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all incident impacts (paginated)")
    public ResponseEntity<Page<IncidentImpactReadDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /crisis/incident-impact?page={}&size={}", page, size);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(impactService.getAll(pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all incident impacts (unpaginated)")
    public ResponseEntity<List<IncidentImpactReadDTO>> getAllUnpaginated() {
        log.debug("GET /crisis/incident-impact/all");
        return ResponseEntity.ok(impactService.getAll());
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Count all incident impacts")
    public ResponseEntity<Long> count() {
        log.debug("GET /crisis/incident-impact/count");
        return ResponseEntity.ok(impactService.count());
    }

    @GetMapping("/by-incident/{incidentId}")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get all impacts for a specific incident (unpaginated)")
    public ResponseEntity<List<IncidentImpactReadDTO>> getByIncidentId(
            @Parameter(description = "Incident ID", required = true) @PathVariable Long incidentId) {
        log.debug("GET /crisis/incident-impact/by-incident/{}", incidentId);
        return ResponseEntity.ok(impactService.getByIncidentId(incidentId));
    }

    @GetMapping("/by-incident/{incidentId}/paged")
    @PreAuthorize("hasAuthority('CRISIS:READ')")
    @Operation(summary = "Get impacts for a specific incident (paginated)")
    public ResponseEntity<Page<IncidentImpactReadDTO>> getByIncidentIdPaged(
            @Parameter(description = "Incident ID", required = true) @PathVariable Long incidentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /crisis/incident-impact/by-incident/{}/paged", incidentId);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(impactService.getByIncidentId(incidentId, pageable));
    }
}
