package dz.sh.hyflo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;

import dz.sh.hyflo.modules.foundation.identity.application.service.AuthorizationScopeService;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Group;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupMembership;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LoginSession;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RefreshToken;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.organization.application.service.EmployeeAssignmentService;
import dz.sh.hyflo.modules.foundation.organization.application.service.OrganizationUnitScopeService;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.PositionLevel;
import dz.sh.hyflo.modules.foundation.organization.domain.model.PositionStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.AssignmentType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.AssignmentStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Employee;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Position;
import dz.sh.hyflo.modules.foundation.infra.domain.model.File;
import dz.sh.hyflo.modules.foundation.reference.domain.model.Country;
import dz.sh.hyflo.modules.foundation.reference.domain.model.District;
import dz.sh.hyflo.modules.foundation.reference.domain.model.Locality;
import dz.sh.hyflo.modules.foundation.reference.domain.model.Language;
import dz.sh.hyflo.modules.foundation.reference.domain.model.Location;
import dz.sh.hyflo.modules.foundation.reference.domain.model.MeasurementUnit;
import dz.sh.hyflo.modules.foundation.reference.domain.model.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class HyFloApiIT {

	@SuppressWarnings("resource")
    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("hyflo_test")
            .withUsername("hyflo_test")
            .withPassword("synthetic-db-password");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    EmployeeAssignmentService employeeAssignments;

    @Autowired
    AuthorizationScopeService authorizationScopes;

    @Autowired
    PlatformTransactionManager transactions;

    @PersistenceContext
    EntityManager entityManager;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    @Test
    void registersOneRepositoryForEveryFoundationEntity() {
        var repositories = new Repositories(applicationContext);
        var entities = entityManager.getMetamodel().getEntities().stream()
                .map(entity -> entity.getJavaType())
                .filter(type -> type.getPackageName().startsWith("dz.sh.hyflo.modules.foundation."))
                .toList();
        assertThat(entities).hasSize(38);
        for (var entity : entities) {
            var expectedInterface = entity.getPackageName().replace(".domain.model", ".infrastructure.persistence.repository")
                    + "." + entity.getSimpleName() + "Repository";
            var information = repositories.getRepositoryInformationFor(entity).orElseThrow();
            assertThat(information.getDomainType()).isEqualTo(entity);
            assertThat(information.getIdType()).isEqualTo(Long.class);
            assertThat(information.getRepositoryInterface().getName()).isEqualTo(expectedInterface);
            assertThat(repositories.getRepositoryFor(entity)).isPresent();
            assertThat(applicationContext.getBeansOfType(information.getRepositoryInterface())).hasSize(1);
        }
        assertThat(repositories.getRepositoryFor(AuthorizationScope.class)).isEmpty();
        assertThat(repositories.getRepositoryFor(SchemaProbe.class)).isEmpty();
    }

    @Test
    void registersOneApplicationServiceForEveryFoundationEntity() throws ClassNotFoundException {
        var repositories = new Repositories(applicationContext);
        var entities = entityManager.getMetamodel().getEntities().stream()
                .map(entity -> entity.getJavaType())
                .filter(type -> type.getPackageName().startsWith("dz.sh.hyflo.modules.foundation."))
                .toList();
        assertThat(entities).hasSize(38);
        for (var entity : entities) {
            var expectedService = entity.getPackageName().replace(".domain.model", ".application.service")
                    + "." + entity.getSimpleName() + "Service";
            Class<?> serviceType = Class.forName(expectedService);
            assertThat(applicationContext.getBeansOfType(serviceType)).hasSize(1);
            if (serviceType != EmployeeAssignmentService.class) {
                var repositoryType = repositories.getRepositoryInformationFor(entity).orElseThrow().getRepositoryInterface();
                assertThat(serviceType.getDeclaredConstructors()).hasSize(1);
                assertThat(serviceType.getDeclaredConstructors()[0].getParameterTypes()).containsExactly(repositoryType);
                // JaCoCo injects a private static synthetic method into instrumented classes.
                var declaredBusinessMethods = java.util.Arrays.stream(serviceType.getDeclaredMethods())
                        .filter(method -> !(method.isSynthetic()
                                && java.lang.reflect.Modifier.isPrivate(method.getModifiers())
                                && java.lang.reflect.Modifier.isStatic(method.getModifiers())
                                && method.getName().equals("$jacocoInit")))
                        .toList();
                if (entity.getSimpleName().equals("UserLifecycleEvent")) {
                    assertThat(declaredBusinessMethods).extracting(java.lang.reflect.Method::getName).containsExactly("record");
                } else {
                    assertThat(declaredBusinessMethods)
                            .as("Declared business methods of %s", serviceType.getName()).isEmpty();
                }
            }
        }
        assertThat(applicationContext.getBeansOfType(AuthorizationScopeService.class)).hasSize(1);
        assertThat(applicationContext.getBeansOfType(OrganizationUnitScopeService.class)).hasSize(1);
    }

    @Test
    @Transactional
    void hibernateCreatesSchemaAndPersistsSyntheticDataInMySql() {
        assertThat(jdbc.queryForObject("SELECT DATABASE()", String.class))
                .isEqualTo("hyflo_test");

        entityManager.persist(new SchemaProbe(1L, "synthetic"));
        entityManager.flush();
        entityManager.clear();

        assertThat(entityManager.find(SchemaProbe.class, 1L).getLabel())
                .isEqualTo("synthetic");
        assertThat(jdbc.queryForObject(
                "SELECT label FROM hyflo_test_probe WHERE id = ?", String.class, 1L))
                .isEqualTo("synthetic");
    }

    @Test
    @Transactional
    void persistsLocalAccountAndCredentialSeparately() {
        var provider = IdentityProvider.local();
        entityManager.persist(provider);

        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        var userType = new UserType(UserType.HUMAN_CODE, null, null, "Human");
        var active = new UserStatus(UserStatus.ACTIVE_CODE, "Active");
        var suspended = new UserStatus(UserStatus.SUSPENDED_CODE, "Suspended");
        var disabled = new UserStatus(UserStatus.DISABLED_CODE, "Disabled");
        entityManager.persist(pending);
        entityManager.persist(userType);
        entityManager.persist(active);
        entityManager.persist(suspended);
        entityManager.persist(disabled);

        assertThatThrownBy(() -> new UserStatus("UNKNOWN", "Unknown"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new User(provider, "synthetic", "other@example.invalid", null, 1L, active, userType))
                .isInstanceOf(IllegalArgumentException.class);

        var account = new User(provider, "  Synthetic.User  ", "account@example.invalid", null, 1L, pending, userType);
        entityManager.persist(account);

        var encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("synthetic-account-password");
        var credential = new LocalCredential(account, hash, Instant.parse("2026-11-24T00:00:00Z"));
        entityManager.persist(credential);
        assertThatThrownBy(() -> account.activate(credential, disabled))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(account.getUserStatus().getCode()).isEqualTo(UserStatus.PENDING_CODE);
        account.activate(credential, active);
        account.suspend(suspended);
        account.activate(credential, active);

        entityManager.flush();
        entityManager.clear();

        var savedAccount = entityManager.find(User.class, account.getId());
        var savedCredential = entityManager.find(LocalCredential.class, credential.getId());
        assertThat(savedAccount.getUsername()).isEqualTo("synthetic.user");
        assertThat(savedAccount.getUserStatus().getCode()).isEqualTo(UserStatus.ACTIVE_CODE);
        assertThat(savedAccount.getIdentityProvider().getCode()).isEqualTo(IdentityProvider.LOCAL_CODE);
        assertThat(savedAccount.getUserType().getCode()).isEqualTo(UserType.HUMAN_CODE);
        assertThat(savedAccount.getUserType().getNameEn()).isEqualTo("Human");
        assertThat(savedCredential.getUser().getId()).isEqualTo(savedAccount.getId());
        assertThat(savedCredential.getPasswordHash()).isEqualTo(hash);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_08 WHERE F_00 = ?", String.class,
                savedAccount.getId())).isEqualTo("synthetic.user");
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_10 WHERE F_00 = ?", String.class,
                savedCredential.getId())).isEqualTo(hash);

        assertThat(savedAccount.getOwningOrganizationUnitId()).isEqualTo(1L);
        assertThat(jdbc.queryForObject("SELECT F_05 FROM T_10_01_08 WHERE F_00=?", Long.class, savedAccount.getId())).isEqualTo(1L);
        assertTableColumns("T_10_01_08", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "F_08", "A_01");
        assertThat(jdbc.queryForObject("SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='T_10_01_08' AND COLUMN_NAME='F_05'", String.class)).isEqualTo("NO");
        assertTableColumns("T_10_01_18", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "F_08", "F_09");
        assertForeignKey("T_10_01_18", "F_08", "T_10_01_08", "T_10_01_18_FK_01", false);
        assertForeignKey("T_10_01_18", "F_09", "T_10_01_08", "T_10_01_18_FK_02", false);
        assertTableConstraints("T_10_01_18", "T_10_01_18_FK_01", "T_10_01_18_FK_02");
        assertTableColumns("T_10_01_07", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_01_07", "T_10_01_07_UK_01");
        assertForeignKey("T_10_01_08", "F_06", "T_10_01_01", "T_10_01_08_FK_01", false);
        assertForeignKey("T_10_01_08", "F_07", "T_10_01_02", "T_10_01_08_FK_02", false);
        assertForeignKey("T_10_01_08", "F_08", "T_10_01_07", "T_10_01_08_FK_03", false);
        assertTableColumns("T_10_01_01", "F_00", "F_01", "F_02", "F_03");
        assertTableColumns("T_10_01_10", "F_00", "F_01", "F_02", "F_03");
        assertTableColumns("T_10_01_02", "F_00", "F_01", "F_02");
        assertTableConstraints("T_10_01_08", "T_10_01_08_UK_01", "T_10_01_08_UK_02",
                "T_10_01_08_FK_01", "T_10_01_08_FK_02", "T_10_01_08_FK_03");
        assertTableConstraints("T_10_01_01", "T_10_01_01_UK_01");
        assertTableConstraints("T_10_01_10", "T_10_01_10_UK_01", "T_10_01_10_FK_01");
        assertTableConstraints("T_10_01_02", "T_10_01_02_UK_01");

        savedAccount.disable(entityManager.find(UserStatus.class, disabled.getId()));
        assertThatThrownBy(() -> savedAccount.activate(savedCredential, entityManager.find(UserStatus.class, active.getId())))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @Transactional
    void persistsDirectAndGroupRoleGrantsWithIndependentResourceScopes() {
        var starts = Instant.parse("2026-11-24T10:00:00Z");
        var ends = starts.plusSeconds(3600);
        var now = starts.plusSeconds(600);
        var provider = IdentityProvider.local();
        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        var userType = new UserType(UserType.HUMAN_CODE, null, null, "Human");
        entityManager.persist(provider);
        entityManager.persist(pending);
        entityManager.persist(userType);
        var actor = new User(provider, "grant-admin", "admin@example.invalid", null, 1L, pending, userType);
        var revoker = new User(provider, "grant-revoker", "revoker@example.invalid", null, 1L, pending, userType);
        var account = new User(provider, "station-engineer", "engineer@example.invalid", null, 1L, pending, userType);
        account.changeProfilePicture(42L);
        entityManager.persist(actor);
        entityManager.persist(revoker);
        entityManager.persist(account);

        var action = new Action("update", "Update");
        var resourceType = new ResourceType("station", "Station");
        var role = new Role("station-engineer", "Station Engineer");
        var group = new Group("gz2-engineers", "GZ2 Engineers");
        entityManager.persist(action);
        entityManager.persist(resourceType);
        entityManager.persist(role);
        entityManager.persist(group);
        var permission = new Permission("station.update", "Update station", action, resourceType);
        entityManager.persist(permission);
        var rolePermission = new RolePermission(role, permission);
        var membership = new GroupMembership(account, group, starts, ends, actor);
        entityManager.persist(rolePermission);
        entityManager.persist(membership);

        var companyType = new OrganizationUnitType(OrganizationUnitType.COMPANY_CODE, null, null, "Synthetic company type");
        var divisionType = new OrganizationUnitType("DIVISION", null, null, "Company");
        var unitStatus = entityManager.createQuery(
                        "select s from OrganizationUnitStatus s where s.code = :code", OrganizationUnitStatus.class)
                .setParameter("code", OrganizationUnitStatus.ACTIVE_CODE)
                .getResultList().stream().findFirst().orElseGet(() -> {
                    var status = new OrganizationUnitStatus(OrganizationUnitStatus.ACTIVE_CODE, null, null, "Active");
                    entityManager.persist(status);
                    return status;
                });
        entityManager.persist(companyType);
        entityManager.persist(divisionType);
        var company = new OrganizationUnit("SYNTH_COMPANY", null, null, "Synthetic company", companyType, null,
                LocalDate.of(2026, 1, 1), null, unitStatus);
        entityManager.persist(company);
        var division = new OrganizationUnit("SYNTH_DIVISION", null, null, "Synthetic division", divisionType, company,
                LocalDate.of(2026, 1, 1), null, unitStatus);
        entityManager.persist(division);
        entityManager.flush();

        long organizationUnitId = company.getId();
        assertThat(authorizationScopes.validateCompanyBoundary(AuthorizationScope.global(organizationUnitId))
                .getOrganizationUnitId()).isEqualTo(organizationUnitId);
        assertThat(authorizationScopes.validateCompanyBoundary(AuthorizationScope.region(organizationUnitId, 2))
                .getScopeReferenceId()).isEqualTo(2L);

        var direct = new UserRoleGrant(account, role,
                authorizationScopes.validateCompanyBoundary(AuthorizationScope.infrastructure(organizationUnitId, 3)),
                "Regional station assignment", starts, ends, actor);
        var inherited = new GroupRoleGrant(group, role,
                authorizationScopes.validateCompanyBoundary(AuthorizationScope.pipeline(organizationUnitId, 2)),
                "Assigned-region station access", starts, ends, actor);
        entityManager.persist(direct);
        entityManager.persist(inherited);
        direct.revoke(now, revoker);

        var session = new LoginSession(account, starts, ends);
        var event = new AuthenticationEvent(AuthenticationEvent.LOGIN_FAILED, starts, "synthetic-login", provider, null);
        entityManager.persist(session);
        entityManager.persist(event);
        entityManager.flush();
        entityManager.clear();

        var savedDirect = entityManager.find(UserRoleGrant.class, direct.getId());
        var savedInherited = entityManager.find(GroupRoleGrant.class, inherited.getId());
        assertThat(savedDirect.getAuthorizationScope().getScopeType()).isEqualTo(AuthorizationScope.INFRASTRUCTURE);
        assertThat(savedDirect.getAuthorizationScope().getScopeReferenceId()).isEqualTo(3L);
        assertThat(savedDirect.getAuthorizationScope().getOrganizationUnitId()).isEqualTo(organizationUnitId);
        assertThat(savedInherited.getAuthorizationScope().getOrganizationUnitId()).isEqualTo(organizationUnitId);
        assertThat(savedDirect.getGrantedBy().getId()).isEqualTo(actor.getId());
        assertThat(savedDirect.getRevokedBy().getId()).isEqualTo(revoker.getId());
        assertThat(savedDirect.getRevokedAt()).isEqualTo(now);
        assertThat(savedInherited.getGrantedBy().getId()).isEqualTo(actor.getId());
        assertThat(savedInherited.getRevokedBy()).isNull();
        assertThat(savedInherited.getRevokedAt()).isNull();
        assertThat(savedDirect.isActiveAt(now)).isFalse();
        assertThat(savedInherited.isActiveAt(now)).isTrue();
        assertThat(savedInherited.isActiveAt(ends)).isFalse();
        assertThat(savedInherited.getAuthorizationScope().getScopeType()).isEqualTo(AuthorizationScope.PIPELINE);
        assertThat(savedInherited.getAuthorizationScope().getScopeReferenceId()).isEqualTo(2L);
        var savedMembership = entityManager.find(GroupMembership.class, membership.getId());
        assertThat(savedMembership.getUser().getId()).isEqualTo(account.getId());
        assertThat(savedMembership.getGrantedBy().getId()).isEqualTo(actor.getId());
        assertThat(savedMembership.getRevokedBy()).isNull();
        assertThat(savedMembership.getRevokedAt()).isNull();
        assertThat(savedMembership.isActiveAt(now)).isTrue();
        assertThat(entityManager.find(RolePermission.class, rolePermission.getId()).getPermission().getCode())
                .isEqualTo("station.update");
        assertThat(entityManager.find(LoginSession.class, session.getId()).isActiveAt(now)).isTrue();
        assertThat(entityManager.find(AuthenticationEvent.class, event.getId()).getUser()).isNull();
        assertThat(entityManager.find(User.class, account.getId()).getProfilePictureFileId()).isEqualTo(42L);

        assertTableColumns("T_10_01_15", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06",
                "F_07", "F_08", "F_09", "F_10", "F_11", "A_01");
        assertTableColumns("T_10_01_16", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06",
                "F_07", "F_08", "F_09", "F_10", "F_11", "A_01");
        assertActorForeignKeys("T_10_01_15", "F_10", "F_11");
        assertActorForeignKeys("T_10_01_16", "F_10", "F_11");
        assertActorForeignKeys("T_10_01_13", "F_04", "F_05");
        assertTableConstraints("T_10_01_13", "T_10_01_13_FK_01", "T_10_01_13_FK_02");
        assertTableConstraints("T_10_01_15", "T_10_01_15_FK_01", "T_10_01_15_FK_02");
        assertTableConstraints("T_10_01_16", "T_10_01_16_FK_01", "T_10_01_16_FK_02");
        assertTableConstraints("T_10_01_14", "T_10_01_14_UK_01", "T_10_01_14_FK_01", "T_10_01_14_FK_02");

        var revokingUser = entityManager.getReference(User.class, revoker.getId());
        savedInherited.revoke(now, revokingUser);
        savedMembership.revoke(now, revokingUser);
        entityManager.flush();
        entityManager.clear();

        var revokedInherited = entityManager.find(GroupRoleGrant.class, inherited.getId());
        assertThat(revokedInherited.getRevokedAt()).isEqualTo(now);
        assertThat(revokedInherited.getRevokedBy().getId()).isEqualTo(revoker.getId());
        assertThat(revokedInherited.isActiveAt(now)).isFalse();
        var revokedMembership = entityManager.find(GroupMembership.class, membership.getId());
        assertThat(revokedMembership.getRevokedAt()).isEqualTo(now);
        assertThat(revokedMembership.getRevokedBy().getId()).isEqualTo(revoker.getId());
        assertThat(revokedMembership.isActiveAt(now)).isFalse();

        // Expected failures can mark the participating transaction rollback-only.
        // Keep them after all successful database writes and readback assertions.
        assertThatThrownBy(() -> authorizationScopes.validateCompanyBoundary(AuthorizationScope.global(division.getId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid company organization-unit reference");
        assertThatThrownBy(() -> authorizationScopes.validateCompanyBoundary(AuthorizationScope.pipeline(Long.MAX_VALUE, 2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid company organization-unit reference");
    }

    @Test
    @Transactional
    void persistsConsumedAndReplacementRefreshTokensForTheSameSession() {
        var startedAt = Instant.parse("2026-11-24T10:00:00Z");
        var expiresAt = startedAt.plusSeconds(3600);
        var rotatedAt = startedAt.plusSeconds(300);
        var provider = IdentityProvider.local();
        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        var userType = new UserType(UserType.SERVICE_CODE, null, null, "Service");
        entityManager.persist(provider);
        entityManager.persist(pending);
        entityManager.persist(userType);
        var account = new User(provider, "refresh-user", "refresh@example.invalid", null, 1L, pending, userType);
        entityManager.persist(account);
        var session = new LoginSession(account, startedAt, expiresAt);
        entityManager.persist(session);

        var originalToken = new RefreshToken(session, "a".repeat(64), startedAt);
        entityManager.persist(originalToken);
        originalToken.consume(rotatedAt);
        var replacementToken = new RefreshToken(session, "b".repeat(64), rotatedAt);
        entityManager.persist(replacementToken);
        entityManager.flush();
        entityManager.clear();

        var savedOriginal = entityManager.find(RefreshToken.class, originalToken.getId());
        var savedReplacement = entityManager.find(RefreshToken.class, replacementToken.getId());
        assertThat(savedOriginal.getLoginSession().getId()).isEqualTo(session.getId());
        assertThat(entityManager.find(User.class, account.getId()).getUserType().getCode()).isEqualTo(UserType.SERVICE_CODE);
        assertThat(savedOriginal.getTokenDigest()).isEqualTo("a".repeat(64));
        assertThat(savedOriginal.getIssuedAt()).isEqualTo(startedAt);
        assertThat(savedOriginal.getConsumedAt()).isEqualTo(rotatedAt);
        assertThat(savedReplacement.getLoginSession().getId()).isEqualTo(session.getId());
        assertThat(savedReplacement.getConsumedAt()).isNull();
        assertThatThrownBy(() -> savedOriginal.consume(rotatedAt.plusSeconds(1)))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> savedReplacement.consume(expiresAt))
                .isInstanceOf(IllegalStateException.class);
        savedReplacement.getLoginSession().revoke(rotatedAt.plusSeconds(1));
        assertThatThrownBy(() -> savedReplacement.consume(rotatedAt.plusSeconds(2)))
                .isInstanceOf(IllegalStateException.class);
        assertThat(savedReplacement.getConsumedAt()).isNull();

        assertTableColumns("T_10_01_17", "F_00", "F_01", "F_02", "F_03", "F_04", "A_01");
        assertTableConstraints("T_10_01_17", "T_10_01_17_UK_01", "T_10_01_17_FK_01");
    }

    @Test
    void transfersEmployeeAndResolvesTheAssignmentAsOfEachDate() {
        var hire = LocalDate.of(2026, 11, 1);
        var transfer = hire.plusDays(20);
        var endOfEmployment = hire.plusDays(40);
        var type = new OrganizationUnitType("OPS_UNIT", null, "Unité d’exploitation", "Operations unit",
                "وحدة تشغيل", "Unité chargée de l’exploitation", "Operational responsibility unit");
        var employeeType = new EmployeeType(EmployeeType.PERMANENT_CODE, null, null, "Permanent");
        var employeeStatus = new EmployeeStatus(EmployeeStatus.ACTIVE_CODE, null, null, "Active");
        var positionLevel = new PositionLevel(PositionLevel.ENGINEER_CODE, null, null, "Engineer");
        var positionStatus = new PositionStatus(PositionStatus.ACTIVE_CODE, null, null, "Active");
        var unitStatus = new OrganizationUnitStatus(OrganizationUnitStatus.ACTIVE_CODE, null, null, "Active");
        var assignmentType = new AssignmentType(AssignmentType.PRIMARY_CODE, null, null, "Primary");
        var activeStatus = new AssignmentStatus(AssignmentStatus.ACTIVE_CODE, null, null, "Active");
        var endedStatus = new AssignmentStatus(AssignmentStatus.ENDED_CODE, null, null, "Ended");
        var root = new OrganizationUnit("OPS", null, null, "Operations", type, null, hire, null, unitStatus);
        var origin = new OrganizationUnit("OPS_A", null, null, "Operations A", type, root, hire, null, unitStatus);
        var destination = new OrganizationUnit("OPS_B", null, null, "Operations B", type, root, hire, null, unitStatus);
        var engineer = new Position("STATION_ENGINEER", null, null, "Station Engineer", null, "Ingénierie", "Engineering", positionLevel, positionStatus);
        var employee = new Employee("SYN-ORG-01", null, null, "Sample", "Employee", hire, endOfEmployment, employeeType, employeeStatus);
        new TransactionTemplate(transactions).executeWithoutResult(transaction -> {
            for (var catalog : java.util.List.of(employeeType, employeeStatus, positionLevel, positionStatus, unitStatus,
                    assignmentType, activeStatus, endedStatus)) {
                entityManager.persist(catalog);
            }
            var nationality = new Country("DZA", null, null, "Synthetic nationality");
            entityManager.persist(nationality);
            var state = new State("ORG-S", null, null, "State", nationality);
            entityManager.persist(state);
            var district = new District("ORG-D", null, null, "District", state);
            entityManager.persist(district);
            var locality = new Locality("ORG-L", null, null, "Locality", district);
            entityManager.persist(locality);
            locality.updatePostalCode("16000");
            var picture = new File("synthetic/org-profile.png", "profile.png", "image/png", 100,
                    Instant.parse("2026-11-24T12:00:00Z"));
            entityManager.persist(picture);
            employee.updatePersonalDetails(LocalDate.of(1990, 1, 2), "مكان", "Birthplace",
                    "عنوان", "Address", "employee@example.invalid", "0123456789",
                    locality.getId(), locality.getId(), nationality.getId(), picture.getId());
            entityManager.persist(type);
            entityManager.persist(root);
            entityManager.persist(origin);
            entityManager.persist(destination);
            entityManager.persist(engineer);
            entityManager.persist(employee);
            entityManager.flush();
        });

        employeeAssignments.assign(employee.getId(), origin, engineer, hire, assignmentType, activeStatus);
        assertThatThrownBy(() -> employeeAssignments.assign(employee.getId(), destination, engineer, hire.plusDays(1), assignmentType, activeStatus))
                .isInstanceOf(IllegalStateException.class);
        employeeAssignments.transfer(employee.getId(), destination, engineer, transfer, assignmentType, activeStatus, endedStatus);

        var previous = employeeAssignments.findEffectiveOn(employee.getId(), transfer.minusDays(1)).orElseThrow();
        var current = employeeAssignments.findEffectiveOn(employee.getId(), transfer).orElseThrow();
        assertThat(previous.getOrganizationUnit().getId()).isEqualTo(origin.getId());
        assertThat(previous.getValidTo()).isEqualTo(transfer);
        assertThat(current.getOrganizationUnit().getId()).isEqualTo(destination.getId());
        assertThat(current.getPosition().getId()).isEqualTo(engineer.getId());
        assertThat(current.getValidTo()).isEqualTo(endOfEmployment);
        assertThat(employeeAssignments.findEffectiveOn(employee.getId(), endOfEmployment)).isEmpty();
        assertThat(employeeAssignments.findEffectiveOn(employee.getId(), hire.minusDays(1))).isEmpty();
        assertThat(previous.getAssignmentStatus().getCode()).isEqualTo(AssignmentStatus.ENDED_CODE);
        assertThat(current.getAssignmentStatus().getCode()).isEqualTo(AssignmentStatus.ACTIVE_CODE);
        assertThat(current.getAssignmentType().getCode()).isEqualTo(AssignmentType.PRIMARY_CODE);
        new TransactionTemplate(transactions).executeWithoutResult(transaction -> {
            var saved = entityManager.find(Employee.class, employee.getId());
            assertThat(saved.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 2));
            assertThat(saved.getBirthPlaceAr()).isEqualTo("مكان");
            assertThat(saved.getBirthPlaceLt()).isEqualTo("Birthplace");
            assertThat(saved.getAddressAr()).isEqualTo("عنوان");
            assertThat(saved.getAddressLt()).isEqualTo("Address");
            assertThat(saved.getEmail()).isEqualTo("employee@example.invalid");
            assertThat(saved.getPhone()).isEqualTo("0123456789");
            assertThat(entityManager.find(Country.class, saved.getNationalityCountryId()).getCode()).isEqualTo("DZA");
            assertThat(entityManager.find(Locality.class, saved.getBirthLocalityId()).getPostalCode()).isEqualTo("16000");
            assertThat(saved.getAddressLocalityId()).isEqualTo(saved.getBirthLocalityId());
            assertThat(entityManager.find(File.class, saved.getPictureFileId()).getOriginalName()).isEqualTo("profile.png");
            assertThat(saved.getEmployeeType().getCode()).isEqualTo(EmployeeType.PERMANENT_CODE);
            assertThat(saved.getEmployeeStatus().getCode()).isEqualTo(EmployeeStatus.ACTIVE_CODE);
            var savedPosition = entityManager.find(Position.class, engineer.getId());
            assertThat(savedPosition.getPositionLevel().getCode()).isEqualTo(PositionLevel.ENGINEER_CODE);
            assertThat(savedPosition.getPositionStatus().getCode()).isEqualTo(PositionStatus.ACTIVE_CODE);
            assertThat(savedPosition.getDescriptionEn()).isEqualTo("Engineering");
            var savedUnit = entityManager.find(OrganizationUnit.class, destination.getId());
            var savedType = savedUnit.getOrganizationUnitType();
            assertThat(savedType.getDescriptionAr()).isEqualTo("وحدة تشغيل");
            assertThat(savedType.getDescriptionFr()).isEqualTo("Unité chargée de l’exploitation");
            assertThat(savedType.getDescriptionEn()).isEqualTo("Operational responsibility unit");
            assertThat(savedType.isActive()).isTrue();
            assertThat(savedUnit.getOrganizationUnitStatus().getCode()).isEqualTo(OrganizationUnitStatus.ACTIVE_CODE);
            assertThat(savedUnit.getValidFrom()).isEqualTo(hire);
            assertThat(savedUnit.getParentUnit().getId()).isEqualTo(root.getId());
            savedType.retire();
            entityManager.flush();
            entityManager.clear();
            var historicalUnit = entityManager.find(OrganizationUnit.class, destination.getId());
            assertThat(historicalUnit.getOrganizationUnitType().getId()).isEqualTo(type.getId());
            assertThat(historicalUnit.getOrganizationUnitType().isActive()).isFalse();
            assertThat(historicalUnit.getOrganizationUnitType().getDescriptionEn()).isEqualTo("Operational responsibility unit");
        });
        assertThat(jdbc.queryForObject("SELECT F_08 FROM T_10_02_11 WHERE F_00 = ?", Long.class,
                destination.getId())).isEqualTo(root.getId());
        assertTableColumns("T_10_02_01", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "F_08");
        assertTableConstraints("T_10_02_01", "T_10_02_01_UK_01");
        assertTableColumns("T_10_02_02", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_02", "T_10_02_02_UK_01");
        assertTableColumns("T_10_02_03", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_03", "T_10_02_03_UK_01");
        assertTableColumns("T_10_02_04", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_04", "T_10_02_04_UK_01");
        assertTableColumns("T_10_02_05", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_05", "T_10_02_05_UK_01");
        assertTableColumns("T_10_02_06", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_06", "T_10_02_06_UK_01");
        assertTableColumns("T_10_02_07", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_07", "T_10_02_07_UK_01");
        assertTableColumns("T_10_02_08", "F_00", "F_01", "F_02", "F_03", "F_04");
        assertTableConstraints("T_10_02_08", "T_10_02_08_UK_01");
        assertTableColumns("T_10_02_09", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "F_08", "F_09", "F_10", "F_11", "F_12", "F_13", "F_14", "F_15", "F_16", "F_17", "F_18", "F_19", "F_20");
        assertTableConstraints("T_10_02_09", "T_10_02_09_UK_01", "T_10_02_09_FK_01", "T_10_02_09_FK_02");
        assertTableColumns("T_10_02_10", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "F_08", "F_09");
        assertTableConstraints("T_10_02_10", "T_10_02_10_UK_01", "T_10_02_10_FK_01", "T_10_02_10_FK_02");
        assertTableColumns("T_10_02_11", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "F_08", "F_09");
        assertTableConstraints("T_10_02_11", "T_10_02_11_UK_01", "T_10_02_11_FK_01", "T_10_02_11_FK_02", "T_10_02_11_FK_03");
        assertTableColumns("T_10_02_12", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07", "A_01");
        assertTableConstraints("T_10_02_12", "T_10_02_12_UK_01", "T_10_02_12_FK_01", "T_10_02_12_FK_02", "T_10_02_12_FK_03", "T_10_02_12_FK_04", "T_10_02_12_FK_05");
    }

    @Test
    @Transactional
    void persistsTheReferenceHierarchyAndSharedFileMetadata() {
        var created = Instant.parse("2026-11-24T12:00:00Z");
        var country = new Country(" xaa ", null, null, "Synthetic country");
        var otherCountry = new Country("XBB", null, null, "Another synthetic country");
        entityManager.persist(country);
        entityManager.persist(otherCountry);
        var state = new State("S01", null, null, "Synthetic state", country);
        entityManager.persist(state);
        entityManager.persist(new State("S01", null, null, "Another state", otherCountry));
        var district = new District("D01", null, "District synthétique", null, state);
        entityManager.persist(district);
        var locality = new Locality("L01", null, null, "Synthetic locality", district);
        entityManager.persist(locality);
        locality.updatePostalCode("12345");
        var arabic = new Language(" AR ", "العربية", "Arabe", "Arabic", "العربية", true);
        var english = new Language("en", null, null, "English", "English", false);
        var pressure = new MeasurementUnit(" pascal ", null, "Pascal", "Pascal", "Pa");
        var flow = new MeasurementUnit("CUBIC_METRE_PER_HOUR", null, null, "Cubic metre per hour", "m³/h");
        var located = new Location(null, "Point synthétique", "Synthetic point", 31.5, 6.25, 138.5, locality);
        var remote = new Location(null, null, "Remote synthetic point", -90.0, 180.0, null, null);
        entityManager.persist(arabic);
        entityManager.persist(english);
        entityManager.persist(pressure);
        entityManager.persist(flow);
        entityManager.persist(located);
        entityManager.persist(remote);
        english.retire();
        flow.retire();
        var file = new File("synthetic/sample.png", "sample.png", "image/png", 128L, created);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.clear();

        var savedLocality = entityManager.find(Locality.class, locality.getId());
        assertThat(savedLocality.getPostalCode()).isEqualTo("12345");
        assertThat(savedLocality.isActive()).isTrue();
        assertThat(savedLocality.getDistrict().getNameFr()).isEqualTo("District synthétique");
        assertThat(savedLocality.getDistrict().getState().getCountry().getCode()).isEqualTo("XAA");
        var savedArabic = entityManager.find(Language.class, arabic.getId());
        assertThat(savedArabic.getCode()).isEqualTo("ar");
        assertThat(savedArabic.getNameFr()).isEqualTo("Arabe");
        assertThat(savedArabic.getNativeName()).isEqualTo("العربية");
        assertThat(savedArabic.isRightToLeft()).isTrue();
        assertThat(savedArabic.isActive()).isTrue();
        var savedEnglish = entityManager.find(Language.class, english.getId());
        assertThat(savedEnglish.isRightToLeft()).isFalse();
        assertThat(savedEnglish.isActive()).isFalse();
        var savedPressure = entityManager.find(MeasurementUnit.class, pressure.getId());
        assertThat(savedPressure.getCode()).isEqualTo("PASCAL");
        assertThat(savedPressure.getSymbol()).isEqualTo("Pa");
        assertThat(savedPressure.isActive()).isTrue();
        var savedFlow = entityManager.find(MeasurementUnit.class, flow.getId());
        assertThat(savedFlow.getSymbol()).isEqualTo("m³/h");
        assertThat(savedFlow.isActive()).isFalse();
        var savedLocation = entityManager.find(Location.class, located.getId());
        assertThat(savedLocation.getNameFr()).isEqualTo("Point synthétique");
        assertThat(savedLocation.getLatitude()).isEqualTo(31.5);
        assertThat(savedLocation.getLongitude()).isEqualTo(6.25);
        assertThat(savedLocation.getElevation()).isEqualTo(138.5);
        assertThat(savedLocation.getLocality().getId()).isEqualTo(locality.getId());
        var savedRemote = entityManager.find(Location.class, remote.getId());
        assertThat(savedRemote.getLatitude()).isEqualTo(-90.0);
        assertThat(savedRemote.getLongitude()).isEqualTo(180.0);
        assertThat(savedRemote.getElevation()).isNull();
        assertThat(savedRemote.getLocality()).isNull();
        var savedFile = entityManager.find(File.class, file.getId());
        assertThat(savedFile.getStorageKey()).isEqualTo("synthetic/sample.png");
        assertThat(savedFile.getOriginalName()).isEqualTo("sample.png");
        assertThat(savedFile.getMediaType()).isEqualTo("image/png");
        assertThat(savedFile.getSizeBytes()).isEqualTo(128L);
        assertThat(savedFile.getCreatedAt()).isEqualTo(created);
        assertTableColumns("T_10_03_01", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05");
        assertTableColumns("T_10_03_04", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06");
        assertTableColumns("T_10_03_05", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06");
        assertTableColumns("T_10_03_06", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07");
        assertTableColumns("T_10_03_02", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07");
        assertTableColumns("T_10_03_03", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06");
        assertTableColumns("T_10_03_07", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "F_07");
        assertTableConstraints("T_10_03_02", "T_10_03_02_UK_01");
        assertTableConstraints("T_10_03_03", "T_10_03_03_UK_01");
        assertTableConstraints("T_10_03_07", "T_10_03_07_FK_01");
        assertForeignKey("T_10_03_07", "F_07", "T_10_03_06", "T_10_03_07_FK_01", true);
        assertTableConstraints("T_10_03_01", "T_10_03_01_UK_01");
        assertTableConstraints("T_10_03_04", "T_10_03_04_UK_01", "T_10_03_04_FK_01");
        assertTableConstraints("T_10_03_05", "T_10_03_05_UK_01", "T_10_03_05_FK_01");
        assertTableConstraints("T_10_03_06", "T_10_03_06_UK_01", "T_10_03_06_FK_01");
        assertTableConstraints("T_10_00_01", "T_10_00_01_UK_01");
    }

    private void assertForeignKey(String table, String column, String referencedTable, String constraint, boolean nullable) {
        assertThat(jdbc.queryForList("""
                SELECT CONSTRAINT_NAME
                FROM information_schema.KEY_COLUMN_USAGE
                WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?
                    AND COLUMN_NAME = ? AND BINARY REFERENCED_TABLE_NAME = BINARY ?
                    AND REFERENCED_COLUMN_NAME = 'F_00'
                """, String.class, table, column, referencedTable)).containsExactly(constraint);
        assertThat(jdbc.queryForObject("""
                SELECT IS_NULLABLE FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ? AND COLUMN_NAME = ?
                """, String.class, table, column)).isEqualTo(nullable ? "YES" : "NO");
    }

    private void assertActorForeignKeys(String table, String grantedByColumn, String revokedByColumn) {
        assertThat(jdbc.queryForList("""
                SELECT CONCAT(CONSTRAINT_NAME, ':', COLUMN_NAME)
                FROM information_schema.KEY_COLUMN_USAGE
                WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?
                    AND BINARY REFERENCED_TABLE_NAME = BINARY 'T_10_01_08' AND REFERENCED_COLUMN_NAME = 'F_00'
                    AND COLUMN_NAME IN (?, ?)
                """, String.class, table, grantedByColumn, revokedByColumn))
                .containsExactlyInAnyOrder(table + "_FK_03:" + grantedByColumn, table + "_FK_04:" + revokedByColumn);
    }

    private void assertTableColumns(String table, String... columns) {
        assertThat(jdbc.queryForList(
                "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?",
                String.class, table)).containsExactlyInAnyOrder(columns);
    }

    private void assertTableConstraints(String table, String... constraints) {
        assertThat(jdbc.queryForList(
                "SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?",
                String.class, table)).contains(constraints);
    }

    @Test
    void healthEndpointIsUp() throws Exception {
        var response = get("/actuator/health", null);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("UP");
    }

    @Test
    void openApiRequiresLocalAuthentication() throws Exception {
        assertThat(get("/api-docs", null).statusCode()).isEqualTo(401);
        assertThat(get("/api-docs", "hyflo-test:wrong-password").statusCode()).isEqualTo(401);
        // The removed in-memory account is no longer an alternate authentication path.
        // Positive persistent bearer access is verified in IdentityAuthenticationIT.
        assertThat(get("/api-docs", "hyflo-test:synthetic-test-password").statusCode()).isEqualTo(401);
    }

    private HttpResponse<String> get(String path, String credentials) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/hyflo/api" + path))
                .timeout(Duration.ofSeconds(15)).header("Accept", "application/json").GET();
        if (credentials != null) {
            request.header("Authorization", "Basic " + Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8)));
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }
}
