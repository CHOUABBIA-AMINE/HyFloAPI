/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: JobController
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Common / Administration
 *
 **/

package dz.sh.trc.hyflo.common.administration.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.common.administration.dto.JobDTO;
import dz.sh.trc.hyflo.common.administration.service.JobService;
import dz.sh.trc.hyflo.configuration.template.GenericController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Job REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Job management
 */
@RestController
@RequestMapping("/common/administration/job")
@Slf4j
public class JobController extends GenericController<JobDTO, Long> {

    private final JobService jobService;

    public JobController(JobService jobService) {
        super(jobService, "Job");
        this.jobService = jobService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    public ResponseEntity<JobDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    public ResponseEntity<Page<JobDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    public ResponseEntity<List<JobDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:ADMIN')")
    public ResponseEntity<JobDTO> create(@Valid @RequestBody JobDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:ADMIN')")
    public ResponseEntity<JobDTO> update(@PathVariable Long id, @Valid @RequestBody JobDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    public ResponseEntity<Page<JobDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('JOB:READ')")
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
    public ResponseEntity<List<JobDTO>> getByStructureId(@PathVariable Long structureId) {
        log.debug("GET /job/structure/{} - Getting jobs by structure ID", structureId);
        List<JobDTO> jobs = jobService.getByStructureId(structureId);
        return success(jobs);
    }
}
