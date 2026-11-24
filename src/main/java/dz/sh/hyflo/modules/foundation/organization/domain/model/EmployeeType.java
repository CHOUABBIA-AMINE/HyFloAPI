/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : EmployeeType
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Defines the finite database-managed EmployeeType catalog.
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
@Table(name = "T_10_02_02", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_02_UK_01", columnNames = {"F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeType {

    public static final String PERMANENT_CODE = "PERMANENT";
    public static final String TEMPORARY_CODE = "TEMPORARY";
    public static final String CONTRACTUAL_CODE = "CONTRACTUAL";
    public static final String INTERN_CODE = "INTERN";
    public static final String CONSULTANT_CODE = "CONSULTANT";
    private static final Set<String> CODES = Set.of(PERMANENT_CODE, TEMPORARY_CODE, CONTRACTUAL_CODE, INTERN_CODE, CONSULTANT_CODE);

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

    public EmployeeType(String code, String nameAr, String nameFr, String nameEn) {
        if (code == null || !CODES.contains(code)) {
            throw new IllegalArgumentException("Unsupported EmployeeType code");
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
