/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityReconciliationResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : CapabilityReconciliationResponse for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.util.List;

public record CapabilityReconciliationResponse(String manifestId, String manifestDigest,
        List<String> insertedActionCodes, List<String> insertedResourceTypeCodes, List<String> insertedPermissionCodes) {}
