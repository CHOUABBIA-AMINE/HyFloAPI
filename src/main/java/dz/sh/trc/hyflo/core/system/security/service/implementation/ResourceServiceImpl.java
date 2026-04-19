package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.ResourceMapper;
import dz.sh.trc.hyflo.core.system.security.model.Resource;
import dz.sh.trc.hyflo.core.system.security.model.ResourceType;
import dz.sh.trc.hyflo.core.system.security.repository.ResourceRepository;
import dz.sh.trc.hyflo.core.system.security.service.ResourceService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ResourceServiceImpl extends AbstractCrudService<CreateResourceRequest, UpdateResourceRequest, ResourceResponse, ResourceSummary, Resource> implements ResourceService {

    public ResourceServiceImpl(ResourceRepository repository, ResourceMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }

    @Override
    protected void beforeCreate(CreateResourceRequest request, Resource entity) {
        if (request.resourceTypeId() != null) {
            entity.setResourceType(referenceResolver.resolve(request.resourceTypeId(), ResourceType.class));
        }
    }

    @Override
    protected void beforeUpdate(UpdateResourceRequest request, Resource entity) {
        if (request.resourceTypeId() != null) {
            entity.setResourceType(referenceResolver.resolve(request.resourceTypeId(), ResourceType.class));
        }
    }
}
