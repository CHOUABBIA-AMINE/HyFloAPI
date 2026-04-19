package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface CompanyTypeService extends BaseService<CreateCompanyTypeRequest, UpdateCompanyTypeRequest, CompanyTypeResponse, CompanyTypeSummary> {
}