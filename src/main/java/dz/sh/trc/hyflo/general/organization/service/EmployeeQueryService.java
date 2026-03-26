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

import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDto;

/**
 * Query contract for Employee read operations.
 *
 * All methods return EmployeeReadDto — never raw entities.
 */
public interface EmployeeQueryService {

    EmployeeReadDto getById(Long id);

    Page<EmployeeReadDto> getAll(Pageable pageable);

    List<EmployeeReadDto> getByDepartment(Long departmentId);

    Page<EmployeeReadDto> search(String query, Pageable pageable);
}
