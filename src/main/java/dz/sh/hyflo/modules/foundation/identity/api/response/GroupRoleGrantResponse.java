/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupRoleGrantResponse
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

public record GroupRoleGrantResponse(long id, long groupId, long roleId, String roleCode, long companyId,
        String scopeType, Instant validFrom, Instant validTo, Instant revokedAt, String reason,
        long grantedBy, Long revokedBy) {}
