/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupMembershipResponse
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : GroupMembershipResponse for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.time.Instant;

public record GroupMembershipResponse(long id, long groupId, long userId, Instant validFrom, Instant validTo,
        Instant revokedAt, long grantedBy, Long revokedBy, boolean active) {}
