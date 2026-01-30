/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Structure
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
import dz.sh.trc.hyflo.general.type.model.StructureType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
 * Represents organizational structures within SONATRACH (departments, divisions, directorates).
 * Supports hierarchical organization with parent-child relationships.
 */
@Schema(description = "Organizational structure entity with hierarchical parent-child relationships")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Structure")
@Table(name="T_01_03_01", uniqueConstraints = {@UniqueConstraint(name = "T_01_03_01_UK_01", columnNames = { "F_01" }),
											   @UniqueConstraint(name = "T_01_03_01_UK_02", columnNames = { "F_04" })})
public class Structure extends GenericModel {
	
	@Schema(
		description = "Unique code identifying the organizational structure",
		example = "ACT-HM",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Structure code is mandatory")
	@Size(max = 50, message = "Structure code must not exceed 50 characters")
	@Column(name="F_01", length=50, nullable=false)
	private String code;
	
	@Schema(
		description = "Structure designation in Arabic",
		example = "إدارة النشاطات حاسي مسعود",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "Arabic designation must not exceed 100 characters")
	@Column(name="F_02", length=100)
	private String designationAr;

	@Schema(
		description = "Structure designation in English",
		example = "Hassi Messaoud Activities Division",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 100
	)
	@Size(max = 100, message = "English designation must not exceed 100 characters")
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Schema(
		description = "Structure designation in French (required for SONATRACH operations)",
		example = "Division des Activités Hassi Messaoud",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "French designation is mandatory")
	@Size(max = 100, message = "French designation must not exceed 100 characters")
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
	
	@Schema(
		description = "Type of organizational structure",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Structure type is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_03_01_FK_01"), nullable=false)
	private StructureType structureType;
	
	@Schema(
		description = "Parent structure in the organizational hierarchy",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_03_01_FK_02"), nullable=true)
	private Structure parentStructure;
}
