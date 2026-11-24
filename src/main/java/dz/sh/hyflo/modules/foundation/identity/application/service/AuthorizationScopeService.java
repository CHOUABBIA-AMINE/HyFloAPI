/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthorizationScopeService
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Checks an identity scope boundary through the Organization owner.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.util.Objects;

import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationScopeService {

    private final OrganizationUnitScopeService organizationUnitScopes;

    public AuthorizationScopeService(OrganizationUnitScopeService organizationUnitScopes) {
        this.organizationUnitScopes = organizationUnitScopes;
    }

    /**
     * Validates a company boundary for an already-authorized internal use case.
     * Does not grant permission or establish the ownership of a specific resource.
     */
    @Transactional(readOnly = true)
    public AuthorizationScope validateCompanyBoundary(AuthorizationScope scope) {
        Objects.requireNonNull(scope, "Scope is required");
        organizationUnitScopes.requireCompanyUnit(scope.getOrganizationUnitId());
        return scope;
    }
}
