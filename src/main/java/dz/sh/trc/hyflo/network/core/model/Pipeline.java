/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Pipeline
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity(name="Pipeline")
@Table(name="T_02_03_07")
public class Pipeline extends Infrastructure {  
    
    @Column(name="F_08", nullable=false)
    private Double nominalDiameter;

    @Column(name="F_09", nullable=false)
    private Double length;

    @Column(name="F_10", nullable=false)
    private Double nominalThickness;

    @Column(name="F_11", nullable=false)
    private Double nominalRoughness;

    @Column(name="F_12", nullable=false)
    private Double designMaxServicePressure;

    @Column(name="F_13", nullable=false)
    private Double operationalMaxServicePressure;

    @Column(name="F_14", nullable=false)
    private Double designMinServicePressure;

    @Column(name="F_15", nullable=false)
    private Double operationalMinServicePressure;

    @Column(name="F_16", nullable=false)
    private Double designCapacity;

    @Column(name="F_17", nullable=false)
    private Double operationalCapacity;

    @ManyToOne
    @JoinColumn(name="F_18", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_01"), nullable=false)
    private Alloy nominalConstructionMaterial;

    @ManyToOne
    @JoinColumn(name="F_19", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_02"), nullable=false)
    private Alloy nominalExteriorCoating;

    @ManyToOne
    @JoinColumn(name="F_20", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_03"), nullable=false)
    private Alloy nominalInteriorCoating;

	@ManyToOne
    @JoinColumn(name="F_21", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_04"), nullable=false)
    private Vendor vendor;
	
    @ManyToOne
    @JoinColumn(name="F_22", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_05"), nullable=false)
    private PipelineSystem pipelineSystem;

    @ManyToOne
    @JoinColumn(name="F_23", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_06"), nullable=false)
    private Facility departureFacility;

    @ManyToOne
    @JoinColumn(name="F_24", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_07"), nullable=false)
    private Facility arrivalFacility;
    
}
