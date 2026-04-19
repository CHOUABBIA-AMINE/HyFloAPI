package dz.sh.trc.hyflo.core.general.organization.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateEmployeeRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.EmployeeSummary;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.general.organization.service.EmployeeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/general/employees")
@Tag(name = "Employee API", description = "Endpoints for managing employees")
public class EmployeeController extends BaseController<CreateEmployeeRequest, UpdateEmployeeRequest, EmployeeResponse, EmployeeSummary, Employee, Long> {

    public EmployeeController(EmployeeService service) {
        super(service);
    }

    @Override
    protected Page<EmployeeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
