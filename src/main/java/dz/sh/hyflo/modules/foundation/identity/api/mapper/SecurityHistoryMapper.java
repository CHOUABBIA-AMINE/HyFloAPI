/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : SecurityHistoryMapper
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Mapper
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.api.mapper
 *
 * @Description : Maps application security-history projections to REST response DTOs.
 */

package dz.sh.hyflo.modules.foundation.identity.api.mapper;

import dz.sh.hyflo.modules.foundation.identity.api.response.SecurityHistoryResponse;
import dz.sh.hyflo.modules.foundation.identity.application.service.SecurityHistoryService;
import org.springframework.stereotype.Component;

@Component
public class SecurityHistoryMapper {
    public SecurityHistoryResponse.Authentication authentication(SecurityHistoryService.Authentication result) {
        return new SecurityHistoryResponse.Authentication(result.id(), result.occurredAt(), result.correlationId(), result.eventCode(), result.userId(), result.identityProviderCode(), result.loginSessionId());
    }

    public SecurityHistoryResponse.AccountLifecycle accountLifecycle(SecurityHistoryService.AccountLifecycle result) {
        return new SecurityHistoryResponse.AccountLifecycle(result.id(), result.occurredAt(), result.correlationId(), result.eventCode(), result.companyId(), result.userId(), result.performedByUserId(), result.previousStatusCode(), result.resultingStatusCode());
    }

    public SecurityHistoryResponse.EmployeeAssociation employeeAssociations(SecurityHistoryService.EmployeeAssociation result) {
        return new SecurityHistoryResponse.EmployeeAssociation(result.id(), result.occurredAt(), result.correlationId(), result.eventCode(), result.companyId(), result.userId(), result.performedByUserId(), result.previousEmployeeId(), result.resultingEmployeeId());
    }

    public SecurityHistoryResponse.RoleDefinition roleDefinitions(SecurityHistoryService.RoleDefinition result) {
        return new SecurityHistoryResponse.RoleDefinition(result.id(), result.occurredAt(), result.correlationId(), result.eventCode(), result.roleId(), result.roleCode(), result.displayName(), result.platformRole(), result.performedByUserId(), result.permissionId());
    }

    public SecurityHistoryResponse.GroupDefinition groupDefinitions(SecurityHistoryService.GroupDefinition result) {
        return new SecurityHistoryResponse.GroupDefinition(result.id(), result.occurredAt(), result.correlationId(), result.eventCode(), result.groupId(), result.groupCode(), result.displayName(), result.enabled(), result.performedByUserId());
    }

    public SecurityHistoryResponse.CapabilityReconciliation capabilityReconciliations(SecurityHistoryService.CapabilityReconciliation result) {
        return new SecurityHistoryResponse.CapabilityReconciliation(result.id(), result.occurredAt(), result.correlationId(), result.performedByUserId(), result.manifestId(), result.manifestDigest(), result.insertedActionCodes(), result.insertedResourceTypeCodes(), result.insertedPermissionCodes());
    }

    public SecurityHistoryResponse.UnattributedAuthentication unattributedAuthentication(SecurityHistoryService.UnattributedAuthentication result) {
        return new SecurityHistoryResponse.UnattributedAuthentication(result.id(), result.occurredAt(), result.correlationId(), result.eventCode(), result.identityProviderCode());
    }
}
