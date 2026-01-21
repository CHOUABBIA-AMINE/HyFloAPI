/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowAlert
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
@Entity(name="FlowAlert")
@Table(name="T_03_03_06",
    indexes = {
        @Index(name="T_03_03_06_IX_01", columnList="F_01"),
        @Index(name="T_03_03_06_IX_02", columnList="F_07"),
        @Index(name="T_03_03_06_IX_03", columnList="F_02,F_07")
    })
public class FlowAlert extends GenericModel {
    
    @Column(name="F_01", nullable=false)
    @NotNull(message = "Alert timestamp is required")
    private LocalDateTime alertTimestamp;
    
    @ManyToOne
    @JoinColumn(name="F_02", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_06_FK_01"), nullable=false)
    @NotNull(message = "Threshold is required")
    private FlowThreshold threshold;
    
    @ManyToOne
    @JoinColumn(name="F_03", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_06_FK_02"), nullable=true)
    private FlowReading flowReading;
    
    @Column(name="F_04", nullable=false)
    @NotNull(message = "Actual value is required")
    private Double actualValue;
    
    @Column(name="F_05", nullable=true)
    private Double thresholdValue;
    
    @Column(name="F_06", length=1000, nullable=true)
    private String message;
    
    @Column(name="F_07", length=20, nullable=false)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "NEW|ACKNOWLEDGED|INVESTIGATING|RESOLVED|FALSE_ALARM", 
             message = "Invalid status")
    private String status;
    
    @Column(name="F_08", nullable=true)
    private LocalDateTime acknowledgedAt;
    
    @ManyToOne
    @JoinColumn(name="F_09", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_06_FK_03"), nullable=true)
    private Employee acknowledgedBy;
    
    @Column(name="F_10", nullable=true)
    private LocalDateTime resolvedAt;
    
    @ManyToOne
    @JoinColumn(name="F_11", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_06_FK_04"), nullable=true)
    private Employee resolvedBy;
    
    @Column(name="F_12", length=1000, nullable=true)
    private String resolutionNotes;
    
    @Column(name="F_13", nullable=false)
    @NotNull(message = "Notification sent flag is required")
    private Boolean notificationSent;
    
    @Column(name="F_14", nullable=true)
    private LocalDateTime notificationSentAt;
}
