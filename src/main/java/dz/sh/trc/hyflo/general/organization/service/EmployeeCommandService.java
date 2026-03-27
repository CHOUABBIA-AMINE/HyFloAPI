/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeCommandService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : General / Organization
 *
 *  Phase 3 — Commit 24
 *
 **/

package dz.sh.trc.hyflo.general.organization.service;

import dz.sh.trc.hyflo.general.organization.dto.command.EmployeeCommandDTO;
import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDTO;

/**
 * Command contract for Employee write operations.
 *
 * All methods return EmployeeReadDTO — never raw entities.
 *
 * Phase 3 — Commit 24
 */
public interface EmployeeCommandService {

    EmployeeReadDTO createEmployee(EmployeeCommandDTO command);

    EmployeeReadDTO updateEmployee(Long id, EmployeeCommandDTO command);

    /**
     * Deactivate an employee (soft disable — does not delete).
     * Deactivated employees cannot perform workflow actions.
     */
    void deactivateEmployee(Long id);
}
