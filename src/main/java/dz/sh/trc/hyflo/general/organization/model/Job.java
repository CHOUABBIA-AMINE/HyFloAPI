/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Job
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
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

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Job")
@Table(name="T_01_03_02", uniqueConstraints = { @UniqueConstraint(name = "T_01_03_02_UK_01", columnNames = { "F_01" }),
												@UniqueConstraint(name = "T_01_03_02_UK_02", columnNames = { "F_04" })})
public class Job extends GenericModel {
	
	@Column(name="F_01", length=10, nullable=false)
	private String code;
	
	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
	
	@ManyToOne
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_03_02_FK_01"), nullable=false)
	private Structure structure;
}
