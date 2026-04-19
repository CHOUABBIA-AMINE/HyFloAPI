package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateGroupRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateGroupRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.GroupResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.GroupSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.GroupMapper;
import dz.sh.trc.hyflo.core.system.security.model.Group;
import dz.sh.trc.hyflo.core.system.security.model.Role;
import dz.sh.trc.hyflo.core.system.security.repository.GroupRepository;
import dz.sh.trc.hyflo.core.system.security.service.GroupService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class GroupServiceImpl extends AbstractCrudService<CreateGroupRequest, UpdateGroupRequest, GroupResponse, GroupSummary, Group> implements GroupService {

    public GroupServiceImpl(GroupRepository repository, GroupMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Group> getEntityClass() {
        return Group.class;
    }

    @Override
    @Transactional
    public void assignRole(Long groupId, Long roleId) {
        Group group = findEntityById(groupId);
        Role role = referenceResolver.resolve(roleId, Role.class);
        group.getRoles().add(role);
        repository.save(group);
    }

    @Override
    @Transactional
    public void removeRole(Long groupId, Long roleId) {
        Group group = findEntityById(groupId);
        Role role = referenceResolver.resolve(roleId, Role.class);
        group.getRoles().remove(role);
        repository.save(group);
    }
}
