/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeQueryService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Phase 4: search signature aligned to repository
 *                             searchByAnyField(String, Pageable) — returns Page
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDTO;

/**
 * Query contract for Employee read operations.
 *
 * All methods return EmployeeReadDTO — never raw entities.
 */
public interface EmployeeQueryService {

    EmployeeReadDTO getById(Long id);

    Page<EmployeeReadDTO> getAll(Pageable pageable);

    List<EmployeeReadDTO> getByDepartment(Long departmentId);

    Page<EmployeeReadDTO> search(String query, Pageable pageable);
}
