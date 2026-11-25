/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : EmployeeAssignmentService
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.application.service
 *
 * @Description : Coordinates dated assignments and atomic transfers within Organization.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.application.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.Optional;

import dz.sh.hyflo.modules.foundation.organization.domain.model.AssignmentType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.AssignmentStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Employee;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeAssignment;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Position;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.EmployeeAssignmentRepository;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.EmployeeRepository;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.OrganizationUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeAssignmentService {

    private final EmployeeRepository employees;
    private final EmployeeAssignmentRepository assignments;
    private final OrganizationUnitRepository units;

    public EmployeeAssignmentService(EmployeeRepository employees, EmployeeAssignmentRepository assignments,
            OrganizationUnitRepository units) {
        this.employees = employees;
        this.assignments = assignments;
        this.units = units;
    }

    /** Already-authorized Identity write: validate and lock current placement, not legal employment ownership. */
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean lockEligibleEmployeeForIdentity(long employeeId, long companyId, LocalDate date) {
        if (employeeId <= 0 || companyId <= 0 || date == null || !date.equals(LocalDate.now(ZoneOffset.UTC))) return false;
        var employee = employees.findForAssociation(employeeId).orElse(null);
        if (employee == null || !employee.getEmployeeStatus().hasCode(EmployeeStatus.ACTIVE_CODE)
                || date.isBefore(employee.getHiredOn())
                || (employee.getEmploymentEndsOn() != null && !date.isBefore(employee.getEmploymentEndsOn()))) return false;
        var effective = assignments.findForAssociation(employeeId, date).stream()
                .filter(a -> a.isEffectiveOn(date)).toList();
        if (effective.size() != 1 || !effective.getFirst().getAssignmentStatus().hasCode(AssignmentStatus.ACTIVE_CODE)) return false;
        Long unitId = effective.getFirst().getOrganizationUnit().getId();
        Set<Long> visited = new HashSet<>();
        Long nearestCompany = null;
        while (unitId != null) {
            if (unitId <= 0 || !visited.add(unitId)) return false;
            var unit = units.findForInitialization(unitId).orElse(null);
            if (unit == null || !unit.getOrganizationUnitType().isActive()
                    || !unit.getOrganizationUnitStatus().hasCode(OrganizationUnitStatus.ACTIVE_CODE)
                    || date.isBefore(unit.getValidFrom())
                    || (unit.getValidTo() != null && !date.isBefore(unit.getValidTo()))) return false;
            if (nearestCompany == null && OrganizationUnitType.COMPANY_CODE.equals(unit.getOrganizationUnitType().getCode())) {
                nearestCompany = unitId;
                if (nearestCompany != companyId) return false;
            }
            unitId = unit.getParentUnit() == null ? null : unit.getParentUnit().getId();
        }
        return nearestCompany != null && nearestCompany == companyId && date.equals(LocalDate.now(ZoneOffset.UTC));
    }

    @Transactional
    public EmployeeAssignment assign(long employeeId, OrganizationUnit organizationUnit, Position position, LocalDate start,
            AssignmentType assignmentType, AssignmentStatus activeStatus) {
        Employee employee = lockEmployee(employeeId);
        EmployeeAssignment assignment = new EmployeeAssignment(employee, organizationUnit, position, start, assignmentType, activeStatus);
        if (!assignments.findCurrentOrFutureForUpdate(employeeId, start).isEmpty()) {
            throw new IllegalStateException("Assignment would overlap an existing period");
        }
        return assignments.save(assignment);
    }

    @Transactional
    public EmployeeAssignment transfer(long employeeId, OrganizationUnit organizationUnit, Position position, LocalDate date,
            AssignmentType assignmentType, AssignmentStatus activeStatus, AssignmentStatus endedStatus) {
        Employee employee = lockEmployee(employeeId);
        EmployeeAssignment replacement = new EmployeeAssignment(employee, organizationUnit, position, date, assignmentType, activeStatus);
        var candidates = assignments.findCurrentOrFutureForUpdate(employeeId, date);
        if (candidates.size() != 1 || !candidates.getFirst().isEffectiveOn(date)) {
            throw new IllegalStateException("Transfer requires one effective assignment and no later assignment");
        }
        candidates.getFirst().endOn(date, endedStatus);
        return assignments.save(replacement);
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeAssignment> findEffectiveOn(long employeeId, LocalDate date) {
        return assignments.findEffectiveOn(employeeId, Objects.requireNonNull(date, "Effective date is required"));
    }

    private Employee lockEmployee(long employeeId) {
        return employees.findByIdForUpdate(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee does not exist"));
    }
}
