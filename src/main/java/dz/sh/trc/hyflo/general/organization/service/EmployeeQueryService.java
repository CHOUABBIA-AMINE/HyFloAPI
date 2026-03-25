/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeQueryService
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

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDto;

/**
 * Query contract for Employee read operations.
 *
 * All methods return EmployeeReadDto — never raw entities.
 *
 * Phase 3 — Commit 24
 */
public interface EmployeeQueryService {

    EmployeeReadDto getById(Long id);

    Page<EmployeeReadDto> getAll(Pageable pageable);

    List<EmployeeReadDto> getByDepartment(Long departmentId);

    List<EmployeeReadDto> search(String query);
}
