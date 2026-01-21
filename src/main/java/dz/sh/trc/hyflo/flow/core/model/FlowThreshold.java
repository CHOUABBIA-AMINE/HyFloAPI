/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowThreshold
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.common.model.Product;
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
@Entity(name="FlowThreshold")
@Table(name="T_03_03_02", indexes = {@Index(name="T_03_03_02_IX_01", columnList="F_07"),
        							 @Index(name="T_03_03_02_IX_02", columnList="F_02,F_03")},
    					  uniqueConstraints = {@UniqueConstraint(name="T_03_03_02_UK_01", columnNames={"F_07", "F_02", "F_03"})})
public class FlowThreshold extends GenericModel {
    
    @Column(name="F_01", nullable = false, precision = 12, scale = 2)
    private Double pressureMin;
    
    @Column(name="F_02", nullable = false, precision = 12, scale = 2)
    private Double pressureMax;
    
    @Column(name="F_03", nullable = false, precision = 12, scale = 2)
    private Double temperatureMin;
    
    @Column(name="F_04", nullable = false, precision = 12, scale = 2)
    private Double temperatureMax;
    
    @Column(name="F_05", nullable = false, precision = 12, scale = 2)
    private Double flowRateMin;
    
    @Column(name="F_06", nullable = false, precision = 12, scale = 2)
    private Double flowRateMax;
    
    @Column(name="F_07", nullable = false)
    private Double alertTolerance;       // ±5%, ±10%
    
    @Column(name="F_08", nullable = false)
    private Boolean active;
    
	@ManyToOne
	@JoinColumn(name="F_09", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_03_02_FK_01"), nullable=false)
    private Pipeline pipeline;
    
	@ManyToOne
	@JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_03_02_FK_01"), nullable=false)
    private Product product;
	
}
