/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityReconciliationEventService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : CapabilityReconciliationEventService for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.List;
import dz.sh.hyflo.modules.foundation.identity.domain.model.CapabilityReconciliationEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.CapabilityReconciliationEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CapabilityReconciliationEventService {
    private final CapabilityReconciliationEventRepository events;

    public CapabilityReconciliationEventService(CapabilityReconciliationEventRepository events) {
        this.events = events;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(User actor, Instant now, String correlationId, String reason, String manifestId,
            String digest, List<String> actions, List<String> resources, List<String> permissions) {
        events.save(new CapabilityReconciliationEvent(actor, now, correlationId, reason, manifestId, digest,
                actions, resources, permissions));
    }
}
