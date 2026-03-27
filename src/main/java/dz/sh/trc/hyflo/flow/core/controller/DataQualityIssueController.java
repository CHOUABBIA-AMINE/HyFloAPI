/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssueController
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
import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.core.service.DataQualityIssueService;
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
 * REST Controller for DataQualityIssue.
 *
 * @deprecated Since HyFlo v2 Phase 4.
 *             Backing service lacks intelligence CQRS separation.
 *             A dedicated DataQualityIssueV2Controller will be introduced in Phase 5
 *             backed by proper query service interfaces aligned with the
 *             hyflo-intelligence module.
 *             Scheduled for removal in Phase 8 cleanup.
 */
@Deprecated(since = "v2-phase4", forRemoval = true)
@RestController
@RequestMapping("/flow/core/quality-issue")
@Slf4j
@Tag(name = "Data Quality Issues", description = "APIs for querying data quality evaluations")
@SecurityRequirement(name = "bearer-auth")
public class DataQualityIssueController extends GenericController<DataQualityIssueReadDTO, Long> {

    private final DataQualityIssueService dataQualityIssueService;

    public DataQualityIssueController(DataQualityIssueService dataQualityIssueService) {
        super(dataQualityIssueService, "DataQualityIssue");
        this.dataQualityIssueService = dataQualityIssueService;
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get data quality issue by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Issue found", content = @Content(schema = @Schema(implementation = DataQualityIssueReadDTO.class))),
        @ApiResponse(responseCode = "404", description = "Issue not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<DataQualityIssueReadDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all data quality issues (paginated)")
    public ResponseEntity<Page<DataQualityIssueReadDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all data quality issues (unpaginated)")
    public ResponseEntity<List<DataQualityIssueReadDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search data quality issues")
    public ResponseEntity<Page<DataQualityIssueReadDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Count data quality issues")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @Override
    protected Page<DataQualityIssueReadDTO> searchByQuery(String query, Pageable pageable) {
        return dataQualityIssueService.searchByQuery(query, pageable);
    }

    @GetMapping("/reading/{readingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get data quality issues by reading ID")
    public ResponseEntity<List<DataQualityIssueReadDTO>> getByReadingId(
            @Parameter(description = "Reading ID", required = true) @PathVariable Long readingId) {
        log.debug("GET /flow/core/quality-issue/reading/{}", readingId);
        return success(dataQualityIssueService.getByReadingId(readingId));
    }
}
