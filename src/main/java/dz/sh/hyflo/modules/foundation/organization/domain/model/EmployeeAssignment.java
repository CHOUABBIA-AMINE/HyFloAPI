/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : EmployeeAssignment
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.domain.model
 *
 * @Description : Records the effective period of an employee in a position and unit.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.domain.model;

import java.time.LocalDate;
import java.util.Objects;

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
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_10_02_12", uniqueConstraints =
        @UniqueConstraint(name = "T_10_02_12_UK_01", columnNames = {"F_05", "F_01"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", nullable = false, updatable = false)
    private LocalDate validFrom;

    @Column(name = "F_02")
    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_02_12_FK_01"))
    private OrganizationUnit organizationUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_02_12_FK_02"))
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_02_12_FK_03"))
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_10_02_12_FK_04"))
    private AssignmentType assignmentType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, foreignKey = @ForeignKey(name = "T_10_02_12_FK_05"))
    private AssignmentStatus assignmentStatus;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public EmployeeAssignment(Employee employee, OrganizationUnit organizationUnit, Position position, LocalDate validFrom,
            AssignmentType assignmentType, AssignmentStatus assignmentStatus) {
        this.employee = Objects.requireNonNull(employee, "Employee is required");
        this.organizationUnit = Objects.requireNonNull(organizationUnit, "Organization unit is required");
        this.position = Objects.requireNonNull(position, "Position is required");
        this.validFrom = Objects.requireNonNull(validFrom, "Assignment start is required");
        this.validTo = employee.getEmploymentEndsOn();
        this.assignmentType = Objects.requireNonNull(assignmentType, "Assignment type is required");
        this.assignmentStatus = Objects.requireNonNull(assignmentStatus, "Assignment status is required");
        if (!assignmentStatus.hasCode(AssignmentStatus.ACTIVE_CODE)) {
            throw new IllegalArgumentException("New assignments require ACTIVE status");
        }
        if (validFrom.isBefore(employee.getHiredOn()) || (validTo != null && !validFrom.isBefore(validTo))) {
            throw new IllegalArgumentException("Assignment start must be within the employment period");
        }
    }

    public boolean isEffectiveOn(LocalDate date) {
        Objects.requireNonNull(date, "Effective date is required");
        return !date.isBefore(validFrom) && (validTo == null || date.isBefore(validTo));
    }

    public void endOn(LocalDate date, AssignmentStatus endedStatus) {
        if (endedStatus == null || !endedStatus.hasCode(AssignmentStatus.ENDED_CODE)) {
            throw new IllegalArgumentException("Ending an assignment requires ENDED status");
        }
        Objects.requireNonNull(date, "Assignment end is required");
        if (!date.isAfter(validFrom) || (validTo != null && !date.isBefore(validTo))) {
            throw new IllegalArgumentException("Assignment end must shorten the current period without making it empty");
        }
        validTo = date;
        assignmentStatus = endedStatus;
    }
}
