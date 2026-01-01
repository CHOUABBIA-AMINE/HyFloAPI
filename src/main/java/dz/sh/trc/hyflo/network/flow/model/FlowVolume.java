/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowVolume
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.flow.model;

import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
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

/**
 * Facility Entity - Extends Facility
 * 
 * Database table: T_03_02_04
 * inherited from GenericModel:
 * - Primary key: F_00 (id) 
 * inherited from Infrastructure:
 * - Field: F_01 (code)
 * - Field: F_02 (name)
 * - Field: F_03 (installationDate)
 * - Field: F_04 (commissioningDate)
 * - Field: F_05 (decommissioningDate)
 * - Field: F_06 (operationalStatus)
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: name - Facility name (required)
 * - F_02: code - Facility code (unique, required)
 * - F_03: operationalStatus - Operational status relationship (required)
 * - F_04: location - Location relationship (required)
 * - F_05: facilityType - Facility type relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FlowVolume")
@Table(name="T_03_04_02", uniqueConstraints = { @UniqueConstraint(name="T_03_04_02_UK_01", columnNames={"F_02", "F_03", "F_04"})})
public class FlowVolume extends GenericModel {

    @Column(name="F_01", nullable=false)
    private Double volume;
    
    @Column(name = "F_02", nullable = false)
    protected LocalDate measurementDate;

    @ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_04_02_FK_01"), nullable=false)
    private MeasurementHour measurementHour;

    @ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_04_02_FK_02"), nullable=false)
    private Pipeline pipeline;
    
}
