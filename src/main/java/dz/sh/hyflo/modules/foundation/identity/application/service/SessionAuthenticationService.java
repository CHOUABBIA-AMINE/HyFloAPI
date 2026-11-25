/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : SessionAuthenticationService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Resolves live account and session eligibility for verified access tokens.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.Optional;

import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.LoginSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionAuthenticationService {

    private final LoginSessionRepository sessions;

    public SessionAuthenticationService(LoginSessionRepository sessions) {
        this.sessions = sessions;
    }

    @Transactional(readOnly = true)
    public Optional<CurrentIdentity> resolve(long userId, long sessionId, Instant now) {
        if (userId <= 0 || sessionId <= 0 || now == null) {
            return Optional.empty();
        }
        return sessions.findById(sessionId)
                .filter(session -> session.isActiveAt(now))
                .filter(session -> userId == session.getUser().getId())
                .map(session -> session.getUser())
                .filter(SessionAuthenticationService::eligible)
                .map(user -> new CurrentIdentity(user.getId(), user.getUsername(), sessionId));
    }

    static boolean eligible(User user) {
        return user.getUserStatus().hasCode(UserStatus.ACTIVE_CODE)
                && IdentityProvider.LOCAL_CODE.equals(user.getIdentityProvider().getCode())
                && user.getIdentityProvider().isEnabled()
                && (user.getUserType().hasCode(UserType.HUMAN_CODE) || user.getUserType().hasCode(UserType.SERVICE_CODE))
                && !"hyflo-bootstrap".equals(user.getUsername());
    }

    public record CurrentIdentity(long userId, String username, long sessionId) {}
}
