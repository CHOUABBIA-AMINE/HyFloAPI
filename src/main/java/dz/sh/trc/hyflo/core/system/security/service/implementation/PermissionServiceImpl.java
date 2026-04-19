package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.PermissionMapper;
import dz.sh.trc.hyflo.core.system.security.model.Action;
import dz.sh.trc.hyflo.core.system.security.model.Permission;
import dz.sh.trc.hyflo.core.system.security.model.Resource;
import dz.sh.trc.hyflo.core.system.security.repository.PermissionRepository;
import dz.sh.trc.hyflo.core.system.security.service.PermissionService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class PermissionServiceImpl extends AbstractCrudService<CreatePermissionRequest, UpdatePermissionRequest, PermissionResponse, PermissionSummary, Permission> implements PermissionService {

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Permission> getEntityClass() {
        return Permission.class;
    }

    @Override
    protected void beforeCreate(CreatePermissionRequest request, Permission entity) {
        if (request.resourceId() != null) {
            entity.setResource(referenceResolver.resolve(request.resourceId(), Resource.class));
        }
        if (request.actionId() != null) {
            entity.setAction(referenceResolver.resolve(request.actionId(), Action.class));
        }
    }
}
