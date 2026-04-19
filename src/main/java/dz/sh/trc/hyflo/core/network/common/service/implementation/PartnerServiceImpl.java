package dz.sh.trc.hyflo.core.network.common.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.localization.model.Country;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerSummary;
import dz.sh.trc.hyflo.core.network.common.mapper.PartnerMapper;
import dz.sh.trc.hyflo.core.network.common.model.Partner;
import dz.sh.trc.hyflo.core.network.common.repository.PartnerRepository;
import dz.sh.trc.hyflo.core.network.common.service.PartnerService;
import dz.sh.trc.hyflo.core.network.type.model.PartnerType;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PartnerServiceImpl extends AbstractCrudService<CreatePartnerRequest, UpdatePartnerRequest, PartnerResponse, PartnerSummary, Partner, Long> implements PartnerService {

    public PartnerServiceImpl(PartnerRepository repository, PartnerMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Partner> getEntityClass() {
        return Partner.class;
    }

    @Override
    protected void beforeCreate(CreatePartnerRequest request, Partner entity) {
        entity.setPartnerType(referenceResolver.resolve(request.partnerTypeId(), PartnerType.class));
        entity.setCountry(referenceResolver.resolve(request.countryId(), Country.class));
    }

    @Override
    protected void beforeUpdate(UpdatePartnerRequest request, Partner entity) {
        if (request.partnerTypeId() != null) {
            entity.setPartnerType(referenceResolver.resolve(request.partnerTypeId(), PartnerType.class));
        }
        if (request.countryId() != null) {
            entity.setCountry(referenceResolver.resolve(request.countryId(), Country.class));
        }
    }
}
