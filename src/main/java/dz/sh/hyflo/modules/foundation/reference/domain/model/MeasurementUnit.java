/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : MeasurementUnit
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Reference
 * @Package     : dz.sh.hyflo.modules.foundation.reference.domain.model
 *
 * @Description : Defines engineering unit identity and symbol without conversion semantics.
 */

package dz.sh.hyflo.modules.foundation.reference.domain.model;

import java.util.Locale;
import java.util.stream.Stream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "T_10_03_03", uniqueConstraints =
        @UniqueConstraint(name = "T_10_03_03_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeasurementUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(name = "F_01", length = 64, nullable = false, updatable = false)
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
    @Size(max = 40)
    @Column(name = "F_05", length = 40, nullable = false)
    private String symbol;

    @Column(name = "F_06", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_03_03_FK_01"))
    private MeasurementDomain measurementDomain;

    public MeasurementUnit(String code, String nameAr, String nameFr, String nameEn, String symbol,
            MeasurementDomain measurementDomain) {
        if (measurementDomain == null) {
            throw new IllegalArgumentException("A measurement domain is required");
        }
        if (code == null || !code.strip().toUpperCase(Locale.ROOT).matches("[A-Z][A-Z0-9_]{0,63}")) {
            throw new IllegalArgumentException("A valid stable measurement unit code is required");
        }
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        if (Stream.of(nameAr, nameFr, nameEn).anyMatch(value -> value != null && value.length() > 120)) {
            throw new IllegalArgumentException("Localized names must not exceed 120 characters");
        }
        if (symbol == null || symbol.isBlank() || symbol.length() > 40) {
            throw new IllegalArgumentException("A valid measurement unit symbol is required");
        }
        this.code = code.strip().toUpperCase(Locale.ROOT);
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.symbol = symbol;
        this.active = true;
        this.measurementDomain = measurementDomain;
    }

    public void retire() {
        active = false;
    }
}
