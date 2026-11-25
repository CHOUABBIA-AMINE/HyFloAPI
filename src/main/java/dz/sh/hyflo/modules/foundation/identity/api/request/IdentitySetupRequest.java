/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentitySetupRequest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Record
 * @Layer       : Request
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.request
 *
 * @Description : Carries bounded first-company and administrator setup inputs.
 */

package dz.sh.hyflo.modules.foundation.identity.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdentitySetupRequest(
        @NotBlank @Size(max = 64) String companyCode,
        @Size(max = 120) String companyNameAr,
        @Size(max = 120) String companyNameFr,
        @Size(max = 120) String companyNameEn,
        @NotBlank @Size(max = 100) String username,
        @NotBlank @Email @Size(max = 100) String email,
        @Schema(accessMode = Schema.AccessMode.WRITE_ONLY) @NotBlank @Size(max = 72) String password,
        @Size(max = 64) String initialPlatformRoleCode,
        @Size(max = 120) String initialPlatformRoleName) {

    @Override
    public String toString() {
        return "IdentitySetupRequest[redacted]";
    }
}
