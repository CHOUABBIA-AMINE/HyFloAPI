package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.InfrastructureMapper;
import dz.sh.trc.hyflo.core.network.topology.model.Infrastructure;
import dz.sh.trc.hyflo.core.network.topology.repository.InfrastructureRepository;
import dz.sh.trc.hyflo.core.network.topology.service.InfrastructureService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class InfrastructureServiceImpl extends AbstractCrudService<CreateInfrastructureRequest, UpdateInfrastructureRequest, InfrastructureResponse, InfrastructureSummary, Infrastructure, Long> implements InfrastructureService {

    public InfrastructureServiceImpl(InfrastructureRepository repository, InfrastructureMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Infrastructure> getEntityClass() {
        return Infrastructure.class;
    }
}
