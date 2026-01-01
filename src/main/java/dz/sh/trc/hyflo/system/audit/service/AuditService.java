/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuditService
 *	@CreatedOn	: 10-27-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.system.audit.dto.AuditLogDTO;
import dz.sh.trc.hyflo.system.audit.model.AuditLog;
import dz.sh.trc.hyflo.system.audit.model.AuditLog.AuditAction;
import dz.sh.trc.hyflo.system.audit.model.AuditLog.AuditStatus;
import dz.sh.trc.hyflo.system.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 * Service for managing audit logs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Log an audit event (using separate transaction to ensure logging even if main transaction fails)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditEvent(AuditEventBuilder eventBuilder) {
        try {
            AuditLog auditLog = eventBuilder.build();
            auditLogRepository.save(auditLog);
            log.debug("Audit event saved: {} {} for entity {}:{}", 
                     auditLog.getAction(), auditLog.getStatus(), 
                     auditLog.getEntityName(), auditLog.getEntityId());
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
            // Don't throw exception to avoid breaking the main business flow
        }
    }

    /**
     * Get audit logs for a specific entity
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getEntityAuditHistory(String entityName, Long entityId) {
        return auditLogRepository
                .findByEntityNameAndEntityIdOrderByTimestampDesc(entityName, entityId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Get audit logs by user
     */
    @Transactional(readOnly = true)
    public Page<AuditLogDTO> getUserAuditHistory(String username, Pageable pageable) {
        return auditLogRepository
                .findByUsernameOrderByTimestampDesc(username, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Get audit logs by date range
     */
    @Transactional(readOnly = true)
    public Page<AuditLogDTO> getAuditLogsByDateRange(Date startDate, Date endDate, Pageable pageable) {
        return auditLogRepository
                .findByTimestampBetween(startDate, endDate, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Get failed operations
     */
    @Transactional(readOnly = true)
    public Page<AuditLogDTO> getFailedOperations(Pageable pageable) {
        return auditLogRepository
                .findByStatusOrderByTimestampDesc(AuditStatus.FAILED, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Get user activity summary
     */
    @Transactional(readOnly = true)
    public UserActivitySummary getUserActivitySummary(String username, int days) {
        Date since = new Date(System.currentTimeMillis() - (long) days * 24 * 60 * 60 * 1000);
        
        long totalOperations = auditLogRepository.countByUsernameAndTimestampAfter(username, since);
        List<Object[]> activityData = auditLogRepository.getUserActivitySummary(username, since);
        
        return UserActivitySummary.builder()
                .username(username)
                .periodDays(days)
                .totalOperations(totalOperations)
                .activityBreakdown(activityData.stream()
                        .collect(java.util.stream.Collectors.toMap(
                                row -> (AuditAction) row[0],
                                row -> (Long) row[1]
                        )))
                .build();
    }

    /**
     * Convert entity to DTO
     */
    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        return AuditLogDTO.builder()
                .id(auditLog.getId())
                .entityName(auditLog.getEntityName())
                .entityId(auditLog.getEntityId())
                .action(auditLog.getAction())
                .username(auditLog.getUsername())
                .timestamp(auditLog.getTimestamp())
                .ipAddress(auditLog.getIpAddress())
                .methodName(auditLog.getMethodName())
                .description(auditLog.getDescription())
                .status(auditLog.getStatus())
                .duration(auditLog.getDuration())
                .module(auditLog.getModule())
                .businessProcess(auditLog.getBusinessProcess())
                .build();
    }

    /**
     * Builder class for creating audit events
     */
    public static class AuditEventBuilder {
        private AuditLog auditLog = new AuditLog();

        public static AuditEventBuilder create() {
            return new AuditEventBuilder();
        }

        public AuditEventBuilder entityName(String entityName) {
            auditLog.setEntityName(entityName);
            return this;
        }

        public AuditEventBuilder entityId(Long entityId) {
            auditLog.setEntityId(entityId);
            return this;
        }

        public AuditEventBuilder action(AuditAction action) {
            auditLog.setAction(action);
            return this;
        }

        public AuditEventBuilder username(String username) {
            auditLog.setUsername(username);
            return this;
        }

        public AuditEventBuilder ipAddress(String ipAddress) {
            auditLog.setIpAddress(ipAddress);
            return this;
        }

        public AuditEventBuilder userAgent(String userAgent) {
            auditLog.setUserAgent(userAgent);
            return this;
        }

        public AuditEventBuilder methodName(String methodName) {
            auditLog.setMethodName(methodName);
            return this;
        }

        public AuditEventBuilder oldValues(Object oldValues) {
            try {
                auditLog.setOldValues(new ObjectMapper().writeValueAsString(oldValues));
            } catch (Exception e) {
                log.warn("Failed to serialize old values", e);
            }
            return this;
        }

        public AuditEventBuilder newValues(Object newValues) {
            try {
                auditLog.setNewValues(new ObjectMapper().writeValueAsString(newValues));
            } catch (Exception e) {
                log.warn("Failed to serialize new values", e);
            }
            return this;
        }

        public AuditEventBuilder parameters(Object parameters) {
            try {
                auditLog.setParameters(new ObjectMapper().writeValueAsString(parameters));
            } catch (Exception e) {
                log.warn("Failed to serialize parameters", e);
            }
            return this;
        }

        public AuditEventBuilder description(String description) {
            auditLog.setDescription(description);
            return this;
        }

        public AuditEventBuilder status(AuditStatus status) {
            auditLog.setStatus(status);
            return this;
        }

        public AuditEventBuilder errorMessage(String errorMessage) {
            auditLog.setErrorMessage(errorMessage);
            return this;
        }

        public AuditEventBuilder duration(Long duration) {
            auditLog.setDuration(duration);
            return this;
        }

        public AuditEventBuilder module(String module) {
            auditLog.setModule(module);
            return this;
        }

        public AuditEventBuilder businessProcess(String businessProcess) {
            auditLog.setBusinessProcess(businessProcess);
            return this;
        }

        public AuditEventBuilder sessionId(String sessionId) {
            auditLog.setSessionId(sessionId);
            return this;
        }

        public AuditLog build() {
            auditLog.setTimestamp(new Date());
            return auditLog;
        }
    }

    /**
     * User activity summary DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class UserActivitySummary {
        private String username;
        private int periodDays;
        private long totalOperations;
        private java.util.Map<AuditAction, Long> activityBreakdown;
    }
}
