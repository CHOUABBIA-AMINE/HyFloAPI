/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Parameter
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * File metadata entity for document and media management.
 * Stores file references for employee photos, technical documents, reports, etc.
 */
@Schema(description = "Parameter used across the system")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="File")
@Table(name="T_00_00_01")
public class Parameter extends GenericModel {

	@Schema(
		description = "Identifier of the parameter",
		example = "flow_reading_validator",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 100
	)
	@NotBlank(message = "Key is mandatory")
	@Size(max = 100, message = "Key must not exceed 100 characters")
	@Column(name="F_01", length=100, nullable=false)
	private String key;

	@Schema(
		description = "Value of parameter",
		example = "FK:3 -> TRC",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 250
	)
	@NotBlank(message = "Value is mandatory")
	@Size(max = 250, message = "Value must not exceed 250 characters")
	@Column(name="F_02", length=250, nullable=false)
	private String value;

	@Schema(
		description = "Type of the parameter value",
		example = "FOREIGN_KEY",
		requiredMode = Schema.RequiredMode.REQUIRED,
		allowableValues = {"NUMBER", "STRING", "FOREIGN_KEY", "OTHER"},
		maxLength = 20
	)
	@NotBlank(message = "Type is mandatory")
	@Size(max = 20, message = "Type must not exceed 20 characters")
	@Column(name="F_03", length=20, nullable=false)
	private String type;
	
	@Schema(
		description = "Parameter description",
		example = "the parameter is used to define ....",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 250
	)
	@Size(max = 250, message = "File type must not exceed 20 characters")
	@Column(name="F_04", length=250, nullable=true)
	private String description;
}
