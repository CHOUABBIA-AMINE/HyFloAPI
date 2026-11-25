/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CompanyPermissionsResponse
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Response
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Exposes only the caller's effective direct permission codes for one eligible company.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.util.List;

public record CompanyPermissionsResponse(long companyId, List<String> permissionCodes) {
    public CompanyPermissionsResponse {
        permissionCodes = List.copyOf(permissionCodes);
    }
}
