/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditedDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * AuditLog Data Transfer Object
 * Extends GenericDTO for common DTO functionality
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditedDTO extends GenericDTO<Audited> {

    @NotBlank(message = "Entity name is required")
    @Size(max = 100, message = "Entity name must not exceed 100 characters")
    private String entityName;

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    @NotNull(message = "Action is required")
    private String action;

    @Size(max = 100, message = "Username must not exceed 100 characters")
    private String username;

    @NotNull(message = "Timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    @Size(max = 45, message = "IP address must not exceed 45 characters")
    private String ipAddress;

    @Size(max = 500, message = "User agent must not exceed 500 characters")
    private String userAgent;

    @Size(max = 200, message = "Method name must not exceed 200 characters")
    private String methodName;

    private String oldValues;

    private String newValues;

    private String parameters;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Status is required")
    private String status;

    private String errorMessage;

    private Long duration;

    @Size(max = 100, message = "Session ID must not exceed 100 characters")
    private String sessionId;

    @Size(max = 50, message = "Module must not exceed 50 characters")
    private String module;

    @Size(max = 50, message = "Business process must not exceed 50 characters")
    private String businessProcess;

    private Long parentAuditId;

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
