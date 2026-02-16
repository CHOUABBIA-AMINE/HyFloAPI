/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: QualityFlagDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - CRITICAL: Fixed code size and added missing severityLevel
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.model.QualityFlag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for QualityFlag entity.
 * Used for API requests and responses related to data quality assessment.
 */
@Schema(description = "Quality flag DTO for flow measurement quality assessment and data validation")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QualityFlagDTO extends GenericDTO<QualityFlag> {

    @Schema(
        description = "Unique code identifying the quality flag (uppercase letters, numbers, hyphens, underscores)",
        example = "SENSOR_ERROR",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    @NotBlank(message = "Quality flag code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    private String code;

    @Schema(
        description = "Quality flag designation in Arabic",
        example = "خطأ في الحساس",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Quality flag designation in English",
        example = "Sensor Error",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Quality flag designation in French (required for system use)",
        example = "Erreur de capteur",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "Detailed description in Arabic",
        example = "بيانات غير صحيحة من الحساس",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 255
    )
    @Size(max = 255, message = "Arabic description must not exceed 255 characters")
    private String descriptionAr;

    @Schema(
        description = "Detailed description in English",
        example = "Invalid data received from sensor",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 255
    )
    @Size(max = 255, message = "English description must not exceed 255 characters")
    private String descriptionEn;

    @Schema(
        description = "Detailed description in French",
        example = "Données invalides reçues du capteur",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 255
    )
    @Size(max = 255, message = "French description must not exceed 255 characters")
    private String descriptionFr;

    @Schema(
        description = "Severity level of the quality issue",
        example = "HIGH",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 20,
        allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"}
    )
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", 
             message = "Severity level must be LOW, MEDIUM, HIGH, or CRITICAL")
    @Size(max = 20, message = "Severity level must not exceed 20 characters")
    private String severityLevel;

    @Override
    public QualityFlag toEntity() {
        QualityFlag entity = new QualityFlag();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDescriptionAr(this.descriptionAr);
        entity.setDescriptionEn(this.descriptionEn);
        entity.setDescriptionFr(this.descriptionFr);
        entity.setSeverityLevel(this.severityLevel);
        return entity;
    }

    @Override
    public void updateEntity(QualityFlag entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.descriptionAr != null) entity.setDescriptionAr(this.descriptionAr);
        if (this.descriptionEn != null) entity.setDescriptionEn(this.descriptionEn);
        if (this.descriptionFr != null) entity.setDescriptionFr(this.descriptionFr);
        if (this.severityLevel != null) entity.setSeverityLevel(this.severityLevel);
    }

    /**
     * Converts a QualityFlag entity to its DTO representation.
     *
     * @param entity the QualityFlag entity to convert
     * @return QualityFlagDTO or null if entity is null
     */
    public static QualityFlagDTO fromEntity(QualityFlag entity) {
        if (entity == null) return null;
        
        return QualityFlagDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .descriptionAr(entity.getDescriptionAr())
                .descriptionEn(entity.getDescriptionEn())
                .descriptionFr(entity.getDescriptionFr())
                .severityLevel(entity.getSeverityLevel())
                .build();
    }
}
