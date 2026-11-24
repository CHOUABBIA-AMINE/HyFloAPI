/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Location
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Reference
 * @Package     : dz.sh.hyflo.modules.foundation.reference.domain.model
 *
 * @Description : Represents a named WGS84 geographic point, optionally linked to a locality.
 */

package dz.sh.hyflo.modules.foundation.reference.domain.model;

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
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "T_10_03_07")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 120)
    @Column(name = "F_01", length = 120)
    private String nameAr;

    @Size(max = 120)
    @Column(name = "F_02", length = 120)
    private String nameFr;

    @Size(max = 120)
    @Column(name = "F_03", length = 120)
    private String nameEn;

    @NotNull
    @Column(name = "F_04", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "F_05", nullable = false)
    private Double longitude;

    // Metres above mean sea level; no survey datum or engineering qualification is implied.
    @Column(name = "F_06")
    private Double elevation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_10_03_07_FK_01"))
    private Locality locality;

    public Location(String nameAr, String nameFr, String nameEn,
            Double latitude, Double longitude, Double elevation, Locality locality) {
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        if (Stream.of(nameAr, nameFr, nameEn).anyMatch(value -> value != null && value.length() > 120)) {
            throw new IllegalArgumentException("Localized names must not exceed 120 characters");
        }
        if (latitude == null || !Double.isFinite(latitude) || latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be finite and between -90 and 90 degrees");
        }
        if (longitude == null || !Double.isFinite(longitude) || longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be finite and between -180 and 180 degrees");
        }
        if (elevation != null && !Double.isFinite(elevation)) {
            throw new IllegalArgumentException("Elevation must be finite when supplied");
        }
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.locality = locality;
    }
}
