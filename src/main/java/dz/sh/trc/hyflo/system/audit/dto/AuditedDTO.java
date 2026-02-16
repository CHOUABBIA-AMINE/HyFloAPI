/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditedDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.audit.model.Audited;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Comprehensive audit log Data Transfer Object for tracking all business operations.
 * Records who did what, when, and the results for compliance and traceability.
 * Extends GenericDTO for common DTO functionality with complete validation alignment.
 */
@Schema(description = "Data Transfer Object for comprehensive audit log tracking all system operations and changes")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditedDTO extends GenericDTO<Audited> {

    @Schema(
        description = "Name of the audited entity class",
        example = "Pipeline",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Entity name is required")
    @Size(max = 100, message = "Entity name must not exceed 100 characters")
    private String entityName;

    @Schema(
        description = "Unique identifier of the audited entity instance",
        example = "1234",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Entity ID is required")
    @Positive(message = "Entity ID must be positive")
    private Long entityId;

    @Schema(
        description = "Action performed on the entity (uppercase with underscores)",
        example = "UPDATE",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20,
        allowableValues = {"CREATE", "READ", "UPDATE", "DELETE", "APPROVE", "REJECT"}
    )
    @NotBlank(message = "Action is required")
    @Size(max = 20, message = "Action must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Action must be uppercase with underscores")
    private String action;

    @Schema(
        description = "Username of the user who performed the action",
        example = "achouabbia",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Username must not exceed 100 characters")
    private String username;

    @Schema(
        description = "Timestamp when the action was performed (must be in past or present)",
        example = "2026-02-16 19:30:00",
        requiredMode = Schema.RequiredMode.REQUIRED,
        format = "yyyy-MM-dd HH:mm:ss"
    )
    @NotNull(message = "Timestamp is required")
    @PastOrPresent(message = "Timestamp must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    @Schema(
        description = "IP address of the client that initiated the action (IPv4 or IPv6 format)",
        example = "192.168.1.100",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 45
    )
    @Size(max = 45, message = "IP address must not exceed 45 characters")
    @Pattern(
        regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$|^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$",
        message = "IP address must be valid IPv4 or IPv6 format"
    )
    private String ipAddress;

    @Schema(
        description = "User agent string from the client browser or application",
        example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "User agent must not exceed 500 characters")
    private String userAgent;

    @Schema(
        description = "Service method name that was invoked",
        example = "updatePipeline",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Method name must not exceed 200 characters")
    private String methodName;

    @Schema(
        description = "JSON representation of old values before update operation",
        example = "{\"diameter\": 1000, \"pressure\": 150}",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String oldValues;

    @Schema(
        description = "JSON representation of new values after create/update operation",
        example = "{\"diameter\": 1200, \"pressure\": 180}",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String newValues;

    @Schema(
        description = "JSON representation of method parameters passed to the operation",
        example = "{\"id\": 1234, \"force\": true}",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String parameters;

    @Schema(
        description = "Human-readable description of the operation performed",
        example = "Updated pipeline diameter from 1000mm to 1200mm",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 1000
    )
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(
        description = "Operation execution status",
        example = "SUCCESS",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20,
        allowableValues = {"SUCCESS", "FAILED", "PARTIAL"}
    )
    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    @Schema(
        description = "Error message if the operation failed",
        example = "Validation failed: Diameter exceeds maximum allowed value",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String errorMessage;

    @Schema(
        description = "Operation duration in milliseconds",
        example = "1250",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "0"
    )
    @PositiveOrZero(message = "Duration must be zero or positive")
    private Long duration;

    @Schema(
        description = "User session identifier for tracking related operations",
        example = "550e8400-e29b-41d4-a716-446655440000",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Session ID must not exceed 100 characters")
    private String sessionId;

    @Schema(
        description = "Application module name where the operation occurred",
        example = "NETWORK",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Module must not exceed 50 characters")
    private String module;

    @Schema(
        description = "Business process identifier for the operation",
        example = "PIPELINE_CREATION",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Business process must not exceed 50 characters")
    private String businessProcess;

    @Schema(
        description = "ID of parent audit entry for linking related operations in a transaction",
        example = "1000",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Positive(message = "Parent audit ID must be positive")
    private Long parentAuditId;

    @Schema(
        description = "Additional JSON metadata for custom audit information",
        example = "{\"customField1\": \"value1\", \"customField2\": \"value2\"}",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String metadata;

    /**
     * Convert DTO to entity (for create operations)
     */
    @Override
    public Audited toEntity() {
        Audited audited = new Audited();
        audited.setId(this.getId());
        audited.setEntityName(this.entityName);
        audited.setEntityId(this.entityId);
        audited.setAction(this.action);
        audited.setUsername(this.username);
        audited.setTimestamp(this.timestamp);
        audited.setIpAddress(this.ipAddress);
        audited.setUserAgent(this.userAgent);
        audited.setMethodName(this.methodName);
        audited.setOldValues(this.oldValues);
        audited.setNewValues(this.newValues);
        audited.setParameters(this.parameters);
        audited.setDescription(this.description);
        audited.setStatus(this.status);
        audited.setErrorMessage(this.errorMessage);
        audited.setDuration(this.duration);
        audited.setSessionId(this.sessionId);
        audited.setModule(this.module);
        audited.setBusinessProcess(this.businessProcess);
        audited.setParentAuditId(this.parentAuditId);
        audited.setMetadata(this.metadata);
        return audited;
    }

    /**
     * Update entity from DTO (for update operations)
     */
    @Override
    public void updateEntity(Audited entity) {
        if (this.entityName != null) entity.setEntityName(this.entityName);
        if (this.entityId != null) entity.setEntityId(this.entityId);
        if (this.action != null) entity.setAction(this.action);
        if (this.username != null) entity.setUsername(this.username);
        if (this.timestamp != null) entity.setTimestamp(this.timestamp);
        if (this.ipAddress != null) entity.setIpAddress(this.ipAddress);
        if (this.userAgent != null) entity.setUserAgent(this.userAgent);
        if (this.methodName != null) entity.setMethodName(this.methodName);
        if (this.oldValues != null) entity.setOldValues(this.oldValues);
        if (this.newValues != null) entity.setNewValues(this.newValues);
        if (this.parameters != null) entity.setParameters(this.parameters);
        if (this.description != null) entity.setDescription(this.description);
        if (this.status != null) entity.setStatus(this.status);
        if (this.errorMessage != null) entity.setErrorMessage(this.errorMessage);
        if (this.duration != null) entity.setDuration(this.duration);
        if (this.sessionId != null) entity.setSessionId(this.sessionId);
        if (this.module != null) entity.setModule(this.module);
        if (this.businessProcess != null) entity.setBusinessProcess(this.businessProcess);
        if (this.parentAuditId != null) entity.setParentAuditId(this.parentAuditId);
        if (this.metadata != null) entity.setMetadata(this.metadata);
    }

    /**
     * Create DTO from entity
     */
    public static AuditedDTO fromEntity(Audited audited) {
        if (audited == null) return null;

        return AuditedDTO.builder()
                .id(audited.getId())
                .entityName(audited.getEntityName())
                .entityId(audited.getEntityId())
                .action(audited.getAction())
                .username(audited.getUsername())
                .timestamp(audited.getTimestamp())
                .ipAddress(audited.getIpAddress())
                .userAgent(audited.getUserAgent())
                .methodName(audited.getMethodName())
                .oldValues(audited.getOldValues())
                .newValues(audited.getNewValues())
                .parameters(audited.getParameters())
                .description(audited.getDescription())
                .status(audited.getStatus())
                .errorMessage(audited.getErrorMessage())
                .duration(audited.getDuration())
                .sessionId(audited.getSessionId())
                .module(audited.getModule())
                .businessProcess(audited.getBusinessProcess())
                .parentAuditId(audited.getParentAuditId())
                .metadata(audited.getMetadata())
                .build();
    }
}
