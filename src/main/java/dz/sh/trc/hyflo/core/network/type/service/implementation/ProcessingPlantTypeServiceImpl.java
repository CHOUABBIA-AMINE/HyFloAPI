package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProcessingPlantTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProcessingPlantTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.ProcessingPlantTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.ProcessingPlantType;
import dz.sh.trc.hyflo.core.network.type.repository.ProcessingPlantTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.ProcessingPlantTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ProcessingPlantTypeServiceImpl extends AbstractCrudService<CreateProcessingPlantTypeRequest, UpdateProcessingPlantTypeRequest, ProcessingPlantTypeResponse, ProcessingPlantTypeSummary, ProcessingPlantType> implements ProcessingPlantTypeService {

    public ProcessingPlantTypeServiceImpl(ProcessingPlantTypeRepository repository, ProcessingPlantTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ProcessingPlantType> getEntityClass() {
        return ProcessingPlantType.class;
    }
}