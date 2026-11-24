/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationUnit
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Represents a unit and its parent in the organization hierarchy.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.domain.model;

import java.time.LocalDate;
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
@Table(name = "T_10_02_11", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_11_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationUnit {

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

    @Column(name = "F_05", nullable = false, updatable = false)
    private LocalDate validFrom;

    @Column(name = "F_06")
    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_02_11_FK_01"))
    private OrganizationUnitType organizationUnitType;

    /** The parent unit for a non-root unit. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", updatable = false, foreignKey = @ForeignKey(name = "T_10_02_11_FK_02"))
    private OrganizationUnit organizationUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_10_02_11_FK_03"))
    private OrganizationUnitStatus organizationUnitStatus;

    public OrganizationUnit(String code, String nameAr, String nameFr, String nameEn,
            OrganizationUnitType organizationUnitType, OrganizationUnit organizationUnit,
            LocalDate validFrom, LocalDate validTo, OrganizationUnitStatus organizationUnitStatus) {
        this.code = code;
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
        this.organizationUnitType = Objects.requireNonNull(organizationUnitType, "Organization unit type is required");
        this.organizationUnit = organizationUnit;
        this.validFrom = Objects.requireNonNull(validFrom, "Unit validity start is required");
        if (validTo != null && !validTo.isAfter(validFrom)) {
            throw new IllegalArgumentException("Unit validity end must follow start");
        }
        this.validTo = validTo;
        this.organizationUnitStatus = Objects.requireNonNull(organizationUnitStatus, "Organization unit status is required");
    }
    /** Semantic parent accessor; the persistent association retains the repository naming convention. */
    public OrganizationUnit getParentUnit() {
        return organizationUnit;
    }
}
