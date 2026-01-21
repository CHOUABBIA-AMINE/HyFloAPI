/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSystem
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Product;
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
 * Represents a pipeline system consisting of multiple interconnected pipelines,
 * stations, and terminals dedicated to transporting a specific hydrocarbon product.
 * Examples: Hassi R'Mel Gas System, Hassi Messaoud Crude System.
 */
@Schema(description = "Integrated pipeline system for transporting specific hydrocarbon products across multiple facilities")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="PipelineSystem")
@Table(name="T_02_03_07", uniqueConstraints = {@UniqueConstraint(name="T_02_03_07_UK_01", columnNames={"F_01"})})
public class PipelineSystem extends GenericModel {

	@Schema(
		description = "Unique identification code for the pipeline system",
		example = "SYS-HM-SKD",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 50
	)
	@NotBlank(message = "Pipeline system code is mandatory")
	@Size(max = 50, message = "Pipeline system code must not exceed 50 characters")
	@Column(name="F_01", length=50, nullable=false)
	private String code;

	@Schema(
		description = "Official name of the pipeline system",
		example = "Hassi Messaoud - Skikda Crude Oil System",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Pipeline system name is mandatory")
	@Size(max = 100, message = "Pipeline system name must not exceed 100 characters")
	@Column(name="F_02", length=100, nullable=false)
	private String name;

	@Schema(
		description = "Primary hydrocarbon product transported through this system",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Product is mandatory")
	@ManyToOne
	@JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_01"), nullable=false)
	private Product product;

	@Schema(
		description = "Current operational status of the pipeline system",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Operational status is mandatory")
	@ManyToOne
	@JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_02"), nullable=false)
	private OperationalStatus operationalStatus;

	@Schema(
		description = "Organizational structure responsible for managing this pipeline system",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Structure is mandatory")
	@ManyToOne
	@JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_07_FK_03"), nullable=false)
	private Structure structure;
}
