/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuditController
 *	@CreatedOn	: 10-27-2025
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
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.system.audit.dto.AuditLogDTO;
import dz.sh.trc.hyflo.system.audit.service.AuditService;
import dz.sh.trc.hyflo.system.audit.service.AuditService.UserActivitySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for audit log management
 */
@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@Slf4j
public class AuditController {

    private final AuditService auditService;

    /**
     * Get audit history for specific entity
     */
    @GetMapping("/entity/{entityName}/{entityId}")
    public ResponseEntity<List<AuditLogDTO>> getEntityAuditHistory(
            @PathVariable String entityName,
            @PathVariable Long entityId) {
        
        log.debug("Getting audit history for entity {}:{}", entityName, entityId);
        
        List<AuditLogDTO> auditHistory = auditService.getEntityAuditHistory(entityName, entityId);
        return ResponseEntity.ok(auditHistory);
    }

    /**
     * Get user audit history
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<AuditLogDTO>> getUserAuditHistory(
            @PathVariable String username,
            Pageable pageable) {
        
        log.debug("Getting audit history for user: {}", username);
        
        Page<AuditLogDTO> auditHistory = auditService.getUserAuditHistory(username, pageable);
        return ResponseEntity.ok(auditHistory);
    }

    /**
     * Get audit logs by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Pageable pageable) {
        
        log.debug("Getting audit logs between {} and {}", startDate, endDate);
        
        Page<AuditLogDTO> auditLogs = auditService.getAuditLogsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(auditLogs);
    }

    /**
     * Get failed operations
     */
    @GetMapping("/failed")
    public ResponseEntity<Page<AuditLogDTO>> getFailedOperations(Pageable pageable) {
        log.debug("Getting failed operations");
        
        Page<AuditLogDTO> failedOperations = auditService.getFailedOperations(pageable);
        return ResponseEntity.ok(failedOperations);
    }

    /**
     * Get user activity summary
     */
    @GetMapping("/user/{username}/summary")
    public ResponseEntity<UserActivitySummary> getUserActivitySummary(
            @PathVariable String username,
            @RequestParam(defaultValue = "30") int days) {
        
        log.debug("Getting activity summary for user {} over {} days", username, days);
        
        UserActivitySummary summary = auditService.getUserActivitySummary(username, days);
        return ResponseEntity.ok(summary);
    }
}
