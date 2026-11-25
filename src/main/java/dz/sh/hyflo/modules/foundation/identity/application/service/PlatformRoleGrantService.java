/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleGrantService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : PlatformRoleGrantService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.PlatformRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PlatformRoleGrantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlatformRoleGrantService {
    private final PlatformRoleGrantRepository grants;

    public PlatformRoleGrantService(PlatformRoleGrantRepository grants) {
        this.grants = grants;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void provision(User user, Role role, User actor, Instant now, String reason, String correlationId) {
        if (!"hyflo-bootstrap".equals(actor.getUsername()) || grants.existsByUserId(user.getId()))
            throw new IllegalStateException("Initial platform grant unavailable");
        grants.save(new PlatformRoleGrant(user, role, now, null, reason, correlationId, actor));
    }
}
