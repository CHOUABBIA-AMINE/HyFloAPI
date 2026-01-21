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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents SONATRACH employees extending Person with employment details.
 * Links personnel to specific job positions within the organizational structure.
 */
@Schema(description = "SONATRACH employee with employment details, job assignment, and registration number")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Employee")
@Table(name="T_01_03_04")
public class Employee extends Person {

	@Schema(
		description = "Employee registration number (matricule) assigned by HR",
		example = "20250001",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 50
	)
	@Size(max = 50, message = "Registration number must not exceed 50 characters")
	@Column(name="F_14", length=50)
	private String registrationNumber;

	@Schema(
		description = "Current job position held by the employee",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Job is mandatory")
	@ManyToOne
	@JoinColumn(name="F_15", referencedColumnName="F_00", foreignKey=@ForeignKey(name="T_01_03_04_FK_01"), nullable=false)
	private Job job;
	
}
