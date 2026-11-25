/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleAdministrationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : PlatformRoleAdministrationService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformRoleAdministrationService {
    private final PlatformAuthorizationService authorization;
    private final RoleRepository roles;
    private final RoleDefinitionEventService events;

    public PlatformRoleAdministrationService(PlatformAuthorizationService authorization, RoleRepository roles,
            RoleDefinitionEventService events) {
        this.authorization = authorization;
        this.roles = roles;
        this.events = events;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<RoleEntry> list(CurrentIdentity principal, long afterId) {
        if (afterId < 0) throw new IllegalArgumentException("Invalid cursor");
        authorization.requireRead(principal, PlatformAuthorizationService.ROLE_READ);
        return roles.findByIdGreaterThanOrderByIdAsc(afterId, PageRequest.of(0, 100)).stream()
                .map(PlatformRoleAdministrationService::entry).toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RoleEntry create(CurrentIdentity principal, String code, String name, Boolean platformRole, String reason) {
        var actor = authorization.requireWrite(principal, PlatformAuthorizationService.ROLE_CREATE);
        if (platformRole == null || reason == null || reason.isBlank() || reason.length() > 255)
            throw new IllegalArgumentException("Invalid role input");
        var candidate = new Role(code, name, platformRole);
        var role = roles.save(candidate);
        // Recheck temporal authority after the potentially blocking insert; all authority locks are retained.
        actor = authorization.requireWrite(principal, PlatformAuthorizationService.ROLE_CREATE);
        events.record(role, actor, Instant.now(), UUID.randomUUID().toString(), reason);
        return entry(role);
    }

    private static RoleEntry entry(Role role) {
        return new RoleEntry(role.getId(), role.getCode(), role.getDisplayName(), role.isEnabled(), role.isPlatformRole());
    }

    public record RoleEntry(long id, String code, String displayName, boolean enabled, boolean platformRole) {}
}
