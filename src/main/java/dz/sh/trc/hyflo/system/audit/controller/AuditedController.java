/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditedController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.controller;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.audit.dto.AuditedDTO;
import dz.sh.trc.hyflo.system.audit.service.AuditedService;
import dz.sh.trc.hyflo.system.audit.service.AuditedService.UserActivitySummary;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for audit log management
 * Extends GenericController and adds PreAuthorize security
 */
@RestController
@RequestMapping("/system/audit/audited")
@Slf4j
public class AuditedController extends GenericController<AuditedDTO, Long> {

    private final AuditedService auditedService;

    public AuditedController(AuditedService auditedService) {
        super(auditedService, "Audited");
        this.auditedService = auditedService;
    }

    // ========== Override GenericController methods with PreAuthorize ==========

    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<AuditedDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<List<AuditedDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:ADMIN')")
    public ResponseEntity<AuditedDTO> create(@Valid @org.springframework.web.bind.annotation.RequestBody AuditedDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:ADMIN')")
    public ResponseEntity<AuditedDTO> update(@PathVariable Long id, @Valid @org.springframework.web.bind.annotation.RequestBody AuditedDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== Custom audit-specific endpoints ==========

    /**
     * Get audit history for specific entity
     * GET /entity/{entityName}/{entityId}
     */
    @GetMapping("/entity/{entityName}/{entityId}")
    @PreAuthorize("hasAnyAuthority('AUDIT:READ', 'AUDIT:USER')")
    public ResponseEntity<List<AuditedDTO>> getEntityAuditHistory(
            @PathVariable String entityName,
            @PathVariable Long entityId) {
        
        log.debug("Getting audit history for entity {}:{}", entityName, entityId);
        List<AuditedDTO> auditHistory = auditedService.getEntityAuditHistory(entityName, entityId);
        return success(auditHistory);
    }

    /**
     * Get user audit history
     * GET /user/{username}
     * Users can only view their own audit history unless they have AUDIT:READ authority
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAuthority('AUDIT:READ') or #username == authentication.principal.username")
    public ResponseEntity<Page<AuditedDTO>> getUserAuditHistory(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("Getting audit history for user: {}", username);
        Page<AuditedDTO> auditHistory = auditedService.getUserAuditHistory(
            username, 
            buildPageable(page, size, sortBy, sortDir)
        );
        return success(auditHistory);
    }

    /**
     * Get audit logs by date range
     * GET /date-range?startDate=2025-01-01&endDate=2025-12-31
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("Getting audit logs between {} and {}", startDate, endDate);
        Page<AuditedDTO> auditLogs = auditedService.getAuditLogsByDateRange(
            startDate, 
            endDate, 
            buildPageable(page, size, sortBy, sortDir)
        );
        return success(auditLogs);
    }

    /**
     * Get failed operations
     * GET /failed
     */
    @GetMapping("/failed")
    @PreAuthorize("hasAuthority('AUDIT:READ')")
    public ResponseEntity<Page<AuditedDTO>> getFailedOperations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("Getting failed operations");
        Page<AuditedDTO> failedOperations = auditedService.getFailedOperations(
            buildPageable(page, size, sortBy, sortDir)
        );
        return success(failedOperations);
    }

    /**
     * Get user activity summary
     * GET /user/{username}/summary?days=30
     */
    @GetMapping("/user/{username}/summary")
    @PreAuthorize("hasAuthority('AUDIT:READ') or #username == authentication.principal.username")
    public ResponseEntity<UserActivitySummary> getUserActivitySummary(
            @PathVariable String username,
            @RequestParam(defaultValue = "30") int days) {
        
        log.debug("Getting activity summary for user {} over {} days", username, days);
        UserActivitySummary summary = auditedService.getUserActivitySummary(username, days);
        return success(summary);
    }
}
