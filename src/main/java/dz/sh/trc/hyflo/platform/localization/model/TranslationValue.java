package dz.sh.trc.hyflo.platform.localization.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores a translated value for a specific TranslationKey + language pair.
 * Unique constraint: one value per (key, language).
 *
 * Supported languages: FR, EN, AR
 */
@Schema(description = "Translated value for a specific key and language")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Entity(name = "TranslationValue")
@Table(name = "T_01_02_09", uniqueConstraints = {
    @UniqueConstraint(name = "T_01_02_09_UK_01", columnNames = {"F_01", "F_02"})
})
public class TranslationValue extends GenericModel {

    @Schema(description = "FK to translation key")
    @NotNull(message = "Translation key is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_01_02_09_FK_01"), nullable = false)
    private TranslationKey translationKey;

    @Schema(description = "Language code (FR, EN, AR)", example = "FR")
    @NotBlank(message = "Language is mandatory")
    @Size(max = 5, message = "Language code must not exceed 5 characters")
    @Column(name = "F_02", length = 5, nullable = false)
    private String language;

    @Schema(description = "Translated text value", example = "Lecture soumise pour validation")
    @NotBlank(message = "Translation value is mandatory")
    @Size(max = 2000, message = "Value must not exceed 2000 characters")
    @Column(name = "F_03", length = 2000, nullable = false)
    private String value;
}
