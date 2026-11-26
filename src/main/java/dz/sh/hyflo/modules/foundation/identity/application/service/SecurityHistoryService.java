/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : SecurityHistoryService
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Reads scoped, immutable Identity evidence in a live authorized snapshot.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.AuthenticationEventRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserLifecycleEventRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserEmployeeAssociationEventRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleDefinitionEventRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupDefinitionEventRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.CapabilityReconciliationEventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityHistoryService {
    public static final String COMPANY_READ = "security-history.read";
    private static final PageRequest PAGE = PageRequest.of(0, 100);
    private static final Pattern PART = Pattern.compile("[a-z0-9_-]{1,64}");
    private static final Pattern PAIR = Pattern.compile("[a-z0-9_-]{1,64}\\.[a-z0-9_-]{1,64}");
    private final EffectiveAuthorizationService companyAuthorization;
    private final PlatformAuthorizationService platformAuthorization;
    private final AuthenticationEventRepository authenticationEvents;
    private final UserLifecycleEventRepository userLifecycleEvents;
    private final UserEmployeeAssociationEventRepository userEmployeeAssociationEvents;
    private final RoleDefinitionEventRepository roleDefinitionEvents;
    private final GroupDefinitionEventRepository groupDefinitionEvents;
    private final CapabilityReconciliationEventRepository capabilityReconciliationEvents;

    public SecurityHistoryService(EffectiveAuthorizationService companyAuthorization,
            PlatformAuthorizationService platformAuthorization,
            AuthenticationEventRepository authenticationEvents,
            UserLifecycleEventRepository userLifecycleEvents,
            UserEmployeeAssociationEventRepository userEmployeeAssociationEvents,
            RoleDefinitionEventRepository roleDefinitionEvents,
            GroupDefinitionEventRepository groupDefinitionEvents,
            CapabilityReconciliationEventRepository capabilityReconciliationEvents) {
        this.companyAuthorization = companyAuthorization;
        this.platformAuthorization = platformAuthorization;
        this.authenticationEvents = authenticationEvents;
        this.userLifecycleEvents = userLifecycleEvents;
        this.userEmployeeAssociationEvents = userEmployeeAssociationEvents;
        this.roleDefinitionEvents = roleDefinitionEvents;
        this.groupDefinitionEvents = groupDefinitionEvents;
        this.capabilityReconciliationEvents = capabilityReconciliationEvents;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<Authentication> authentication(CurrentIdentity principal, long companyId, long afterId) {
        companyAuthorization.requireCompanyPermission(principal, companyId, COMPANY_READ);
        requireCursor(afterId);
        return authenticationEvents.findCompanyPage(companyId, afterId, PAGE).stream()
                .map(e -> new Authentication(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getEventCode(), e.getUser().getId(), e.getIdentityProvider().getCode(), e.getLoginSession() == null ? null : e.getLoginSession().getId())).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<AccountLifecycle> accountLifecycle(CurrentIdentity principal, long companyId, long afterId) {
        companyAuthorization.requireCompanyPermission(principal, companyId, COMPANY_READ);
        requireCursor(afterId);
        return userLifecycleEvents.findCompanyPage(companyId, afterId, PAGE).stream()
                .map(e -> new AccountLifecycle(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getEventCode(), e.getOwningOrganizationUnitId(), e.getUser().getId(), e.getPerformedBy().getId(), e.getPreviousStatusCode(), e.getResultingStatusCode())).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<EmployeeAssociation> employeeAssociations(CurrentIdentity principal, long companyId, long afterId) {
        companyAuthorization.requireCompanyPermission(principal, companyId, COMPANY_READ);
        requireCursor(afterId);
        return userEmployeeAssociationEvents.findCompanyPage(companyId, afterId, PAGE).stream()
                .map(e -> new EmployeeAssociation(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getEventCode(), e.getOwningOrganizationUnitId(), e.getUser().getId(), e.getPerformedBy().getId(), e.getPreviousEmployeeId(), e.getResultingEmployeeId())).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<RoleDefinition> roleDefinitions(CurrentIdentity principal, long afterId) {
        platformAuthorization.requireRead(principal, PlatformAuthorizationService.SECURITY_HISTORY_READ);
        requireCursor(afterId);
        return roleDefinitionEvents.findHistoryPage(afterId, PAGE).stream()
                .map(e -> new RoleDefinition(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getEventCode(), e.getRole().getId(), e.getRoleCode(), e.getDisplayName(), e.isPlatformRole(), e.getPerformedBy().getId(), e.getPermission() == null ? null : e.getPermission().getId())).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<GroupDefinition> groupDefinitions(CurrentIdentity principal, long afterId) {
        platformAuthorization.requireRead(principal, PlatformAuthorizationService.SECURITY_HISTORY_READ);
        requireCursor(afterId);
        return groupDefinitionEvents.findHistoryPage(afterId, PAGE).stream()
                .map(e -> new GroupDefinition(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getEventCode(), e.getGroup().getId(), e.getGroupCode(), e.getDisplayName(), e.isEnabled(), e.getPerformedBy().getId())).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<CapabilityReconciliation> capabilityReconciliations(CurrentIdentity principal, long afterId) {
        platformAuthorization.requireRead(principal, PlatformAuthorizationService.SECURITY_HISTORY_READ);
        requireCursor(afterId);
        return capabilityReconciliationEvents.findHistoryPage(afterId, PAGE).stream()
                .map(e -> new CapabilityReconciliation(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getPerformedBy().getId(), e.getManifestId(), e.getManifestDigest(), codes(e.getInsertedActionCodes(), PART), codes(e.getInsertedResourceTypeCodes(), PART), codes(e.getInsertedPermissionCodes(), PAIR))).toList();
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<UnattributedAuthentication> unattributedAuthentication(CurrentIdentity principal, long afterId) {
        platformAuthorization.requireRead(principal, PlatformAuthorizationService.SECURITY_HISTORY_READ);
        requireCursor(afterId);
        return authenticationEvents.findUnattributedPage(afterId, PAGE).stream()
                .map(e -> new UnattributedAuthentication(e.getId(), e.getOccurredAt(), e.getCorrelationId(), e.getEventCode(), e.getIdentityProvider().getCode())).toList();
    }

    private static void requireCursor(long afterId) {
        if (afterId < 0) throw new IllegalArgumentException("Invalid history cursor");
    }

    private static List<String> codes(String stored, Pattern pattern) {
        if (stored == null || stored.length() > 8192) throw new IllegalStateException("Invalid history snapshot");
        if (stored.isEmpty()) return List.of();
        List<String> result = List.of(stored.split("\\n", -1));
        if (result.stream().anyMatch(code -> code.length() > 120 || !pattern.matcher(code).matches())
                || !result.equals(result.stream().distinct().sorted().toList()))
            throw new IllegalStateException("Invalid history snapshot");
        return result;
    }

    public record Authentication(long id, Instant occurredAt, String correlationId, String eventCode, long userId, String identityProviderCode, Long loginSessionId) {
        @Override public String toString() { return "Authentication[redacted]"; }
    }

    public record AccountLifecycle(long id, Instant occurredAt, String correlationId, String eventCode, long companyId, long userId, long performedByUserId, String previousStatusCode, String resultingStatusCode) {
        @Override public String toString() { return "AccountLifecycle[redacted]"; }
    }

    public record EmployeeAssociation(long id, Instant occurredAt, String correlationId, String eventCode, long companyId, long userId, long performedByUserId, Long previousEmployeeId, Long resultingEmployeeId) {
        @Override public String toString() { return "EmployeeAssociation[redacted]"; }
    }

    public record RoleDefinition(long id, Instant occurredAt, String correlationId, String eventCode, long roleId, String roleCode, String displayName, boolean platformRole, long performedByUserId, Long permissionId) {
        @Override public String toString() { return "RoleDefinition[redacted]"; }
    }

    public record GroupDefinition(long id, Instant occurredAt, String correlationId, String eventCode, long groupId, String groupCode, String displayName, boolean enabled, long performedByUserId) {
        @Override public String toString() { return "GroupDefinition[redacted]"; }
    }

    public record CapabilityReconciliation(long id, Instant occurredAt, String correlationId, long performedByUserId, String manifestId, String manifestDigest, List<String> insertedActionCodes, List<String> insertedResourceTypeCodes, List<String> insertedPermissionCodes) {
        public CapabilityReconciliation {
            insertedActionCodes = List.copyOf(insertedActionCodes);
            insertedResourceTypeCodes = List.copyOf(insertedResourceTypeCodes);
            insertedPermissionCodes = List.copyOf(insertedPermissionCodes);
        }
        @Override public String toString() { return "CapabilityReconciliation[redacted]"; }
    }

    public record UnattributedAuthentication(long id, Instant occurredAt, String correlationId, String eventCode, String identityProviderCode) {
        @Override public String toString() { return "UnattributedAuthentication[redacted]"; }
    }
}
