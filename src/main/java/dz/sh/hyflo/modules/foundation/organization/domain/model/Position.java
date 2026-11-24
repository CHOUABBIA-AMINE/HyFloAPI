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
@Table(name = "T_10_02_03", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_03_UK_01", columnNames = {"F_01"}))
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

    public Position(String code, String titleAr, String titleFr, String titleEn) {
        this.code = code;
        if (Stream.of(titleAr, titleFr, titleEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized title is required");
        }
        this.titleAr = titleAr;
        this.titleFr = titleFr;
        this.titleEn = titleEn;
    }
}
