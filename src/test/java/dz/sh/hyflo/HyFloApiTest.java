package dz.sh.hyflo;

import java.time.Instant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;

import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.reference.domain.model.Language;
import dz.sh.hyflo.modules.foundation.reference.domain.model.Location;
import dz.sh.hyflo.modules.foundation.reference.domain.model.MeasurementDomain;
import dz.sh.hyflo.modules.foundation.reference.domain.model.MeasurementUnit;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HyFloApiTest {

    @Test
    void lombokAndMapStructAnnotationProcessorsWorkTogether() {
        var mapped = Mappers.getMapper(BuildProbeMapper.class)
                .map(new BuildProbeSource("synthetic"));
        assertThat(mapped.getValue()).isEqualTo("synthetic");
    }

    @Test
    void activationRequiresOwnCredentialAndDisabledAccountsCannotBeReactivated() {
        var provider = IdentityProvider.local();
        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        var userType = new UserType(UserType.HUMAN_CODE, null, null, "Human");
        var active = new UserStatus(UserStatus.ACTIVE_CODE, "Active");
        var suspended = new UserStatus(UserStatus.SUSPENDED_CODE, "Suspended");
        var disabled = new UserStatus(UserStatus.DISABLED_CODE, "Disabled");
        var account = new User(provider, "  Synthetic.User  ", "account@example.invalid", null, 1L, pending, userType);
        var other = new User(provider, "other", "other@example.invalid", null, 1L, pending, userType);
        String hash = new BCryptPasswordEncoder().encode("synthetic-account-password");
        var changedAt = Instant.parse("2026-11-24T00:00:00Z");
        var foreignCredential = new LocalCredential(other, hash, changedAt);
        var ownCredential = new LocalCredential(account, hash, changedAt);
        String replacement = "{bcrypt}" + new BCryptPasswordEncoder().encode("synthetic-replacement-password");
        assertThat(LocalCredential.supportsPasswordHash(replacement)).isTrue();
        assertThat(LocalCredential.supportsPasswordHash(hash)).isFalse();
        assertThat(LocalCredential.supportsPasswordHash(null)).isFalse();
        assertThatThrownBy(() -> ownCredential.replacePasswordHash("plaintext", changedAt.plusSeconds(1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ownCredential.replacePasswordHash(replacement, changedAt.minusSeconds(1)))
                .isInstanceOf(IllegalArgumentException.class);
        ownCredential.replacePasswordHash(replacement, changedAt.plusSeconds(1));
        assertThat(ownCredential.getPasswordHash()).isEqualTo(replacement);
        assertThat(ownCredential.getPasswordChangedAt()).isEqualTo(changedAt.plusSeconds(1));

        assertThat(account.getUsername()).isEqualTo("synthetic.user");
        assertThatThrownBy(() -> account.activate(foreignCredential, active))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(account.getUserStatus().getCode()).isEqualTo(UserStatus.PENDING_CODE);

        account.activate(ownCredential, active);
        account.suspend(suspended);
        assertThat(account.getUserStatus().getCode()).isEqualTo(UserStatus.SUSPENDED_CODE);
        account.activate(ownCredential, active);
        account.disable(disabled);
        assertThatThrownBy(() -> account.activate(ownCredential, active)).isInstanceOf(IllegalStateException.class);
        assertThat(account.getUserStatus().getCode()).isEqualTo(UserStatus.DISABLED_CODE);
        assertThatThrownBy(() -> ownCredential.replacePasswordHash(replacement, changedAt.plusSeconds(2)))
                .isInstanceOf(IllegalStateException.class);
        assertThat(new dz.sh.hyflo.modules.foundation.identity.api.request.LocalCredentialRequest("synthetic-secret", "reason").toString())
                .doesNotContain("synthetic-secret", "reason");
    }

    @Test
    void employeeAssociationIsOptionalHumanOnlyAndRequiresExplicitUnlink() {
        var provider = IdentityProvider.local();
        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        var disabled = new UserStatus(UserStatus.DISABLED_CODE, "Disabled");
        var human = new UserType(UserType.HUMAN_CODE, null, null, "Human");
        var service = new UserType(UserType.SERVICE_CODE, null, null, "Service");
        var user = new User(provider, "link-human", "link-human@example.invalid", null, 1L, pending, human);
        assertThat(user.getEmployeeId()).isNull();
        assertThatThrownBy(user::unlinkEmployee).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> user.linkEmployee(0)).isInstanceOf(IllegalArgumentException.class);
        user.linkEmployee(10);
        assertThat(user.getEmployeeId()).isEqualTo(10L);
        assertThatThrownBy(() -> user.linkEmployee(11)).isInstanceOf(IllegalStateException.class);
        user.unlinkEmployee();
        user.linkEmployee(11);
        user.disable(disabled);
        user.unlinkEmployee();
        assertThat(user.getEmployeeId()).isNull();
        assertThatThrownBy(() -> user.linkEmployee(11)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> new User(provider, "link-service", "link-service@example.invalid", 1L, 1L, pending, service))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new User(provider, "link-invalid", "link-invalid@example.invalid", -1L, 1L, pending, human))
                .isInstanceOf(IllegalArgumentException.class);
        var serviceAccount = new User(provider, "link-service", "link-service@example.invalid", null, 1L, pending, service);
        assertThatThrownBy(() -> serviceAccount.linkEmployee(1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void roleNamesAreDataAndAuthorityBoundariesRejectCrossScopeLinks() {
        var company = new Role("arbitrary", "Any label");
        var platform = new Role("operator-chosen", "Any label", true);
        var permission = new Permission("platform-role.read", "Read", new Action("read", "Read"),
                new ResourceType("platform-role", "Platform role"));
        var companyPermission = new Permission("role.read", "Read", new Action("read", "Read"),
                new ResourceType("role", "Role"));
        assertThat(company.isPlatformRole()).isFalse();
        assertThat(platform.isPlatformRole()).isTrue();
        assertThat(new RolePermission(platform, permission).getPermission()).isSameAs(permission);
        assertThat(new RolePermission(company, companyPermission).getPermission()).isSameAs(companyPermission);
        assertThatThrownBy(() -> new RolePermission(company, permission)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new RolePermission(platform, companyPermission)).isInstanceOf(IllegalArgumentException.class);
        var malformed = new Permission("PLATFORM-role.read", "Malformed", new Action("read", "Read"),
                new ResourceType("role", "Role"));
        assertThat(malformed.isPlatformPermission()).isTrue();
        assertThatThrownBy(() -> new RolePermission(company, malformed)).isInstanceOf(IllegalArgumentException.class);
        var actor = new User(IdentityProvider.local(), "synthetic-admin", "admin@example.invalid", null, 1L,
                new UserStatus(UserStatus.PENDING_CODE, "Pending"), new UserType(UserType.HUMAN_CODE, null, null, "Human"));
        assertThatThrownBy(() -> new UserRoleGrant(actor, platform, AuthorizationScope.global(1),
                "synthetic", Instant.now(), null, actor)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Role("bad code", "Role", true)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void distinguishesCompanyWideAndSpecificResourceScopes() {
        var global = AuthorizationScope.global(1);
        assertThat(global.getScopeType()).isEqualTo(AuthorizationScope.GLOBAL);
        assertThat(global.getOrganizationUnitId()).isEqualTo(1L);
        assertThat(global.getScopeReferenceId()).isNull();

        for (var scope : java.util.List.of(
                AuthorizationScope.region(1, 2),
                AuthorizationScope.pipeline(1, 2),
                AuthorizationScope.infrastructure(1, 2))) {
            assertThat(scope.getOrganizationUnitId()).isEqualTo(1L);
            assertThat(scope.getScopeReferenceId()).isEqualTo(2L);
        }
        assertThat(AuthorizationScope.region(1, 2).getScopeType()).isEqualTo(AuthorizationScope.REGION);
        assertThat(AuthorizationScope.pipeline(1, 2).getScopeType()).isEqualTo(AuthorizationScope.PIPELINE);
        assertThat(AuthorizationScope.infrastructure(1, 2).getScopeType()).isEqualTo(AuthorizationScope.INFRASTRUCTURE);
        assertThatThrownBy(() -> AuthorizationScope.global(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AuthorizationScope.region(1, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AuthorizationScope.pipeline(1, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AuthorizationScope.infrastructure(1, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void organizationDataRejectsInvalidCodesBirthAndValidity() {
        var type = new dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeType("PERMANENT", null, null, "Permanent");
        var status = new dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeStatus("ACTIVE", null, null, "Active");
        var hire = java.time.LocalDate.of(2020, 1, 1);
        var employee = new dz.sh.hyflo.modules.foundation.organization.domain.model.Employee(
                "SYN-01", null, null, "Sample", "Employee", hire, null, type, status);
        assertThatThrownBy(() -> employee.updatePersonalDetails(java.time.LocalDate.of(2021, 1, 1),
                null, null, null, null, null, null, null, null, null, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> employee.updatePersonalDetails(null, null, null, null, null,
                null, null, null, null, 0L, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeStatus("UNKNOWN", null, null, "Unknown"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.reference.domain.model.Country("DZ", null, null, "Invalid"))
                .isInstanceOf(IllegalArgumentException.class);
        var unitType = new dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType("UNIT", null, null, "Unit");
        var unitStatus = new dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus("ACTIVE", null, null, "Active");
        assertThatThrownBy(() -> new dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit(
                "SYN", null, null, "Synthetic", unitType, null, hire, hire, unitStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void organizationUnitTypeRetirementPreservesCodeAndDescriptions() {
        var type = new dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType(
                "UNIT", null, "Unité", "Unit", "وحدة", "Description", "Unit description");
        assertThat(type.isActive()).isTrue();
        assertThat(type.getDescriptionAr()).isEqualTo("وحدة");
        assertThat(type.getDescriptionFr()).isEqualTo("Description");
        assertThat(type.getDescriptionEn()).isEqualTo("Unit description");
        type.retire();
        assertThat(type.isActive()).isFalse();
        assertThat(type.getCode()).isEqualTo("UNIT");
        assertThat(type.getDescriptionEn()).isEqualTo("Unit description");

        var minimal = new dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType(
                "OTHER", null, null, "Other");
        assertThat(minimal.isActive()).isTrue();
        assertThat(minimal.getDescriptionEn()).isNull();
    }

    @Test
    void userTypeRequiresSupportedCodeAndLocalizedName() {
        var human = new UserType(UserType.HUMAN_CODE, null, null, "Human");
        assertThat(human.hasCode(UserType.HUMAN_CODE)).isTrue();
        assertThat(new UserType(UserType.SERVICE_CODE, null, "Service", null).hasCode(UserType.SERVICE_CODE)).isTrue();
        assertThatThrownBy(() -> new UserType("SYSTEM", null, null, "System"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new UserType(UserType.HUMAN_CODE, null, " ", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new User(IdentityProvider.local(), "sample", "sample@example.invalid", null, 1L,
                new UserStatus(UserStatus.PENDING_CODE, "Pending"), null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new User(IdentityProvider.local(), "sample", "sample@example.invalid", null, 0L,
                new UserStatus(UserStatus.PENDING_CODE, "Pending"), human))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void languageAndMeasurementUnitRetainBusinessLabelsAndRejectInvalidData() {
        var arabic = new Language(" AR ", "العربية", null, null, "العربية", true);
        assertThat(arabic.getCode()).isEqualTo("ar");
        assertThat(arabic.isRightToLeft()).isTrue();
        arabic.retire();
        assertThat(arabic.isActive()).isFalse();
        assertThatThrownBy(() -> new Language("ara", null, null, "Arabic", "Arabic", true))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Language("ar", null, null, "Arabic", " ", true))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Language("ar", null, null, null, "Arabic", true))
                .isInstanceOf(IllegalArgumentException.class);
        var pressure = new MeasurementDomain(" pressure ", "الضغط", "Pression", "Pressure");
        assertThat(pressure.getCode()).isEqualTo("PRESSURE");
        assertThat(pressure.getNameAr()).isEqualTo("الضغط");
        assertThat(pressure.getNameFr()).isEqualTo("Pression");
        assertThat(pressure.getNameEn()).isEqualTo("Pressure");
        assertThatThrownBy(() -> new MeasurementDomain("bad code", null, null, "Pressure"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MeasurementDomain("PRESSURE", null, " ", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MeasurementDomain("PRESSURE", null, null, "a".repeat(121)))
                .isInstanceOf(IllegalArgumentException.class);
        var unit = new MeasurementUnit(" pascal ", null, null, "Pascal", "Pa", pressure);
        assertThat(unit.getCode()).isEqualTo("PASCAL");
        assertThat(unit.getSymbol()).isEqualTo("Pa");
        unit.retire();
        assertThat(unit.isActive()).isFalse();
        assertThat(unit.getMeasurementDomain()).isSameAs(pressure);
        assertThatThrownBy(() -> new MeasurementUnit("PASCAL", null, null, "Pascal", "Pa", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A measurement domain is required");
        assertThatThrownBy(() -> new MeasurementUnit("bad code", null, null, "Unit", "Pa", pressure))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MeasurementUnit("PASCAL", null, null, "Pascal", " ", pressure))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MeasurementUnit("PASCAL", null, null, null, "Pa", pressure))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void locationRejectsInvalidCoordinatesAndPreservesUnknownElevation() {
        var remote = new Location(null, null, "Remote point", -90.0, 180.0, null, null);
        assertThat(remote.getElevation()).isNull();
        assertThat(remote.getLocality()).isNull();
        assertThatThrownBy(() -> new Location(null, null, "Point", 90.1, 0.0, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Location(null, null, "Point", 0.0, -180.1, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Location(null, null, "Point", Double.NaN, 0.0, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Location(null, null, "Point", 0.0, Double.POSITIVE_INFINITY, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Location(null, null, "Point", 0.0, 0.0, Double.NEGATIVE_INFINITY, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Location(null, null, "Point", null, 0.0, null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Location(null, null, null, 0.0, 0.0, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jwtSigningAndParsingWorkWithTheRuntimeSerializer() {
        var key = Jwts.SIG.HS256.key().build();
        var token = Jwts.builder().subject("synthetic-user").signWith(key).compact();
        var claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();
        assertThat(claims.getSubject()).isEqualTo("synthetic-user");
    }
}

@Getter
class BuildProbeSource {
    private final String value;

    BuildProbeSource(String value) {
        this.value = value;
    }
}

record BuildProbeTarget(String value) {
    public String getValue() {
        return value;
    }
}

@Mapper
interface BuildProbeMapper {
    BuildProbeTarget map(BuildProbeSource source);
}
