/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : EmployeeStatus
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Defines the finite database-managed EmployeeStatus catalog.
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
@Table(name = "T_10_02_03", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_03_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeStatus {

    public static final String REGISTERED_CODE = "REGISTERED";
    public static final String ACTIVE_CODE = "ACTIVE";
    public static final String SUSPENDED_CODE = "SUSPENDED";
    public static final String RETIRED_CODE = "RETIRED";
    public static final String TERMINATED_CODE = "TERMINATED";
    private static final Set<String> CODES = Set.of(REGISTERED_CODE, ACTIVE_CODE, SUSPENDED_CODE, RETIRED_CODE, TERMINATED_CODE);

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

    public EmployeeStatus(String code, String nameAr, String nameFr, String nameEn) {
        if (code == null || !CODES.contains(code)) {
            throw new IllegalArgumentException("Unsupported EmployeeStatus code");
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
