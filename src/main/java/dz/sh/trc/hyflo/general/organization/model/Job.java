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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents job positions within the organizational structure.
 * Defines roles, titles, and responsibilities within specific structures.
 */
@Schema(description = "Job position within organizational structure defining roles and responsibilities")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Job")
@Table(name="T_01_03_02", uniqueConstraints = {@UniqueConstraint(name = "T_01_03_02_UK_01", columnNames = { "F_01" }),
											   @UniqueConstraint(name = "T_01_03_02_UK_02", columnNames = { "F_04" })})
public class Job extends GenericModel {
	
	@Schema(
		description = "Unique code identifying the job position",
		example = "ENG-PIP",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 10
	)
	@NotBlank(message = "Job code is mandatory")
	@Size(max = 10, message = "Job code must not exceed 10 characters")
	@Column(name="F_01", length=10, nullable=false)
	private String code;
	
	@Schema(
		description = "Job title in Arabic",
		example = "مهندس أنابيب",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Job title in English",
		example = "Pipeline Engineer",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Schema(
		description = "Job title in French (required for SONATRACH operations)",
		example = "Ingénieur Pipeline",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
	
	@Schema(
		description = "Organizational structure (department/division) where this job position exists",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Structure is mandatory")
	@ManyToOne
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_03_02_FK_01"), nullable=false)
	private Structure structure;
}
