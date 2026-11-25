/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CreateUserRequest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Request
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.request
 *
 * @Description : Explicit company-owned pending account registration input.
 */

package dz.sh.hyflo.modules.foundation.identity.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max = 64) String username,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(max = 64) String userTypeCode,
        @NotBlank @Size(max = 255) String reason) {
    @Override public String toString() { return "CreateUserRequest[redacted]"; }
}
