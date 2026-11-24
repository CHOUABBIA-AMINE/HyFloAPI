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

import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

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
@Table(name = "T_10_02_09", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_09_UK_01", columnNames = {"F_01"}))
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

    @Past
    @Column(name = "F_08")
    private LocalDate birthDate;

    @Size(max = 255)
    @Column(name = "F_09", length = 255)
    private String birthPlaceAr;

    @Size(max = 255)
    @Column(name = "F_10", length = 255)
    private String birthPlaceLt;

    @Size(max = 255)
    @Column(name = "F_11", length = 255)
    private String addressAr;

    @Size(max = 255)
    @Column(name = "F_12", length = 255)
    private String addressLt;

    @Email
    @Size(max = 254)
    @Column(name = "F_13", length = 254)
    private String email;

    @Size(max = 80)
    @Column(name = "F_14", length = 80)
    private String phone;

    @Positive
    @Column(name = "F_15")
    private Long birthLocalityId;

    @Positive
    @Column(name = "F_16")
    private Long addressLocalityId;

    /** Nationality, not birthplace or residence country. Reference owns the country catalog. */
    @Positive
    @Column(name = "F_17")
    private Long nationalityCountryId;

    @Positive
    @Column(name = "F_18")
    private Long pictureFileId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_19", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_10_02_09_FK_01"))
    private EmployeeType employeeType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_20", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_10_02_09_FK_02"))
    private EmployeeStatus employeeStatus;

    public Employee(String employeeNumber, String firstNameAr, String lastNameAr, String firstNameLt, String lastNameLt,
            LocalDate hiredOn, LocalDate employmentEndsOn, EmployeeType employeeType, EmployeeStatus employeeStatus) {
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
        this.employeeType = Objects.requireNonNull(employeeType, "Employee type is required");
        this.employeeStatus = Objects.requireNonNull(employeeStatus, "Employee status is required");
    }

    public void updatePersonalDetails(LocalDate birthDate, String birthPlaceAr, String birthPlaceLt,
            String addressAr, String addressLt, String email, String phone,
            Long birthLocalityId, Long addressLocalityId, Long nationalityCountryId, Long pictureFileId) {
        if (birthDate != null && (!birthDate.isBefore(LocalDate.now()) || !birthDate.isBefore(hiredOn))) {
            throw new IllegalArgumentException("Birth date must precede today and the hire date");
        }
        for (Long reference : new Long[] {birthLocalityId, addressLocalityId, nationalityCountryId, pictureFileId}) {
            if (reference != null && reference <= 0) {
                throw new IllegalArgumentException("Reference IDs must be positive");
            }
        }
        this.birthDate = birthDate;
        this.birthPlaceAr = birthPlaceAr;
        this.birthPlaceLt = birthPlaceLt;
        this.addressAr = addressAr;
        this.addressLt = addressLt;
        this.email = email;
        this.phone = phone;
        this.birthLocalityId = birthLocalityId;
        this.addressLocalityId = addressLocalityId;
        this.nationalityCountryId = nationalityCountryId;
        this.pictureFileId = pictureFileId;
    }

    private static boolean hasName(String firstName, String lastName) {
        return firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank();
    }
}
