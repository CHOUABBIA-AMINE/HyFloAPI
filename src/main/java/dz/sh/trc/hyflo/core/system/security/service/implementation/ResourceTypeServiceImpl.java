package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.ResourceTypeMapper;
import dz.sh.trc.hyflo.core.system.security.model.ResourceType;
import dz.sh.trc.hyflo.core.system.security.repository.ResourceTypeRepository;
import dz.sh.trc.hyflo.core.system.security.service.ResourceTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ResourceTypeServiceImpl extends AbstractCrudService<CreateResourceTypeRequest, UpdateResourceTypeRequest, ResourceTypeResponse, ResourceTypeSummary, ResourceType, Long> implements ResourceTypeService {

    public ResourceTypeServiceImpl(ResourceTypeRepository repository, ResourceTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ResourceType> getEntityClass() {
        return ResourceType.class;
    }
}
