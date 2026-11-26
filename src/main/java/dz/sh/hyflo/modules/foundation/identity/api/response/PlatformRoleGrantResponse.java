/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleGrantResponse
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Company-local direct grant projection with actor attribution.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.time.Instant;

public record PlatformRoleGrantResponse(long id, long userId, long roleId, String roleCode,
        Instant validFrom, Instant validTo, Instant revokedAt, String reason, String correlationId,
        long grantedBy, String revocationReason, String revocationCorrelationId, Long revokedBy) {}
