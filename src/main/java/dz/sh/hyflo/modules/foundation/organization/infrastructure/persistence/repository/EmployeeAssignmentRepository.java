/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : EmployeeAssignmentRepository
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository
 *
 * @Description : Persists assignments and queries assignment history for an employee.
 *
 */

package dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeAssignment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface EmployeeAssignmentRepository extends Repository<EmployeeAssignment, Long> {

    EmployeeAssignment save(EmployeeAssignment assignment);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select a from EmployeeAssignment a
            where a.employee.id = :employeeId and (a.validTo is null or a.validTo > :date)
            """)
    List<EmployeeAssignment> findCurrentOrFutureForUpdate(@Param("employeeId") long employeeId, @Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"employee", "organizationUnit", "position"})
    @Query("""
            select a from EmployeeAssignment a
            where a.employee.id = :employeeId and a.validFrom <= :date and (a.validTo is null or a.validTo > :date)
            """)
    Optional<EmployeeAssignment> findEffectiveOn(@Param("employeeId") long employeeId, @Param("date") LocalDate date);
}
