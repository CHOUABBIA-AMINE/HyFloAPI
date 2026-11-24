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
import java.util.Objects;
import java.util.Optional;

import dz.sh.hyflo.modules.foundation.organization.domain.model.Employee;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeAssignment;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Position;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.EmployeeAssignmentRepository;
import dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeAssignmentService {

    private final EmployeeRepository employees;
    private final EmployeeAssignmentRepository assignments;

    public EmployeeAssignmentService(EmployeeRepository employees, EmployeeAssignmentRepository assignments) {
        this.employees = employees;
        this.assignments = assignments;
    }

    @Transactional
    public EmployeeAssignment assign(long employeeId, OrganizationUnit organizationUnit, Position position, LocalDate start) {
        Employee employee = lockEmployee(employeeId);
        EmployeeAssignment assignment = new EmployeeAssignment(employee, organizationUnit, position, start);
        if (!assignments.findCurrentOrFutureForUpdate(employeeId, start).isEmpty()) {
            throw new IllegalStateException("Assignment would overlap an existing period");
        }
        return assignments.save(assignment);
    }

    @Transactional
    public EmployeeAssignment transfer(long employeeId, OrganizationUnit organizationUnit, Position position, LocalDate date) {
        Employee employee = lockEmployee(employeeId);
        EmployeeAssignment replacement = new EmployeeAssignment(employee, organizationUnit, position, date);
        var candidates = assignments.findCurrentOrFutureForUpdate(employeeId, date);
        if (candidates.size() != 1 || !candidates.getFirst().isEffectiveOn(date)) {
            throw new IllegalStateException("Transfer requires one effective assignment and no later assignment");
        }
        candidates.getFirst().endOn(date);
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
