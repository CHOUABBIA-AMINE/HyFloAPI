/**
 *	
 *	@Author		: CHOUABBIA-AMINE
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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FlowEvent")
@Table(name="T_03_03_04",
    indexes = {
        @Index(name="T_03_03_04_IX_01", columnList="F_01"),
        @Index(name="T_03_03_04_IX_02", columnList="F_06"),
        @Index(name="T_03_03_04_IX_03", columnList="F_02,F_01")
    })
public class FlowEvent extends GenericModel {
    
    @Column(name="F_01", nullable=false)
    @NotNull(message = "Event timestamp is required")
    private LocalDateTime eventTimestamp;
    
    @Column(name="F_02", length=50, nullable=false)
    @NotBlank(message = "Event type is required")
    @Pattern(regexp = "VALVE_OPENED|VALVE_CLOSED|PUMP_STARTED|PUMP_STOPPED|MAINTENANCE_START|MAINTENANCE_END|LEAK_DETECTED|PRESSURE_SURGE|FLOW_INTERRUPTION|FLOW_RESTORED|SYSTEM_STARTUP|SYSTEM_SHUTDOWN|EMERGENCY_STOP|CALIBRATION|INSPECTION|OTHER", 
             message = "Invalid event type")
    private String eventType;
    
    @Column(name="F_03", length=100, nullable=false)
    @NotBlank(message = "Title is required")
    private String title;
    
    @Column(name="F_04", length=2000, nullable=true)
    private String description;
    
    @Column(name="F_05", length=20, nullable=false)
    @NotBlank(message = "Severity is required")
    @Pattern(regexp = "INFO|WARNING|CRITICAL|EMERGENCY", 
             message = "Invalid severity")
    private String severity;
    
    @ManyToOne
    @JoinColumn(name="F_06", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_04_FK_01"), nullable=false)
    @NotNull(message = "Infrastructure is required")
    private Infrastructure infrastructure;
    
    @ManyToOne
    @JoinColumn(name="F_07", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_04_FK_02"), nullable=false)
    @NotNull(message = "Reporter is required")
    private Employee reportedBy;
    
    @ManyToOne
    @JoinColumn(name="F_08", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_04_FK_03"), nullable=true)
    private FlowReading relatedReading;
    
    @ManyToOne
    @JoinColumn(name="F_09", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_04_FK_04"), nullable=true)
    private FlowAlert relatedAlert;
    
    @Column(name="F_10", nullable=true)
    private LocalDateTime startTime;
    
    @Column(name="F_11", nullable=true)
    private LocalDateTime endTime;
    
    @Column(name="F_12", length=20, nullable=false)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "OPEN|IN_PROGRESS|COMPLETED|CANCELLED", 
             message = "Invalid status")
    private String status;
    
    @Column(name="F_13", length=2000, nullable=true)
    private String actionTaken;
    
    @Column(name="F_14", nullable=false)
    @NotNull(message = "Impact on flow flag is required")
    private Boolean impactOnFlow;
}
