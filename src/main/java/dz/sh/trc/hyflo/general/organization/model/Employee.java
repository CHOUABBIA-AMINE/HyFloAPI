/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Employee
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.model;

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
@Entity(name="Employee")
@Table(name="T_01_03_04")
public class Employee extends Person {

	@Column(name="F_14", length=50)
	private String registrationNumber;

	@ManyToOne
	@JoinColumn(name="F_15", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_04_FK_01"), nullable=false)
	private Job job;
	
}
