/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: JobController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.organization.dto.JobDTO;
import dz.sh.trc.hyflo.general.organization.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Job REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Job management
 */
@RestController
@RequestMapping("/general/organization/job")
@Slf4j
@Tag(name = "Job Management", description = "APIs for managing job positions and roles within the organization")
@SecurityRequirement(name = "bearer-auth")
public class JobController extends GenericController<JobDTO, Long> {

    private final JobService jobService;

    public JobController(JobService jobService) {
        super(jobService, "Job");
        this.jobService = jobService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Get job by ID", description = "Retrieves a single job position by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job found", content = @Content(schema = @Schema(implementation = JobDTO.class))),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobDTO> getById(
            @Parameter(description = "Job ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Get all jobs (paginated)", description = "Retrieves a paginated list of all job positions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<JobDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Get all jobs (unpaginated)", description = "Retrieves all job positions without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<JobDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:MANAGE')")
    @Operation(summary = "Create new job", description = "Creates a new job position with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Job created successfully", content = @Content(schema = @Schema(implementation = JobDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Job title/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobDTO> create(
            @Parameter(description = "Job data", required = true) 
            @Valid @RequestBody JobDTO dto) {
        JobDTO created = jobService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:MANAGE')")
    @Operation(summary = "Update job", description = "Updates an existing job position")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job updated successfully", content = @Content(schema = @Schema(implementation = JobDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "409", description = "Job title/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JobDTO> update(
            @Parameter(description = "Job ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated job data", required = true) @Valid @RequestBody JobDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:MANAGE')")
    @Operation(summary = "Delete job", description = "Deletes a job position permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Job deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Job not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete job - has dependencies (employees assigned)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Job ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Search jobs", description = "Searches jobs by title, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<JobDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Check if job exists", description = "Checks if a job with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Job ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Count jobs", description = "Returns the total number of job positions in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<JobDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return jobService.getAll(pageable);
        }
        return jobService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all jobs as list (non-paginated)
     * GET /job/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Get all jobs as list", description = "Retrieves all job positions as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<JobDTO>> getAllList() {
        log.debug("GET /job/list - Getting all jobs as list");
        List<JobDTO> jobs = jobService.getAll();
        return success(jobs);
    }

    /**
     * Get jobs by structure ID
     * GET /job/structure/{structureId}
     */
    @GetMapping("/structure/{structureId}")
    @PreAuthorize("hasAuthority('JOB:READ')")
    @Operation(summary = "Get jobs by structure", description = "Retrieves all job positions belonging to a specific organizational structure")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Structure not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires JOB:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<JobDTO>> getByStructureId(
            @Parameter(description = "Structure ID", required = true, example = "1") 
            @PathVariable Long structureId) {
        log.debug("GET /job/structure/{} - Getting jobs by structure ID", structureId);
        List<JobDTO> jobs = jobService.getByStructureId(structureId);
        return success(jobs);
    }
}
