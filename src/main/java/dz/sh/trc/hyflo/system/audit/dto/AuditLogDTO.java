/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuditLogDTO
 *	@CreatedOn	: 10-27-2025
 *	@UpdatedOn	: 12-06-2025 (Fixed Jackson ObjectMapper issue)
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dz.sh.trc.hyflo.system.audit.model.AuditLog;
import dz.sh.trc.hyflo.system.audit.model.AuditLog.AuditAction;
import dz.sh.trc.hyflo.system.audit.model.AuditLog.AuditStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuditLog Data Transfer Object
 * Maps exactly to AuditLog model fields: F_00=id through F_20=metadata
 * Provides comprehensive audit information for business operations tracking
 * 
 * FIXED: ObjectMapper is now a static final instance (thread-safe and reusable)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLogDTO {

    /**
     * Shared ObjectMapper instance for JSON parsing
     * Thread-safe and reusable - avoids creating new instances for each parsing operation
     */
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private Long id; // F_00

    @NotBlank(message = "Entity name is required")
    @Size(max = 100, message = "Entity name must not exceed 100 characters")
    private String entityName; // F_01

    @NotNull(message = "Entity ID is required")
    private Long entityId; // F_02

    @NotNull(message = "Action is required")
    private AuditAction action; // F_03

    @Size(max = 100, message = "Username must not exceed 100 characters")
    private String username; // F_04

    @NotNull(message = "Timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp; // F_05

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress; // F_06

    @Size(max = 500, message = "User agent must not exceed 500 characters")
    private String userAgent; // F_07

    @Size(max = 200, message = "Method name must not exceed 200 characters")
    private String methodName; // F_08

    private String oldValues; // F_09 - JSON string

    private String newValues; // F_10 - JSON string

    private String parameters; // F_11 - JSON string

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description; // F_12

    @NotNull(message = "Status is required")
    private AuditStatus status; // F_13

    private String errorMessage; // F_14

    private Long duration; // F_15 - in milliseconds

    @Size(max = 100, message = "Session ID must not exceed 100 characters")
    private String sessionId; // F_16

    @Size(max = 50, message = "Module must not exceed 50 characters")
    private String module; // F_17

    @Size(max = 50, message = "Business process must not exceed 50 characters")
    private String businessProcess; // F_18

    private Long parentAuditId; // F_19

    private String metadata; // F_20 - JSON string

    // Additional computed fields for enhanced functionality
    @SuppressWarnings("unused")
    private String formattedTimestamp;
    @SuppressWarnings("unused")
    private String formattedDuration;
    @SuppressWarnings("unused")
    private String actionDescription;
    @SuppressWarnings("unused")
    private String statusDescription;
    @SuppressWarnings("unused")
    private Boolean isSuccess;
    @SuppressWarnings("unused")
    private Boolean isCriticalOperation;
    @SuppressWarnings("unused")
    private String riskLevel;

    /**
     * Create DTO from entity
     */
    public static AuditLogDTO fromEntity(AuditLog auditLog) {
        if (auditLog == null) return null;

        AuditLogDTO.AuditLogDTOBuilder builder = AuditLogDTO.builder()
                .id(auditLog.getId())
                .entityName(auditLog.getEntityName())
                .entityId(auditLog.getEntityId())
                .action(auditLog.getAction())
                .username(auditLog.getUsername())
                .timestamp(auditLog.getTimestamp())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .methodName(auditLog.getMethodName())
                .oldValues(auditLog.getOldValues())
                .newValues(auditLog.getNewValues())
                .parameters(auditLog.getParameters())
                .description(auditLog.getDescription())
                .status(auditLog.getStatus())
                .errorMessage(auditLog.getErrorMessage())
                .duration(auditLog.getDuration())
                .sessionId(auditLog.getSessionId())
                .module(auditLog.getModule())
                .businessProcess(auditLog.getBusinessProcess())
                .parentAuditId(auditLog.getParentAuditId())
                .metadata(auditLog.getMetadata());

        AuditLogDTO dto = builder.build();

        // Set computed fields
        dto.setFormattedTimestamp(dto.getFormattedTimestamp());
        dto.setFormattedDuration(dto.getFormattedDuration());
        dto.setActionDescription(dto.getActionDescription());
        dto.setStatusDescription(dto.getStatusDescription());
        dto.setIsSuccess(dto.getIsSuccess());
        dto.setIsCriticalOperation(dto.getIsCriticalOperation());
        dto.setRiskLevel(dto.getRiskLevel());

        return dto;
    }

    /**
     * Create DTO from entity with minimal fields (for list views)
     */
    public static AuditLogDTO fromEntityMinimal(AuditLog auditLog) {
        if (auditLog == null) return null;

        return AuditLogDTO.builder()
                .id(auditLog.getId())
                .entityName(auditLog.getEntityName())
                .entityId(auditLog.getEntityId())
                .action(auditLog.getAction())
                .username(auditLog.getUsername())
                .timestamp(auditLog.getTimestamp())
                .status(auditLog.getStatus())
                .duration(auditLog.getDuration())
                .module(auditLog.getModule())
                .description(auditLog.getDescription())
                .build();
    }

    /**
     * Convert to entity (for create/update operations)
     */
    public AuditLog toEntity() {
        AuditLog auditLog = new AuditLog();

        auditLog.setId(this.id);
        auditLog.setEntityName(this.entityName);
        auditLog.setEntityId(this.entityId);
        auditLog.setAction(this.action);
        auditLog.setUsername(this.username);
        auditLog.setTimestamp(this.timestamp);
        auditLog.setIpAddress(this.ipAddress);
        auditLog.setUserAgent(this.userAgent);
        auditLog.setMethodName(this.methodName);
        auditLog.setOldValues(this.oldValues);
        auditLog.setNewValues(this.newValues);
        auditLog.setParameters(this.parameters);
        auditLog.setDescription(this.description);
        auditLog.setStatus(this.status);
        auditLog.setErrorMessage(this.errorMessage);
        auditLog.setDuration(this.duration);
        auditLog.setSessionId(this.sessionId);
        auditLog.setModule(this.module);
        auditLog.setBusinessProcess(this.businessProcess);
        auditLog.setParentAuditId(this.parentAuditId);
        auditLog.setMetadata(this.metadata);

        return auditLog;
    }

    /**
     * Get formatted timestamp for display
     */
    public String getFormattedTimestamp() {
        if (timestamp == null) return "N/A";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(timestamp);
    }

    /**
     * Get formatted duration for display
     */
    public String getFormattedDuration() {
        if (duration == null) return "N/A";

        if (duration < 1000) {
            return duration + "ms";
        } else if (duration < 60000) {
            return String.format("%.2fs", duration / 1000.0);
        } else {
            long minutes = duration / 60000;
            long seconds = (duration % 60000) / 1000;
            return String.format("%dm %ds", minutes, seconds);
        }
    }

    /**
     * Get human-readable action description
     */
    public String getActionDescription() {
        if (action == null) return "Unknown";

        return switch (action) {
            case CREATE -> "Created";
            case UPDATE -> "Updated";
            case DELETE -> "Deleted";
            case READ -> "Retrieved";
            case APPROVE -> "Approved";
            case REJECT -> "Rejected";
            case SUBMIT -> "Submitted";
            case CANCEL -> "Cancelled";
            case ARCHIVE -> "Archived";
            case RESTORE -> "Restored";
        };
    }

    /**
     * Get human-readable status description
     */
    public String getStatusDescription() {
        if (status == null) return "Unknown";

        return switch (status) {
            case SUCCESS -> "Successful";
            case FAILED -> "Failed";
            case PARTIAL -> "Partially Completed";
        };
    }

    /**
     * Check if operation was successful
     */
    public Boolean getIsSuccess() {
        return status == AuditStatus.SUCCESS;
    }

    /**
     * Check if operation is considered critical
     */
    public Boolean getIsCriticalOperation() {
        if (action == null) return false;

        return action == AuditAction.DELETE || 
               action == AuditAction.APPROVE || 
               action == AuditAction.REJECT ||
               (action == AuditAction.UPDATE && isCriticalEntity());
    }

    /**
     * Get risk level of the operation
     */
    public String getRiskLevel() {
        if (action == null || status == null) return "UNKNOWN";

        // High risk operations
        if (action == AuditAction.DELETE || 
            (action == AuditAction.UPDATE && isCriticalEntity()) ||
            action == AuditAction.APPROVE) {
            return "HIGH";
        }

        // Medium risk operations
        if (action == AuditAction.CREATE || 
            action == AuditAction.UPDATE ||
            action == AuditAction.SUBMIT) {
            return "MEDIUM";
        }

        // Low risk operations
        if (action == AuditAction.READ) {
            return "LOW";
        }

        return "MEDIUM";
    }

    /**
     * Check if entity is considered critical
     */
    private boolean isCriticalEntity() {
        if (entityName == null) return false;

        String entity = entityName.toLowerCase();
        return entity.contains("contract") || 
               entity.contains("consultation") || 
               entity.contains("submission") ||
               entity.contains("provider") ||
               entity.contains("approval");
    }

    /**
     * Get operation summary for display
     */
    public String getOperationSummary() {
        StringBuilder summary = new StringBuilder();

        summary.append(getActionDescription());
        if (entityName != null) {
            summary.append(" ").append(entityName.toLowerCase());
        }
        if (entityId != null) {
            summary.append(" (ID: ").append(entityId).append(")");
        }

        return summary.toString();
    }

    /**
     * Get user display with IP address
     */
    public String getUserDisplay() {
        if (username == null) return "Anonymous";

        StringBuilder display = new StringBuilder(username);
        if (ipAddress != null && !ipAddress.isEmpty()) {
            display.append(" (").append(ipAddress).append(")");
        }

        return display.toString();
    }

    /**
     * Get module and process display
     */
    public String getModuleProcessDisplay() {
        StringBuilder display = new StringBuilder();

        if (module != null && !module.isEmpty()) {
            display.append(module);
        }

        if (businessProcess != null && !businessProcess.isEmpty()) {
            if (display.length() > 0) {
                display.append(" / ");
            }
            display.append(businessProcess);
        }

        return display.length() > 0 ? display.toString() : "N/A";
    }

    /**
     * Check if operation has changes (for updates)
     */
    public Boolean hasChanges() {
        return oldValues != null && newValues != null && !oldValues.equals(newValues);
    }

    /**
     * Check if operation has error details
     */
    public Boolean hasError() {
        return status == AuditStatus.FAILED && errorMessage != null && !errorMessage.isEmpty();
    }

    /**
     * Get performance category based on duration
     */
    public String getPerformanceCategory() {
        if (duration == null) return "UNKNOWN";

        if (duration < 100) return "FAST";
        if (duration < 1000) return "NORMAL";
        if (duration < 5000) return "SLOW";
        return "VERY_SLOW";
    }

    /**
     * Get time since operation (human readable)
     */
    public String getTimeSince() {
        if (timestamp == null) return "Unknown";

        long diff = System.currentTimeMillis() - timestamp.getTime();

        if (diff < 60000) { // Less than 1 minute
            return "Just now";
        } else if (diff < 3600000) { // Less than 1 hour
            long minutes = diff / 60000;
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (diff < 86400000) { // Less than 1 day
            long hours = diff / 3600000;
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else {
            long days = diff / 86400000;
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
    }

    /**
     * Parse old values as JSON map
     */
    public Map<String, Object> getOldValuesAsMap() {
        return parseJsonString(oldValues);
    }

    /**
     * Parse new values as JSON map
     */
    public Map<String, Object> getNewValuesAsMap() {
        return parseJsonString(newValues);
    }

    /**
     * Parse parameters as JSON map
     */
    public Map<String, Object> getParametersAsMap() {
        return parseJsonString(parameters);
    }

    /**
     * Parse metadata as JSON map
     */
    public Map<String, Object> getMetadataAsMap() {
        return parseJsonString(metadata);
    }

    /**
     * Helper method to parse JSON strings using shared ObjectMapper
     * 
     * FIXED: Uses static final ObjectMapper instead of creating new instances
     * This is more efficient and thread-safe
     */
    private Map<String, Object> parseJsonString(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new HashMap<>();
        }

        try {
            // Use shared ObjectMapper instance with TypeReference for proper generic type handling
            return JSON_MAPPER.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            // Log the exception in production (using logger)
            // For now, return empty map
            return new HashMap<>();
        }
    }

    /**
     * Create simplified DTO for dropdowns and lists
     */
    public static AuditLogDTO createSimple(Long id, String entityName, Long entityId, 
                                          AuditAction action, String username, Date timestamp) {
        return AuditLogDTO.builder()
                .id(id)
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .username(username)
                .timestamp(timestamp)
                .build();
    }

    /**
     * Validate required fields are present
     */
    public boolean isValid() {
        return entityName != null && !entityName.isEmpty() &&
               entityId != null &&
               action != null &&
               timestamp != null &&
               status != null;
    }

    /**
     * Get comparison key for sorting (by timestamp desc, then by ID)
     */
    public String getComparisonKey() {
        long timestampLong = timestamp != null ? timestamp.getTime() : 0;
        long idLong = id != null ? id : 0;
        return String.format("%020d_%020d", Long.MAX_VALUE - timestampLong, idLong);
    }

    /**
     * Get display color based on status and risk level
     */
    public String getDisplayColor() {
        if (status == AuditStatus.FAILED) return "danger";
        if (status == AuditStatus.PARTIAL) return "warning";
        if (getIsCriticalOperation()) return "info";
        return "success";
    }

    /**
     * Get icon based on action type
     */
    public String getActionIcon() {
        if (action == null) return "question-circle";

        return switch (action) {
            case CREATE -> "plus-circle";
            case UPDATE -> "edit";
            case DELETE -> "trash";
            case READ -> "eye";
            case APPROVE -> "check-circle";
            case REJECT -> "times-circle";
            case SUBMIT -> "paper-plane";
            case CANCEL -> "ban";
            case ARCHIVE -> "archive";
            case RESTORE -> "undo";
        };
    }

    /**
     * Get full display for detailed views
     */
    public String getFullDisplay() {
        StringBuilder display = new StringBuilder();

        display.append(getActionDescription());
        if (entityName != null) {
            display.append(" ").append(entityName);
        }
        if (entityId != null) {
            display.append(" (ID: ").append(entityId).append(")");
        }
        if (username != null) {
            display.append(" by ").append(username);
        }
        if (timestamp != null) {
            display.append(" at ").append(getFormattedTimestamp());
        }
        display.append(" - ").append(getStatusDescription());

        return display.toString();
    }

    /**
     * Check if audit log should be highlighted (critical or failed operations)
     */
    public Boolean shouldHighlight() {
        return status == AuditStatus.FAILED || getIsCriticalOperation();
    }

    /**
     * Get audit trail context (for linked operations)
     */
    public String getAuditTrailContext() {
        if (parentAuditId != null) {
            return "Part of audit trail #" + parentAuditId;
        }
        return "Standalone operation";
    }
}