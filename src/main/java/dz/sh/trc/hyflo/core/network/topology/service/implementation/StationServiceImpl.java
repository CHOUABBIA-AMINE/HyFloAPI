package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.StationMapper;
import dz.sh.trc.hyflo.core.network.topology.model.Station;
import dz.sh.trc.hyflo.core.network.topology.repository.StationRepository;
import dz.sh.trc.hyflo.core.network.topology.service.StationService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class StationServiceImpl extends AbstractCrudService<CreateStationRequest, UpdateStationRequest, StationResponse, StationSummary, Station> implements StationService {

    public StationServiceImpl(StationRepository repository, StationMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Station> getEntityClass() {
        return Station.class;
    }
}