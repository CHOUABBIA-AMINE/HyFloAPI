/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReading
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Entity(name="FlowReading")
@Table(name="T_03_03_01", indexes = {@Index(name="T_03_03_01_IX_01", columnList="F_01"),
        							 @Index(name="T_03_03_01_IX_02", columnList="F_08"),
        							 @Index(name="T_03_03_01_IX_03", columnList="F_14,F_01"),
        							 @Index(name="T_03_03_01_IX_04", columnList="F_12,F_08")},
    					  uniqueConstraints = {@UniqueConstraint(name="T_03_03_01_UK_01", columnNames={"F_14", "F_02", "F_01"})})
public class FlowReading extends GenericModel {
    
    @Column(name = "F_01", nullable = false)
    private LocalDateTime recordedAt;
    
    // TECHNICAL MEASUREMENTS
    @Column(name = "F_02", precision = 12, scale = 2)
    private Double pressure;
    
    @Column(name = "F_03", precision = 12, scale = 2)
    private Double temperature;
    
    @Column(name = "F_04", precision = 12, scale = 2)
    private Double flowRate;
    
    @Column(name = "F_05")
    private LocalDateTime validatedAt;
    
    @Column(name = "F_06", length = 500)
    private String notes;
    
    // FULL WORKFLOW
    @ManyToOne
	@JoinColumn(name="F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_01_FK_01"), nullable = false)
    private Employee recordedBy;
    
    @ManyToOne
    @JoinColumn(name="F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_01_FK_02"), nullable = false)
    private Employee validatedBy;
    
    @ManyToOne
    @JoinColumn(name="F_09", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_01_FK_03"), nullable = false)
    private ValidationStatus validationStatus;
    
    @ManyToOne
    @JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_01_FK_04"), nullable = false)
    private Pipeline pipeline;           // Pipeline (inherits Product)
}