/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FacilityType
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.sh.trc.hyflo.network.type.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * FacilityType Entity - Extends GenericModel
 * 
 * Database table: T_03_02_01
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: code - Type code (required)
 * - F_02: designation - Type designation (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FacilityType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_03_01_04", uniqueConstraints = { @UniqueConstraint(name="T_03_01_04_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_03_01_04_UK_02", columnNames={"F_04"})})
public class FacilityType extends GenericModel {

    @Column(name="F_01", length=20, nullable=false)
    private String code;

    @Column(name="F_02", length=100, nullable=true)
    private String designationAr;

    @Column(name="F_03", length=100, nullable=true)
    private String designationEn;

    @Column(name="F_04", length=100, nullable=false)
    private String designationFr;
}
