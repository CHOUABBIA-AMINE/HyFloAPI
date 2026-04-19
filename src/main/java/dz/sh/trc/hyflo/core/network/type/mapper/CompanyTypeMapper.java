package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.CompanyType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface CompanyTypeMapper extends BaseMapper<CreateCompanyTypeRequest, UpdateCompanyTypeRequest, CompanyTypeResponse, CompanyTypeSummary, CompanyType> {
}