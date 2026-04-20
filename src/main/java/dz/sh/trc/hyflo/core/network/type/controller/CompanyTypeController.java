package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.CompanyTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/company-types")
@Tag(name = "CompanyType API", description = "Endpoints for managing CompanyType")
public class CompanyTypeController extends BaseController<CreateCompanyTypeRequest, UpdateCompanyTypeRequest, CompanyTypeResponse, CompanyTypeSummary> {

    public CompanyTypeController(CompanyTypeService service) {
        super(service);
    }

    @Override
    protected Page<CompanyTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}