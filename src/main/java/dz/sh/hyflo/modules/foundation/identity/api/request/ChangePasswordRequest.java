/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : ChangePasswordRequest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Request
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.request
 *
 * @Description : Carries current and replacement local passwords without diagnostic exposure.
 */

package dz.sh.hyflo.modules.foundation.identity.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(@NotBlank @Size(max = 72) String currentPassword,
        @NotBlank @Size(max = 72) String newPassword) {
    @Override public String toString() { return "ChangePasswordRequest[redacted]"; }
}
