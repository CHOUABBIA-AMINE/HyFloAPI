/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthorizationScope
 * @CreatedOn   : 11-24-2026
 * @UpdatedOn   : 11-24-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.domain.model
 *
 * @Description : Defines the company and resource boundary of a role grant.
 *
 */

package dz.sh.hyflo.modules.foundation.identity.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationScope {

    public static final String GLOBAL = "GLOBAL";
    public static final String REGION = "REGION";
    public static final String PIPELINE = "PIPELINE";
    public static final String INFRASTRUCTURE = "INFRASTRUCTURE";

    @Column(name = "F_05", length = 16, nullable = false, updatable = false)
    private String scopeType;

    @Column(name = "F_06", nullable = false, updatable = false)
    private long companyId;

    @Column(name = "F_07", updatable = false)
    private Long scopeReferenceId;

    private AuthorizationScope(String scopeType, long companyId, Long scopeReferenceId) {
        if (companyId <= 0 || (scopeReferenceId != null && scopeReferenceId <= 0)) {
            throw new IllegalArgumentException("Scope references must be positive");
        }
        this.scopeType = scopeType;
        this.companyId = companyId;
        this.scopeReferenceId = scopeReferenceId;
    }

    public static AuthorizationScope global(long companyId) {
        return new AuthorizationScope(GLOBAL, companyId, null);
    }

    public static AuthorizationScope region(long companyId, long regionId) {
        return new AuthorizationScope(REGION, companyId, regionId);
    }

    public static AuthorizationScope pipeline(long companyId, long pipelineId) {
        return new AuthorizationScope(PIPELINE, companyId, pipelineId);
    }

    public static AuthorizationScope infrastructure(long companyId, long infrastructureId) {
        return new AuthorizationScope(INFRASTRUCTURE, companyId, infrastructureId);
    }
}
