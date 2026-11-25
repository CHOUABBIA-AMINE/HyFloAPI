/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserLifecycleEventService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Appends account lifecycle evidence within the owning transaction.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserLifecycleEvent;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserLifecycleEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLifecycleEventService {
    private final UserLifecycleEventRepository repository;

    public UserLifecycleEventService(UserLifecycleEventRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(String eventCode, Instant now, String correlation, String reason,
            String previous, User target, User actor) {
        repository.save(new UserLifecycleEvent(eventCode, now, correlation, reason, previous, target, actor));
    }
}
