/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MeasurementHour
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.flow.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity(name="MeasurementHour")
@Table(name="T_03_04_01", uniqueConstraints = { @UniqueConstraint(name="T_03_04_01_UK_01", columnNames={"F_01"})})
public class MeasurementHour extends GenericModel {

    @Column(name="F_01", length=20, nullable=false)
    private String code;
    
}
