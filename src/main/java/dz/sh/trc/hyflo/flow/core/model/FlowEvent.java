/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowEvent
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Operational event log for infrastructure activities and incidents.
 */
@Schema(description = "Flow event entity logging operational activities, maintenance, and incidents affecting flow infrastructure")
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
    
	@Schema(description = "Timestamp when the event occurred", example = "2026-01-21T10:00:00", required = true)
    @NotNull(message = "Event timestamp is required")
    @PastOrPresent(message = "Event timestamp cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDateTime eventTimestamp;
    
	@Schema(description = "Type of operational event", example = "MAINTENANCE_START", required = true)
    @NotBlank(message = "Event type is required")
    @Pattern(regexp = "VALVE_OPENED|VALVE_CLOSED|PUMP_STARTED|PUMP_STOPPED|MAINTENANCE_START|MAINTENANCE_END|LEAK_DETECTED|PRESSURE_SURGE|FLOW_INTERRUPTION|FLOW_RESTORED|SYSTEM_STARTUP|SYSTEM_SHUTDOWN|EMERGENCY_STOP|CALIBRATION|INSPECTION|OTHER", 
        message = "Invalid event type")
    @Size(max = 50, message = "Event type cannot exceed 50 characters")
    @Column(name = "F_02", length = 50, nullable = false)
    private String eventType;
    
	@Schema(description = "Brief title describing the event", example = "Scheduled Valve Maintenance", required = true)
    @NotBlank(message = "Event title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(name = "F_03", length = 100, nullable = false)
    private String title;
    
	@Schema(description = "Detailed description of the event", example = "Routine maintenance of pressure relief valve PRV-001 at station SP-12")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    @Column(name = "F_04", length = 2000)
    private String description;
    
	@Schema(description = "Severity level of the event", example = "INFO", required = true)
    @NotBlank(message = "Severity is required")
    @Pattern(regexp = "INFO|WARNING|CRITICAL|EMERGENCY", message = "Severity must be one of: INFO, WARNING, CRITICAL, EMERGENCY")
    @Size(max = 20, message = "Severity cannot exceed 20 characters")
    @Column(name = "F_05", length = 20, nullable = false)
    private String severity;
    
	@Schema(description = "Infrastructure element where the event occurred", required = true)
    @NotNull(message = "Infrastructure reference is required")
    @ManyToOne
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_01"), nullable = false)
    private Infrastructure infrastructure;
    
	@Schema(description = "Employee who reported the event", required = true)
    @NotNull(message = "Reporter is required")
    @ManyToOne
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_02"), nullable = false)
    private Employee reportedBy;
    
	@Schema(description = "Associated flow reading if applicable")
    @ManyToOne
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_03"))
    private FlowReading relatedReading;
    
	@Schema(description = "Associated alert if this event was triggered by an alert")
    @ManyToOne
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_04"))
    private FlowAlert relatedAlert;
    
	@Schema(description = "Start time for events with duration", example = "2026-01-21T10:00:00")
    @PastOrPresent(message = "Start time cannot be in the future")
    @Column(name = "F_10")
    private LocalDateTime startTime;
    
	@Schema(description = "End time for events with duration", example = "2026-01-21T12:00:00")
    @Column(name = "F_11")
    private LocalDateTime endTime;
    
	@Schema(description = "Current status of the event", example = "COMPLETED", required = true)
    @NotBlank(message = "Event status is required")
    @Pattern(regexp = "OPEN|IN_PROGRESS|COMPLETED|CANCELLED", message = "Status must be one of: OPEN, IN_PROGRESS, COMPLETED, CANCELLED")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "F_12", length = 20, nullable = false)
    private String status;
    
	@Schema(description = "Description of actions taken in response to the event", example = "Valve serviced and tested, returned to operational status")
    @Size(max = 2000, message = "Action taken cannot exceed 2000 characters")
    @Column(name = "F_13", length = 2000)
    private String actionTaken;
    
	@Schema(description = "Whether this event affected flow operations", example = "false", required = true)
    @NotNull(message = "Impact on flow flag is required")
    @Column(name = "F_14", nullable = false)
    private Boolean impactOnFlow;
}