/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CreateGroupRequest
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Record
 * @Layer       : Request
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.request
 *
 * @Description : CreateGroupRequest for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateGroupRequest(@NotBlank @Pattern(regexp = "[A-Za-z0-9_-]{1,64}") String code,
        @NotBlank @Size(max = 120) String displayName, @NotBlank @Size(max = 255) String reason) {}
