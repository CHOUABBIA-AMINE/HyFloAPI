/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EmployeeService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Employee Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeService extends GenericService<Employee, EmployeeDTO, Long> {

    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<Employee, Long> getRepository() {
        return employeeRepository;
    }

    @Override
    protected String getEntityName() {
        return "Employee";
    }

    @Override
    protected EmployeeDTO toDTO(Employee entity) {
        return EmployeeDTO.fromEntity(entity);
    }

    @Override
    protected Employee toEntity(EmployeeDTO dto) {
    	return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Employee entity, EmployeeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        log.info("Creating employee: registrationNumber={}", 
                dto.getRegistrationNumber());
        return super.create(dto);
    }

    @Override
    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        log.info("Updating employee with ID: {}", id);
        return super.update(id, dto);
    }

    public List<EmployeeDTO> getAll() {
        log.debug("Getting all employees without pagination");
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<EmployeeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for employees with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<EmployeeDTO> getByStructureId(Long structureId) {
        log.debug("Getting employees by structure ID: {}", structureId);
        return employeeRepository.findByJob_StructureId(structureId).stream()
                .map(EmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
