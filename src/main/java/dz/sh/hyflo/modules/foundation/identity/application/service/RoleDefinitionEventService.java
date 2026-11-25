/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : RoleDefinitionEventService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : RoleDefinitionEventService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RoleDefinitionEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleDefinitionEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleDefinitionEventService {
    private final RoleDefinitionEventRepository events;

    public RoleDefinitionEventService(RoleDefinitionEventRepository events) {
        this.events = events;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(Role role, User actor, Instant now, String correlationId, String reason) {
        events.save(new RoleDefinitionEvent(role, actor, now, correlationId, reason));
    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void recordPermissionChange(Role role, Permission permission, User actor, Instant now,
            String correlationId, String reason, boolean associated) {
        events.save(RoleDefinitionEvent.permissionChanged(role, permission, actor, now, correlationId, reason, associated));
    }
}
