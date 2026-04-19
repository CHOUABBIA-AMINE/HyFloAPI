package dz.sh.trc.hyflo.core.general.organization.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeSummary;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface EmployeeMapper extends BaseMapper<CreateEmployeeRequest, UpdateEmployeeRequest, EmployeeResponse, EmployeeSummary, Employee> {
    
    @Override
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "birthLocality", ignore = true)
    @Mapping(target = "addressLocality", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "picture", ignore = true)
    Employee toEntity(CreateEmployeeRequest dto);

    @Override
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobDesignationFr", source = "job.designationFr")
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "birthLocalityId", source = "birthLocality.id")
    @Mapping(target = "addressLocalityId", source = "addressLocality.id")
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "pictureId", source = "picture.id")
    EmployeeResponse toResponse(Employee entity);

    @Override
    @Mapping(target = "jobDesignationFr", source = "job.designationFr")
    EmployeeSummary toSummary(Employee entity);

    @Override
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "birthLocality", ignore = true)
    @Mapping(target = "addressLocality", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "picture", ignore = true)
    void updateEntityFromRequest(UpdateEmployeeRequest dto, @MappingTarget Employee entity);
}
