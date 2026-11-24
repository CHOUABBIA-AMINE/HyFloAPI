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
import dz.sh.hyflo.modules.foundation.organization.application.service.EmployeeAssignmentService;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Employee;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Position;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    EmployeeAssignmentService employeeAssignments;

    @Autowired
    PlatformTransactionManager transactions;

    @PersistenceContext
    EntityManager entityManager;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

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
        var active = new UserStatus(UserStatus.ACTIVE_CODE, "Active");
        var suspended = new UserStatus(UserStatus.SUSPENDED_CODE, "Suspended");
        var disabled = new UserStatus(UserStatus.DISABLED_CODE, "Disabled");
        entityManager.persist(pending);
        entityManager.persist(active);
        entityManager.persist(suspended);
        entityManager.persist(disabled);

        assertThatThrownBy(() -> new UserStatus("UNKNOWN", "Unknown"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new User(provider, "synthetic", "other@example.invalid", null, active))
                .isInstanceOf(IllegalArgumentException.class);

        var account = new User(provider, "  Synthetic.User  ", "account@example.invalid", null, pending);
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
        assertThat(savedCredential.getUser().getId()).isEqualTo(savedAccount.getId());
        assertThat(savedCredential.getPasswordHash()).isEqualTo(hash);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_07 WHERE F_00 = ?", String.class,
                savedAccount.getId())).isEqualTo("synthetic.user");
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_09 WHERE F_00 = ?", String.class,
                savedCredential.getId())).isEqualTo(hash);

        assertIdentityColumns("T_10_01_07", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06", "A_01");
        assertIdentityColumns("T_10_01_01", "F_00", "F_01", "F_02", "F_03");
        assertIdentityColumns("T_10_01_09", "F_00", "F_01", "F_02", "F_03");
        assertIdentityColumns("T_10_01_02", "F_00", "F_01", "F_02");
        assertIdentityConstraints("T_10_01_07", "T_10_01_07_UK_01", "T_10_01_07_UK_02",
                "T_10_01_07_FK_01", "T_10_01_07_FK_02");
        assertIdentityConstraints("T_10_01_01", "T_10_01_01_UK_01");
        assertIdentityConstraints("T_10_01_09", "T_10_01_09_UK_01", "T_10_01_09_FK_01");
        assertIdentityConstraints("T_10_01_02", "T_10_01_02_UK_01");

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
        entityManager.persist(provider);
        entityManager.persist(pending);
        var actor = new User(provider, "grant-admin", "admin@example.invalid", null, pending);
        var revoker = new User(provider, "grant-revoker", "revoker@example.invalid", null, pending);
        var account = new User(provider, "station-engineer", "engineer@example.invalid", null, pending);
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

        var direct = new UserRoleGrant(account, role, AuthorizationScope.infrastructure(1, 3),
                "Regional station assignment", starts, ends, actor);
        var inherited = new GroupRoleGrant(group, role, AuthorizationScope.pipeline(1, 2),
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
        assertThat(savedDirect.getAuthorizationScope().getCompanyId()).isEqualTo(1L);
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

        assertIdentityColumns("T_10_01_14", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06",
                "F_07", "F_08", "F_09", "F_10", "F_11", "A_01");
        assertIdentityColumns("T_10_01_15", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "F_06",
                "F_07", "F_08", "F_09", "F_10", "F_11", "A_01");
        assertActorForeignKeys("T_10_01_14", "F_10", "F_11");
        assertActorForeignKeys("T_10_01_15", "F_10", "F_11");
        assertActorForeignKeys("T_10_01_12", "F_04", "F_05");
        assertIdentityConstraints("T_10_01_12", "T_10_01_12_FK_01", "T_10_01_12_FK_02");
        assertIdentityConstraints("T_10_01_14", "T_10_01_14_FK_01", "T_10_01_14_FK_02");
        assertIdentityConstraints("T_10_01_15", "T_10_01_15_FK_01", "T_10_01_15_FK_02");
        assertIdentityConstraints("T_10_01_13", "T_10_01_13_UK_01", "T_10_01_13_FK_01", "T_10_01_13_FK_02");

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
    }

    @Test
    @Transactional
    void persistsConsumedAndReplacementRefreshTokensForTheSameSession() {
        var startedAt = Instant.parse("2026-11-24T10:00:00Z");
        var expiresAt = startedAt.plusSeconds(3600);
        var rotatedAt = startedAt.plusSeconds(300);
        var provider = IdentityProvider.local();
        var pending = new UserStatus(UserStatus.PENDING_CODE, "Pending");
        entityManager.persist(provider);
        entityManager.persist(pending);
        var account = new User(provider, "refresh-user", "refresh@example.invalid", null, pending);
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

        assertIdentityColumns("T_10_01_16", "F_00", "F_01", "F_02", "F_03", "F_04", "A_01");
        assertIdentityConstraints("T_10_01_16", "T_10_01_16_UK_01", "T_10_01_16_FK_01");
    }

    @Test
    void transfersEmployeeAndResolvesTheAssignmentAsOfEachDate() {
        var hire = LocalDate.of(2026, 11, 1);
        var transfer = hire.plusDays(20);
        var endOfEmployment = hire.plusDays(40);
        var type = new OrganizationUnitType("OPS_UNIT", null, "Unité d’exploitation", "Operations unit");
        var root = new OrganizationUnit("OPS", null, null, "Operations", type, null);
        var origin = new OrganizationUnit("OPS_A", null, null, "Operations A", type, root);
        var destination = new OrganizationUnit("OPS_B", null, null, "Operations B", type, root);
        var engineer = new Position("STATION_ENGINEER", null, null, "Station Engineer");
        var employee = new Employee("SYN-ORG-01", null, null, "Sample", "Employee", hire, endOfEmployment);
        new TransactionTemplate(transactions).executeWithoutResult(transaction -> {
            entityManager.persist(type);
            entityManager.persist(root);
            entityManager.persist(origin);
            entityManager.persist(destination);
            entityManager.persist(engineer);
            entityManager.persist(employee);
        });

        employeeAssignments.assign(employee.getId(), origin, engineer, hire);
        assertThatThrownBy(() -> employeeAssignments.assign(employee.getId(), destination, engineer, hire.plusDays(1)))
                .isInstanceOf(IllegalStateException.class);
        employeeAssignments.transfer(employee.getId(), destination, engineer, transfer);

        var previous = employeeAssignments.findEffectiveOn(employee.getId(), transfer.minusDays(1)).orElseThrow();
        var current = employeeAssignments.findEffectiveOn(employee.getId(), transfer).orElseThrow();
        assertThat(previous.getOrganizationUnit().getId()).isEqualTo(origin.getId());
        assertThat(previous.getValidTo()).isEqualTo(transfer);
        assertThat(current.getOrganizationUnit().getId()).isEqualTo(destination.getId());
        assertThat(current.getPosition().getId()).isEqualTo(engineer.getId());
        assertThat(current.getValidTo()).isEqualTo(endOfEmployment);
        assertThat(employeeAssignments.findEffectiveOn(employee.getId(), endOfEmployment)).isEmpty();
        assertThat(employeeAssignments.findEffectiveOn(employee.getId(), hire.minusDays(1))).isEmpty();
        assertThat(jdbc.queryForObject("SELECT F_06 FROM T_10_02_04 WHERE F_00 = ?", Long.class,
                destination.getId())).isEqualTo(root.getId());
        assertIdentityColumns("T_10_02_05", "F_00", "F_01", "F_02", "F_03", "F_04", "F_05", "A_01");
        assertIdentityConstraints("T_10_02_05", "T_10_02_05_UK_01", "T_10_02_05_FK_01",
                "T_10_02_05_FK_02", "T_10_02_05_FK_03");
    }

    private void assertActorForeignKeys(String table, String grantedByColumn, String revokedByColumn) {
        assertThat(jdbc.queryForList("""
                SELECT CONCAT(CONSTRAINT_NAME, ':', COLUMN_NAME)
                FROM information_schema.KEY_COLUMN_USAGE
                WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?
                    AND BINARY REFERENCED_TABLE_NAME = BINARY 'T_10_01_07' AND REFERENCED_COLUMN_NAME = 'F_00'
                    AND COLUMN_NAME IN (?, ?)
                """, String.class, table, grantedByColumn, revokedByColumn))
                .containsExactlyInAnyOrder(table + "_FK_03:" + grantedByColumn, table + "_FK_04:" + revokedByColumn);
    }

    private void assertIdentityColumns(String table, String... columns) {
        assertThat(jdbc.queryForList(
                "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?",
                String.class, table)).containsExactlyInAnyOrder(columns);
    }

    private void assertIdentityConstraints(String table, String... constraints) {
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
        var authenticated = get("/api-docs", "hyflo-test:synthetic-test-password");
        assertThat(authenticated.statusCode()).isEqualTo(200);
        assertThat(authenticated.body()).contains("\"openapi\"");
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
