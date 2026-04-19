package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateCompanyTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.CompanyTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.CompanyTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.CompanyType;
import dz.sh.trc.hyflo.core.network.type.repository.CompanyTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.CompanyTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class CompanyTypeServiceImpl extends AbstractCrudService<CreateCompanyTypeRequest, UpdateCompanyTypeRequest, CompanyTypeResponse, CompanyTypeSummary, CompanyType> implements CompanyTypeService {

    public CompanyTypeServiceImpl(CompanyTypeRepository repository, CompanyTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<CompanyType> getEntityClass() {
        return CompanyType.class;
    }
}