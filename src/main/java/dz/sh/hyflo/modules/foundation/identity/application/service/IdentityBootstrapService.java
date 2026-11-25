/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityBootstrapService
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Service
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.application.service
 *
 * @Description : Initializes the first company administrator atomically in a fresh Identity schema.
 */

package dz.sh.hyflo.modules.foundation.identity.application.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.ActionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.GroupRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.IdentityProviderRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.LocalCredentialRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.PermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.ResourceTypeRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RoleRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.RolePermissionRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserRoleGrantRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserStatusRepository;
import dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository.UserTypeRepository;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import jakarta.validation.Validator;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdentityBootstrapService {

    private static final String BOOTSTRAP_USERNAME = "hyflo-bootstrap";
    private static final String BOOTSTRAP_EMAIL = "hyflo-bootstrap@hyflo.invalid";
    private static final String ADMIN_ROLE = "IDENTITY_ADMIN";
    private static final Pattern OPERATOR_REFERENCE = Pattern.compile("[A-Za-z0-9][A-Za-z0-9_.@:/-]{0,119}");
    private static final List<String> INITIAL_PERMISSIONS = List.of(
            "user.read",
            "user-employee.read",
            "user-employee.associate",
            "user.create",
            "user.activate",
            "user.suspend",
            "user.reactivate",
            "user.disable",
            "credential.initialize",
            "credential.reset",
            "login-session.revoke",
            "role.read",
            "permission.read",
            "user-role-grant.read",
            "user-role-grant.assign",
            "user-role-grant.revoke",
            "identity-catalog.read");

    private final IdentityProviderRepository providers;
    private final UserStatusRepository statuses;
    private final UserTypeRepository types;
    private final ActionRepository actions;
    private final ResourceTypeRepository resourceTypes;
    private final RoleRepository roles;
    private final GroupRepository groups;
    private final UserRepository users;
    private final LocalCredentialRepository credentials;
    private final PermissionRepository permissions;
    private final RolePermissionRepository rolePermissions;
    private final UserRoleGrantRepository grants;
    private final OrganizationUnitScopeService organizationScopes;
    private final IdentityPasswordEncoder passwordEncoder;
    private final Validator validator;

    public IdentityBootstrapService(
            IdentityProviderRepository providers,
            UserStatusRepository statuses,
            UserTypeRepository types,
            ActionRepository actions,
            ResourceTypeRepository resourceTypes,
            RoleRepository roles,
            GroupRepository groups,
            UserRepository users,
            LocalCredentialRepository credentials,
            PermissionRepository permissions,
            RolePermissionRepository rolePermissions,
            UserRoleGrantRepository grants,
            OrganizationUnitScopeService organizationScopes,
            IdentityPasswordEncoder passwordEncoder,
            Validator validator) {
        this.providers = providers;
        this.statuses = statuses;
        this.types = types;
        this.actions = actions;
        this.resourceTypes = resourceTypes;
        this.roles = roles;
        this.groups = groups;
        this.users = users;
        this.credentials = credentials;
        this.permissions = permissions;
        this.rolePermissions = rolePermissions;
        this.grants = grants;
        this.organizationScopes = organizationScopes;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    /** Internal installation operation; external callers enter through IdentitySetupService. */
    @Transactional
    public long initialize(long companyUnitId, String username, String email, String password, String operatorReference) {
        if (companyUnitId <= 0) throw new IllegalArgumentException("Invalid company reference");
        String normalizedUsername = validateInput(username, email, password, operatorReference);

        Instant now = Instant.now();
        organizationScopes.requireEligibleCompanyUnit(companyUnitId, LocalDate.ofInstant(now, ZoneOffset.UTC));
        requireFreshIdentity();

        // The unique LOCAL provider insert is the transaction-wide initialization gate.
        // A competing transaction cannot also commit this row; never upsert or retry it.
        IdentityProvider provider = providers.save(IdentityProvider.local());
        UserStatus pending = statuses.save(new UserStatus(UserStatus.PENDING_CODE, "Pending"));
        UserStatus active = statuses.save(new UserStatus(UserStatus.ACTIVE_CODE, "Active"));
        statuses.save(new UserStatus(UserStatus.SUSPENDED_CODE, "Suspended"));
        UserStatus disabled = statuses.save(new UserStatus(UserStatus.DISABLED_CODE, "Disabled"));
        UserType human = types.save(new UserType(UserType.HUMAN_CODE, "بشري", "Humain", "Human"));
        UserType service = types.save(new UserType(UserType.SERVICE_CODE, "خدمة", "Service", "Service"));

        Role role = roles.save(new Role(ADMIN_ROLE, "Identity administrator"));
        Map<String, Action> actionByCode = new LinkedHashMap<>();
        Map<String, ResourceType> resourceByCode = new LinkedHashMap<>();
        for (String code : INITIAL_PERMISSIONS) {
            String[] parts = code.split("\\.", 2);
            String resourceCode = parts[0];
            String actionCode = parts[1];
            Action action = actionByCode.computeIfAbsent(actionCode, key -> actions.save(new Action(key, label(key))));
            ResourceType resource = resourceByCode.computeIfAbsent(resourceCode,
                    key -> resourceTypes.save(new ResourceType(key, label(key))));
            Permission permission = permissions.save(new Permission(code, label(resourceCode) + " / " + label(actionCode),
                    action, resource));
            rolePermissions.save(new RolePermission(role, permission));
        }

        User attribution = new User(provider, BOOTSTRAP_USERNAME, BOOTSTRAP_EMAIL, null, companyUnitId, pending, service);
        attribution.disable(disabled);
        attribution = users.save(attribution);

        User administrator = users.save(new User(provider, normalizedUsername, email, null, companyUnitId, pending, human));
        LocalCredential credential = credentials.save(new LocalCredential(administrator, passwordEncoder.encode(password), now));
        administrator.activate(credential, active);
        // This attribution exception is confined to the one-time trusted initialization transaction.
        grants.save(new UserRoleGrant(administrator, role, AuthorizationScope.global(companyUnitId),
                "IDN-01 trusted local initialization; operator=" + operatorReference, now, null, attribution));
        return administrator.getId();
    }

    String validateInput(String username, String email, String password, String operatorReference) {
        if (username == null) throw new IllegalArgumentException("Invalid identity initialization input");
        String normalizedUsername = User.normalizeUsername(username);
        if (email == null || email.isBlank() || email.equalsIgnoreCase(BOOTSTRAP_EMAIL)
                || normalizedUsername.equals(BOOTSTRAP_USERNAME)
                || !validator.validateValue(User.class, "email", email).isEmpty()
                || password == null || password.isBlank() || password.codePointCount(0, password.length()) < 15
                || password.getBytes(StandardCharsets.UTF_8).length > 72
                || operatorReference == null || !OPERATOR_REFERENCE.matcher(operatorReference).matches()) {
            throw new IllegalArgumentException("Invalid identity initialization input");
        }
        return normalizedUsername;
    }

    public boolean isFresh() {
        // Every independent Identity root; dependent rows have required owner foreign keys.
        return providers.count() == 0 && statuses.count() == 0 && types.count() == 0 && actions.count() == 0
                && resourceTypes.count() == 0 && roles.count() == 0 && groups.count() == 0;
    }

    private void requireFreshIdentity() {
        if (!isFresh()) {
            throw new IllegalStateException("Identity initialization requires an empty Identity schema");
        }
    }

    private static String label(String code) {
        String words = code.replace('-', ' ');
        return Character.toUpperCase(words.charAt(0)) + words.substring(1);
    }
}
