/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Language
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Reference
 * @Package     : dz.sh.hyflo.modules.foundation.reference.domain.model
 *
 * @Description : Defines supported-language metadata and reading direction.
 */

package dz.sh.hyflo.modules.foundation.reference.domain.model;

import java.util.Locale;
import java.util.stream.Stream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "T_10_03_02", uniqueConstraints =
        @UniqueConstraint(name = "T_10_03_02_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "F_01", length = 2, nullable = false, updatable = false)
    private String code;

    @Size(max = 120)
    @Column(name = "F_02", length = 120)
    private String nameAr;

    @Size(max = 120)
    @Column(name = "F_03", length = 120)
    private String nameFr;

    @Size(max = 120)
    @Column(name = "F_04", length = 120)
    private String nameEn;

    @NotBlank
    @Size(max = 120)
    @Column(name = "F_05", length = 120, nullable = false)
    private String nativeName;

    @Column(name = "F_06", nullable = false)
    private boolean rightToLeft;

    @Column(name = "F_07", nullable = false)
    private boolean active;

    public Language(String code, String nameAr, String nameFr, String nameEn, String nativeName, boolean rightToLeft) {
        if (code == null || !code.strip().matches("[A-Za-z]{2}")) {
            throw new IllegalArgumentException("A two-letter language code is required");
        }
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        if (Stream.of(nameAr, nameFr, nameEn).anyMatch(value -> value != null && value.length() > 120)) {
            throw new IllegalArgumentException("Localized names must not exceed 120 characters");
        }
        if (nativeName == null || nativeName.isBlank() || nativeName.length() > 120) {
            throw new IllegalArgumentException("A valid native language name is required");
        }
        this.code = code.strip().toLowerCase(Locale.ROOT);
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.nativeName = nativeName;
        this.rightToLeft = rightToLeft;
        this.active = true;
    }

    public void retire() {
        active = false;
    }
}
