package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateRoleRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateRoleRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.RoleResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.RoleSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.RoleMapper;
import dz.sh.trc.hyflo.core.system.security.model.Permission;
import dz.sh.trc.hyflo.core.system.security.model.Role;
import dz.sh.trc.hyflo.core.system.security.repository.RoleRepository;
import dz.sh.trc.hyflo.core.system.security.service.RoleService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class RoleServiceImpl extends AbstractCrudService<CreateRoleRequest, UpdateRoleRequest, RoleResponse, RoleSummary, Role, Long> implements RoleService {

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    @Transactional
    public void assignPermission(Long roleId, Long permissionId) {
        Role role = findEntityById(roleId);
        Permission permission = referenceResolver.resolve(permissionId, Permission.class);
        role.getPermissions().add(permission);
        repository.save(role);
    }

    @Override
    @Transactional
    public void removePermission(Long roleId, Long permissionId) {
        Role role = findEntityById(roleId);
        Permission permission = referenceResolver.resolve(permissionId, Permission.class);
        role.getPermissions().remove(permission);
        repository.save(role);
    }
}
