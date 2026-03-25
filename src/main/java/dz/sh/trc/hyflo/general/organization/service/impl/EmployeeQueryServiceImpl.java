/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeQueryServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : General / Organization
 *
 *  Phase 3 — Commit 24
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
import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDto;
import dz.sh.trc.hyflo.general.organization.mapper.EmployeeMapper;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.service.EmployeeQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query implementation for Employee read operations.
 *
 * Uses EmployeeMapper from Phase 2.
 * Returns EmployeeReadDto — no raw entity exposure.
 *
 * Phase 3 — Commit 24
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeReadDto getById(Long id) {
        return employeeRepository.findById(id)
                .map(EmployeeMapper::toReadDto)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    @Override
    public Page<EmployeeReadDto> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper::toReadDto);
    }

    @Override
    public List<EmployeeReadDto> getByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(EmployeeMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeReadDto> search(String query) {
        return employeeRepository.searchByAnyField(query)
                .stream()
                .map(EmployeeMapper::toReadDto)
                .collect(Collectors.toList());
    }
}
