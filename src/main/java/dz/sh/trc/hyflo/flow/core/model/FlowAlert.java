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
import dz.sh.trc.hyflo.flow.common.model.AlertStatus;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
@Entity(name = "FlowAlert")
@Table(name = "T_03_03_05", indexes = {@Index(name = "T_03_03_05_IX_01", columnList = "F_01"),
        							   @Index(name = "T_03_03_05_IX_02", columnList = "F_13"),
        							   @Index(name = "T_03_03_05_IX_03", columnList = "F_11, F_13")})
public class FlowAlert extends GenericModel {
    
    @NotNull(message = "Alert timestamp is required")
    @PastOrPresent(message = "Alert timestamp cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDateTime alertTimestamp;
    
    @NotNull(message = "Actual value is required")
    @Column(name = "F_02", nullable = false)
    private Double actualValue;
    
    @Column(name = "F_03")
    private Double thresholdValue;
    
    @Size(max = 1000, message = "Alert message cannot exceed 1000 characters")
    @Column(name = "F_04", length = 1000)
    private String message;
    
    @PastOrPresent(message = "Acknowledgment time cannot be in the future")
    @Column(name = "F_05")
    private LocalDateTime acknowledgedAt;
    
    @PastOrPresent(message = "Resolution time cannot be in the future")
    @Column(name = "F_06")
    private LocalDateTime resolvedAt;
    
    @Size(max = 1000, message = "Resolution notes cannot exceed 1000 characters")
    @Column(name = "F_07", length = 1000)
    private String resolutionNotes;
    
    @NotNull(message = "Notification sent flag is required")
    @Column(name = "F_08", nullable = false)
    private Boolean notificationSent;
    
    @PastOrPresent(message = "Notification time cannot be in the future")
    @Column(name = "F_09")
    private LocalDateTime notificationSentAt;
    
    @ManyToOne
    @JoinColumn(name = "F_10", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_04"))
    private Employee resolvedBy;
    
    @NotNull(message = "Threshold reference is required")
    @ManyToOne
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_01"), nullable = false)
    private FlowThreshold threshold;
    
    @ManyToOne
    @JoinColumn(name = "F_12", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_02"))
    private FlowReading flowReading;
    
    @ManyToOne
    @JoinColumn(name = "F_13", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_02"))
    private AlertStatus status;
    
    @ManyToOne
    @JoinColumn(name = "F_14", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_05_FK_03"))
    private Employee acknowledgedBy;
    
}
