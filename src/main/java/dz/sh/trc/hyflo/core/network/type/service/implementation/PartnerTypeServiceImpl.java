package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.PartnerTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.PartnerType;
import dz.sh.trc.hyflo.core.network.type.repository.PartnerTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.PartnerTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PartnerTypeServiceImpl extends AbstractCrudService<CreatePartnerTypeRequest, UpdatePartnerTypeRequest, PartnerTypeResponse, PartnerTypeSummary, PartnerType> implements PartnerTypeService {

    public PartnerTypeServiceImpl(PartnerTypeRepository repository, PartnerTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<PartnerType> getEntityClass() {
        return PartnerType.class;
    }
}