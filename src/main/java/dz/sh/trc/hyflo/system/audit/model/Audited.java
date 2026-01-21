/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Audited
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.model;

import java.util.Date;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Comprehensive audit log entity for tracking all business operations.
 * Records who did what, when, and the results for compliance and traceability.
 */
@Schema(description = "Comprehensive audit log for tracking all system operations and changes")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Audited")
@Table(name="T_00_03_01")
public class Audited extends GenericModel {

	@Schema(
		description = "Name of the audited entity class",
		example = "Pipeline",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Entity name is mandatory")
	@Size(max = 100, message = "Entity name must not exceed 100 characters")
	@Column(name = "F_01", nullable = false, length = 100)
	private String entityName;

	@Schema(
		description = "Unique identifier of the audited entity instance",
		example = "1234",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Entity ID is mandatory")
	@Positive(message = "Entity ID must be positive")
	@Column(name = "F_02", nullable = false)
	private Long entityId;

	@Schema(
		description = "Action performed on the entity",
		example = "UPDATE",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20,
		allowableValues = {"CREATE", "READ", "UPDATE", "DELETE", "APPROVE", "REJECT"}
	)
	@NotBlank(message = "Action is mandatory")
	@Size(max = 20, message = "Action must not exceed 20 characters")
	@Pattern(regexp = "^[A-Z_]+$", message = "Action must be uppercase with underscores")
	@Column(name = "F_03", nullable = false, length = 20)
	private String action;

	@Schema(
		description = "Username of the user who performed the action",
		example = "achouabbia",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Username must not exceed 100 characters")
	@Column(name = "F_04", nullable = true, length = 100)
	private String username;

	@Schema(
		description = "Timestamp when the action was performed",
		example = "2026-01-21T23:37:00Z",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Timestamp is mandatory")
	@PastOrPresent(message = "Timestamp must be in the past or present")
	@Column(name = "F_05", nullable = false)
	private Date timestamp;

	@Schema(
		description = "IP address of the client that initiated the action",
		example = "192.168.1.100",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 45
	)
	@Size(max = 45, message = "IP address must not exceed 45 characters")
	@Pattern(regexp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$|^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$", message = "IP address must be valid IPv4 or IPv6")
	@Column(name = "F_06", nullable = true, length = 45)
	private String ipAddress;

	@Schema(
		description = "User agent string from the client",
		example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 500
	)
	@Size(max = 500, message = "User agent must not exceed 500 characters")
	@Column(name = "F_07", nullable = true, length = 500)
	private String userAgent;

	@Schema(
		description = "Service method name that was invoked",
		example = "updatePipeline",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 200
	)
	@Size(max = 200, message = "Method name must not exceed 200 characters")
	@Column(name = "F_08", nullable = true, length = 200)
	private String methodName;

	@Schema(
		description = "JSON representation of old values before update",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_09", nullable = true, columnDefinition = "TEXT")
	private String oldValues;

	@Schema(
		description = "JSON representation of new values after create/update",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_10", nullable = true, columnDefinition = "TEXT")
	private String newValues;

	@Schema(
		description = "JSON representation of method parameters",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_11", nullable = true, columnDefinition = "TEXT")
	private String parameters;

	@Schema(
		description = "Human-readable description of the operation",
		example = "Updated pipeline diameter from 1000mm to 1200mm",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 1000
	)
	@Size(max = 1000, message = "Description must not exceed 1000 characters")
	@Column(name = "F_12", nullable = true, length = 1000)
	private String description;

	@Schema(
		description = "Operation execution status",
		example = "SUCCESS",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20,
		allowableValues = {"SUCCESS", "FAILED", "PARTIAL"}
	)
	@NotBlank(message = "Status is mandatory")
	@Size(max = 20, message = "Status must not exceed 20 characters")
	@Column(name = "F_13", nullable = false, length = 20)
	private String status;

	@Schema(
		description = "Error message if operation failed",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_14", nullable = true, columnDefinition = "TEXT")
	private String errorMessage;

	@Schema(
		description = "Operation duration in milliseconds",
		example = "1250",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PositiveOrZero(message = "Duration must be zero or positive")
	@Column(name = "F_15", nullable = true)
	private Long duration;

	@Schema(
		description = "User session identifier for tracking related operations",
		example = "550e8400-e29b-41d4-a716-446655440000",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Session ID must not exceed 100 characters")
	@Column(name = "F_16", nullable = true, length = 100)
	private String sessionId;

	@Schema(
		description = "Application module name",
		example = "NETWORK",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 50
	)
	@Size(max = 50, message = "Module must not exceed 50 characters")
	@Column(name = "F_17", nullable = true, length = 50)
	private String module;

	@Schema(
		description = "Business process identifier",
		example = "PIPELINE_CREATION",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 50
	)
	@Size(max = 50, message = "Business process must not exceed 50 characters")
	@Column(name = "F_18", nullable = true, length = 50)
	private String businessProcess;

	@Schema(
		description = "ID of parent audit entry for linking related operations",
		example = "1000",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Positive(message = "Parent audit ID must be positive")
	@Column(name = "F_19", nullable = true)
	private Long parentAuditId;

	@Schema(
		description = "Additional JSON metadata for custom audit information",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_20", nullable = true, columnDefinition = "TEXT")
	private String metadata;

}
