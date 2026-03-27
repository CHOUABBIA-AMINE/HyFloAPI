/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: DataQualityIssueController
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor: moved from flow.core.controller to flow.intelligence.controller
 *                             Mapping updated from /flow/core/qualityIssue to /flow/intelligence/quality-issue
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

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

import dz.sh.trc.hyflo.flow.intelligence.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.service.DataQualityIssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/intelligence/quality-issue")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Data Quality Issues",
     description = "APIs for querying data quality issues flagged by the validation engine")
@SecurityRequirement(name = "bearer-auth")
public class DataQualityIssueController {

    private final DataQualityIssueService dataQualityIssueService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get data quality issue by ID")
    public ResponseEntity<DataQualityIssueReadDTO> getById(@PathVariable Long id) {
        log.debug("GET /flow/intelligence/quality-issue/{}", id);
        return ResponseEntity.ok(dataQualityIssueService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all data quality issues (paginated)")
    public ResponseEntity<Page<DataQualityIssueReadDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(dataQualityIssueService.getAll(page, size, sortBy, sortDir));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get all data quality issues (unpaginated)")
    public ResponseEntity<List<DataQualityIssueReadDTO>> getAll() {
        return ResponseEntity.ok(dataQualityIssueService.getAll());
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Count data quality issues")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(dataQualityIssueService.count());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search data quality issues")
    public ResponseEntity<Page<DataQualityIssueReadDTO>> search(
            @RequestParam(required = false) String q,
            Pageable pageable) {
        return ResponseEntity.ok(dataQualityIssueService.searchByQuery(q, pageable));
    }

    @GetMapping("/reading/{readingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get data quality issues by reading ID")
    public ResponseEntity<List<DataQualityIssueReadDTO>> getByReadingId(
            @Parameter(description = "Reading ID", required = true) @PathVariable Long readingId) {
        log.debug("GET /flow/intelligence/quality-issue/reading/{}", readingId);
        return ResponseEntity.ok(dataQualityIssueService.getByReadingId(readingId));
    }
}
