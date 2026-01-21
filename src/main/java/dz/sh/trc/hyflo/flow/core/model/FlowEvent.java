/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowEvent
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import dz.sh.trc.hyflo.flow.common.model.Severity;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Operational event log for infrastructure activities and incidents.
 * Records maintenance, operational changes, incidents, and investigations.
 */
@Schema(description = "Operational event log for infrastructure activities, incidents, and maintenance tracking")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowEvent")
@Table(name = "T_03_03_04", indexes = {@Index(name = "T_03_03_04_IX_01", columnList = "F_01"),
									   @Index(name = "T_03_03_04_IX_02", columnList = "F_06"),
									   @Index(name = "T_03_03_04_IX_03", columnList = "F_01,F_02,F_07")})
public class FlowEvent extends GenericModel {
    
	@Schema(
		description = "Timestamp when event occurred",
		example = "2026-01-22T03:15:00",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Event timestamp is mandatory")
	@PastOrPresent(message = "Event timestamp cannot be in the future")
	@Column(name = "F_01", nullable = false)
	private LocalDateTime eventTimestamp;
    
	@Schema(
		description = "Brief title summarizing the event",
		example = "Emergency Shutdown - Pipeline P-101",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Event title is mandatory")
	@Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
	@Column(name = "F_02", length = 100, nullable = false)
	private String title;
    
	@Schema(
		description = "Detailed description of the event",
		example = "Automatic shutdown triggered due to pressure spike. Leak detected at KP 45.2.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 2000
	)
	@Size(max = 2000, message = "Description must not exceed 2000 characters")
	@Column(name = "F_03", length = 2000)
	private String description;
    
	@Schema(
		description = "Severity level of this event",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_04", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_01"))
	private Severity severity;
    
	@Schema(
		description = "Infrastructure affected by this event",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Infrastructure reference is mandatory")
	@ManyToOne
	@JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_03"), nullable = false)
	private Infrastructure infrastructure;
    
	@Schema(
		description = "Employee who reported this event",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Reporter is mandatory")
	@ManyToOne
	@JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_04"), nullable = false)
	private Employee reportedBy;
    
	@Schema(
		description = "Related flow reading if this event was triggered by a measurement",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_05"))
	private FlowReading relatedReading;
    
	@Schema(
		description = "Related alert if this event originated from an alert",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_06"))
	private FlowAlert relatedAlert;
    
	@Schema(
		description = "When the event started (for ongoing events)",
		example = "2026-01-22T03:15:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@PastOrPresent(message = "Start time cannot be in the future")
	@Column(name = "F_10")
	private LocalDateTime startTime;
    
	@Schema(
		description = "When the event ended (for completed events)",
		example = "2026-01-22T05:30:00",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Column(name = "F_11")
	private LocalDateTime endTime;
    
	@Schema(
		description = "Current status of this event",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne
	@JoinColumn(name = "F_12", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_07"))
	private EventStatus status;
    
	@Schema(
		description = "Description of corrective action taken",
		example = "Isolated affected segment, deployed repair crew. Leak sealed and pressure restored.",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 2000
	)
	@Size(max = 2000, message = "Action taken must not exceed 2000 characters")
	@Column(name = "F_13", length = 2000)
	private String actionTaken;
    
	@Schema(
		description = "Indicates if this event impacted flow operations",
		example = "true",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Impact on flow flag is mandatory")
	@Column(name = "F_14", nullable = false)
	private Boolean impactOnFlow;
}
