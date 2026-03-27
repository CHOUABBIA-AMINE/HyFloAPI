/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : DataQualityIssueController
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.controller → flow.intelligence.controller
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all data quality issues (paginated)")
    public ResponseEntity<Page<DataQualityIssueReadDTO>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dataQualityIssueService.getAll(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Search quality issues by keyword")
    public ResponseEntity<Page<DataQualityIssueReadDTO>> search(
            @RequestParam(required = false) String query,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dataQualityIssueService.searchByQuery(query, pageable));
    }

    @GetMapping("/reading/{readingId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get quality issues by source reading ID")
    public ResponseEntity<List<DataQualityIssueReadDTO>> getByReadingId(
            @PathVariable Long readingId) {
        return ResponseEntity.ok(dataQualityIssueService.getByReadingId(readingId));
    }
}
