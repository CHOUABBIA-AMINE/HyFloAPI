/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ShelfFloor
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Model
 *
 **/

package dz.sh.trc.hyflo.common.environment.model;

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
 * ShelfFloor Entity - Extends GenericModel
 * Represents floors/levels within shelves with multilingual designations
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ShelfFloor")
@Table(name="T_01_01_05", uniqueConstraints = { @UniqueConstraint(name = "T_01_01_05_UK_01", columnNames = { "F_01" }),
												@UniqueConstraint(name = "T_01_01_05_UK_02", columnNames = { "F_02" }),
												@UniqueConstraint(name = "T_01_01_05_UK_03", columnNames = { "F_05" })})
public class ShelfFloor extends GenericModel {
	
	@Column(name="F_01")
	private Integer floorLevel;
	
	@Column(name="F_02", length=20, nullable=false)
	private String code;
	
	@Column(name="F_03", length=200)
	private String designationAr;
	
	@Column(name="F_04", length=200)
	private String designationEn;
	
	@Column(name="F_05", length=200, nullable=false)
	private String designationFr;

}
