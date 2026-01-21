/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: File
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: System / Utility
 *
 **/

package dz.sh.trc.hyflo.system.utility.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * File metadata entity for document and media management.
 * Stores file references for employee photos, technical documents, reports, etc.
 */
@Schema(description = "File metadata for document and media management across the system")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="File")
@Table(name="T_00_01_01")
public class File extends GenericModel {

	@Schema(
		description = "File extension (without dot)",
		example = "pdf",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 20
	)
	@NotBlank(message = "File extension is mandatory")
	@Size(max = 20, message = "File extension must not exceed 20 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "File extension must contain only alphanumeric characters")
	@Column(name="F_01", length=20, nullable=false)
	private String extension;

	@Schema(
		description = "File size in bytes",
		example = "2048576",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@Positive(message = "File size must be positive")
	@Column(name="F_02")
	private Long size;

	@Schema(
		description = "File storage path (relative or absolute)",
		example = "/uploads/documents/2026/01/pipeline-report.pdf",
		requiredMode = Schema.RequiredMode.REQUIRED,
		maxLength = 250
	)
	@NotBlank(message = "File path is mandatory")
	@Size(max = 250, message = "File path must not exceed 250 characters")
	@Column(name="F_03", length=250, nullable=false)
	private String path;
	
	@Schema(
		description = "File type category for classification",
		example = "DOCUMENT",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
		maxLength = 20,
		allowableValues = {"DOCUMENT", "IMAGE", "VIDEO", "AUDIO", "ARCHIVE", "OTHER"}
	)
	@Size(max = 20, message = "File type must not exceed 20 characters")
	@Column(name="F_04", length=20, nullable=true)
	private String fileType;
}
