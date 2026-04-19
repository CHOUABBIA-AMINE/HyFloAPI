package dz.sh.trc.hyflo.core.general.organization.service;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface EmployeeService extends BaseService<CreateEmployeeRequest, UpdateEmployeeRequest, EmployeeResponse, EmployeeSummary> {
}
