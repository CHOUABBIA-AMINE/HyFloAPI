package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.StationTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.StationType;
import dz.sh.trc.hyflo.core.network.type.repository.StationTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.StationTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class StationTypeServiceImpl extends AbstractCrudService<CreateStationTypeRequest, UpdateStationTypeRequest, StationTypeResponse, StationTypeSummary, StationType> implements StationTypeService {

    public StationTypeServiceImpl(StationTypeRepository repository, StationTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<StationType> getEntityClass() {
        return StationType.class;
    }
}