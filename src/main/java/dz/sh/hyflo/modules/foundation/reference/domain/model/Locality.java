/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Locality
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Reference
 * @Package     : dz.sh.hyflo.modules.foundation.reference.domain.model
 *
 * @Description : Defines a locality within a district.
 *
 */

package dz.sh.hyflo.modules.foundation.reference.domain.model;

import java.util.Locale;
import java.util.Objects;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_03_06", uniqueConstraints =
        @UniqueConstraint(name = "T_10_03_06_UK_01", columnNames = {"F_07", "F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Locality {

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

    @Column(name = "F_05", nullable = false)
    private boolean active;

    @Size(max = 40)
    @Column(name = "F_06", length = 40)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_03_06_FK_01"))
    private District district;

    public Locality(String code, String nameAr, String nameFr, String nameEn, District district) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("A geographic code is required");
        }
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        this.code = code.strip().toUpperCase(Locale.ROOT);
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.active = true;
        this.district = Objects.requireNonNull(district, "District is required");
    }
    public void retire() {
        active = false;
    }

    public void updatePostalCode(String postalCode) {
        if (postalCode != null && postalCode.length() > 40) {
            throw new IllegalArgumentException("Postal code must not exceed 40 characters");
        }
        this.postalCode = postalCode;
    }
}
