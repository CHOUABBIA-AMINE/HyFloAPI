/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Declares the module-owned application service for Group.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Group;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupDefinitionEvent;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupRepository;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupService {
    private final GroupRepository groups;
    private final PlatformAuthorizationService authorization;
    private final GroupDefinitionEventService events;

    public GroupService(GroupRepository groups, PlatformAuthorizationService authorization, GroupDefinitionEventService events) {
        this.groups = groups;
        this.authorization = authorization;
        this.events = events;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Entry> list(CurrentIdentity principal, long afterId) {
        authorization.requireRead(principal, PlatformAuthorizationService.GROUP_READ);
        if (afterId < 0) throw new IllegalArgumentException("Invalid cursor");
        return groups.findByIdGreaterThanOrderByIdAsc(afterId, PageRequest.of(0, 100)).stream().map(GroupService::entry).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Entry read(CurrentIdentity principal, long id) {
        authorization.requireRead(principal, PlatformAuthorizationService.GROUP_READ);
        return entry(groups.findById(id).orElseThrow(GroupService::denied));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Entry create(CurrentIdentity principal, String code, String label, String reason) {
        String permission = PlatformAuthorizationService.GROUP_CREATE;
        authorization.requireCatalogWrite(principal, permission);
        Group group = groups.save(new Group(code, label));
        var actor = authorization.requireCatalogWrite(principal, permission);
        events.record(GroupDefinitionEvent.created(group, actor, Instant.now(), UUID.randomUUID().toString(), reason));
        return entry(group);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Entry rename(CurrentIdentity principal, long id, String label, String reason) {
        String permission = PlatformAuthorizationService.GROUP_RENAME;
        authorization.requireCatalogWrite(principal, permission);
        Group group = groups.findForUpdate(id).orElseThrow(GroupService::denied);
        var actor = authorization.requireCatalogWrite(principal, permission);
        if (!group.isEnabled() || group.getDisplayName().equals(label)) throw new GroupRejected();
        Instant now = Instant.now();
        String correlation = UUID.randomUUID().toString();
        var before = GroupDefinitionEvent.renamedFrom(group, actor, now, correlation, reason);
        group.rename(label);
        events.record(before);
        events.record(GroupDefinitionEvent.renamed(group, actor, now, correlation, reason));
        return entry(group);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Entry retire(CurrentIdentity principal, long id, String reason) {
        String permission = PlatformAuthorizationService.GROUP_RETIRE;
        authorization.requireCatalogWrite(principal, permission);
        Group group = groups.findForUpdate(id).orElseThrow(GroupService::denied);
        var actor = authorization.requireCatalogWrite(principal, permission);
        if (!group.isEnabled()) throw new GroupRejected();
        group.retire();
        events.record(GroupDefinitionEvent.retired(group, actor, Instant.now(), UUID.randomUUID().toString(), reason));
        return entry(group);
    }

    private static Entry entry(Group group) {
        return new Entry(group.getId(), group.getCode(), group.getDisplayName(), group.isEnabled());
    }
    static AccessDeniedException denied() { return new AccessDeniedException("Access denied"); }
    public static final class GroupRejected extends RuntimeException {
        public GroupRejected() { super("Group operation rejected"); }
    }
    public record Entry(long id, String code, String displayName, boolean enabled) {}
}
