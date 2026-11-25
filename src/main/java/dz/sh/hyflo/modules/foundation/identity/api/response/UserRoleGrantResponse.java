/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserRoleGrantResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
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

public record UserRoleGrantResponse(long id, long userId, long roleId, String roleCode, long companyId,
        String scopeType, Instant validFrom, Instant validTo, Instant revokedAt, String reason,
        long grantedBy, Long revokedBy) {}
