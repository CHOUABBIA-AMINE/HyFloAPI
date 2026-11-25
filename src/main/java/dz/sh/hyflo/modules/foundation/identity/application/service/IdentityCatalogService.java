/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityCatalogService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Reads the initialized account status catalog only after company authorization.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.util.List;

import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdentityCatalogService {

    private final EffectiveAuthorizationService authorization;
    private final UserStatusRepository statuses;

    public IdentityCatalogService(EffectiveAuthorizationService authorization, UserStatusRepository statuses) {
        this.authorization = authorization;
        this.statuses = statuses;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<StatusEntry> userStatuses(CurrentIdentity principal, long companyId) {
        authorization.requireCompanyPermission(principal, companyId, "identity-catalog.read");
        return statuses.findAllByOrderByCodeAsc().stream()
                .map(status -> new StatusEntry(status.getId(), status.getCode(), status.getDisplayName()))
                .toList();
    }

    public record StatusEntry(long id, String code, String displayName) {}
}
