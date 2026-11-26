/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : ChangeOwnEmailRequest
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Record
 * @Layer       : Request
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.request
 *
 * @Description : Own contact email and current password confirmation.
 */

package dz.sh.hyflo.modules.foundation.identity.api.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeOwnEmailRequest(@NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(max = 72) String currentPassword) {
    @JsonAnySetter
    public void rejectUnknown(String name, Object value) {
        throw new IllegalArgumentException("Unexpected profile field");
    }

    @Override public String toString() { return "ChangeOwnEmailRequest[redacted]"; }
}
