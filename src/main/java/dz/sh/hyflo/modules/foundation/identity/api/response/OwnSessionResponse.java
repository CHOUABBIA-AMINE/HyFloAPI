/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OwnSessionResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Returns only safe metadata for one own active session.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.time.Instant;

public record OwnSessionResponse(long sessionId, Instant startedAt, Instant expiresAt, boolean current) {}
