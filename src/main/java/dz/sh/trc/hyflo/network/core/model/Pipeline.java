/**
 *	
 *	@author		: MEDJERAB ABIR
 *
 *	@Name		: Pipeline
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
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

/**
 * Pipeline Entity - Extends GenericModel
 * 
 * Database table: T_03_03_07
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: code - Pipeline code (unique, required)
 * - F_02: diameter - Pipeline diameter (required)
 * - F_03: length - Pipeline length in km (required)
 * - F_04: maxPressure - Maximum operating pressure (required)
 * - F_05: constructionMaterial - Material type (required)
 * - F_06: commissioningDate - Commissioning date (required)
 * - F_07: operationalStatus - Operational status relationship (required)
 * - F_08: pipelineSystem - Pipeline system relationship (required)
 * - F_09: departureTerminal - Departure facility relationship (required)
 * - F_10: arrivalTerminal - Arrival facility relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Pipeline")
@Table(name="T_03_03_07")
public class Pipeline extends Infrastructure {  
    
    @Column(name="F_07", nullable=false)
    private Double nominalDiameter;

    @Column(name="F_08", nullable=false)
    private Double length;

    @Column(name="F_09", nullable=false)
    private Double nominalThickness;

    @Column(name="F_10", nullable=false)
    private Double nominalRoughness;

    @Column(name="F_11", nullable=false)
    private Double designMaxServicePressure;

    @Column(name="F_12", nullable=false)
    private Double operationalMaxServicePressure;

    @Column(name="F_13", nullable=false)
    private Double designMinServicePressure;

    @Column(name="F_14", nullable=false)
    private Double operationalMinServicePressure;

    @Column(name="F_15", nullable=false)
    private Double designCapacity;

    @Column(name="F_16", nullable=false)
    private Double operationalCapacity;

    @ManyToOne
    @JoinColumn(name="F_17", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_01"), nullable=false)
    private Alloy nominalConstructionMaterial;

    @ManyToOne
    @JoinColumn(name="F_18", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_02"), nullable=false)
    private Alloy nominalExteriorCoating;

    @ManyToOne
    @JoinColumn(name="F_19", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_03"), nullable=false)
    private Alloy nominalInteriorCoating;

	@ManyToOne
    @JoinColumn(name="F_20", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_04"), nullable=false)
    private Vendor vendor;
	
    @ManyToOne
    @JoinColumn(name="F_21", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_05"), nullable=false)
    private PipelineSystem pipelineSystem;

    @ManyToOne
    @JoinColumn(name="F_22", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_06"), nullable=false)
    private Facility departureFacility;

    @ManyToOne
    @JoinColumn(name="F_23", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_07_FK_07"), nullable=false)
    private Facility arrivalFacility;
}
