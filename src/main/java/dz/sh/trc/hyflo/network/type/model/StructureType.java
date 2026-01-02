/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: StructureType
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.model;

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

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="StructureType")
@Table(name="T_01_04_06", uniqueConstraints = { @UniqueConstraint(name = "T_01_04_06_UK_01", columnNames = { "F_03" })})
public class StructureType extends GenericModel {
	
	@Column(name="F_01", length=100)
	private String designationAr;

	@Column(name="F_02", length=100)
	private String designationEn;
	
	@Column(name="F_03", length=100, nullable=false)
	private String designationFr;
	
	@Column(name="F_04", length=10)
	private String acronymAr;

	@Column(name="F_05", length=10)
	private String acronymEn;
	
	@Column(name="F_06", length=10)
	private String acronymFr;
}
