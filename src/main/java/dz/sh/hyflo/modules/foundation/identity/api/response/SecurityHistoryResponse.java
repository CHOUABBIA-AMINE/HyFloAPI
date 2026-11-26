/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : SecurityHistoryResponse
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Response
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.response
 *
 * @Description : Explicit read-only historical response projections without sensitive reasons.
 */

package dz.sh.hyflo.modules.foundation.identity.api.response;

import java.time.Instant;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public final class SecurityHistoryResponse {
    private SecurityHistoryResponse() {}

    @Schema(name = "SecurityHistoryAuthentication")
    public record Authentication(long id, Instant occurredAt, String correlationId, String eventCode, long userId, String identityProviderCode, Long loginSessionId) {
        @Override public String toString() { return "Authentication[redacted]"; }
    }

    @Schema(name = "SecurityHistoryAccountLifecycle")
    public record AccountLifecycle(long id, Instant occurredAt, String correlationId, String eventCode, long companyId, long userId, long performedByUserId, String previousStatusCode, String resultingStatusCode) {
        @Override public String toString() { return "AccountLifecycle[redacted]"; }
    }

    @Schema(name = "SecurityHistoryEmployeeAssociation")
    public record EmployeeAssociation(long id, Instant occurredAt, String correlationId, String eventCode, long companyId, long userId, long performedByUserId, Long previousEmployeeId, Long resultingEmployeeId) {
        @Override public String toString() { return "EmployeeAssociation[redacted]"; }
    }

    @Schema(name = "SecurityHistoryRoleDefinition")
    public record RoleDefinition(long id, Instant occurredAt, String correlationId, String eventCode, long roleId, String roleCode, String displayName, boolean platformRole, long performedByUserId, Long permissionId) {
        @Override public String toString() { return "RoleDefinition[redacted]"; }
    }

    @Schema(name = "SecurityHistoryGroupDefinition")
    public record GroupDefinition(long id, Instant occurredAt, String correlationId, String eventCode, long groupId, String groupCode, String displayName, boolean enabled, long performedByUserId) {
        @Override public String toString() { return "GroupDefinition[redacted]"; }
    }

    @Schema(name = "SecurityHistoryCapabilityReconciliation")
    public record CapabilityReconciliation(long id, Instant occurredAt, String correlationId, long performedByUserId, String manifestId, String manifestDigest, List<String> insertedActionCodes, List<String> insertedResourceTypeCodes, List<String> insertedPermissionCodes) {
        public CapabilityReconciliation {
            insertedActionCodes = List.copyOf(insertedActionCodes);
            insertedResourceTypeCodes = List.copyOf(insertedResourceTypeCodes);
            insertedPermissionCodes = List.copyOf(insertedPermissionCodes);
        }
        @Override public String toString() { return "CapabilityReconciliation[redacted]"; }
    }

    @Schema(name = "SecurityHistoryUnattributedAuthentication")
    public record UnattributedAuthentication(long id, Instant occurredAt, String correlationId, String eventCode, String identityProviderCode) {
        @Override public String toString() { return "UnattributedAuthentication[redacted]"; }
    }
}
