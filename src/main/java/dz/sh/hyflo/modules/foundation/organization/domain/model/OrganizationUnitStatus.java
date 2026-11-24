/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationUnitStatus
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Defines the finite database-managed OrganizationUnitStatus catalog.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.domain.model;

import java.util.Set;
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
@Table(name = "T_10_02_06", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_06_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationUnitStatus {

    public static final String ACTIVE_CODE = "ACTIVE";
    public static final String INACTIVE_CODE = "INACTIVE";
    public static final String MERGED_CODE = "MERGED";
    public static final String CLOSED_CODE = "CLOSED";
    private static final Set<String> CODES = Set.of(ACTIVE_CODE, INACTIVE_CODE, MERGED_CODE, CLOSED_CODE);

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

    public OrganizationUnitStatus(String code, String nameAr, String nameFr, String nameEn) {
        if (code == null || !CODES.contains(code)) {
            throw new IllegalArgumentException("Unsupported OrganizationUnitStatus code");
        }
        if (Stream.of(nameAr, nameFr, nameEn).noneMatch(value -> value != null && !value.isBlank())) {
            throw new IllegalArgumentException("At least one localized name is required");
        }
        this.code = code;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.nameEn = nameEn;
    }

    public boolean hasCode(String expectedCode) {
        return code != null && code.equals(expectedCode);
    }
}
