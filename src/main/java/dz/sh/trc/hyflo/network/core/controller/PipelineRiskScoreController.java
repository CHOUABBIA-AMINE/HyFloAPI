/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineRiskScoreController
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

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
import dz.sh.trc.hyflo.network.core.dto.PipelineRiskScoreReadDto;
import dz.sh.trc.hyflo.network.core.service.PipelineRiskScoreService;
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
 * REST Controller for PipelineRiskScore.
 */
@RestController
@RequestMapping("/network/core/risk-score")
@Slf4j
@Tag(name = "Pipeline Risk Scores", description = "APIs for querying pipeline segment risk scores")
@SecurityRequirement(name = "bearer-auth")
public class PipelineRiskScoreController extends GenericController<PipelineRiskScoreReadDto, Long> {

    private final PipelineRiskScoreService pipelineRiskScoreService;

    public PipelineRiskScoreController(PipelineRiskScoreService pipelineRiskScoreService) {
        super(pipelineRiskScoreService, "PipelineRiskScore");
        this.pipelineRiskScoreService = pipelineRiskScoreService;
    }

    @Override
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Get risk score by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Risk score found", content = @Content(schema = @Schema(implementation = PipelineRiskScoreReadDto.class))),
        @ApiResponse(responseCode = "404", description = "Risk score not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<PipelineRiskScoreReadDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Get all risk scores (paginated)")
    public ResponseEntity<Page<PipelineRiskScoreReadDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Get all risk scores (unpaginated)")
    public ResponseEntity<List<PipelineRiskScoreReadDto>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Search risk scores")
    public ResponseEntity<Page<PipelineRiskScoreReadDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Count risk scores")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<PipelineRiskScoreReadDto> searchByQuery(String query, Pageable pageable) {
        return pipelineRiskScoreService.searchByQuery(query, pageable);
    }

    @GetMapping("/segment/{segmentId}")
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Get risk scores by segment ID")
    public ResponseEntity<List<PipelineRiskScoreReadDto>> getBySegmentId(
            @Parameter(description = "Segment ID", required = true) @PathVariable Long segmentId) {
        log.debug("GET /network/core/risk-score/segment/{}", segmentId);
        return success(pipelineRiskScoreService.getBySegmentId(segmentId));
    }

    @GetMapping("/segment/{segmentId}/latest")
    @PreAuthorize("hasAuthority('NETWORK:READ')")
    @Operation(summary = "Get the latest risk score for a segment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Latest risk score retrieved"),
        @ApiResponse(responseCode = "404", description = "No risk score found for this segment"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<PipelineRiskScoreReadDto> getLatestBySegmentId(
            @Parameter(description = "Segment ID", required = true) @PathVariable Long segmentId) {
        log.debug("GET /network/core/risk-score/segment/{}/latest", segmentId);
        return pipelineRiskScoreService.getLatestBySegmentId(segmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
