package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilityResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilitySummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.FacilityMapper;
import dz.sh.trc.hyflo.core.network.topology.model.Facility;
import dz.sh.trc.hyflo.core.network.topology.repository.FacilityRepository;
import dz.sh.trc.hyflo.core.network.topology.service.FacilityService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FacilityServiceImpl extends AbstractCrudService<CreateFacilityRequest, UpdateFacilityRequest, FacilityResponse, FacilitySummary, Facility, Long> implements FacilityService {

    public FacilityServiceImpl(FacilityRepository repository, FacilityMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Facility> getEntityClass() {
        return Facility.class;
    }
}