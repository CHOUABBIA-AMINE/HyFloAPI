package dz.sh.trc.hyflo.platform.localization.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a translatable key in the localization system.
 * Each key maps to one or more TranslationValue entries (one per language).
 *
 * Examples:
 *   FLOW_READING_SUBMITTED, FLOW_READING_VALIDATED,
 *   FLOW_ALERT_TRIGGERED, NOTIFICATION_TITLE_READING_SUBMITTED
 */
@Schema(description = "Translation key for the localization system")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Entity(name = "TranslationKey")
@Table(name = "T_01_02_08", uniqueConstraints = {
    @UniqueConstraint(name = "T_01_02_08_UK_01", columnNames = "F_01")
})
public class TranslationKey extends GenericModel {

    @Schema(description = "Unique translation key code", example = "FLOW_READING_SUBMITTED")
    @NotBlank(message = "Translation key code is mandatory")
    @Size(max = 100, message = "Code must not exceed 100 characters")
    @Column(name = "F_01", length = 100, nullable = false, unique = true)
    private String code;

    @Schema(description = "Module this key belongs to", example = "FLOW")
    @Size(max = 50, message = "Module must not exceed 50 characters")
    @Column(name = "F_02", length = 50)
    private String module;

    @Schema(description = "Description of what this key represents")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "F_03", length = 500)
    private String description;
}
