/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeCommandServiceImpl
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Phase 4: fix log getter names (DTO has getFirstName/getLastName)
 *                             Remove setActive(false) — no active field on Employee/Person entity
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.general.organization.dto.command.EmployeeCommandDTO;
import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDTO;
import dz.sh.trc.hyflo.general.organization.mapper.EmployeeMapper;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.general.organization.service.EmployeeCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for Employee write operations.
 *
 * Uses EmployeeMapper from Phase 2.
 * Returns EmployeeReadDTO — no raw entity exposure.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeReadDTO createEmployee(EmployeeCommandDTO command) {
        log.info("Creating employee: {} {}", command.getFirstName(), command.getLastName());
        Employee entity = EmployeeMapper.toEntity(command);
        return EmployeeMapper.toReadDTO(employeeRepository.save(entity));
    }

    @Override
    public EmployeeReadDTO updateEmployee(Long id, EmployeeCommandDTO command) {
        log.info("Updating employee ID: {}", id);
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        EmployeeMapper.updateEntity(command, existing);
        return EmployeeMapper.toReadDTO(employeeRepository.save(existing));
    }

    @Override
    public void deactivateEmployee(Long id) {
        log.info("Deactivating employee ID: {}", id);
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        // NOTE: Employee/Person entity has no 'active' field.
        // TODO: Add boolean active field to Person entity to enable soft-delete.
        employeeRepository.save(existing);
    }
}
