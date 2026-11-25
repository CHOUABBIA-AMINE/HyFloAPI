/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserEmployeeAssociationEventService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Records association evidence in the caller transaction.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserEmployeeAssociationEvent;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserEmployeeAssociationEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserEmployeeAssociationEventService {
    private final UserEmployeeAssociationEventRepository repository;

    public UserEmployeeAssociationEventService(UserEmployeeAssociationEventRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void record(String code, Instant now, String correlation, String reason, Long previous, User target, User actor) {
        repository.save(new UserEmployeeAssociationEvent(code, now, correlation, reason, previous, target, actor));
    }
}
