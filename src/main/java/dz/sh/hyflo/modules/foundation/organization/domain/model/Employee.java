/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : Employee
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Records an employee and their employment dates.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.domain.model;

import java.time.LocalDate;
import java.util.Objects;

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
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(name = "F_01", length = 64, nullable = false, updatable = false)
    private String employeeNumber;

    @Size(max = 120)
    @Column(name = "F_02", length = 120)
    private String firstNameAr;

    @Size(max = 120)
    @Column(name = "F_03", length = 120)
    private String lastNameAr;

    @Size(max = 120)
    @Column(name = "F_04", length = 120)
    private String firstNameLt;

    @Size(max = 120)
    @Column(name = "F_05", length = 120)
    private String lastNameLt;

    @Column(name = "F_06", nullable = false, updatable = false)
    private LocalDate hiredOn;

    @Column(name = "F_07", updatable = false)
    private LocalDate employmentEndsOn;

    public Employee(String employeeNumber, String firstNameAr, String lastNameAr, String firstNameLt, String lastNameLt,
            LocalDate hiredOn, LocalDate employmentEndsOn) {
        if (!hasName(firstNameAr, lastNameAr) && !hasName(firstNameLt, lastNameLt)) {
            throw new IllegalArgumentException("At least one complete employee name is required");
        }
        this.employeeNumber = employeeNumber;
        this.firstNameAr = firstNameAr;
        this.lastNameAr = lastNameAr;
        this.firstNameLt = firstNameLt;
        this.lastNameLt = lastNameLt;
        this.hiredOn = Objects.requireNonNull(hiredOn, "Hire date is required");
        if (employmentEndsOn != null && !employmentEndsOn.isAfter(hiredOn)) {
            throw new IllegalArgumentException("Employment end must follow the hire date");
        }
        this.employmentEndsOn = employmentEndsOn;
    }

    private static boolean hasName(String firstName, String lastName) {
        return firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank();
    }
}
