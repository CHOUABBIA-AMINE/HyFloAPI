/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityAuthorizationTest
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Test
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo
 *
 * @Description : Verifies exact permission matching, temporal and company gates and protected read ordering.
 */

package dz.sh.hyflo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import dz.sh.hyflo.modules.foundation.identity.application.service.EffectiveAuthorizationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityCatalogService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService;
import dz.sh.hyflo.modules.foundation.identity.application.service.SessionAuthenticationService.CurrentIdentity;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.ActionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.ResourceTypeRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RolePermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserRoleGrantRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserStatusRepository;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class IdentityAuthorizationTest {

    private final CurrentIdentity principal = new CurrentIdentity(10L, "synthetic", 20L);
    private final SessionAuthenticationService sessions = mock(SessionAuthenticationService.class);
    private final UserRoleGrantRepository grants = mock(UserRoleGrantRepository.class);
    private final RolePermissionRepository links = mock(RolePermissionRepository.class);
    private final OrganizationUnitScopeService companies = mock(OrganizationUnitScopeService.class);
    private final UserStatusRepository statuses = mock(UserStatusRepository.class);
    private final RoleRepository roles = mock(RoleRepository.class);
    private final PermissionRepository permissions = mock(PermissionRepository.class);
    private final RolePermissionRepository catalogLinks = mock(RolePermissionRepository.class);
    private final ActionRepository actions = mock(ActionRepository.class);
    private final ResourceTypeRepository resourceTypes = mock(ResourceTypeRepository.class);
    private final EffectiveAuthorizationService authorization =
            new EffectiveAuthorizationService(sessions, grants, links, companies);
    private final IdentityCatalogService catalogs =
            new IdentityCatalogService(authorization, statuses, roles, permissions, catalogLinks, actions, resourceTypes);
    private final AtomicReference<Instant> decisionTime = new AtomicReference<>();
    private final User user = mock(User.class);
    private final Role role = mock(Role.class);

    @BeforeEach
    void authenticateSyntheticPrincipal() {
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(principal, null, List.of()));
        when(sessions.resolve(eq(10L), eq(20L), any(Instant.class))).thenAnswer(invocation -> {
            decisionTime.set(invocation.getArgument(2));
            return Optional.of(principal);
        });
        when(user.getId()).thenReturn(10L);
        when(role.getId()).thenReturn(30L);
        when(role.isEnabled()).thenReturn(true);
    }

    @AfterEach
    void clearPrincipal() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void resolvesExactDistinctPermissionCodesAndRejectsMalformedPairs() {
        when(grants.findCompanyCandidates(10L, 1L)).thenAnswer(invocation ->
                List.of(grant(AuthorizationScope.global(1L), decisionTime.get(), null),
                        grant(AuthorizationScope.global(1L), decisionTime.get(), null)));
        when(links.findForRoles(Set.of(30L))).thenReturn(List.of(
                link("user.read", "user", "read"), link("identity-catalog.read", "identity-catalog", "read"),
                link("user.read", "user", "read"), link("IDENTITY-CATALOG.read", "identity-catalog", "read"),
                link("other.read", "user", "read"), link("user.*", "user", "*")));
        when(companies.isEligibleCompanyUnit(eq(1L), any(LocalDate.class))).thenReturn(true);
        assertThat(authorization.ownPermissions(principal, 1L).permissionCodes())
                .containsExactly("identity-catalog.read", "user.read");
        verify(companies).isEligibleCompanyUnit(1L, LocalDate.ofInstant(decisionTime.get(), ZoneOffset.UTC));
        clearInvocations(companies);
        assertThatThrownBy(() -> authorization.requireCompanyPermission(principal, 1L, "USER.read"))
                .isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(companies);
        authorization.requireCompanyPermission(principal, 1L, "user.read");
        verify(companies).isEligibleCompanyUnit(eq(1L), any(LocalDate.class));
    }

    @Test
    void rejectsUnsupportedWrongCompanyFutureExpiredAndRevokedGrantsBeforeOwnerLookup() {
        when(grants.findCompanyCandidates(10L, 1L)).thenAnswer(invocation -> {
            Instant now = decisionTime.get();
            var revoked = grant(AuthorizationScope.global(1L), now.minusSeconds(10), null);
            revoked.revoke(now, user);
            return List.of(grant(AuthorizationScope.global(2L), now, null),
                    grant(AuthorizationScope.region(1L, 2L), now, null),
                    grant(AuthorizationScope.pipeline(1L, 2L), now, null),
                    grant(AuthorizationScope.infrastructure(1L, 2L), now, null),
                    grant(AuthorizationScope.global(1L), now.plusSeconds(1), null),
                    grant(AuthorizationScope.global(1L), now.minusSeconds(10), now),
                    revoked);
        });
        assertThatThrownBy(() -> authorization.ownPermissions(principal, 1L)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(links, companies, statuses);
        when(grants.findCompanyCandidates(10L, 1L)).thenAnswer(invocation ->
                List.of(grant(AuthorizationScope.global(1L), decisionTime.get(), null)));
        when(role.isEnabled()).thenReturn(false);
        assertThatThrownBy(() -> catalogs.userStatuses(principal, 1L)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(links, companies, statuses);
    }

    @Test
    void gatesProtectedCatalogBeforeAnyDataReadAndFailsClosedOnOwnerOrRepositoryErrors() {
        when(grants.findCompanyCandidates(10L, 1L)).thenAnswer(invocation ->
                List.of(grant(AuthorizationScope.global(1L), decisionTime.get(), null)));
        when(links.findForRoles(Set.of(30L))).thenReturn(List.of(link("user.read", "user", "read")));
        assertThatThrownBy(() -> catalogs.userStatuses(principal, 1L)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(companies, statuses);
        assertThatThrownBy(() -> catalogs.roles(principal, 1L, 0L)).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> catalogs.permissions(principal, 1L, 0L)).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> catalogs.rolePermissions(principal, 1L, 30L, 0L))
                .isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> catalogs.actions(principal, 1L, 0L)).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> catalogs.resourceTypes(principal, 1L, 0L)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(companies, roles, permissions, catalogLinks, actions, resourceTypes);
        when(links.findForRoles(Set.of(30L))).thenReturn(List.of(link("identity-catalog.read", "identity-catalog", "read")));
        when(companies.isEligibleCompanyUnit(eq(1L), any(LocalDate.class))).thenReturn(false);
        assertThatThrownBy(() -> catalogs.userStatuses(principal, 1L)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(statuses);
        when(companies.isEligibleCompanyUnit(eq(1L), any(LocalDate.class)))
                .thenThrow(new DataAccessResourceFailureException("synthetic owner failure"));
        assertThatThrownBy(() -> catalogs.userStatuses(principal, 1L))
                .isInstanceOf(DataAccessResourceFailureException.class);
        verifyNoInteractions(statuses);
        clearInvocations(companies);
        when(grants.findCompanyCandidates(10L, 1L)).thenThrow(new DataAccessResourceFailureException("synthetic grant failure"));
        assertThatThrownBy(() -> catalogs.userStatuses(principal, 1L))
                .isInstanceOf(DataAccessResourceFailureException.class);
        verifyNoInteractions(companies, statuses);
    }

    @Test
    void rejectsUntrustedOrNoLongerLivePrincipalBeforeGrantRead() {
        assertThatThrownBy(() -> authorization.ownPermissions(new CurrentIdentity(99L, "other", 20L), 1L))
                .isInstanceOf(InsufficientAuthenticationException.class);
        verifyNoInteractions(sessions, grants, links, companies, statuses);
        when(sessions.resolve(eq(10L), eq(20L), any(Instant.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> catalogs.userStatuses(principal, 1L)).isInstanceOf(InsufficientAuthenticationException.class);
        verifyNoInteractions(grants, links, companies, statuses);
    }

    @Test
    void excludesInjectedPlatformPermissionsFromCompanyAuthority() {
        when(grants.findCompanyCandidates(10L, 1L)).thenAnswer(invocation ->
                List.of(grant(AuthorizationScope.global(1L), decisionTime.get(), null)));
        RolePermission injected = mock(RolePermission.class);
        when(injected.getRole()).thenReturn(role);
        when(injected.getPermission()).thenReturn(new Permission("platform-role.read", "Read",
                new Action("read", "Read"), new ResourceType("platform-role", "Platform role")));
        when(links.findForRoles(Set.of(30L))).thenReturn(List.of(injected, link("user.read", "user", "read")));
        when(companies.isEligibleCompanyUnit(eq(1L), any(LocalDate.class))).thenReturn(true);
        assertThat(authorization.ownPermissions(principal, 1L).permissionCodes()).containsExactly("user.read");
        assertThatThrownBy(() -> authorization.requireCompanyPermission(principal, 1L, "platform-role.read"))
                .isInstanceOf(AccessDeniedException.class);
    }

    private UserRoleGrant grant(AuthorizationScope scope, Instant from, Instant to) {
        return new UserRoleGrant(user, role, scope, "synthetic", from, to, user);
    }

    private RolePermission link(String code, String resource, String action) {
        return new RolePermission(role, new Permission(code, code, new Action(action, action),
                new ResourceType(resource, resource)));
    }
}
