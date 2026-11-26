/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupDefinitionEventService
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : GroupDefinitionEventService for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.util.Objects;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupDefinitionEvent;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupDefinitionEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupDefinitionEventService {
    private final GroupDefinitionEventRepository events;

    public GroupDefinitionEventService(GroupDefinitionEventRepository events) {
        this.events = events;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(GroupDefinitionEvent event) {
        events.save(Objects.requireNonNull(event));
    }
}
