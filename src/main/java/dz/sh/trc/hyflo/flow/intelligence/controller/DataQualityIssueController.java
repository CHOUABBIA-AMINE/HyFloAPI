/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueController
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Intelligence
 *
 *  @Description: REST controller for data quality issues.
 *                Exposed at /flow/intelligence/quality-issue per HyFlo v2 architecture.
 *                Replaces deprecated flow.core.controller.DataQualityIssueController.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.util.List;

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
@Tag(name = "Flow Intelligence — Data Quality",
     description = "Query data quality issues flagged on flow readings")
@SecurityRequirement(name = "bearer-auth")
public class DataQualityIssueController {

    private final DataQualityIssueService dataQualityIssueService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get data quality issue by ID")
    public ResponseEntity<DataQualityIssueReadDTO> getById(
            @Parameter(description = "Issue ID") @PathVariable Long id) {
        log.debug("GET /flow/intelligence/quality-issue/{}", id);
        return ResponseEntity.ok(dataQualityIssueService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all data quality issues (paginated)")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /flow/intelligence/quality-issue page={} size={}", page, size);
        return ResponseEntity.ok(dataQualityIssueService.findAll(
                org.springframework.data.domain.PageRequest.of(
                        page, size,
                        "desc".equalsIgnoreCase(sortDir)
                                ? org.springframework.data.domain.Sort.Direction.DESC
                                : org.springframework.data.domain.Sort.Direction.ASC,
                        sortBy)));
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
