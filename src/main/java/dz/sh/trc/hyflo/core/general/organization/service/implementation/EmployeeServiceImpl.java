package dz.sh.trc.hyflo.core.general.organization.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.localization.model.Country;
import dz.sh.trc.hyflo.core.general.localization.model.Locality;
import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeSummary;
import dz.sh.trc.hyflo.core.general.organization.mapper.EmployeeMapper;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.general.organization.model.Job;
import dz.sh.trc.hyflo.core.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.core.general.organization.service.EmployeeService;
import dz.sh.trc.hyflo.core.system.security.model.Role;
import dz.sh.trc.hyflo.core.system.utility.model.File;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class EmployeeServiceImpl extends AbstractCrudService<CreateEmployeeRequest, UpdateEmployeeRequest, EmployeeResponse, EmployeeSummary, Employee> implements EmployeeService {

    public EmployeeServiceImpl(EmployeeRepository repository, EmployeeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

    @Override
    protected void beforeCreate(CreateEmployeeRequest request, Employee entity) {
        entity.setJob(referenceResolver.resolve(request.jobId(), Job.class));
        
        if (request.roleId() != null) {
            entity.setRole(referenceResolver.resolve(request.roleId(), Role.class));
        }
        if (request.birthLocalityId() != null) {
            entity.setBirthLocality(referenceResolver.resolve(request.birthLocalityId(), Locality.class));
        }
        if (request.addressLocalityId() != null) {
            entity.setAddressLocality(referenceResolver.resolve(request.addressLocalityId(), Locality.class));
        }
        if (request.countryId() != null) {
            entity.setCountry(referenceResolver.resolve(request.countryId(), Country.class));
        }
        if (request.pictureId() != null) {
            entity.setPicture(referenceResolver.resolve(request.pictureId(), File.class));
        }
    }

    @Override
    protected void beforeUpdate(UpdateEmployeeRequest request, Employee entity) {
        if (request.jobId() != null) {
            entity.setJob(referenceResolver.resolve(request.jobId(), Job.class));
        }
        if (request.roleId() != null) {
            entity.setRole(referenceResolver.resolve(request.roleId(), Role.class));
        }
        if (request.birthLocalityId() != null) {
            entity.setBirthLocality(referenceResolver.resolve(request.birthLocalityId(), Locality.class));
        }
        if (request.addressLocalityId() != null) {
            entity.setAddressLocality(referenceResolver.resolve(request.addressLocalityId(), Locality.class));
        }
        if (request.countryId() != null) {
            entity.setCountry(referenceResolver.resolve(request.countryId(), Country.class));
        }
        if (request.pictureId() != null) {
            entity.setPicture(referenceResolver.resolve(request.pictureId(), File.class));
        }
    }
}
