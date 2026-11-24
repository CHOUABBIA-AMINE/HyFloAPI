/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Position
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Defines a reusable position in the organization.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.domain.model;

import java.util.stream.Stream;

import java.util.Objects;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_02_10", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_10_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position {

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
    private String titleAr;

    @Size(max = 120)
    @Column(name = "F_03", length = 120)
    private String titleFr;

    @Size(max = 120)
    @Column(name = "F_04", length = 120)
    private String titleEn;

    @Column(name = "F_05", columnDefinition = "text")
    private String descriptionAr;

    @Column(name = "F_06", columnDefinition = "text")
    private String descriptionFr;

    @Column(name = "F_07", columnDefinition = "text")
    private String descriptionEn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_10_02_10_FK_01"))
    private PositionLevel positionLevel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_10_02_10_FK_02"))
    private PositionStatus positionStatus;

    public Position(String code, String titleAr, String titleFr, String titleEn,
            String descriptionAr, String descriptionFr, String descriptionEn, PositionLevel positionLevel, PositionStatus positionStatus) {
        this.code = code;
        if (Stream.of(titleAr, titleFr, titleEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized title is required");
        }
        this.titleAr = titleAr;
        this.titleFr = titleFr;
        this.titleEn = titleEn;
        this.descriptionAr = descriptionAr;
        this.descriptionFr = descriptionFr;
        this.descriptionEn = descriptionEn;
        this.positionLevel = Objects.requireNonNull(positionLevel, "Position level is required");
        this.positionStatus = Objects.requireNonNull(positionStatus, "Position status is required");
    }
}
