/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Phase 4:
 *                  - findByDepartmentId → findByJob_StructureId (correct repo method)
 *                  - searchByAnyField(query) → searchByAnyField(query, pageable)
 *                  - search() returns Page<EmployeeReadDTO> to match interface
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDTO;
import dz.sh.trc.hyflo.general.organization.mapper.EmployeeMapper;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query implementation for Employee read operations.
 *
 * Uses EmployeeMapper from Phase 2.
 * Returns EmployeeReadDTO — no raw entity exposure.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeReadDTO getById(Long id) {
        return employeeRepository.findById(id)
                .map(EmployeeMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    @Override
    public Page<EmployeeReadDTO> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper::toReadDTO);
    }

    @Override
    public List<EmployeeReadDTO> getByDepartment(Long departmentId) {
        // Repository method: findByJob_StructureId (not findByDepartmentId)
        return employeeRepository.findByJob_StructureId(departmentId)
                .stream()
                .map(EmployeeMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeReadDTO> search(String query, Pageable pageable) {
        return employeeRepository.searchByAnyField(query, pageable)
                .map(EmployeeMapper::toReadDTO);
    }
}
