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
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import dz.sh.trc.hyflo.flow.common.model.Severity;
import dz.sh.trc.hyflo.flow.type.model.EventType;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Operational event log for infrastructure activities and incidents.
 */
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
    
    @NotNull(message = "Event timestamp is required")
    @PastOrPresent(message = "Event timestamp cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDateTime eventTimestamp;
    
    @NotBlank(message = "Event title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(name = "F_02", length = 100, nullable = false)
    private String title;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    @Column(name = "F_03", length = 2000)
    private String description;
    
	@ManyToOne
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_01"), nullable = false)
    private Severity severity;
    
	@ManyToOne
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_01"), nullable = false)
    private EventType eventType;
    
	@NotNull(message = "Infrastructure reference is required")
    @ManyToOne
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_01"), nullable = false)
    private Infrastructure infrastructure;
    
	@NotNull(message = "Reporter is required")
    @ManyToOne
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_02"), nullable = false)
    private Employee reportedBy;
    
	@ManyToOne
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_03"))
    private FlowReading relatedReading;
    
    @ManyToOne
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_04_FK_04"))
    private FlowAlert relatedAlert;
    
    @PastOrPresent(message = "Start time cannot be in the future")
    @Column(name = "F_10")
    private LocalDateTime startTime;
    
    @Column(name = "F_11")
    private LocalDateTime endTime;
    
    @NotBlank(message = "Event status is required")
    @Pattern(regexp = "OPEN|IN_PROGRESS|COMPLETED|CANCELLED", message = "Status must be one of: OPEN, IN_PROGRESS, COMPLETED, CANCELLED")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "F_12", length = 20, nullable = false)
    private EventStatus status;
    
    @Size(max = 2000, message = "Action taken cannot exceed 2000 characters")
    @Column(name = "F_13", length = 2000)
    private String actionTaken;
    
    @NotNull(message = "Impact on flow flag is required")
    @Column(name = "F_14", nullable = false)
    private Boolean impactOnFlow;
}