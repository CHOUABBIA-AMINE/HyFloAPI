package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProcessingPlantRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProcessingPlantSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.ProcessingPlantMapper;
import dz.sh.trc.hyflo.core.network.topology.model.ProcessingPlant;
import dz.sh.trc.hyflo.core.network.topology.repository.ProcessingPlantRepository;
import dz.sh.trc.hyflo.core.network.topology.service.ProcessingPlantService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ProcessingPlantServiceImpl extends AbstractCrudService<CreateProcessingPlantRequest, UpdateProcessingPlantRequest, ProcessingPlantResponse, ProcessingPlantSummary, ProcessingPlant, Long> implements ProcessingPlantService {

    public ProcessingPlantServiceImpl(ProcessingPlantRepository repository, ProcessingPlantMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ProcessingPlant> getEntityClass() {
        return ProcessingPlant.class;
    }
}