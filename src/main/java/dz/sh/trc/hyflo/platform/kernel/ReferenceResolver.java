/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ReferenceResolver
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: Class
 *	@Layer		: Template
 *	@Package	: Configuration / Template
 *
 **/

package dz.sh.trc.hyflo.platform.kernel;

import org.springframework.stereotype.Component;

import dz.sh.trc.hyflo.core.flow.reference.model.ValidationStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.core.network.topology.repository.PipelineRepository;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.mapstruct.TargetType;

@Component
@RequiredArgsConstructor
public class ReferenceResolver {

    private final EntityManager entityManager;
    private final PipelineRepository pipelineRepository;
    private final EmployeeRepository employeeRepository;
    private final ValidationStatusRepository validationStatusRepository;

    public Pipeline getPipeline(Long id) {
        if (id == null) return null;
        return pipelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline with id " + id + " not found"));
    }

    public Employee getEmployee(Long id) {
        if (id == null) return null;
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    public ValidationStatus getValidationStatus(Long id) {
        if (id == null) return null;
        return validationStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus with id " + id + " not found"));
    }

    /**
     * Generic resolver for MapStruct to map IDs to Entities.
     * Uses JPA getReference to avoid database hits for associations.
     */
    public <T> T resolve(Long id, @TargetType Class<T> entityClass) {
        if (id == null) {
            return null;
        }
        return entityManager.getReference(entityClass, id);
    }
}