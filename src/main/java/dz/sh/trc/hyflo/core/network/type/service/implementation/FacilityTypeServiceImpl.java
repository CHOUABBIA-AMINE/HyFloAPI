package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.FacilityTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.FacilityType;
import dz.sh.trc.hyflo.core.network.type.repository.FacilityTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.FacilityTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FacilityTypeServiceImpl extends AbstractCrudService<CreateFacilityTypeRequest, UpdateFacilityTypeRequest, FacilityTypeResponse, FacilityTypeSummary, FacilityType, Long> implements FacilityTypeService {

    public FacilityTypeServiceImpl(FacilityTypeRepository repository, FacilityTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<FacilityType> getEntityClass() {
        return FacilityType.class;
    }
}