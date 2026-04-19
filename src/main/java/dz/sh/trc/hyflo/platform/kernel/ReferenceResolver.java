/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ReferenceResolver
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-10-2025
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
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReferenceResolver {

    private final PipelineRepository pipelineRepository;
    private final EmployeeRepository employeeRepository;
    private final ValidationStatusRepository validationStatusRepository;

    public Pipeline getPipeline(Long id) {
        return pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline not found"));
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public ValidationStatus getValidationStatus(Long id) {
        return validationStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ValidationStatus not found"));
    }
}