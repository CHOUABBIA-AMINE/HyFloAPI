/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityBootstrapIT
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Class
 * @Layer       : Test
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo
 *
 * @Description : Verifies HTTP first-use setup and internal company eligibility in isolated MySQL schemas.
 */

package dz.sh.hyflo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.jayway.jsonpath.JsonPath;
import dz.sh.hyflo.modules.foundation.identity.application.service.IdentityBootstrapService;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.PlatformRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RoleDefinitionEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.CapabilityReconciliationEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Group;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupMembership;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupDefinitionEvent;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import java.util.UUID;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "hyflo.setup.secret=ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj8=",
        "hyflo.setup.operator=http-test-operator"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IdentityBootstrapIT {

    private static final String SETUP_SECRET = "ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj8=";
    private static final String WRONG_SECRET = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8=";
    @LocalServerPort int port;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static final String SYNTHETIC_PASSWORD = "Synthetic-only-password-2026!";
    private static final List<String> EXPECTED_PERMISSIONS = List.of(
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
            "identity-catalog.read",
            "security-history.read");

    @SuppressWarnings("resource")
    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("hyflo_bootstrap_test")
            .withUsername("hyflo_test")
            .withPassword("synthetic-db-password")
            .withCommand("--log-bin-trust-function-creators=1");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @Autowired
    IdentityBootstrapService bootstrap;

    @Autowired
    IdentityPasswordEncoder passwordEncoder;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    PlatformTransactionManager transactions;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void initializesExactlyOnceWithCompanyScopeAndAtomicRollback() throws Exception {
        assertThat(applicationContext.getBeansOfType(PasswordEncoder.class)).isEmpty();
        assertEmptyIdentity();
        Fixture fixture = createOrganizationFixture();
        Map<String, String> alpha = configuration(fixture.companyId(), "alpha-admin", "operator-alpha");
        Map<String, String> beta = configuration(fixture.companyId(), "beta-admin", "operator-beta");

        assertThat(status(port).body()).isEqualTo("{\"available\":false}");
        assertThat(send(port, "/v1/setup/initialize", body("OTHER", "other-admin"), null, SETUP_SECRET)
                .statusCode()).isEqualTo(409);
        assertEmptyIdentity();
        var invalid = new HashMap<>(alpha);
        invalid.remove("HYFLO_BOOTSTRAP_PASSWORD");
        assertThatThrownBy(() -> initializeFixture(invalid)).isInstanceOf(IllegalArgumentException.class);
        invalid.put("HYFLO_BOOTSTRAP_PASSWORD", "short");
        assertThatThrownBy(() -> initializeFixture(invalid)).isInstanceOf(IllegalArgumentException.class);
        invalid.put("HYFLO_BOOTSTRAP_PASSWORD", "a".repeat(73));
        assertThatThrownBy(() -> initializeFixture(invalid)).isInstanceOf(IllegalArgumentException.class);
        invalid.put("HYFLO_BOOTSTRAP_PASSWORD", SYNTHETIC_PASSWORD);
        invalid.put("HYFLO_BOOTSTRAP_USERNAME", "HYFLO-BOOTSTRAP");
        assertThatThrownBy(() -> initializeFixture(invalid)).isInstanceOf(IllegalArgumentException.class);
        for (long invalidCompany : List.of(fixture.divisionId(), fixture.inactiveId(), fixture.expiredId(),
                fixture.futureId(), Long.MAX_VALUE)) {
            assertThatThrownBy(() -> initializeFixture(configuration(invalidCompany, "alpha-admin", "operator-alpha")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid or ineligible company organization-unit reference");
        }
        new TransactionTemplate(transactions).executeWithoutResult(transaction -> {
            entityManager.find(OrganizationUnitType.class, fixture.companyTypeId()).retire();
            entityManager.flush();
            assertThatThrownBy(() -> initializeFixture(alpha))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid or ineligible company organization-unit reference");
            transaction.setRollbackOnly();
        });
        assertEmptyIdentity();

        // A real database failure at the final grant insert must roll back every earlier write.
        jdbc.execute("CREATE TRIGGER hyflo_test_bootstrap_failure BEFORE INSERT ON T_10_01_15 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertThatThrownBy(() -> initializeFixture(alpha))
                    .isInstanceOf(RuntimeException.class)
                    .hasStackTraceContaining("synthetic rollback probe");
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_bootstrap_failure");
        }
        assertEmptyIdentity();

        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        long administratorId;
        try (var executor = Executors.newFixedThreadPool(2)) {
            var first = executor.submit(() -> attempt(alpha, ready, start));
            var second = executor.submit(() -> attempt(beta, ready, start));
            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();
            var results = List.of(first.get(60, TimeUnit.SECONDS), second.get(60, TimeUnit.SECONDS));
            var successes = results.stream().flatMap(Optional::stream).toList();
            assertThat(successes).hasSize(1);
            administratorId = successes.getFirst();
        }

        assertThat(codes("T_10_01_01")).containsExactly("LOCAL");
        assertThat(codes("T_10_01_02")).containsExactlyInAnyOrder("PENDING", "ACTIVE", "SUSPENDED", "DISABLED");
        assertThat(codes("T_10_01_07")).containsExactlyInAnyOrder("HUMAN", "SERVICE");
        assertThat(codes("T_10_01_04")).containsExactly("IDENTITY_ADMIN");
        assertThat(codes("T_10_01_09")).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
        assertThat(codes("T_10_01_05")).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS.stream()
                .map(code -> code.substring(code.indexOf('.') + 1)).distinct().toList());
        assertThat(codes("T_10_01_06")).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS.stream()
                .map(code -> code.substring(0, code.indexOf('.'))).distinct().toList());
        assertThat(jdbc.queryForList("SELECT p.F_01 FROM T_10_01_14 rp JOIN T_10_01_09 p ON p.F_00 = rp.F_02",
                String.class)).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
        assertThat(count("T_10_01_08")).isEqualTo(2L);
        assertThat(jdbc.queryForList("SELECT F_05 FROM T_10_01_08", Long.class)).containsOnly(fixture.companyId());
        assertThat(count("T_10_01_18")).isZero();
        assertThat(count("T_10_01_19")).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_08 WHERE F_03 IS NOT NULL", Long.class)).isZero();
        assertThat(count("T_10_01_10")).isEqualTo(1L);
        assertThat(count("T_10_01_15")).isEqualTo(1L);
        for (String suffix : List.of("03", "11", "12", "13", "16", "17", "18")) {
            assertThat(count("T_10_01_" + suffix)).isZero();
        }

        String storedHash = jdbc.queryForObject("SELECT F_01 FROM T_10_01_10 WHERE F_03 = ?", String.class, administratorId);
        assertThat(storedHash).startsWith("{bcrypt}").isNotEqualTo(SYNTHETIC_PASSWORD);
        assertThat(passwordEncoder.matches(SYNTHETIC_PASSWORD, storedHash)).isTrue();

        new TransactionTemplate(transactions).executeWithoutResult(transaction -> {
            UserRoleGrant grant = entityManager.createQuery("select g from UserRoleGrant g", UserRoleGrant.class).getSingleResult();
            assertThat(grant.getUser().getId()).isEqualTo(administratorId);
            assertThat(grant.getUser().getUserStatus().getCode()).isEqualTo("ACTIVE");
            assertThat(grant.getUser().getUserType().getCode()).isEqualTo("HUMAN");
            assertThat(grant.getUser().getEmployeeId()).isNull();
            assertThat(grant.getRole().getCode()).isEqualTo("IDENTITY_ADMIN");
            assertThat(grant.getRole().isEnabled()).isTrue();
            assertThat(grant.getAuthorizationScope().getScopeType()).isEqualTo("GLOBAL");
            assertThat(grant.getAuthorizationScope().getOrganizationUnitId()).isEqualTo(fixture.companyId());
            assertThat(grant.getAuthorizationScope().getScopeReferenceId()).isNull();
            String username = grant.getUser().getUsername();
            assertThat(username).isIn("alpha-admin", "beta-admin");
            assertThat(grant.getReason()).isEqualTo("IDN-01 trusted local initialization; operator="
                    + (username.equals("alpha-admin") ? "operator-alpha" : "operator-beta"));
            var actor = grant.getGrantedBy();
            assertThat(actor.getUsername()).isEqualTo("hyflo-bootstrap");
            assertThat(actor.getUserType().getCode()).isEqualTo("SERVICE");
            assertThat(actor.getUserStatus().getCode()).isEqualTo("DISABLED");
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_10 WHERE F_03 = ?", Long.class, actor.getId())).isZero();
            var active = entityManager.createQuery("select s from UserStatus s where s.code = 'ACTIVE'", UserStatus.class)
                    .getSingleResult();
            assertThatThrownBy(() -> actor.activate(null, active)).isInstanceOf(IllegalStateException.class);
            grant.revoke(Instant.now(), grant.getUser()); // synthetic test-only state change; no grant API exists.
        });

        assertThatThrownBy(() -> initializeFixture(alpha)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> initializeFixture(beta)).isInstanceOf(IllegalStateException.class);
        assertThat(count("T_10_01_08")).isEqualTo(2L);
        assertThat(count("T_10_01_15")).isEqualTo(1L);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_03 IS NOT NULL", Long.class)).isEqualTo(1L);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_10 WHERE F_03 = ?", String.class, administratorId))
                .isEqualTo(storedHash);
    }

    private long initializeFixture(Map<String, String> configuration) {
        return bootstrap.initialize(Long.parseLong(configuration.get("HYFLO_BOOTSTRAP_COMPANY_ID")),
                configuration.get("HYFLO_BOOTSTRAP_USERNAME"), configuration.get("HYFLO_BOOTSTRAP_EMAIL"),
                configuration.get("HYFLO_BOOTSTRAP_PASSWORD"), configuration.get("HYFLO_BOOTSTRAP_OPERATOR"));
    }

    private Optional<Long> attempt(Map<String, String> configuration, CountDownLatch ready, CountDownLatch start)
            throws InterruptedException {
        ready.countDown();
        if (!start.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Synthetic concurrency start timed out");
        }
        try {
            return Optional.of(initializeFixture(configuration));
        } catch (IllegalStateException | DataIntegrityViolationException | PessimisticLockingFailureException rejected) {
            return Optional.empty();
        }
    }

    private Map<String, String> configuration(long companyId, String username, String operator) {
        return Map.of("HYFLO_BOOTSTRAP_COMPANY_ID", Long.toString(companyId),
                "HYFLO_BOOTSTRAP_USERNAME", username, "HYFLO_BOOTSTRAP_EMAIL", username + "@example.invalid",
                "HYFLO_BOOTSTRAP_PASSWORD", SYNTHETIC_PASSWORD, "HYFLO_BOOTSTRAP_OPERATOR", operator);
    }

    @Test
    void initializesFreshCompanyThroughHttpAndCannotReopen() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        var available = status(port);
        assertThat(available.statusCode()).isEqualTo(200);
        assertThat(available.body()).isEqualTo("{\"available\":true}");
        assertThat(available.headers().firstValue("Cache-Control")).contains("no-store");

        String alpha = configuredBody("FIRST_ALPHA", "alpha-admin", "CHOSEN_ALPHA");
        String beta = configuredBody("FIRST_BETA", "beta-admin", "CHOSEN_BETA");
        for (String secret : List.of("", "bad-base64", WRONG_SECRET)) {
            assertSafe(send(port, "/v1/setup/initialize", alpha, null, secret), 403);
        }
        assertSafe(send(port, "/v1/setup/initialize", alpha, null), 403);
        assertSafe(send(port, "/v1/setup/initialize", alpha, null, SETUP_SECRET, SETUP_SECRET), 403);
        assertSafe(send(port, "/v1/setup/initialize", alpha.replace("FIRST_ALPHA", ""), null, SETUP_SECRET), 400);
        assertSafe(send(port, "/v1/setup/initialize", alpha.replace("Synthetic company", ""), null, SETUP_SECRET), 400);
        assertSafe(send(port, "/v1/setup/initialize", alpha.replace("alpha-admin", "hyflo-bootstrap"), null, SETUP_SECRET), 400);
        assertSafe(send(port, "/v1/setup/initialize", alpha.replace("alpha-admin@example.invalid", "not-an-email"), null, SETUP_SECRET), 400);
        assertSafe(send(port, "/v1/setup/initialize", alpha.replace(SYNTHETIC_PASSWORD, "short"), null, SETUP_SECRET), 400);
        assertSafe(send(port, "/v1/setup/initialize", alpha.replace(SYNTHETIC_PASSWORD, "é".repeat(37)), null, SETUP_SECRET), 400);
        assertEmptyIdentity();
        assertEmptyOrganization();

        jdbc.execute("CREATE TRIGGER hyflo_test_bootstrap_failure BEFORE INSERT ON T_10_01_15 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertSafe(send(port, "/v1/setup/initialize", alpha, null, SETUP_SECRET), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_bootstrap_failure");
        }
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertThat(status(port).body()).isEqualTo("{\"available\":true}");

        assertSafe(send(port, "/v1/setup/initialize",
                configuredBody("FIRST_ALPHA", "alpha-admin", "IDENTITY_ADMIN"), null, SETUP_SECRET), 400);
        assertEmptyIdentity();
        assertEmptyOrganization();
        jdbc.execute("CREATE TRIGGER hyflo_test_platform_failure BEFORE INSERT ON T_10_01_20 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertSafe(send(port, "/v1/setup/initialize", alpha, null, SETUP_SECRET), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_platform_failure");
        }
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertThat(status(port).body()).isEqualTo("{\"available\":true}");

        var first = client.sendAsync(request(port, "/v1/setup/initialize", alpha, null, SETUP_SECRET),
                HttpResponse.BodyHandlers.ofString());
        var second = client.sendAsync(request(port, "/v1/setup/initialize", beta, null, SETUP_SECRET),
                HttpResponse.BodyHandlers.ofString());
        var a = first.get(60, TimeUnit.SECONDS);
        var b = second.get(60, TimeUnit.SECONDS);
        assertThat(List.of(a.statusCode(), b.statusCode())).containsExactlyInAnyOrder(204, 409);
        assertSafe(a.statusCode() == 409 ? a : b, 409);
        var winner = a.statusCode() == 204 ? a : b;
        assertThat(winner.body()).isEmpty();
        assertThat(winner.headers().firstValue("Cache-Control")).contains("no-store");
        String username = a.statusCode() == 204 ? "alpha-admin" : "beta-admin";
        String companyCode = a.statusCode() == 204 ? "FIRST_ALPHA" : "FIRST_BETA";

        assertThat(codes("T_10_02_01")).containsExactly("COMPANY");
        assertThat(codes("T_10_02_06")).containsExactly("ACTIVE");
        assertThat(codes("T_10_02_11")).containsExactly(companyCode);
        long companyId = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_02_11 WHERE F_08 IS NULL AND F_06 IS NULL", Long.class)).isEqualTo(1L);
        assertThat(codes("T_10_01_01")).containsExactly("LOCAL");
        assertThat(codes("T_10_01_09")).containsExactlyInAnyOrderElementsOf(
                java.util.stream.Stream.concat(EXPECTED_PERMISSIONS.stream(),
                        java.util.stream.Stream.of("platform-role.read", "platform-role.create",
                                "platform-capability.reconcile", "platform-permission.read",
                                "platform-role-permission.associate", "platform-role-permission.remove",
                                "platform-role.rename", "platform-role.retire",
                        "platform-user-role-grant.read", "platform-user-role-grant.assign", "platform-user-role-grant.revoke",
                        "platform-group.read", "platform-group.create", "platform-group.rename", "platform-group.retire",
                        "platform-group-membership.read", "platform-group-membership.assign", "platform-group-membership.revoke",
                        "platform-group-role-grant.read", "platform-group-role-grant.assign", "platform-group-role-grant.revoke",
                        "platform-security-history.read")).toList());
        assertThat(count("T_10_01_20")).isEqualTo(1L);
        assertThat(count("T_10_01_21")).isZero();
        assertThat(jdbc.queryForList("SELECT F_01 FROM T_10_01_04 WHERE F_04=TRUE", String.class))
                .containsExactly(a.statusCode() == 204 ? "CHOSEN_ALPHA" : "CHOSEN_BETA");
        assertThat(jdbc.queryForList("SELECT F_02 FROM T_10_01_04 WHERE F_04=TRUE", String.class))
                .containsExactly("Installer chosen authority");
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var grant = entityManager.createQuery("select g from PlatformRoleGrant g", PlatformRoleGrant.class).getSingleResult();
            assertThat(grant.getUser().getUsername()).isEqualTo(username);
            assertThat(grant.getRole().isPlatformRole()).isTrue();
            assertThat(grant.getGrantedBy().getUsername()).isEqualTo("hyflo-bootstrap");
            assertThat(grant.getGrantedBy().getUserStatus().getCode()).isEqualTo("DISABLED");
            assertThat(grant.getCorrelationId()).isNotBlank();
            assertThat(grant.getReason()).contains("operator=http-test-operator");
            assertThat(grant.getValidTo()).isNull();
        });
        assertThat(count("T_10_01_08")).isEqualTo(2L);
        assertThat(count("T_10_01_10")).isEqualTo(1L);
        assertThat(count("T_10_01_15")).isEqualTo(1L);
        assertThat(jdbc.queryForList("SELECT F_05 FROM T_10_01_08", Long.class)).containsOnly(companyId);
        assertThat(passwordEncoder.matches(SYNTHETIC_PASSWORD,
                jdbc.queryForObject("SELECT F_01 FROM T_10_01_10", String.class))).isTrue();
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var grant = entityManager.createQuery("select g from UserRoleGrant g", UserRoleGrant.class).getSingleResult();
            assertThat(grant.getUser().getUsername()).isEqualTo(username);
            assertThat(grant.getAuthorizationScope().getOrganizationUnitId()).isEqualTo(companyId);
            assertThat(grant.getGrantedBy().getUsername()).isEqualTo("hyflo-bootstrap");
            assertThat(grant.getGrantedBy().getUserStatus().getCode()).isEqualTo("DISABLED");
            assertThat(grant.getReason()).isEqualTo("IDN-01 trusted local initialization; operator=http-test-operator");
            assertThat(grant.getGrantedBy().getUserType().getCode()).isEqualTo("SERVICE");
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_10 WHERE F_03=?", Long.class,
                    grant.getGrantedBy().getId())).isZero();
        });
        assertThat(status(port).body()).isEqualTo("{\"available\":false}");
        assertSafe(send(port, "/v1/setup/initialize", alpha, null, SETUP_SECRET), 409);
        assertSafe(send(port, "/v1/setup/initialize", alpha, null, WRONG_SECRET), 403);

        String login = "{\"username\":\"" + username + "\",\"password\":\"" + SYNTHETIC_PASSWORD + "\"}";
        var authenticated = send(port, "/v1/auth/login", login, null);
        assertThat(authenticated.statusCode()).isEqualTo(200);
        String access = JsonPath.read(authenticated.body(), "$.accessToken");
        var permissions = send(port, "/v1/me/permissions?companyId=" + companyId, null, access);
        assertThat(permissions.statusCode()).isEqualTo(200);
        List<String> companyPermissions = JsonPath.read(permissions.body(), "$.permissionCodes");
        assertThat(companyPermissions).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
        assertThat(send(port, "/v1/platform/roles", null, access).statusCode()).isEqualTo(200);
        var openapi = send(port, "/api-docs", null, access);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String, Object> paths = JsonPath.read(openapi.body(), "$.paths");
        assertThat(paths.keySet().stream().filter(path -> path.startsWith("/v1/setup")).toList())
                .containsExactlyInAnyOrder("/v1/setup/status", "/v1/setup/initialize");
        assertThat(openapi.body()).doesNotContain("--identity-bootstrap");
        assertThat(paths.get("/v1/setup/initialize").toString()).contains("X-Hyflo-Setup-Secret").doesNotContain("companyUnitId");

        // A fresh application context validates the same persisted schema, never recreates it.
        try (var restarted = new SpringApplicationBuilder(HyFloApi.class).profiles("test").run(
                "--server.port=0", "--spring.jpa.hibernate.ddl-auto=validate",
                "--spring.datasource.url=" + MYSQL.getJdbcUrl(),
                "--spring.datasource.username=" + MYSQL.getUsername(),
                "--spring.datasource.password=" + MYSQL.getPassword(),
                "--hyflo.setup.secret=" + WRONG_SECRET, "--hyflo.setup.operator=second-test-operator")) {
            int secondPort = restarted.getEnvironment().getRequiredProperty("local.server.port", Integer.class);
            assertThat(status(secondPort).body()).isEqualTo("{\"available\":false}");
            assertSafe(send(secondPort, "/v1/setup/initialize", beta, null, WRONG_SECRET), 409);
            var restartedLogin = send(secondPort, "/v1/auth/login", login, null);
            assertThat(restartedLogin.statusCode()).isEqualTo(200);
            String restartedAccess = JsonPath.read(restartedLogin.body(), "$.accessToken");
            assertThat(send(secondPort, "/v1/platform/roles", null, restartedAccess).statusCode()).isEqualTo(200);
        }
        assertThat(count("T_10_02_11")).isEqualTo(1L);
        assertThat(count("T_10_01_08")).isEqualTo(2L);
        assertThat(count("T_10_01_15")).isEqualTo(1L);
    }

    @Test
    void createsRuntimeRoleDefinitionsAndEnforcesIndependentLiveAuthority() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize", body("RUNTIME_COMPANY", "runtime-admin"), null, SETUP_SECRET), 204);
        long companyId = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long administratorId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='runtime-admin'", Long.class);
        long companyRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=FALSE", Long.class);
        long platformRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=TRUE", Long.class);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_04 WHERE F_00=?", String.class, platformRole))
                .isEqualTo("INITIAL_PLATFORM_ADMIN");
        long initialGrant = jdbc.queryForObject("SELECT F_00 FROM T_10_01_15", Long.class);
        String access = loginAccess("runtime-admin");
        String endpoint = "/v1/platform/roles";
        assertSafe(send(port, endpoint, null, null), 401);
        assertSafe(send(port, endpoint, null, "invalid.jwt"), 401);
        assertSafe(send(port, endpoint + "?afterId=-1", null, access), 400);
        assertSafe(send(port, endpoint, "{\"code\":\"MISSING_SCOPE\",\"displayName\":\"Role\",\"reason\":\"test\"}", access), 400);
        assertSafe(send(port, endpoint, roleBody("bad code", true), access), 400);

        var first = send(port, endpoint, roleBody("USER_DEFINED_PLATFORM", true), access);
        var second = send(port, endpoint, roleBody("USER_DEFINED_COMPANY", false), access);
        assertSafe(first, 201);
        assertSafe(second, 201);
        Number firstRoleId = JsonPath.read(first.body(), "$.id");
        Number secondRoleId = JsonPath.read(second.body(), "$.id");
        assertThat((Boolean) JsonPath.read(first.body(), "$.platformRole")).isTrue();
        assertThat((Boolean) JsonPath.read(second.body(), "$.platformRole")).isFalse();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_14 WHERE F_01 IN (?,?)",
                Long.class, firstRoleId.longValue(), secondRoleId.longValue())).isZero();
        assertThat(count("T_10_01_15")).isEqualTo(1L);
        assertThat(count("T_10_01_20")).isEqualTo(1L);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var evidence = entityManager.createQuery("select e from RoleDefinitionEvent e order by e.id",
                    RoleDefinitionEvent.class).getResultList();
            assertThat(evidence).hasSize(2);
            assertThat(evidence.getFirst().getRoleCode()).isEqualTo("USER_DEFINED_PLATFORM");
            assertThat(evidence.getFirst().getEventCode()).isEqualTo("CREATED");
            assertThat(evidence.getFirst().getPerformedBy().getId()).isEqualTo(administratorId);
            assertThat(evidence.getFirst().getReason()).isEqualTo("synthetic runtime role");
            assertThat(evidence.getFirst().getCorrelationId()).isNotBlank();
            assertThat(evidence.getFirst().getOccurredAt()).isNotNull();
            assertThat(evidence.getFirst().isPlatformRole()).isTrue();
            assertThat(evidence.getLast().isPlatformRole()).isFalse();
        });
        assertSafe(send(port, endpoint, roleBody("USER_DEFINED_PLATFORM", false), access), 409);
        assertThat(count("T_10_01_21")).isEqualTo(2L);
        jdbc.execute("CREATE TRIGGER hyflo_test_role_event_failure BEFORE INSERT ON T_10_01_21 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertSafe(send(port, endpoint, roleBody("ROLLBACK_ROLE", true), access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_role_event_failure");
        }
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_04 WHERE F_01='ROLLBACK_ROLE'", Long.class)).isZero();
        assertThat(count("T_10_01_21")).isEqualTo(2L);
        var raceA = client.sendAsync(request(port, endpoint, roleBody("RACE_ROLE", false), access),
                HttpResponse.BodyHandlers.ofString());
        var raceB = client.sendAsync(request(port, endpoint, roleBody("RACE_ROLE", true), access),
                HttpResponse.BodyHandlers.ofString());
        assertThat(List.of(raceA.get(60, TimeUnit.SECONDS).statusCode(), raceB.get(60, TimeUnit.SECONDS).statusCode()))
                .containsExactlyInAnyOrder(201, 409);
        assertThat(count("T_10_01_21")).isEqualTo(3L);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            for (int i = 0; i < 101; i++) entityManager.persist(new Role("PAGE_ROLE_" + i, "Synthetic page " + i));
        });
        var page = send(port, endpoint, null, access);
        assertSafe(page, 200);
        List<Number> ids = JsonPath.read(page.body(), "$[*].id");
        assertThat(ids).hasSize(100);
        assertThat(ids.stream().map(Number::longValue).toList()).isSorted();
        var next = send(port, endpoint + "?afterId=" + ids.getLast(), null, access);
        assertSafe(next, 200);
        List<Number> remaining = JsonPath.read(next.body(), "$[*].id");
        assertThat(remaining).isNotEmpty();
        assertThat(remaining.stream().map(Number::longValue).toList()).isSorted()
                .allSatisfy(id -> assertThat(id).isGreaterThan(ids.getLast().longValue()));
        assertThat(send(port, endpoint + "?afterId=" + remaining.getLast(), null, access).body()).isEqualTo("[]");
        var openapi = send(port, "/api-docs", null, access);
        Map<String, Object> paths = JsonPath.read(openapi.body(), "$.paths");
        assertThat(paths.keySet().stream().filter(p -> p.startsWith("/v1/platform")).toList())
                .containsExactlyInAnyOrder(endpoint, "/v1/platform/capabilities/reconcile", "/v1/platform/permissions",
                        "/v1/platform/roles/{roleId}/permissions",
                        "/v1/platform/roles/{roleId}/permissions/{permissionId}/associate",
                        "/v1/platform/roles/{roleId}/permissions/{permissionId}/remove",
                        "/v1/platform/roles/{roleId}/rename", "/v1/platform/roles/{roleId}/retire",
                        "/v1/platform/groups", "/v1/platform/groups/{groupId}", "/v1/platform/groups/{groupId}/rename",
                        "/v1/platform/groups/{groupId}/retire", "/v1/platform/groups/{groupId}/memberships",
                        "/v1/platform/groups/{groupId}/memberships/{membershipId}/revoke",
                        "/v1/platform/groups/{groupId}/role-grants", "/v1/platform/groups/{groupId}/role-grants/{grantId}/revoke",
                        "/v1/platform/security-history/role-definitions", "/v1/platform/security-history/group-definitions",
                        "/v1/platform/security-history/capability-reconciliations", "/v1/platform/security-history/unattributed-authentication");
        assertThat(paths.get(endpoint).toString()).contains("get=", "post=");
        assertSafe(send(port, endpoint + "/unregistered", null, access), 403);

        long companyOnlyId = new TransactionTemplate(transactions).execute(tx -> {
            User admin = entityManager.find(User.class, administratorId);
            var pending = entityManager.createQuery("select s from UserStatus s where s.code='PENDING'", UserStatus.class).getSingleResult();
            var active = entityManager.createQuery("select s from UserStatus s where s.code='ACTIVE'", UserStatus.class).getSingleResult();
            User companyOnly = new User(admin.getIdentityProvider(), "company-only", "company-only@example.invalid",
                    null, companyId, pending, admin.getUserType());
            entityManager.persist(companyOnly);
            LocalCredential credential = new LocalCredential(companyOnly, passwordEncoder.encode(SYNTHETIC_PASSWORD), Instant.now());
            entityManager.persist(credential);
            companyOnly.activate(credential, active);
            entityManager.persist(new UserRoleGrant(companyOnly, entityManager.find(Role.class, companyRole),
                    AuthorizationScope.global(companyId), "synthetic fixture", Instant.now().minusSeconds(1), null, admin));
            return companyOnly.getId();
        });
        String companyAccess = loginAccess("company-only");
        assertSafe(send(port, endpoint, null, companyAccess), 403);
        assertSafe(send(port, endpoint, roleBody("COMPANY_FORBIDDEN", false), companyAccess), 403);
        long readPermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='platform-role.read'", Long.class);
        long createPermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='platform-role.create'", Long.class);
        // Synthetic corruption: the database link alone cannot confer authority.
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", companyRole, readPermission);
        try {
            var own = send(port, "/v1/me/permissions?companyId=" + companyId, null, companyAccess);
            List<String> ownCodes = JsonPath.read(own.body(), "$.permissionCodes");
            assertThat(ownCodes).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
            assertSafe(send(port, endpoint, null, companyAccess), 403);
        } finally {
            jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", companyRole, readPermission);
        }
        long companyOnlyGrant = jdbc.queryForObject("SELECT F_00 FROM T_10_01_15 WHERE F_08=?", Long.class, companyOnlyId);
        jdbc.update("UPDATE T_10_01_15 SET F_09=? WHERE F_00=?", platformRole, companyOnlyGrant);
        try {
            assertSafe(send(port, "/v1/me/permissions?companyId=" + companyId, null, companyAccess), 403);
            assertSafe(send(port, endpoint, null, companyAccess), 403);
        } finally {
            jdbc.update("UPDATE T_10_01_15 SET F_09=? WHERE F_00=?", companyRole, companyOnlyGrant);
        }

        // All company mutation paths deny a platform-managed target before changing it.
        assertProtectedTarget(companyId, administratorId, companyRole, initialGrant, companyAccess);
        String storedHash = jdbc.queryForObject("SELECT F_01 FROM T_10_01_10 WHERE F_03=?", String.class, administratorId);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", platformRole, createPermission);
        assertSafe(send(port, endpoint, roleBody("DENIED_CREATE", true), access), 403);
        assertSafe(send(port, endpoint, null, access), 200);
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", platformRole, createPermission);
        jdbc.update("UPDATE T_10_01_04 SET F_03=FALSE WHERE F_00=?", platformRole);
        assertSafe(send(port, endpoint, null, access), 403);
        jdbc.update("UPDATE T_10_01_04 SET F_03=TRUE WHERE F_00=?", platformRole);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", platformRole, readPermission);
        assertSafe(send(port, endpoint, null, access), 403);
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", platformRole, readPermission);

        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var grant = entityManager.find(UserRoleGrant.class, initialGrant);
            grant.revoke(Instant.now(), grant.getUser()); // synthetic: isolate the platform-only authority
        });
        assertSafe(send(port, "/v1/me/permissions?companyId=" + companyId, null, access), 403);
        assertSafe(send(port, endpoint, null, access), 200);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var grant = entityManager.createQuery("select g from PlatformRoleGrant g", PlatformRoleGrant.class).getSingleResult();
            grant.revoke(Instant.now(), grant.getUser(), "synthetic revocation", UUID.randomUUID().toString());
        });
        assertSafe(send(port, endpoint, null, access), 403);
        assertSafe(send(port, endpoint, roleBody("REVOKED_FORBIDDEN", true), access), 403);
        assertProtectedTarget(companyId, administratorId, companyRole, initialGrant, companyAccess);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_10 WHERE F_03=?", String.class, administratorId))
                .isEqualTo(storedHash);
        assertThat(count("T_10_01_18")).isZero();
        assertThat(count("T_10_01_19")).isZero();
        assertThat(count("T_10_01_21")).isEqualTo(3L);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_20 WHERE F_03 IS NOT NULL AND F_06 IS NOT NULL "
                + "AND F_07 IS NOT NULL AND F_11 IS NOT NULL", Long.class)).isEqualTo(1L);
    }

    @Test
    void reconcilesOnlySupportedDefinitionsThroughAuthorizedHttp() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize",
                configuredBody("RECONCILE_COMPANY", "reconcile-admin", "CHOSEN_RECONCILER"), null, SETUP_SECRET), 204);
        String endpoint = "/v1/platform/capabilities/reconcile";
        String requestBody = "{\"reason\":\"synthetic capability synchronization\"}";
        long companyId = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long adminId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='reconcile-admin'", Long.class);
        long companyRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=FALSE", Long.class);
        long platformRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_01='CHOSEN_RECONCILER'", Long.class);
        long capability = permissionId("platform-capability.reconcile");
        assertThat(jdbc.queryForList("SELECT p.F_01 FROM T_10_01_14 rp JOIN T_10_01_09 p ON p.F_00=rp.F_02 "
                + "WHERE rp.F_01=?", String.class, companyRole)).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
        assertThat(jdbc.queryForList("SELECT p.F_01 FROM T_10_01_14 rp JOIN T_10_01_09 p ON p.F_00=rp.F_02 "
                + "WHERE rp.F_01=?", String.class, platformRole)).containsExactlyInAnyOrder(
                        "platform-role.read", "platform-role.create", "platform-capability.reconcile",
                        "platform-permission.read", "platform-role-permission.associate", "platform-role-permission.remove",
                        "platform-role.rename", "platform-role.retire",
                        "platform-user-role-grant.read", "platform-user-role-grant.assign", "platform-user-role-grant.revoke",
                        "platform-group.read", "platform-group.create", "platform-group.rename", "platform-group.retire",
                        "platform-group-membership.read", "platform-group-membership.assign", "platform-group-membership.revoke",
                        "platform-group-role-grant.read", "platform-group-role-grant.assign", "platform-group-role-grant.revoke",
                        "platform-security-history.read");
        String access = loginAccess("reconcile-admin");
        assertSafe(send(port, endpoint, requestBody, null), 401);
        assertSafe(send(port, endpoint, requestBody, "invalid.jwt"), 401);
        assertSafe(send(port, endpoint, "{}", access), 400);
        assertSafe(send(port, endpoint, "{\"reason\":\" \"}", access), 400);
        assertSafe(send(port, endpoint, "{\"reason\":\"" + "x".repeat(256) + "\"}", access), 400);
        assertSafe(send(port, endpoint, "{", access), 400);
        assertSafe(send(port, endpoint, null, access), 403);
        assertSafe(send(port, endpoint + "/unregistered", requestBody, access), 403);
        assertThat(count("T_10_01_22")).isZero();

        var initial = send(port, endpoint, requestBody, access);
        assertSafe(initial, 200);
        String manifestId = JsonPath.read(initial.body(), "$.manifestId");
        String digest = JsonPath.read(initial.body(), "$.manifestDigest");
        assertThat(manifestId).isEqualTo("hyflo-identity-capabilities-v7");
        assertThat(digest).matches("[0-9a-f]{64}");
        assertInserted(initial, List.of(), List.of(), List.of());
        var repeat = send(port, endpoint, requestBody, access);
        assertSafe(repeat, 200);
        assertThat(repeat.body()).isEqualTo(initial.body());
        assertThat(count("T_10_01_22")).isEqualTo(2L);

        // Only disposable test data is altered to simulate a missing definition after an upgrade.
        long readAction = jdbc.queryForObject("SELECT F_00 FROM T_10_01_05 WHERE F_01='read'", Long.class);
        long userResource = jdbc.queryForObject("SELECT F_00 FROM T_10_01_06 WHERE F_01='user'", Long.class);
        jdbc.update("INSERT INTO T_10_01_05(F_01,F_02) VALUES ('synthetic-only','Unrelated action')");
        jdbc.update("INSERT INTO T_10_01_06(F_01,F_02) VALUES ('synthetic-only','Unrelated resource')");
        jdbc.update("INSERT INTO T_10_01_09(F_01,F_02,F_03,F_04) VALUES ('synthetic-only.synthetic-only','Unrelated definition',"
                + "(SELECT F_00 FROM T_10_01_05 WHERE F_01='synthetic-only'),"
                + "(SELECT F_00 FROM T_10_01_06 WHERE F_01='synthetic-only'))");
        long unrelated = permissionId("synthetic-only.synthetic-only");
        removeDefinition("credential.initialize");
        removeDefinition("identity-catalog.read");
        removeDefinition("user.read");
        jdbc.update("DELETE FROM T_10_01_05 WHERE F_01='initialize'");
        jdbc.update("DELETE FROM T_10_01_06 WHERE F_01='identity-catalog'");
        long linksBefore = count("T_10_01_14");
        long companyGrantsBefore = count("T_10_01_15");
        long platformGrantsBefore = count("T_10_01_20");
        var repaired = send(port, endpoint, requestBody, access);
        assertSafe(repaired, 200);
        assertInserted(repaired, List.of("initialize"), List.of("identity-catalog"),
                List.of("credential.initialize", "identity-catalog.read", "user.read"));
        assertThat((String) JsonPath.read(repaired.body(), "$.manifestDigest")).isEqualTo(digest);
        assertThat(jdbc.queryForObject("SELECT F_03 FROM T_10_01_09 WHERE F_01='user.read'", Long.class)).isEqualTo(readAction);
        assertThat(jdbc.queryForObject("SELECT F_04 FROM T_10_01_09 WHERE F_01='user.read'", Long.class)).isEqualTo(userResource);
        assertThat(count("T_10_01_14")).isEqualTo(linksBefore);
        assertThat(count("T_10_01_15")).isEqualTo(companyGrantsBefore);
        assertThat(count("T_10_01_20")).isEqualTo(platformGrantsBefore);
        assertThat(permissionId("synthetic-only.synthetic-only")).isEqualTo(unrelated);
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_09 WHERE F_00=?", String.class, unrelated))
                .isEqualTo("Unrelated definition");

        // Exercise cross-account serialization, using a second synthetic platform grant, not a new public provisioning API.
        long secondId = new TransactionTemplate(transactions).execute(tx -> {
            User admin = entityManager.find(User.class, adminId);
            User other = new User(admin.getIdentityProvider(), "second-reconciler", "second@example.invalid",
                    null, companyId, entityManager.createQuery("select s from UserStatus s where s.code='PENDING'",
                            UserStatus.class).getSingleResult(), admin.getUserType());
            entityManager.persist(other);
            LocalCredential credential = new LocalCredential(other, passwordEncoder.encode(SYNTHETIC_PASSWORD), Instant.now());
            entityManager.persist(credential);
            other.activate(credential, entityManager.createQuery("select s from UserStatus s where s.code='ACTIVE'",
                    UserStatus.class).getSingleResult());
            entityManager.persist(new PlatformRoleGrant(other, entityManager.find(Role.class, platformRole),
                    Instant.now().minusSeconds(1), null, "synthetic second actor", UUID.randomUUID().toString(), admin));
            return other.getId();
        });
        String secondAccess = loginAccess("second-reconciler");
        removeDefinition("credential.initialize");
        jdbc.update("DELETE FROM T_10_01_05 WHERE F_01='initialize'");
        long raceLinks = count("T_10_01_14");
        var raceA = client.sendAsync(request(port, endpoint, requestBody, access), HttpResponse.BodyHandlers.ofString());
        var raceB = client.sendAsync(request(port, endpoint, requestBody, secondAccess), HttpResponse.BodyHandlers.ofString());
        var a = raceA.get(60, TimeUnit.SECONDS);
        var b = raceB.get(60, TimeUnit.SECONDS);
        assertSafe(a, 200);
        assertSafe(b, 200);
        List<String> aCodes = JsonPath.read(a.body(), "$.insertedPermissionCodes");
        List<String> bCodes = JsonPath.read(b.body(), "$.insertedPermissionCodes");
        assertThat(List.of(aCodes, bCodes)).containsExactlyInAnyOrder(List.of("credential.initialize"), List.of());
        assertInserted(aCodes.isEmpty() ? a : b, List.of(), List.of(), List.of());
        assertInserted(aCodes.isEmpty() ? b : a, List.of("initialize"), List.of(), List.of("credential.initialize"));
        assertThat(count("T_10_01_14")).isEqualTo(raceLinks);
        assertThat(count("T_10_01_22")).isEqualTo(5L);
        assertThat(jdbc.queryForList("SELECT F_09 FROM T_10_01_22 ORDER BY F_00 DESC LIMIT 2", Long.class))
                .containsExactlyInAnyOrder(adminId, secondId);

        // All drift probes occur after an earlier missing manifest definition, so 409 must roll it back.
        removeDefinition("credential.initialize");
        jdbc.update("DELETE FROM T_10_01_05 WHERE F_01='initialize'");
        long suspendPermission = permissionId("user.suspend");
        String suspendLabel = jdbc.queryForObject("SELECT F_02 FROM T_10_01_09 WHERE F_00=?", String.class, suspendPermission);
        jdbc.update("UPDATE T_10_01_09 SET F_02='Drifted label' WHERE F_00=?", suspendPermission);
        assertReconcileDrift(endpoint, requestBody, access, 5L);
        jdbc.update("UPDATE T_10_01_09 SET F_02=? WHERE F_00=?", suspendLabel, suspendPermission);
        jdbc.update("UPDATE T_10_01_09 SET F_01='USER.suspend' WHERE F_00=?", suspendPermission);
        assertReconcileDrift(endpoint, requestBody, access, 5L);
        jdbc.update("UPDATE T_10_01_09 SET F_01='synthetic.suspend' WHERE F_00=?", suspendPermission);
        assertReconcileDrift(endpoint, requestBody, access, 5L);
        jdbc.update("UPDATE T_10_01_09 SET F_01='user.suspend' WHERE F_00=?", suspendPermission);
        long suspendAction = jdbc.queryForObject("SELECT F_03 FROM T_10_01_09 WHERE F_00=?", Long.class, suspendPermission);
        long unrelatedAction = jdbc.queryForObject("SELECT F_00 FROM T_10_01_05 WHERE F_01='synthetic-only'", Long.class);
        jdbc.update("UPDATE T_10_01_09 SET F_03=? WHERE F_00=?", unrelatedAction, suspendPermission);
        assertReconcileDrift(endpoint, requestBody, access, 5L);
        jdbc.update("UPDATE T_10_01_09 SET F_03=? WHERE F_00=?", suspendAction, suspendPermission);

        jdbc.execute("CREATE TRIGGER hyflo_test_reconcile_event_failure BEFORE INSERT ON T_10_01_22 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertSafe(send(port, endpoint, requestBody, access), 503);
            assertMissingInitialization(5L);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_reconcile_event_failure");
        }
        var finalRepair = send(port, endpoint, requestBody, access);
        assertSafe(finalRepair, 200);
        assertInserted(finalRepair, List.of("initialize"), List.of(), List.of("credential.initialize"));

        // A live company-only account and a platform role without the exact permission must both fail closed.
        long companyOnlyId = new TransactionTemplate(transactions).execute(tx -> {
            User admin = entityManager.find(User.class, adminId);
            User other = new User(admin.getIdentityProvider(), "company-reconciler", "company@example.invalid",
                    null, companyId, entityManager.createQuery("select s from UserStatus s where s.code='PENDING'",
                            UserStatus.class).getSingleResult(), admin.getUserType());
            entityManager.persist(other);
            LocalCredential credential = new LocalCredential(other, passwordEncoder.encode(SYNTHETIC_PASSWORD), Instant.now());
            entityManager.persist(credential);
            other.activate(credential, entityManager.createQuery("select s from UserStatus s where s.code='ACTIVE'",
                    UserStatus.class).getSingleResult());
            entityManager.persist(new UserRoleGrant(other, entityManager.find(Role.class, companyRole),
                    AuthorizationScope.global(companyId), "synthetic fixture", Instant.now().minusSeconds(1), null, admin));
            return other.getId();
        });
        assertThat(companyOnlyId).isPositive();
        assertSafe(send(port, endpoint, requestBody, loginAccess("company-reconciler")), 403);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", platformRole, capability);
        assertSafe(send(port, endpoint, requestBody, access), 403);
        assertSafe(send(port, "/v1/platform/roles", null, access), 200);
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", platformRole, capability);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var grant = entityManager.createQuery("select g from PlatformRoleGrant g where g.user.id=:id",
                    PlatformRoleGrant.class).setParameter("id", secondId).getSingleResult();
            grant.revoke(Instant.now(), grant.getUser(), "synthetic revocation", UUID.randomUUID().toString());
        });
        assertSafe(send(port, endpoint, requestBody, secondAccess), 403);
        assertThat(count("T_10_01_22")).isEqualTo(6L);
        var openapi = send(port, "/api-docs", null, access);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String, Object> paths = JsonPath.read(openapi.body(), "$.paths");
        assertThat(paths.get(endpoint).toString()).contains("post=").doesNotContain("get=");
        assertSafe(send(port, "/v1/auth/logout", "", access), 204);
        assertSafe(send(port, endpoint, requestBody, access), 401);

        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var events = entityManager.createQuery("select e from CapabilityReconciliationEvent e order by e.id",
                    CapabilityReconciliationEvent.class).getResultList();
            assertThat(events).hasSize(6).allSatisfy(event -> {
                assertThat(event.getManifestId()).isEqualTo(manifestId);
                assertThat(event.getManifestDigest()).isEqualTo(digest);
                assertThat(event.getReason()).isEqualTo("synthetic capability synchronization");
                assertThat(event.getOccurredAt()).isNotNull();
                assertThat(event.getCorrelationId()).matches("[0-9a-f-]{36}");
                assertThat(event.getPerformedBy().getId()).isIn(adminId, secondId);
            });
            assertThat(events.getFirst().getInsertedPermissionCodes()).isEmpty();
            assertThat(events.get(2).getInsertedActionCodes()).isEqualTo("initialize");
            assertThat(events.get(2).getInsertedResourceTypeCodes()).isEqualTo("identity-catalog");
            assertThat(events.get(2).getInsertedPermissionCodes()).isEqualTo("credential.initialize\nidentity-catalog.read\nuser.read");
            assertThat(events.stream().map(CapabilityReconciliationEvent::getCorrelationId).distinct().count()).isEqualTo(6L);
        });
        assertThat(count("T_10_01_09")).isEqualTo(41L); // forty supported, one unrelated synthetic definition
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_09", Long.class)).isEqualTo(
                jdbc.queryForObject("SELECT COUNT(DISTINCT F_03,F_04) FROM T_10_01_09", Long.class));
    }

    private long permissionId(String code) {
        return jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01=?", Long.class, code);
    }

    private void removeDefinition(String code) {
        long id = permissionId(code);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_02=?", id);
        jdbc.update("DELETE FROM T_10_01_09 WHERE F_00=?", id);
    }

    private void assertInserted(HttpResponse<String> response, List<String> actions,
            List<String> resources, List<String> permissions) {
        assertThat((List<String>) JsonPath.read(response.body(), "$.insertedActionCodes")).containsExactlyElementsOf(actions);
        assertThat((List<String>) JsonPath.read(response.body(), "$.insertedResourceTypeCodes")).containsExactlyElementsOf(resources);
        assertThat((List<String>) JsonPath.read(response.body(), "$.insertedPermissionCodes")).containsExactlyElementsOf(permissions);
    }

    private void assertReconcileDrift(String endpoint, String requestBody, String access, long eventCount) throws Exception {
        var rejected = send(port, endpoint, requestBody, access);
        assertSafe(rejected, 409);
        assertThat(rejected.body()).contains("CAPABILITY_DEFINITION_CONFLICT")
                .doesNotContain("user.suspend", "Drifted label");
        assertMissingInitialization(eventCount);
    }

    private void assertMissingInitialization(long eventCount) {
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_05 WHERE F_01='initialize'", Long.class)).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_09 WHERE F_01='credential.initialize'", Long.class)).isZero();
        assertThat(count("T_10_01_22")).isEqualTo(eventCount);
    }

    @Test
    void configuresRolePermissionsWithLiveAuthorityAndPreservesDelegation() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("COMPOSITION", "composition-admin",
                "INSTALLER_AUTHORITY"), null, SETUP_SECRET), 204);
        long companyId = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long adminId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='composition-admin'", Long.class);
        long companyAdminRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=FALSE", Long.class);
        long platformAdminRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=TRUE", Long.class);
        String access = loginAccess("composition-admin");
        String catalog = "/v1/platform/permissions";
        String reason = "{\"reason\":\"synthetic role composition\"}";
        long read = permissionId("user.read");
        long catalogRead = permissionId("identity-catalog.read");
        long roleRead = permissionId("role.read");
        long permissionRead = permissionId("permission.read");
        long platformRead = permissionId("platform-role.read");
        long platformCatalogRead = permissionId("platform-permission.read");
        long associate = permissionId("platform-role-permission.associate");
        long remove = permissionId("platform-role-permission.remove");
        long companyRole = createRuntimeRole("CONFIGURED_COMPANY", false, access);
        long platformRole = createRuntimeRole("CONFIGURED_PLATFORM", true, access);
        String bundle = "/v1/platform/roles/" + companyRole + "/permissions";
        assertSafe(send(port, catalog, null, null), 401);
        assertSafe(send(port, catalog, null, "invalid.jwt"), 401);
        assertSafe(send(port, catalog + "?afterId=-1", null, access), 400);
        assertSafe(send(port, catalog + "?afterId=bad", null, access), 400);
        assertSafe(send(port, "/v1/platform/roles/0/permissions", null, access), 400);
        assertSafe(send(port, "/v1/platform/roles/" + Long.MAX_VALUE + "/permissions", null, access), 403);
        assertThat(send(port, bundle, null, access).body()).isEqualTo("[]");
        assertThat(send(port, "/v1/platform/roles/" + platformRole + "/permissions", null, access).body()).isEqualTo("[]");
        var initial = send(port, catalog, null, access);
        assertSafe(initial, 200);
        List<Map<String, Object>> definitions = JsonPath.read(initial.body(), "$");
        assertThat(definitions).hasSize(40).allSatisfy(entry -> {
            assertThat(entry).containsOnlyKeys("id", "code", "displayName", "actionCode", "resourceTypeCode",
                    "platformPermission", "supported");
            assertThat(entry.get("supported")).isEqualTo(true);
            assertThat(entry.get("code")).isEqualTo(entry.get("resourceTypeCode") + "." + entry.get("actionCode"));
            assertThat(entry.get("platformPermission")).isEqualTo(entry.get("code").toString().startsWith("platform-"));
        });

        // Synthetic unsupported and malformed rows remain visible but cannot be added.
        long[] unsupported = new TransactionTemplate(transactions).execute(tx -> {
            var action = new Action("synthetic-compose", "Synthetic compose");
            entityManager.persist(action);
            var resource = new ResourceType("synthetic-compose", "Synthetic compose");
            entityManager.persist(resource);
            var permission = new Permission("synthetic-compose.synthetic-compose", "Unsupported", action, resource);
            entityManager.persist(permission);
            var other = new ResourceType("malformed-compose", "Malformed compose");
            entityManager.persist(other);
            var malformed = new Permission("wrong.synthetic-compose", "Malformed", action, other);
            entityManager.persist(malformed);
            // Extra definitions exercise the real 100-row cursor boundary.
            for (int i = 0; i < 101; i++) {
                var pageResource = new ResourceType("synthetic-page-" + i, "Synthetic page");
                entityManager.persist(pageResource);
                entityManager.persist(new Permission(pageResource.getCode() + ".synthetic-compose", "Synthetic page", action, pageResource));
            }
            return new long[]{permission.getId(), malformed.getId()};
        });
        var page = send(port, catalog, null, access);
        assertSafe(page, 200);
        List<Number> ids = JsonPath.read(page.body(), "$[*].id");
        assertThat(ids).hasSize(100);
        assertThat(ids.stream().map(Number::longValue).toList()).isSorted();
        List<Map<String, Object>> rows = JsonPath.read(page.body(), "$");
        assertThat(rows.stream().filter(row -> ((Number)row.get("id")).longValue() == unsupported[0]
                || ((Number)row.get("id")).longValue() == unsupported[1]).toList())
                .hasSize(2).allSatisfy(row -> assertThat(row.get("supported")).isEqualTo(false));
        var tail = send(port, catalog + "?afterId=" + ids.getLast(), null, access);
        assertSafe(tail, 200);
        List<Number> tailIds = JsonPath.read(tail.body(), "$[*].id");
        assertThat(tailIds).hasSize(43);
        assertThat(tailIds.stream().map(Number::longValue).toList()).isSorted()
                .allSatisfy(id -> assertThat(id).isGreaterThan(ids.getLast().longValue()));
        assertThat(send(port, catalog + "?afterId=" + tailIds.getLast(), null, access).body()).isEqualTo("[]");

        long initialEvents = count("T_10_01_21");
        assertSafe(send(port, changePath(companyRole, read, true), reason, null), 401);
        assertSafe(send(port, changePath(companyRole, read, true), "{}", access), 400);
        assertSafe(send(port, changePath(companyRole, read, true), null, access), 403);
        assertSafe(send(port, changePath(companyRole, read, false), reason, access), 409);
        assertSafe(send(port, changePath(companyRole, Long.MAX_VALUE, true), reason, access), 403);
        assertSafe(send(port, changePath(companyRole, platformRead, true), reason, access), 409);
        assertSafe(send(port, changePath(platformRole, read, true), reason, access), 409);
        for (long id : unsupported) assertSafe(send(port, changePath(companyRole, id, true), reason, access), 409);
        assertSafe(send(port, changePath(platformAdminRole, platformRead, false), reason, access), 409);
        assertThat(count("T_10_01_21")).isEqualTo(initialEvents);

        // One independent user can observe permission changes with the same login session.
        long observer = createCompositionUser(adminId, companyId, "composition-observer");
        long other = createCompositionUser(adminId, companyId, "compose-delegator");
        Instant now = Instant.now();
        addCompanyFixtureGrant(adminId, observer, companyRole, companyId, now.minusSeconds(5), null);
        addCompanyFixtureGrant(adminId, other, companyAdminRole, companyId, now.minusSeconds(5), null);
        String observerAccess = loginAccess("composition-observer");
        String otherAccess = loginAccess("compose-delegator");
        assertSafe(send(port, catalog, null, otherAccess), 403);
        assertSafe(send(port, changePath(companyRole, read, true), reason, otherAccess), 403);
        assertSafe(send(port, "/v1/me/permissions?companyId=" + companyId, null, observerAccess), 403);
        long grantCount = count("T_10_01_15");
        long platformGrantCount = count("T_10_01_20");
        assertSafe(send(port, changePath(companyRole, read, true), reason, access), 204);
        assertSafe(send(port, changePath(companyRole, read, true), reason, access), 409);
        assertThat((List<String>) JsonPath.read(send(port, "/v1/me/permissions?companyId=" + companyId,
                null, observerAccess).body(), "$.permissionCodes")).containsExactly("user.read");
        assertThat((List<String>) JsonPath.read(send(port, bundle, null, access).body(), "$[*].code")).containsExactly("user.read");
        assertSafe(send(port, changePath(platformRole, platformRead, true), reason, access), 204);
        assertSafe(send(port, changePath(platformRole, platformRead, false), reason, access), 204);
        assertThat(count("T_10_01_15")).isEqualTo(grantCount);
        assertThat(count("T_10_01_20")).isEqualTo(platformGrantCount);
        assertThat((List<String>) JsonPath.read(send(port, "/v1/me/permissions?companyId=" + companyId,
                null, otherAccess).body(), "$.permissionCodes")).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);

        long beforeFailure = count("T_10_01_21");
        jdbc.execute("CREATE TRIGGER hyflo_test_composition_failure BEFORE INSERT ON T_10_01_21 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertSafe(send(port, changePath(companyRole, roleRead, true), reason, access), 503);
            assertSafe(send(port, changePath(companyRole, read, false), reason, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_composition_failure");
        }
        assertThat(count("T_10_01_21")).isEqualTo(beforeFailure);
        assertThat((List<String>) JsonPath.read(send(port, bundle, null, access).body(), "$[*].code")).containsExactly("user.read");
        assertCompositionRace(changePath(companyRole, roleRead, true), reason, access);
        assertCompositionRace(changePath(companyRole, roleRead, false), reason, access);
        assertSafe(send(port, changePath(companyRole, read, false), reason, access), 204);
        assertSafe(send(port, "/v1/me/permissions?companyId=" + companyId, null, observerAccess), 403);
        // Removal is also available for an unsupported existing link, without deleting the definition.
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", companyRole, unsupported[1]);
        assertSafe(send(port, changePath(companyRole, unsupported[1], false), reason, access), 204);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_09 WHERE F_00=?", Long.class, unsupported[1])).isEqualTo(1L);

        long ownRole = createRuntimeRole("FUTURE_OWN", false, access);
        addCompanyFixtureGrant(adminId, adminId, ownRole, companyId, now.plusSeconds(3600), null);
        assertSafe(send(port, changePath(ownRole, read, true), reason, access), 409);
        long endedRole = createRuntimeRole("ENDED_ROLE", false, access);
        addCompanyFixtureGrant(adminId, adminId, endedRole, companyId, now.minusSeconds(7200), now.minusSeconds(3600));
        assertSafe(send(port, changePath(endedRole, read, true), reason, access), 204);
        long revokedRole = createRuntimeRole("REVOKED_ROLE", false, access);
        long revokedGrant = addCompanyFixtureGrant(adminId, adminId, revokedRole, companyId, now.minusSeconds(60), null);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var grant = entityManager.find(UserRoleGrant.class, revokedGrant);
            grant.revoke(Instant.now(), entityManager.find(User.class, adminId));
        });
        assertSafe(send(port, changePath(revokedRole, read, true), reason, access), 204);
        long groupedRole = createRuntimeRole("GROUPED_ROLE", false, access);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var group = new Group("SYNTHETIC_GROUP", "Synthetic group");
            entityManager.persist(group);
            entityManager.persist(new GroupRoleGrant(group, entityManager.find(Role.class, groupedRole),
                    AuthorizationScope.global(companyId), "synthetic future group grant", now.plusSeconds(3600), null,
                    entityManager.find(User.class, adminId)));
        });
        assertSafe(send(port, changePath(groupedRole, read, true), reason, access), 409);
        long futurePlatform = createRuntimeRole("FUTURE_PLATFORM", true, access);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> entityManager.persist(
                new PlatformRoleGrant(entityManager.find(User.class, adminId), entityManager.find(Role.class, futurePlatform),
                        now.plusSeconds(3600), null, "synthetic future grant", UUID.randomUUID().toString(),
                        entityManager.find(User.class, adminId))));
        assertSafe(send(port, changePath(futurePlatform, platformRead, true), reason, access), 409);

        long foreignCompany = new TransactionTemplate(transactions).execute(tx -> {
            var companyType = entityManager.createQuery("select t from OrganizationUnitType t where t.code='COMPANY'",
                    OrganizationUnitType.class).getSingleResult();
            var active = entityManager.createQuery("select s from OrganizationUnitStatus s where s.code='ACTIVE'",
                    OrganizationUnitStatus.class).getSingleResult();
            var unit = new OrganizationUnit("FOREIGN_COMPOSITION", null, null, "Synthetic second company",
                    companyType, null, LocalDate.now(ZoneOffset.UTC).minusDays(1), null, active);
            entityManager.persist(unit);
            return unit.getId();
        });
        long foreign = createCompositionUser(adminId, foreignCompany, "composition-foreign");
        long foreignRole = createRuntimeRole("FOREIGN_CATALOG", false, access);
        addCompanyFixtureGrant(adminId, foreign, foreignRole, companyId, now.plusSeconds(3600), null);
        assertSafe(send(port, changePath(foreignRole, read, true), reason, access), 409);
        assertSafe(send(port, changePath(foreignRole, catalogRead, true), reason, access), 204);
        assertSafe(send(port, changePath(foreignRole, permissionRead, true), reason, access), 204);

        // An actual company grant assignment races an incompatible role change.
        long raceRole = createRuntimeRole("DELEGATION_RACE", false, access);
        assertSafe(send(port, changePath(raceRole, catalogRead, true), reason, access), 204);
        addCompanyFixtureGrant(adminId, other, raceRole, companyId, now.minusSeconds(5), now.plusSeconds(7200));
        String assignPath = "/v1/identity/users/" + foreign + "/role-grants?companyId=" + companyId;
        String assignBody = "{\"roleId\":" + raceRole + ",\"validTo\":\""
                + now.plusSeconds(3600).truncatedTo(java.time.temporal.ChronoUnit.MICROS) + "\",\"reason\":\"synthetic race\"}";
        var adding = client.sendAsync(request(port, changePath(raceRole, read, true), reason, access),
                HttpResponse.BodyHandlers.ofString());
        var assigning = client.sendAsync(request(port, assignPath, assignBody, otherAccess),
                HttpResponse.BodyHandlers.ofString());
        var changeResult = adding.get(60, TimeUnit.SECONDS);
        var assignResult = assigning.get(60, TimeUnit.SECONDS);
        if (changeResult.statusCode() == 204) assertSafe(assignResult, 403);
        else {
            assertSafe(changeResult, 409);
            assertSafe(assignResult, 201);
        }
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 g JOIN T_10_01_14 rp ON rp.F_01=g.F_09 "
                + "WHERE g.F_08=? AND g.F_09=? AND rp.F_02=?", Long.class, foreign, raceRole, read)).isZero();

        // Exact operation gates are evaluated live and independently.
        for (long id : List.of(platformCatalogRead, platformRead, associate, remove)) {
            jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", platformAdminRole, id);
            try {
                if (id == platformCatalogRead) {
                    assertSafe(send(port, catalog, null, access), 403);
                    assertSafe(send(port, bundle, null, access), 403);
                } else if (id == platformRead) {
                    assertSafe(send(port, bundle, null, access), 403);
                    assertSafe(send(port, catalog, null, access), 200);
                } else {
                    assertSafe(send(port, changePath(companyRole, read, id == associate), reason, access), 403);
                }
            } finally {
                jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", platformAdminRole, id);
            }
        }
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var events = entityManager.createQuery("select e from RoleDefinitionEvent e order by e.id",
                    RoleDefinitionEvent.class).getResultList();
            assertThat(events).isNotEmpty().allSatisfy(event -> {
                assertThat(event.getPerformedBy().getId()).isEqualTo(adminId);
                assertThat(event.getOccurredAt()).isNotNull();
                assertThat(event.getCorrelationId()).matches("[0-9a-f-]{36}");
                if ("CREATED".equals(event.getEventCode())) assertThat(event.getPermission()).isNull();
                else {
                    assertThat(event.getEventCode()).isIn("PERMISSION_ASSOCIATED", "PERMISSION_REMOVED");
                    assertThat(event.getPermission().getId()).isPositive();
                    assertThat(event.getReason()).isEqualTo("synthetic role composition");
                    assertThat(event.getRoleCode()).isEqualTo(event.getRole().getCode());
                }
            });
            assertThat(events.stream().filter(e -> e.getRole().getId() == companyRole && e.getPermission() != null
                    && e.getPermission().getId() == read).map(RoleDefinitionEvent::getEventCode).toList())
                    .containsExactly("PERMISSION_ASSOCIATED", "PERMISSION_REMOVED");
        });
        var openapi = send(port, "/api-docs", null, access);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String,Object> paths = JsonPath.read(openapi.body(), "$.paths");
        assertThat(paths.get(catalog).toString()).contains("get=");
        assertThat(paths.get("/v1/platform/roles/{roleId}/permissions").toString()).contains("get=");
        for (String op : List.of("associate", "remove")) {
            assertThat(paths.get("/v1/platform/roles/{roleId}/permissions/{permissionId}/" + op).toString()).contains("post=");
        }
        assertSafe(send(port, "/v1/auth/logout", "", access), 204);
        assertSafe(send(port, catalog, null, access), 401);
    }

    @Test
    void managesRoleLifecycleWithLiveAuthorityAndAtomicEvidence() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("LIFECYCLE", "lifecycle-admin",
                "CUSTOM_AUTHORITY"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='lifecycle-admin'", Long.class);
        long bootstrapActor = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        long initialCompany = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=FALSE", Long.class);
        long initialPlatform = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=TRUE", Long.class);
        String access = loginAccess("lifecycle-admin");
        String root = "/v1/platform/roles/";
        String rename = "{\"displayName\":\"Operator configured label\",\"reason\":\"synthetic rename\"}";
        String retire = "{\"reason\":\"synthetic retirement\"}";
        long role = createRuntimeRole("LIFECYCLE_COMPANY", false, access);
        long platform = createRuntimeRole("LIFECYCLE_PLATFORM", true, access);
        long observer = createCompositionUser(admin, company, "lifecycle-observer");
        long delegator = createCompositionUser(admin, company, "lifecycle-delegator");
        long recipient = createCompositionUser(admin, company, "lifecycle-recipient");
        Instant now = Instant.now();
        long read = permissionId("user.read");
        long renamePermission = permissionId("platform-role.rename");
        long retirePermission = permissionId("platform-role.retire");
        assertSafe(send(port, changePath(role, read, true), retire, access), 204);
        long observerGrant = addCompanyFixtureGrant(admin, observer, role, company, now.minusSeconds(5), null);
        addCompanyFixtureGrant(admin, delegator, initialCompany, company, now.minusSeconds(5), null);
        addCompanyFixtureGrant(admin, delegator, role, company, now.minusSeconds(5), now.plusSeconds(7200));
        String observerAccess = loginAccess("lifecycle-observer");
        String delegatorAccess = loginAccess("lifecycle-delegator");
        String roleRename = root + role + "/rename";
        String roleRetire = root + role + "/retire";
        String permissions = "/v1/me/permissions?companyId=" + company;
        assertThat((List<String>) JsonPath.read(send(port, permissions, null, observerAccess).body(),
                "$.permissionCodes")).containsExactly("user.read");
        long baselineEvents = count("T_10_01_21");
        for (String endpoint : List.of(roleRename, roleRetire)) {
            String body = endpoint.equals(roleRename) ? rename : retire;
            assertSafe(send(port, endpoint, body, null), 401);
            assertSafe(send(port, endpoint, body, "invalid.jwt"), 401);
            assertSafe(send(port, endpoint, body, delegatorAccess), 403);
            assertSafe(send(port, endpoint, null, access), 403);
            assertSafe(send(port, endpoint, "{", access), 400);
            assertSafe(send(port, endpoint, "{}", access), 400);
        }
        assertSafe(send(port, root + "0/rename", rename, access), 400);
        assertSafe(send(port, root + "bad/retire", retire, access), 400);
        assertSafe(send(port, root + Long.MAX_VALUE + "/rename", rename, access), 403);
        assertSafe(send(port, root + Long.MAX_VALUE + "/retire", retire, access), 403);
        assertSafe(send(port, root + Long.MAX_VALUE + "/retire", retire, delegatorAccess), 403);
        assertSafe(send(port, roleRename, "{\"displayName\":\" \",\"reason\":\"x\"}", access), 400);
        assertSafe(send(port, roleRename, "{\"displayName\":\"" + "x".repeat(121) + "\",\"reason\":\"x\"}", access), 400);
        assertSafe(send(port, roleRetire, "{\"reason\":\"" + "x".repeat(256) + "\"}", access), 400);
        assertSafe(send(port, roleRename, "{\"displayName\":\"Runtime chosen role\",\"reason\":\"same\"}", access), 409);
        assertSafe(send(port, root + initialCompany + "/retire", retire, access), 409);
        assertSafe(send(port, root + initialPlatform + "/retire", retire, access), 409);
        assertThat(count("T_10_01_21")).isEqualTo(baselineEvents);

        // Separate live permissions, not role names; the same JWT cannot retain a revoked capability.
        for (long id : List.of(renamePermission, retirePermission)) {
            jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", initialPlatform, id);
            try {
                assertSafe(send(port, id == renamePermission ? roleRename : roleRetire,
                        id == renamePermission ? rename : retire, access), 403);
                assertSafe(send(port, id == renamePermission ? root + initialCompany + "/retire" : roleRename,
                        id == renamePermission ? retire : "{\"displayName\":\"Runtime chosen role\",\"reason\":\"same\"}", access), 409);
            } finally {
                jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", initialPlatform, id);
            }
        }
        assertThat(count("T_10_01_21")).isEqualTo(baselineEvents);

        // Fail the second rename snapshot, then a retirement insert; neither partial evidence nor mutation may survive.
        jdbc.execute("CREATE TRIGGER hyflo_test_role_lifecycle_failure BEFORE INSERT ON T_10_01_21 "
                + "FOR EACH ROW BEGIN IF NEW.F_01 IN ('RENAMED','RETIRED') THEN "
                + "SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'; END IF; END");
        try {
            assertSafe(send(port, roleRename, rename, access), 503);
            assertSafe(send(port, roleRetire, retire, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_role_lifecycle_failure");
        }
        assertThat(count("T_10_01_21")).isEqualTo(baselineEvents);
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_04 WHERE F_00=?", String.class, role))
                .isEqualTo("Runtime chosen role");
        assertThat(jdbc.queryForObject("SELECT F_03 FROM T_10_01_04 WHERE F_00=?", Boolean.class, role)).isTrue();

        long linkCount = count("T_10_01_14");
        long grantCount = count("T_10_01_15");
        long platformGrantCount = count("T_10_01_20");
        for (long id : List.of(role, platform, initialCompany, initialPlatform)) {
            var result = send(port, root + id + "/rename", rename, access);
            assertSafe(result, 200);
            assertThat((Number) JsonPath.read(result.body(), "$.id")).isNotNull();
            assertThat((String) JsonPath.read(result.body(), "$.displayName")).isEqualTo("Operator configured label");
            assertThat((Boolean) JsonPath.read(result.body(), "$.enabled")).isTrue();
            assertThat((Boolean) JsonPath.read(result.body(), "$.platformRole")).isEqualTo(id == platform || id == initialPlatform);
        }
        assertThat(count("T_10_01_14")).isEqualTo(linkCount);
        assertThat(count("T_10_01_15")).isEqualTo(grantCount);
        assertThat(count("T_10_01_20")).isEqualTo(platformGrantCount);
        assertThat((List<String>) JsonPath.read(send(port, permissions, null, observerAccess).body(),
                "$.permissionCodes")).containsExactly("user.read");
        assertThat((List<String>) JsonPath.read(send(port, permissions, null, access).body(),
                "$.permissionCodes")).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
        assertSafe(send(port, root + initialCompany + "/retire", retire, access), 409);
        assertSafe(send(port, root + initialPlatform + "/retire", retire, access), 409);
        assertSafe(send(port, roleRename, rename, access), 409);

        var platformRetired = send(port, root + platform + "/retire", retire, access);
        assertSafe(platformRetired, 200);
        assertThat((Boolean) JsonPath.read(platformRetired.body(), "$.enabled")).isFalse();
        var companyRetired = send(port, roleRetire, retire, access);
        assertSafe(companyRetired, 200);
        assertThat((Boolean) JsonPath.read(companyRetired.body(), "$.enabled")).isFalse();
        assertThat((String) JsonPath.read(companyRetired.body(), "$.code")).isEqualTo("LIFECYCLE_COMPANY");
        assertSafe(send(port, permissions, null, observerAccess), 403);
        assertSafe(send(port, roleRename, rename, access), 409);
        assertSafe(send(port, roleRetire, retire, access), 409);
        assertSafe(send(port, changePath(role, read, false), retire, access), 409);
        assertSafe(send(port, changePath(role, permissionId("role.read"), true), retire, access), 409);
        assertThat(count("T_10_01_14")).isEqualTo(linkCount);
        assertThat(count("T_10_01_15")).isEqualTo(grantCount);
        assertThat(count("T_10_01_20")).isEqualTo(platformGrantCount);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_00=?", Long.class, observerGrant)).isEqualTo(1L);
        var list = send(port, "/v1/platform/roles", null, access);
        assertSafe(list, 200);
        List<Map<String,Object>> rows = JsonPath.read(list.body(), "$");
        assertThat(rows.stream().filter(row -> ((Number) row.get("id")).longValue() == role).toList())
                .singleElement().satisfies(row -> {
                    assertThat(row.get("enabled")).isEqualTo(false);
                    assertThat(row.get("displayName")).isEqualTo("Operator configured label");
                });
        assertThat((List<String>) JsonPath.read(send(port, root + role + "/permissions", null, access).body(),
                "$[*].code")).containsExactly("user.read");

        long futurePlatform = createRuntimeRole("LIFECYCLE_FUTURE_PLATFORM", true, access);
        long endedPlatform = createRuntimeRole("LIFECYCLE_ENDED_PLATFORM", true, access);
        long futureBootstrap = createRuntimeRole("LIFECYCLE_FUTURE_COMPANY", false, access);
        long endedBootstrap = createRuntimeRole("LIFECYCLE_ENDED_COMPANY", false, access);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            User actor = entityManager.find(User.class, admin);
            User target = entityManager.find(User.class, observer);
            entityManager.persist(new PlatformRoleGrant(target, entityManager.find(Role.class, futurePlatform),
                    now.plusSeconds(3600), null, "synthetic future", UUID.randomUUID().toString(), actor));
            entityManager.persist(new PlatformRoleGrant(target, entityManager.find(Role.class, endedPlatform),
                    now.minusSeconds(7200), now.minusSeconds(3600), "synthetic ended", UUID.randomUUID().toString(), actor));
        });
        addCompanyFixtureGrant(bootstrapActor, observer, futureBootstrap, company, now.plusSeconds(3600), null);
        addCompanyFixtureGrant(bootstrapActor, observer, endedBootstrap, company, now.minusSeconds(7200), now.minusSeconds(3600));
        // IDN-23 permits safe retirement, but never expansion of a future-granted platform role.
        assertSafe(send(port, changePath(futurePlatform, permissionId("platform-role.read"), true), retire, access), 409);
        var futureGrantHistory = jdbc.queryForList("SELECT * FROM T_10_01_20 WHERE F_09=?", futurePlatform);
        assertThat(futureGrantHistory).hasSize(1);
        long futureRetirementEvents = count("T_10_01_21");
        long futureRetirementLinks = count("T_10_01_14");
        var futureRetired = send(port, root + futurePlatform + "/retire", retire, access);
        assertSafe(futureRetired, 200);
        assertThat((Boolean) JsonPath.read(futureRetired.body(), "$.enabled")).isFalse();
        assertThat(jdbc.queryForObject("SELECT F_03 FROM T_10_01_04 WHERE F_00=?", Boolean.class, futurePlatform)).isFalse();
        assertThat(jdbc.queryForList("SELECT * FROM T_10_01_20 WHERE F_09=?", futurePlatform)).isEqualTo(futureGrantHistory);
        assertThat(count("T_10_01_14")).isEqualTo(futureRetirementLinks);
        assertThat(count("T_10_01_21")).isEqualTo(futureRetirementEvents + 1);
        assertSafe(send(port, root + futurePlatform + "/retire", retire, access), 409);
        assertThat(count("T_10_01_21")).isEqualTo(futureRetirementEvents + 1);
        assertSafe(send(port, "/v1/platform/roles", null, access), 200);
        assertSafe(send(port, root + endedPlatform + "/retire", retire, access), 200);
        assertSafe(send(port, root + futureBootstrap + "/retire", retire, access), 409);
        assertSafe(send(port, root + endedBootstrap + "/retire", retire, access), 200);

        String assignPath = "/v1/identity/users/" + recipient + "/role-grants?companyId=" + company;
        String end = now.plusSeconds(3600).truncatedTo(java.time.temporal.ChronoUnit.MICROS).toString();
        assertSafe(send(port, assignPath, "{\"roleId\":" + role + ",\"validTo\":\"" + end
                + "\",\"reason\":\"synthetic denied delegation\"}", delegatorAccess), 403);
        long raceRole = createRuntimeRole("LIFECYCLE_RACE", false, access);
        assertSafe(send(port, changePath(raceRole, read, true), retire, access), 204);
        addCompanyFixtureGrant(admin, delegator, raceRole, company, now.minusSeconds(5), now.plusSeconds(7200));
        String racePath = root + raceRole + "/retire";
        long raceEvents = count("T_10_01_21");
        var retiring = client.sendAsync(request(port, racePath, retire, access), HttpResponse.BodyHandlers.ofString());
        var assigning = client.sendAsync(request(port, assignPath, "{\"roleId\":" + raceRole + ",\"validTo\":\""
                + end + "\",\"reason\":\"synthetic concurrent delegation\"}", delegatorAccess), HttpResponse.BodyHandlers.ofString());
        assertSafe(retiring.get(60, TimeUnit.SECONDS), 200);
        var assigned = assigning.get(60, TimeUnit.SECONDS);
        assertThat(assigned.statusCode()).isIn(201, 403);
        assertThat(count("T_10_01_21")).isEqualTo(raceEvents + 1);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=? AND F_09=?",
                Long.class, recipient, raceRole)).isEqualTo(assigned.statusCode() == 201 ? 1L : 0L);
        assertSafe(send(port, permissions, null, loginAccess("lifecycle-recipient")), 403);
        assertSafe(send(port, assignPath, "{\"roleId\":" + raceRole + ",\"validTo\":\"" + end
                + "\",\"reason\":\"synthetic repeat delegation\"}", delegatorAccess), 403);

        long duplicateRole = createRuntimeRole("LIFECYCLE_DUPLICATE", false, access);
        long beforeRace = count("T_10_01_21");
        var a = client.sendAsync(request(port, root + duplicateRole + "/retire", retire, access), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(request(port, root + duplicateRole + "/retire", retire, access), HttpResponse.BodyHandlers.ofString());
        assertThat(List.of(a.get(60, TimeUnit.SECONDS).statusCode(), b.get(60, TimeUnit.SECONDS).statusCode()))
                .containsExactlyInAnyOrder(200, 409);
        assertThat(count("T_10_01_21")).isEqualTo(beforeRace + 1);

        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var evidence = entityManager.createQuery("select e from RoleDefinitionEvent e where e.role.id=:id "
                    + "and e.eventCode in ('RENAMED_FROM','RENAMED','RETIRED') order by e.id", RoleDefinitionEvent.class)
                    .setParameter("id", role).getResultList();
            assertThat(evidence).extracting(RoleDefinitionEvent::getEventCode).containsExactly("RENAMED_FROM", "RENAMED", "RETIRED");
            assertThat(evidence).extracting(RoleDefinitionEvent::getDisplayName)
                    .containsExactly("Runtime chosen role", "Operator configured label", "Operator configured label");
            assertThat(evidence).allSatisfy(event -> {
                assertThat(event.getPerformedBy().getId()).isEqualTo(admin);
                assertThat(event.getPermission()).isNull();
                assertThat(event.getRoleCode()).isEqualTo("LIFECYCLE_COMPANY");
                assertThat(event.isPlatformRole()).isFalse();
                assertThat(event.getOccurredAt()).isNotNull();
                assertThat(event.getCorrelationId()).matches("[0-9a-f-]{36}");
            });
            assertThat(evidence.get(0).getCorrelationId()).isEqualTo(evidence.get(1).getCorrelationId());
            assertThat(evidence.get(0).getOccurredAt()).isEqualTo(evidence.get(1).getOccurredAt());
            assertThat(evidence.get(0).getReason()).isEqualTo("synthetic rename");
            assertThat(evidence.get(1).getReason()).isEqualTo("synthetic rename");
            assertThat(evidence.get(2).getReason()).isEqualTo("synthetic retirement");
            assertThat(evidence.get(2).getCorrelationId()).isNotEqualTo(evidence.get(0).getCorrelationId());
        });
        var openapi = send(port, "/api-docs", null, access);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String,Object> paths = JsonPath.read(openapi.body(), "$.paths");
        for (String op : List.of("rename", "retire")) {
            assertThat(paths.get("/v1/platform/roles/{roleId}/" + op).toString()).contains("post=");
        }
        assertSafe(send(port, "/v1/auth/logout", "", access), 204);
        assertSafe(send(port, root + futurePlatform + "/retire", retire, access), 401);
        assertSafe(send(port, root + futurePlatform + "/rename", rename, access), 401);
    }


    @Test
    void managesPlatformRolesInUserManagementWithContinuity() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("GRANT_COMPANY", "grant-admin",
                "CONFIGURED_ADMIN"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='grant-admin'", Long.class);
        long reserved = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        long fullRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=TRUE", Long.class);
        long companyRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=FALSE", Long.class);
        long initialGrant = jdbc.queryForObject("SELECT F_00 FROM T_10_01_20 WHERE F_08=?", Long.class, admin);
        String access = loginAccess("grant-admin");
        long second = createCompositionUser(admin, company, "grant-second");
        long viewer = createCompositionUser(admin, company, "grant-viewer");
        long capped = createCompositionUser(admin, company, "grant-capped");
        long noCredential = createCompositionUser(admin, company, "grant-ineligible");
        String secondAccess = loginAccess("grant-second");
        String viewerAccess = loginAccess("grant-viewer");
        String root = platformGrantPath(second);
        String own = platformGrantPath(admin);
        String reason = "{\"reason\":\"synthetic revoke\"}";
        long limited = createRuntimeRole("GRANT_VIEWER", true, access);
        long empty = createRuntimeRole("GRANT_EMPTY", true, access);
        long retired = createRuntimeRole("GRANT_RETIRED", true, access);
        assertSafe(send(port, "/v1/platform/roles/" + retired + "/retire", reason, access), 200);
        assertSafe(send(port, changePath(limited, permissionId("platform-role.read"), true), reason, access), 204);
        assertSafe(send(port, changePath(limited, permissionId("platform-user-role-grant.read"), true), reason, access), 204);
        String full = platformGrantBody(fullRole);
        String limitedBody = platformGrantBody(limited);
        assertSafe(send(port, root, null, null), 401);
        assertSafe(send(port, root, full, "invalid.jwt"), 401);
        assertSafe(send(port, root, null, secondAccess), 403);
        assertSafe(send(port, root, full, secondAccess), 403);
        assertSafe(send(port, own + "/" + initialGrant + "/revoke", reason, secondAccess), 403);
        assertSafe(send(port, root, "{}", access), 400);
        assertSafe(send(port, root + "?afterId=-1", null, access), 400);
        assertSafe(send(port, platformGrantPath(Long.MAX_VALUE), null, access), 403);
        assertSafe(send(port, platformGrantPath(reserved), null, access), 403);
        assertSafe(send(port, own, full, access), 403);
        assertSafe(send(port, root, platformGrantBody(companyRole), access), 409);
        assertSafe(send(port, root, platformGrantBody(empty), access), 409);
        assertSafe(send(port, root, platformGrantBody(retired), access), 409);
        jdbc.update("DELETE FROM T_10_01_10 WHERE F_03=?", noCredential);
        assertSafe(send(port, platformGrantPath(noCredential), full, access), 409);
        assertSafe(send(port, own + "/" + initialGrant + "/revoke", reason, access), 409);

        // Finite and future grants do not establish durable continuity.
        Instant now = Instant.now();
        long finite = platformFixtureGrant(admin, second, fullRole, now.minusSeconds(10), now.plusSeconds(3600));
        long future = platformFixtureGrant(admin, viewer, fullRole, now.plusSeconds(3600), null);
        assertSafe(send(port, platformGrantPath(capped), limitedBody, secondAccess), 403);
        long ineligible = platformFixtureGrant(admin, noCredential, fullRole, now.minusSeconds(10), null);
        assertSafe(send(port, own + "/" + initialGrant + "/revoke", reason, access), 409);
        assertSafe(send(port, platformGrantPath(noCredential) + "/" + ineligible + "/revoke", reason, access), 204);
        long ended = platformFixtureGrant(admin, viewer, fullRole, now.minusSeconds(3600), now.minusSeconds(60));
        assertSafe(send(port, own + "/" + initialGrant + "/revoke", reason, access), 409);
        assertSafe(send(port, root, full, access), 409);
        assertSafe(send(port, platformGrantPath(viewer), full, access), 409);
        assertSafe(send(port, platformGrantPath(viewer) + "/" + ended + "/revoke", reason, access), 409);
        assertSafe(send(port, root + "/" + future + "/revoke", reason, access), 403);
        assertSafe(send(port, platformGrantPath(viewer) + "/" + future + "/revoke", reason, access), 204);
        assertSafe(send(port, root + "/" + finite + "/revoke", reason, access), 204);

        // Candidate history counts include ended grants; never create an unusable 1001st candidate.
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            User target = entityManager.find(User.class, capped);
            User actor = entityManager.find(User.class, admin);
            Role role = entityManager.find(Role.class, limited);
            for (int i = 0; i < 1000; i++) {
                entityManager.persist(new PlatformRoleGrant(target, role, now.minusSeconds(7200), now.minusSeconds(3600),
                        "synthetic expired", java.util.UUID.randomUUID().toString(), actor));
            }
        });
        assertSafe(send(port, platformGrantPath(capped), limitedBody, access), 409);
        // A synthetic over-cap administrator must not count as durable continuity.
        long overCap = platformFixtureGrant(admin, capped, fullRole, now.minusSeconds(10), null);
        assertSafe(send(port, own + "/" + initialGrant + "/revoke", reason, access), 409);
        assertSafe(send(port, platformGrantPath(capped) + "/" + overCap + "/revoke", reason, access), 204);

        // Unsupported persisted links cannot be propagated through role delegation.
        long malformed = createRuntimeRole("GRANT_UNSUPPORTED", true, access);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var action = new Action("synthetic-grant", "Synthetic grant");
            var resource = new ResourceType("platform-synthetic-grant", "Synthetic grant");
            entityManager.persist(action);
            entityManager.persist(resource);
            var permission = new Permission("platform-synthetic-grant.synthetic-grant", "Synthetic unsupported", action, resource);
            entityManager.persist(permission);
            entityManager.persist(new RolePermission(entityManager.find(Role.class, malformed), permission));
        });
        assertSafe(send(port, root, platformGrantBody(malformed), access), 409);

        assertSafe(send(port, "/v1/platform/roles", null, viewerAccess), 403);
        long before = count("T_10_01_20");
        jdbc.execute("CREATE TRIGGER hyflo_test_grant_insert_failure BEFORE INSERT ON T_10_01_20 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, platformGrantPath(viewer), limitedBody, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_grant_insert_failure");
        }
        assertThat(count("T_10_01_20")).isEqualTo(before);
        var a = client.sendAsync(request(port, platformGrantPath(viewer), limitedBody, access), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(request(port, platformGrantPath(viewer), limitedBody, access), HttpResponse.BodyHandlers.ofString());
        var ar = a.get(60, TimeUnit.SECONDS);
        var br = b.get(60, TimeUnit.SECONDS);
        assertThat(List.of(ar.statusCode(), br.statusCode())).containsExactlyInAnyOrder(201, 409);
        var created = ar.statusCode() == 201 ? ar : br;
        Number grantNumber = JsonPath.read(created.body(), "$.id");
        long grant = grantNumber.longValue();
        assertThat(count("T_10_01_20")).isEqualTo(before + 1);
        assertSafe(send(port, "/v1/platform/roles", null, viewerAccess), 200);
        var history = send(port, platformGrantPath(viewer), null, viewerAccess);
        assertSafe(history, 200);
        assertThat((List<Number>) JsonPath.read(history.body(), "$[*].id")).hasSize(3);
        assertSafe(send(port, root, limitedBody, viewerAccess), 403);
        assertSafe(send(port, platformGrantPath(viewer) + "/" + grant + "/revoke", reason, viewerAccess), 403);
        assertSafe(send(port, changePath(limited, permissionId("platform-role.read"), false), reason, access), 204);
        assertSafe(send(port, "/v1/platform/roles", null, viewerAccess), 403);
        assertSafe(send(port, changePath(limited, permissionId("platform-role.read"), true), reason, access), 204);
        assertSafe(send(port, "/v1/platform/roles", null, viewerAccess), 200);
        jdbc.execute("CREATE TRIGGER hyflo_test_grant_update_failure BEFORE UPDATE ON T_10_01_20 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, platformGrantPath(viewer) + "/" + grant + "/revoke", reason, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_grant_update_failure");
        }
        assertSafe(send(port, "/v1/platform/roles", null, viewerAccess), 200);
        assertSafe(send(port, platformGrantPath(viewer) + "/" + grant + "/revoke", reason, access), 204);
        assertSafe(send(port, platformGrantPath(viewer) + "/" + grant + "/revoke", reason, access), 409);
        assertSafe(send(port, "/v1/platform/roles", null, viewerAccess), 403);
        assertSafe(send(port, platformGrantPath(viewer), null, viewerAccess), 403);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            PlatformRoleGrant persisted = entityManager.find(PlatformRoleGrant.class, grant);
            assertThat(persisted.getGrantedBy().getId()).isEqualTo(admin);
            assertThat(persisted.getRevokedBy().getId()).isEqualTo(admin);
            assertThat(persisted.getReason()).isEqualTo("synthetic assignment");
            assertThat(persisted.getRevocationReason()).isEqualTo("synthetic revoke");
            assertThat(persisted.getCorrelationId()).isNotBlank().isNotEqualTo(persisted.getRevocationCorrelationId());
            assertThat(persisted.getRevokedAt()).isAfterOrEqualTo(persisted.getValidFrom());
        });
        assertSafe(send(port, "/v1/identity/users/" + viewer + "/suspend?companyId=" + company, reason, access), 403);

        // The second administrator can use the token issued before promotion.
        var promoted = send(port, root, full, access);
        assertSafe(promoted, 201);
        Number promotedNumber = JsonPath.read(promoted.body(), "$.id");
        long promotedId = promotedNumber.longValue();
        assertSafe(send(port, "/v1/platform/roles", null, secondAccess), 200);
        var firstRevoke = client.sendAsync(request(port, own + "/" + initialGrant + "/revoke", reason, access),
                HttpResponse.BodyHandlers.ofString());
        var secondRevoke = client.sendAsync(request(port, root + "/" + promotedId + "/revoke", reason, access),
                HttpResponse.BodyHandlers.ofString());
        List<Integer> results = List.of(firstRevoke.get(60, TimeUnit.SECONDS).statusCode(),
                secondRevoke.get(60, TimeUnit.SECONDS).statusCode());
        assertThat(results.stream().filter(status -> status == 204).count()).isEqualTo(1);
        assertThat(results).allMatch(status -> status == 204 || status == 403 || status == 409 || status == 503);
        boolean firstRetained = jdbc.queryForObject("SELECT F_03 IS NULL FROM T_10_01_20 WHERE F_00=?", Boolean.class, initialGrant);
        if (firstRetained) {
            // Re-promote the second, then intentionally transfer continuity.
            assertSafe(send(port, root, full, access), 201);
            assertSafe(send(port, own + "/" + initialGrant + "/revoke", reason, access), 204);
        }
        assertSafe(send(port, "/v1/platform/roles", null, access), 403);
        assertSafe(send(port, "/v1/platform/roles", null, secondAccess), 200);
        var openapi = send(port, "/api-docs", null, secondAccess);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String,Object> paths = JsonPath.read(openapi.body(), "$.paths");
        assertThat(paths.get("/v1/identity/users/{userId}/platform-role-grants").toString()).contains("get=", "post=");
        assertThat(paths.get("/v1/identity/users/{userId}/platform-role-grants/{grantId}/revoke").toString()).contains("post=");
        assertSafe(send(port, "/v1/auth/logout", "", secondAccess), 204);
        assertSafe(send(port, root, null, secondAccess), 401);
    }


    @Test
    void safelyChangesAssignedPlatformRolesWithProspectiveContinuity() throws Exception {
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("SAFE_ROLE_COMPANY", "safe-admin",
                "SAFE_INITIAL"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='safe-admin'", Long.class);
        long initial = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=TRUE", Long.class);
        long roleRead = permissionId("platform-role.read");
        long groupRead = permissionId("platform-group.read");
        long associate = permissionId("platform-role-permission.associate");
        long remove = permissionId("platform-role-permission.remove");
        long retire = permissionId("platform-role.retire");
        long create = permissionId("platform-role.create");
        String roles = "/v1/platform/roles";
        String groups = "/v1/platform/groups";
        String history = "/v1/platform/security-history/role-definitions";
        String reason = "{\"reason\":\"synthetic safe assigned role change\"}";
        String access = loginAccess("safe-admin");
        long limited = createRuntimeRole("SAFE_LIMITED", true, access);
        long editor = createRuntimeRole("SAFE_EDITOR", true, access);
        long finiteRole = createRuntimeRole("SAFE_FINITE", true, access);
        long futureRole = createRuntimeRole("SAFE_FUTURE", true, access);
        long endedRole = createRuntimeRole("SAFE_ENDED", true, access);
        long alternate = createRuntimeRole("SAFE_ALTERNATE", true, access);
        long viewer = createCompositionUser(admin, company, "safe-viewer");
        long delegated = createCompositionUser(admin, company, "safe-editor");
        long temporal = createCompositionUser(admin, company, "safe-temporal");
        long second = createCompositionUser(admin, company, "safe-second");
        long invalid = createCompositionUser(admin, company, "safe-invalid");
        long capped = createCompositionUser(admin, company, "safe-capped");
        long disabled = createCompositionUser(admin, company, "safe-disabled");
        long partial = createCompositionUser(admin, company, "safe-partial");
        String viewerAccess = loginAccess("safe-viewer");
        String editorAccess = loginAccess("safe-editor");
        String temporalAccess = loginAccess("safe-temporal");
        String secondAccess = loginAccess("safe-second");
        String reasonPath = changePath(limited, groupRead, true);
        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS);
        for (long id : List.of(roleRead, remove))
            assertSafe(send(port, changePath(limited, id, true), reason, access), 204);
        for (long id : List.of(associate, remove, retire))
            assertSafe(send(port, changePath(editor, id, true), reason, access), 204);
        for (long id : List.of(associate, groupRead))
            assertSafe(send(port, changePath(finiteRole, id, true), reason, access), 204);
        assertSafe(send(port, changePath(futureRole, roleRead, true), reason, access), 204);
        assertSafe(send(port, changePath(endedRole, roleRead, true), reason, access), 204);
        assertSafe(send(port, platformGrantPath(viewer), platformGrantBody(limited), access), 201);
        assertSafe(send(port, platformGrantPath(delegated), platformGrantBody(editor), access), 201);
        platformFixtureGrant(admin, temporal, finiteRole, now.minusSeconds(10), now.plusSeconds(3600));
        platformFixtureGrant(admin, temporal, futureRole, now.plusSeconds(3600), null);
        platformFixtureGrant(admin, temporal, endedRole, now.minusSeconds(7200), now.minusSeconds(3600));
        long revoked = platformFixtureGrant(admin, temporal, endedRole, now.minusSeconds(3600), null);
        assertSafe(send(port, platformGrantPath(temporal) + "/" + revoked + "/revoke", reason, access), 204);
        assertSafe(send(port, reasonPath, reason, null), 401);
        assertSafe(send(port, reasonPath, reason, "invalid.jwt"), 401);
        assertSafe(send(port, reasonPath, reason, viewerAccess), 403);
        assertSafe(send(port, reasonPath, reason, editorAccess), 403); // added permission not held
        assertSafe(send(port, changePath(editor, groupRead, true), reason, editorAccess), 403); // self amplification
        assertSafe(send(port, reasonPath, reason, temporalAccess), 403); // only finite operation
        assertSafe(send(port, changePath(futureRole, groupRead, true), reason, access), 409);
        assertSafe(send(port, changePath(finiteRole, roleRead, true), reason, access), 409);
        assertSafe(send(port, changePath(endedRole, groupRead, true), reason, access), 204);
        assertSafe(send(port, changePath(limited, permissionId("user.read"), true), reason, access), 409);
        assertSafe(send(port, changePath(limited, Long.MAX_VALUE, true), reason, access), 403);
        assertSafe(send(port, changePath(0, roleRead, true), reason, access), 400);
        assertSafe(send(port, changePath(limited, roleRead, true), reason, access), 409);
        assertSafe(send(port, changePath(limited, groupRead, false), reason, access), 409);
        assertSafe(send(port, roles + "/" + limited + "/retire", reason, viewerAccess), 403);
        assertSafe(send(port, changePath(editor, remove, false), reason, editorAccess), 409);
        assertSafe(send(port, roles + "/" + editor + "/retire", reason, editorAccess), 409);
        assertSafe(send(port, changePath(initial, groupRead, false), reason, access), 409);
        assertSafe(send(port, roles + "/" + initial + "/retire", reason, access), 409);

        // Preserve actual effective permissions and atomic event evidence across failed mutations.
        long before = count("T_10_01_21");
        jdbc.execute("CREATE TRIGGER hyflo_test_safe_link_insert BEFORE INSERT ON T_10_01_14 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, reasonPath, reason, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_safe_link_insert");
        }
        assertThat(count("T_10_01_21")).isEqualTo(before);
        assertSafe(send(port, groups, null, viewerAccess), 403);
        assertSafe(send(port, reasonPath, reason, access), 204);
        assertSafe(send(port, groups, null, viewerAccess), 200);
        jdbc.execute("CREATE TRIGGER hyflo_test_safe_link_delete BEFORE DELETE ON T_10_01_14 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, changePath(limited, groupRead, false), reason, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_safe_link_delete");
        }
        assertSafe(send(port, groups, null, viewerAccess), 200);
        assertThat(count("T_10_01_21")).isEqualTo(before + 1);
        jdbc.execute("CREATE TRIGGER hyflo_test_safe_event BEFORE INSERT ON T_10_01_21 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, changePath(limited, groupRead, false), reason, access), 503);
            assertSafe(send(port, roles + "/" + limited + "/retire", reason, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_safe_event");
        }
        assertSafe(send(port, groups, null, viewerAccess), 200);
        assertThat(count("T_10_01_21")).isEqualTo(before + 1);
        assertCompositionRace(changePath(limited, groupRead, false), reason, access);
        assertSafe(send(port, groups, null, viewerAccess), 403);
        assertCompositionRace(reasonPath, reason, access);
        assertSafe(send(port, groups, null, viewerAccess), 200);
        assertSafe(send(port, changePath(limited, create, true), reason, access), 204);
        assertSafe(send(port, roles, roleBody("SAFE_VIEWER_WRITE", true), viewerAccess), 201);
        assertSafe(send(port, changePath(limited, create, false), reason, access), 204);
        assertSafe(send(port, roles, roleBody("SAFE_VIEWER_DENIED", true), viewerAccess), 403);

        // An unsupported historical link can be removed but cannot be expanded.
        long unsupported = new TransactionTemplate(transactions).execute(tx -> {
            var action = new Action("synthetic-safe", "Synthetic unsupported action");
            var resource = new ResourceType("platform-synthetic-safe", "Synthetic unsupported resource");
            entityManager.persist(action);
            entityManager.persist(resource);
            var permission = new Permission("platform-synthetic-safe.synthetic-safe", "Synthetic unsupported", action, resource);
            entityManager.persist(permission);
            entityManager.persist(new RolePermission(entityManager.find(Role.class, limited), permission));
            return permission.getId();
        });
        assertSafe(send(port, changePath(limited, create, true), reason, access), 409);
        assertSafe(send(port, changePath(limited, unsupported, false), reason, access), 204);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_09 WHERE F_00=?", Long.class, unsupported)).isEqualTo(1L);
        assertSafe(send(port, changePath(limited, unsupported, true), reason, access), 409);
        // Actual company permission accidentally persisted on a platform role also blocks expansion.
        long companyRead = permissionId("user.read");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", limited, companyRead);
        assertSafe(send(port, changePath(limited, create, true), reason, access), 409);
        assertSafe(send(port, changePath(limited, companyRead, false), reason, access), 204);

        // A partial administrator, finite/future/revoked candidates and ineligible full candidates
        // cannot substitute for a single durable, credential-qualified complete administrator.
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) SELECT ?, F_02 FROM T_10_01_14 WHERE F_01=?", alternate, initial);
        long complete = createRuntimeRole("SAFE_COMPLETE", true, access);
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) SELECT ?, F_02 FROM T_10_01_14 WHERE F_01=?", complete, initial);
        assertSafe(send(port, changePath(alternate, groupRead, false), reason, access), 204);
        assertSafe(send(port, platformGrantPath(second), platformGrantBody(alternate), access), 201);
        long readOnly = createRuntimeRole("SAFE_GROUP_ONLY", true, access);
        assertSafe(send(port, changePath(readOnly, groupRead, true), reason, access), 204);
        assertSafe(send(port, platformGrantPath(partial), platformGrantBody(readOnly), access), 201);
        platformFixtureGrant(admin, temporal, complete, now.minusSeconds(5), now.plusSeconds(3600));
        platformFixtureGrant(admin, temporal, complete, now.plusSeconds(3600), null);
        long endedComplete = platformFixtureGrant(admin, temporal, complete, now.minusSeconds(3600), null);
        assertSafe(send(port, platformGrantPath(temporal) + "/" + endedComplete + "/revoke", reason, access), 204);
        platformFixtureGrant(admin, invalid, complete, now.minusSeconds(5), null);
        jdbc.update("UPDATE T_10_01_10 SET F_01='unsupported-synthetic-hash' WHERE F_03=?", invalid);
        platformFixtureGrant(admin, disabled, complete, now.minusSeconds(5), null);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            User user = entityManager.find(User.class, disabled);
            user.disable(entityManager.createQuery("select s from UserStatus s where s.code='DISABLED'", UserStatus.class).getSingleResult());
            User cap = entityManager.find(User.class, capped);
            User actor = entityManager.find(User.class, admin);
            Role role = entityManager.find(Role.class, endedRole);
            for (int i = 0; i < 1000; i++) entityManager.persist(new PlatformRoleGrant(cap, role,
                    now.minusSeconds(7200), now.minusSeconds(3600), "synthetic ended", UUID.randomUUID().toString(), actor));
        });
        platformFixtureGrant(admin, capped, complete, now.minusSeconds(5), null);
        assertSafe(send(port, changePath(initial, groupRead, false), reason, access), 409);
        assertSafe(send(port, roles + "/" + initial + "/retire", reason, access), 409);

        // The same capability from another durable role counts for the prospective union.
        long duplicateRead = platformFixtureGrant(admin, admin, readOnly, now.minusSeconds(5), null);
        assertSafe(send(port, changePath(initial, groupRead, false), reason, access), 204);
        assertSafe(send(port, groups, null, access), 200);
        assertSafe(send(port, changePath(initial, groupRead, true), reason, access), 204);
        assertSafe(send(port, platformGrantPath(admin) + "/" + duplicateRead + "/revoke", reason, access), 204);
        assertSafe(send(port, changePath(alternate, groupRead, true), reason, access), 204);
        assertSafe(send(port, groups, null, secondAccess), 200);

        // Two separately complete administrators must not both remove their only copy.
        long priorEvents = count("T_10_01_21");
        long priorLinks = count("T_10_01_14");
        long priorGrants = count("T_10_01_20");
        var left = client.sendAsync(request(port, changePath(initial, groupRead, false), reason, access), HttpResponse.BodyHandlers.ofString());
        var right = client.sendAsync(request(port, changePath(alternate, groupRead, false), reason, secondAccess), HttpResponse.BodyHandlers.ofString());
        int leftStatus = left.get(60, TimeUnit.SECONDS).statusCode();
        int rightStatus = right.get(60, TimeUnit.SECONDS).statusCode();
        assertThat(List.of(leftStatus, rightStatus)).contains(204);
        assertThat(List.of(leftStatus, rightStatus).stream().filter(status -> status == 204).count()).isEqualTo(1);
        assertThat(List.of(leftStatus, rightStatus)).allMatch(status -> status == 204 || status == 409 || status == 503);
        assertThat(count("T_10_01_21")).isEqualTo(priorEvents + 1);
        assertThat(count("T_10_01_14")).isEqualTo(priorLinks - 1);
        assertThat(count("T_10_01_20")).isEqualTo(priorGrants);
        long removedRole = leftStatus == 204 ? initial : alternate;
        long eventActor = leftStatus == 204 ? admin : second;
        String survivorAccess = leftStatus == 204 ? secondAccess : access;
        String loserAccess = leftStatus == 204 ? access : secondAccess;
        assertSafe(send(port, groups, null, survivorAccess), 200);
        assertSafe(send(port, groups, null, loserAccess), 403);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_14 WHERE F_01 IN (?,?) AND F_02=?", Long.class,
                initial, alternate, groupRead)).isEqualTo(1L);
        var audit = send(port, history, null, survivorAccess);
        assertSafe(audit, 200);
        List<Map<String,Object>> events = JsonPath.read(audit.body(), "$");
        var event = events.getLast();
        assertThat(event.get("eventCode")).isEqualTo("PERMISSION_REMOVED");
        assertThat(((Number)event.get("roleId")).longValue()).isEqualTo(removedRole);
        assertThat(((Number)event.get("permissionId")).longValue()).isEqualTo(groupRead);
        assertThat(((Number)event.get("performedByUserId")).longValue()).isEqualTo(eventActor);
        assertThat(event.get("correlationId").toString()).isNotBlank();

        // Grant assignment and retirement serialize: the new grant either precedes retirement or is rejected.
        long fresh = createCompositionUser(admin, company, "safe-fresh");
        long beforeRetire = count("T_10_01_21");
        jdbc.execute("CREATE TRIGGER hyflo_test_safe_role_update BEFORE UPDATE ON T_10_01_04 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, roles + "/" + limited + "/retire", reason, survivorAccess), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_safe_role_update");
        }
        assertThat(count("T_10_01_21")).isEqualTo(beforeRetire);
        assertSafe(send(port, roles, null, viewerAccess), 200);
        long retainedLinks = count("T_10_01_14");
        var assignment = client.sendAsync(request(port, platformGrantPath(fresh), platformGrantBody(limited), survivorAccess), HttpResponse.BodyHandlers.ofString());
        var retirement = client.sendAsync(request(port, roles + "/" + limited + "/retire", reason, survivorAccess), HttpResponse.BodyHandlers.ofString());
        int assigned = assignment.get(60, TimeUnit.SECONDS).statusCode();
        assertThat(retirement.get(60, TimeUnit.SECONDS).statusCode()).isEqualTo(200);
        assertThat(assigned).isIn(201, 409);
        assertThat(count("T_10_01_14")).isEqualTo(retainedLinks);
        assertThat(count("T_10_01_21")).isEqualTo(beforeRetire + 1);
        assertThat(jdbc.queryForObject("SELECT F_03 FROM T_10_01_04 WHERE F_00=?", Boolean.class, limited)).isFalse();
        assertSafe(send(port, roles, null, viewerAccess), 403);
        assertSafe(send(port, roles + "/" + removedRole + "/retire", reason, survivorAccess), 200);
        assertSafe(send(port, roles, null, loserAccess), 403);
        assertSafe(send(port, roles, null, survivorAccess), 200);
        assertSafe(send(port, roles + "/" + limited + "/retire", reason, survivorAccess), 409);

        var openapi = send(port, "/api-docs", null, survivorAccess);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String,Object> apiPaths = JsonPath.read(openapi.body(), "$.paths");
        for (String path : List.of("/v1/platform/roles/{roleId}/permissions/{permissionId}/associate",
                "/v1/platform/roles/{roleId}/permissions/{permissionId}/remove", "/v1/platform/roles/{roleId}/retire"))
            assertThat(((Map<?,?>)apiPaths.get(path)).keySet().stream().map(Object::toString).toList()).containsExactly("post");
        assertSafe(send(port, "/v1/auth/logout", "", survivorAccess), 204);
        assertSafe(send(port, roles + "/" + endedRole + "/retire", reason, survivorAccess), 401);
    }

    private static String platformGrantPath(long user) {
        return "/v1/identity/users/" + user + "/platform-role-grants";
    }

    private static String platformGrantBody(long role) {
        return "{\"roleId\":" + role + ",\"reason\":\"synthetic assignment\"}";
    }

    private long platformFixtureGrant(long actor, long user, long role, Instant from, Instant to) {
        return new TransactionTemplate(transactions).execute(tx -> {
            var grant = new PlatformRoleGrant(entityManager.find(User.class, user), entityManager.find(Role.class, role),
                    from, to, "synthetic fixture", java.util.UUID.randomUUID().toString(), entityManager.find(User.class, actor));
            entityManager.persist(grant);
            return grant.getId();
        });
    }


    @Test
    void managesGroupsAndMembershipsThroughHttp() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("GROUP_COMPANY", "group-admin",
                "GROUP_AUTHORITY"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='group-admin'", Long.class);
        long reserved = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        long companyRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=FALSE", Long.class);
        String access = loginAccess("group-admin");
        long member = createCompositionUser(admin, company, "group-member");
        long other = createCompositionUser(admin, company, "group-other");
        long viewer = createCompositionUser(admin, company, "group-viewer");
        long protectedUser = createCompositionUser(admin, company, "group-protected");
        long pending = createCompositionUser(admin, company, "group-pending");
        long fresh = createCompositionUser(admin, company, "group-fresh");
        String memberAccess = loginAccess("group-member");
        String viewerAccess = loginAccess("group-viewer");
        Instant now = Instant.now();
        addCompanyFixtureGrant(admin, member, companyRole, company, now.minusSeconds(10), null);
        String permissions = "/v1/me/permissions?companyId=" + company;
        var beforePermissions = send(port, permissions, null, memberAccess);
        assertSafe(beforePermissions, 200);
        String base = "/v1/platform/groups";
        String reason = "{\"reason\":\"synthetic group change\"}";
        String create = groupBody("OPERATORS");
        String rename = "{\"displayName\":\"Configured name\",\"reason\":\"synthetic group change\"}";
        String assign = memberBody(member);
        assertSafe(send(port, base, null, null), 401);
        assertSafe(send(port, base, create, "invalid.jwt"), 401);
        assertSafe(send(port, base, null, memberAccess), 403);
        assertSafe(send(port, base, create, memberAccess), 403);
        assertSafe(send(port, base + "?afterId=-1", null, access), 400);
        assertSafe(send(port, base, "{}", access), 400);
        assertSafe(send(port, base + "/0", null, access), 400);
        assertSafe(send(port, base + "/" + Long.MAX_VALUE, null, access), 403);
        assertSafe(send(port, base + "/unregistered", create, access), 403);
        jdbc.execute("CREATE TRIGGER hyflo_test_group_event_failure BEFORE INSERT ON T_10_01_23 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, base, create, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_event_failure");
        }
        assertThat(count("T_10_01_03")).isZero();
        assertThat(count("T_10_01_23")).isZero();
        long group = createGroup("OPERATORS", access);
        long second = createGroup("MAINTENANCE", access);
        String selected = base + "/" + group;
        String members = selected + "/memberships";
        assertSafe(send(port, base, create, access), 409);
        var listed = send(port, base, null, access);
        assertSafe(listed, 200);
        assertThat((List<Number>) JsonPath.read(listed.body(), "$[*].id")).hasSize(2);
        assertThat((List<Number>) JsonPath.read(send(port, base + "?afterId=" + group, null, access).body(), "$[*].id"))
                .extracting(Number::longValue).containsExactly(second);
        assertSafe(send(port, selected, null, access), 200);
        assertSafe(send(port, selected + "/rename", rename, memberAccess), 403);
        assertSafe(send(port, selected + "/retire", reason, memberAccess), 403);
        assertSafe(send(port, members, null, memberAccess), 403);
        assertSafe(send(port, members, assign, memberAccess), 403);
        assertSafe(send(port, members + "/1/revoke", "", memberAccess), 403);

        // One group-read capability confers no membership or mutation authority.
        long viewRole = createRuntimeRole("GROUP_READ_ONLY", true, access);
        assertSafe(send(port, changePath(viewRole, permissionId("platform-group.read"), true), reason, access), 204);
        var viewGrant = send(port, platformGrantPath(viewer), platformGrantBody(viewRole), access);
        assertSafe(viewGrant, 201);
        long viewGrantId = ((Number) JsonPath.read(viewGrant.body(), "$.id")).longValue();
        assertSafe(send(port, base, null, viewerAccess), 200);
        assertSafe(send(port, selected + "/rename", rename, viewerAccess), 403);
        assertSafe(send(port, members, null, viewerAccess), 403);
        assertSafe(send(port, members, assign, viewerAccess), 403);
        assertSafe(send(port, platformGrantPath(viewer) + "/" + viewGrantId + "/revoke", reason, access), 204);
        assertSafe(send(port, base, null, viewerAccess), 403);

        jdbc.execute("CREATE TRIGGER hyflo_test_group_rename_failure BEFORE INSERT ON T_10_01_23 "
                + "FOR EACH ROW BEGIN IF NEW.F_01='RENAMED' THEN SIGNAL SQLSTATE '45000' "
                + "SET MESSAGE_TEXT='synthetic rollback probe'; END IF; END");
        try {
            assertSafe(send(port, selected + "/rename", rename, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_rename_failure");
        }
        assertThat(count("T_10_01_23")).isEqualTo(2);
        assertThat((String) JsonPath.read(send(port, selected, null, access).body(), "$.displayName")).isEqualTo("Configured group");
        assertSafe(send(port, selected + "/rename", rename, access), 200);
        assertSafe(send(port, selected + "/rename", rename, access), 409);
        assertThat((String) JsonPath.read(send(port, selected, null, access).body(), "$.code")).isEqualTo("OPERATORS");

        assertSafe(send(port, members, "{}", access), 400);
        assertSafe(send(port, members, memberBody(Long.MAX_VALUE), access), 403);
        assertSafe(send(port, members, memberBody(admin), access), 403);
        assertSafe(send(port, members, memberBody(reserved), access), 403);
        assertSafe(send(port, members, memberBody(viewer), access), 403); // historical platform authority
        assertSafe(send(port, platformGrantPath(protectedUser), platformGrantBody(viewRole), access), 201);
        assertSafe(send(port, members, memberBody(protectedUser), access), 403);
        jdbc.update("UPDATE T_10_01_08 SET F_07=(SELECT F_00 FROM T_10_01_02 WHERE F_01='PENDING') WHERE F_00=?", pending);
        assertSafe(send(port, members, memberBody(pending), access), 409);

        long future = groupMembershipFixture(admin, other, group, now.plusSeconds(3600), null);
        assertSafe(send(port, members, memberBody(other), access), 409);
        assertSafe(send(port, members + "/" + future + "/revoke", "", access), 204);
        long ended = groupMembershipFixture(admin, other, group, now.minusSeconds(7200), now.minusSeconds(3600));
        assertSafe(send(port, members + "/" + ended + "/revoke", "", access), 409);
        assertSafe(send(port, members, memberBody(other), access), 201);
        long historyBefore = count("T_10_01_13");
        jdbc.execute("CREATE TRIGGER hyflo_test_group_member_insert BEFORE INSERT ON T_10_01_13 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, members, assign, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_member_insert");
        }
        assertThat(count("T_10_01_13")).isEqualTo(historyBefore);
        var a = client.sendAsync(request(port, members, assign, access), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(request(port, members, assign, access), HttpResponse.BodyHandlers.ofString());
        var ar = a.get(60, TimeUnit.SECONDS);
        var br = b.get(60, TimeUnit.SECONDS);
        assertThat(List.of(ar.statusCode(), br.statusCode())).containsExactlyInAnyOrder(201, 409);
        var winner = ar.statusCode() == 201 ? ar : br;
        long membership = ((Number) JsonPath.read(winner.body(), "$.id")).longValue();
        assertThat((Boolean) JsonPath.read(winner.body(), "$.active")).isTrue();
        assertThat(((Number) JsonPath.read(winner.body(), "$.grantedBy")).longValue()).isEqualTo(admin);
        assertSafe(send(port, members, assign, access), 409);
        assertThat(send(port, permissions, null, memberAccess).body()).isEqualTo(beforePermissions.body());
        assertSafe(send(port, base + "/" + second + "/memberships/" + membership + "/revoke", "", access), 403);
        var history = send(port, members, null, access);
        assertSafe(history, 200);
        assertThat((List<Number>) JsonPath.read(history.body(), "$[*].id")).hasSize(4);
        assertThat((List<Number>) JsonPath.read(send(port, members + "?afterId=" + membership, null, access).body(), "$[*].id")).isEmpty();

        // A persisted future group grant is not inherited in this slice and blocks new membership.
        new TransactionTemplate(transactions).executeWithoutResult(tx -> entityManager.persist(new GroupRoleGrant(
                entityManager.find(Group.class, group), entityManager.find(Role.class, companyRole),
                AuthorizationScope.global(company), "synthetic future grant", now.plusSeconds(3600), null,
                entityManager.find(User.class, admin))));
        assertSafe(send(port, members, memberBody(fresh), access), 409);
        assertThat(send(port, permissions, null, memberAccess).body()).isEqualTo(beforePermissions.body());
        jdbc.execute("CREATE TRIGGER hyflo_test_group_retire_failure BEFORE INSERT ON T_10_01_23 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, selected + "/retire", reason, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_retire_failure");
        }
        assertThat((Boolean) JsonPath.read(send(port, selected, null, access).body(), "$.enabled")).isTrue();
        long membershipCount = count("T_10_01_13");
        long grantCount = count("T_10_01_16");
        assertSafe(send(port, selected + "/retire", reason, access), 200);
        assertSafe(send(port, selected + "/retire", reason, access), 409);
        assertSafe(send(port, selected + "/rename", rename, access), 409);
        assertSafe(send(port, members, memberBody(fresh), access), 409);
        assertThat(count("T_10_01_13")).isEqualTo(membershipCount);
        assertThat(count("T_10_01_16")).isEqualTo(grantCount);
        assertThat((List<Boolean>) JsonPath.read(send(port, members, null, access).body(), "$[*].active")).containsOnly(false);
        jdbc.execute("CREATE TRIGGER hyflo_test_group_member_update BEFORE UPDATE ON T_10_01_13 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, members + "/" + membership + "/revoke", "", access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_member_update");
        }
        assertThat(jdbc.queryForObject("SELECT F_03 IS NULL FROM T_10_01_13 WHERE F_00=?", Boolean.class, membership)).isTrue();
        assertSafe(send(port, members + "/" + membership + "/revoke", "", access), 204);
        assertSafe(send(port, members + "/" + membership + "/revoke", "", access), 409);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var m = entityManager.find(GroupMembership.class, membership);
            assertThat(m.getGrantedBy().getId()).isEqualTo(admin);
            assertThat(m.getRevokedBy().getId()).isEqualTo(admin);
            assertThat(m.isActiveAt(m.getRevokedAt())).isFalse();
            var events = entityManager.createQuery("select e from GroupDefinitionEvent e where e.group.id=:id order by e.id",
                    GroupDefinitionEvent.class).setParameter("id", group).getResultList();
            assertThat(events).extracting(GroupDefinitionEvent::getEventCode).containsExactly("CREATED", "RENAMED_FROM", "RENAMED", "RETIRED");
            assertThat(events).extracting(GroupDefinitionEvent::getDisplayName).containsExactly("Configured group", "Configured group", "Configured name", "Configured name");
            assertThat(events).allSatisfy(e -> {
                assertThat(e.getPerformedBy().getId()).isEqualTo(admin);
                assertThat(e.getGroupCode()).isEqualTo("OPERATORS");
                assertThat(e.getCorrelationId()).isNotBlank();
                assertThat(e.getOccurredAt()).isNotNull();
            });
            assertThat(events.get(1).getCorrelationId()).isEqualTo(events.get(2).getCorrelationId());
            assertThat(events.get(1).getOccurredAt()).isEqualTo(events.get(2).getOccurredAt());
            assertThat(events.getLast().isEnabled()).isFalse();
        });

        String secondPath = base + "/" + second;
        var retirement = client.sendAsync(request(port, secondPath + "/retire", reason, access), HttpResponse.BodyHandlers.ofString());
        var assignment = client.sendAsync(request(port, secondPath + "/memberships", assign, access), HttpResponse.BodyHandlers.ofString());
        assertThat(retirement.get(60, TimeUnit.SECONDS).statusCode()).isEqualTo(200);
        assertThat(assignment.get(60, TimeUnit.SECONDS).statusCode()).isIn(201, 409);
        assertSafe(send(port, secondPath + "/memberships", memberBody(other), access), 409);
        Map<String,Object> paths = JsonPath.read(send(port, "/api-docs", null, access).body(), "$.paths");
        assertThat(paths.get(base).toString()).contains("get=", "post=");
        assertThat(paths.get(base + "/{groupId}/memberships").toString()).contains("get=", "post=");
        assertSafe(send(port, "/v1/auth/logout", "", access), 204);
        assertSafe(send(port, base, null, access), 401);
    }

    private String groupBody(String code) {
        return "{\"code\":\"" + code + "\",\"displayName\":\"Configured group\",\"reason\":\"synthetic group creation\"}";
    }

    private long createGroup(String code, String access) throws Exception {
        var response = send(port, "/v1/platform/groups", groupBody(code), access);
        assertSafe(response, 201);
        return ((Number) JsonPath.read(response.body(), "$.id")).longValue();
    }

    private static String memberBody(long user) {
        return "{\"userId\":" + user + "}";
    }

    private long groupMembershipFixture(long actor, long user, long group, Instant from, Instant to) {
        return new TransactionTemplate(transactions).execute(tx -> {
            var membership = new GroupMembership(entityManager.find(User.class, user), entityManager.find(Group.class, group),
                    from, to, entityManager.find(User.class, actor));
            entityManager.persist(membership);
            return membership.getId();
        });
    }


    @Test
    void assignsGroupRolesAndResolvesLiveInheritedAuthority() throws Exception {
        assertEmptyIdentity();
        assertEmptyOrganization();
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("INHERIT_COMPANY", "inherit-admin",
                "INHERIT_AUTHORITY"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='inherit-admin'", Long.class);
        long platformRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_04=TRUE", Long.class);
        String access = loginAccess("inherit-admin");
        long member = createCompositionUser(admin, company, "inherit-member");
        long second = createCompositionUser(admin, company, "inherit-second");
        long target = createCompositionUser(admin, company, "inherit-target");
        long delegate = createCompositionUser(admin, company, "inherit-delegate");
        String memberAccess = loginAccess("inherit-member");
        String secondAccess = loginAccess("inherit-second");
        String delegateAccess = loginAccess("inherit-delegate");
        String reason = "{\"reason\":\"synthetic inheritance\"}";
        long role = createRuntimeRole("GROUP_OPERATIONS", false, access);
        long empty = createRuntimeRole("GROUP_EMPTY", false, access);
        long limited = createRuntimeRole("GROUP_ASSIGN_ONLY", true, access);
        for (String code : List.of("user.read", "user.suspend", "user-role-grant.assign"))
            assertSafe(send(port, changePath(role, permissionId(code), true), reason, access), 204);
        for (String code : List.of("platform-group-role-grant.assign", "platform-group-membership.assign"))
            assertSafe(send(port, changePath(limited, permissionId(code), true), reason, access), 204);
        assertSafe(send(port, platformGrantPath(delegate), platformGrantBody(limited), access), 201);
        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS);
        Instant end = now.plusSeconds(3600);
        long group = createGroup("INHERITED_OPERATIONS", access);
        long otherGroup = createGroup("INHERITED_OTHER", access);
        long foreignCompany = new TransactionTemplate(transactions).execute(tx -> {
            var type = entityManager.createQuery("select t from OrganizationUnitType t where t.code='COMPANY'",
                    OrganizationUnitType.class).getSingleResult();
            var active = entityManager.createQuery("select s from OrganizationUnitStatus s where s.code='ACTIVE'",
                    OrganizationUnitStatus.class).getSingleResult();
            var unit = new OrganizationUnit("INHERIT_FOREIGN", null, null, "Synthetic foreign company",
                    type, null, LocalDate.now(ZoneOffset.UTC).minusDays(1), null, active);
            entityManager.persist(unit);
            return unit.getId();
        });
        long foreign = createCompositionUser(admin, foreignCompany, "inherit-foreign");
        String foreignAccess = loginAccess("inherit-foreign");
        String base = "/v1/platform/groups/" + group;
        String selector = "?companyId=" + company;
        String grants = base + "/role-grants";
        String requestBody = groupGrantBody(role, end);
        String permissions = "/v1/me/permissions" + selector;
        String protectedRead = "/v1/identity/users/" + target + selector;
        String suspend = "/v1/identity/users/" + target + "/suspend" + selector;
        assertSafe(send(port, grants + selector, null, null), 401);
        assertSafe(send(port, grants + selector, requestBody, "invalid.jwt"), 401);
        assertSafe(send(port, grants + selector, null, memberAccess), 403);
        assertSafe(send(port, grants + selector, requestBody, memberAccess), 403);
        assertSafe(send(port, grants + selector, null, delegateAccess), 403);
        assertSafe(send(port, grants + selector, requestBody, delegateAccess), 403);
        assertSafe(send(port, grants, null, access), 400);
        assertSafe(send(port, grants + selector + "&afterId=-1", null, access), 400);
        assertSafe(send(port, grants + selector, "{}", access), 400);
        assertSafe(send(port, grants + selector, groupGrantBody(role, now.minusSeconds(5)), access), 400);
        assertSafe(send(port, grants + selector, groupGrantBody(platformRole, end), access), 409);
        assertSafe(send(port, grants + selector, groupGrantBody(empty, end), access), 409);
        assertSafe(send(port, grants + selector, requestBody, access), 403);
        var memberResponse = send(port, base + "/memberships", memberBody(member), access);
        assertSafe(memberResponse, 201);
        long membership = ((Number) JsonPath.read(memberResponse.body(), "$.id")).longValue();
        assertSafe(send(port, permissions, null, memberAccess), 403);
        addCompanyFixtureGrant(admin, admin, role, company, now.minusSeconds(5), now.plusSeconds(7200));
        assertSafe(send(port, grants + selector, groupGrantBody(role, now.plusSeconds(10800)), access), 403);
        String otherBase = "/v1/platform/groups/" + otherGroup;
        var foreignMembershipResponse = send(port, otherBase + "/memberships", memberBody(foreign), access);
        assertSafe(foreignMembershipResponse, 201);
        long foreignMembership = ((Number) JsonPath.read(foreignMembershipResponse.body(), "$.id")).longValue();
        assertSafe(send(port, otherBase + "/role-grants" + selector, requestBody, access), 409);
        assertSafe(send(port, otherBase + "/memberships/" + foreignMembership + "/revoke", "", access), 204);
        long selfMembership = groupMembershipFixture(admin, admin, otherGroup, now.plusSeconds(60), null);
        assertSafe(send(port, otherBase + "/role-grants" + selector, requestBody, access), 409);
        assertSafe(send(port, otherBase + "/memberships/" + selfMembership + "/revoke", "", access), 204);
        long before = count("T_10_01_16");
        jdbc.execute("CREATE TRIGGER hyflo_test_group_grant_insert BEFORE INSERT ON T_10_01_16 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, grants + selector, requestBody, access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_grant_insert");
        }
        assertThat(count("T_10_01_16")).isEqualTo(before);
        var a = client.sendAsync(request(port, grants + selector, requestBody, access), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(request(port, grants + selector, requestBody, access), HttpResponse.BodyHandlers.ofString());
        var ar = a.get(60, TimeUnit.SECONDS);
        var br = b.get(60, TimeUnit.SECONDS);
        assertThat(List.of(ar.statusCode(), br.statusCode())).containsExactlyInAnyOrder(201, 409);
        long grant = ((Number) JsonPath.read((ar.statusCode() == 201 ? ar : br).body(), "$.id")).longValue();
        assertThat(count("T_10_01_16")).isEqualTo(before + 1);
        assertSafe(send(port, grants + selector, requestBody, access), 409);
        var history = send(port, grants + selector, null, access);
        assertSafe(history, 200);
        assertThat((List<Number>) JsonPath.read(history.body(), "$[*].id")).extracting(Number::longValue).containsExactly(grant);
        assertSafe(send(port, grants + selector + "&afterId=" + grant, null, access), 200);
        assertSafe(send(port, grants + "?companyId=" + Long.MAX_VALUE, null, access), 403);
        assertSafe(send(port, "/v1/platform/groups/" + otherGroup + "/role-grants/" + grant + "/revoke" + selector, "", access), 403);
        String revoke = grants + "/" + grant + "/revoke" + selector;
        assertSafe(send(port, revoke, "", delegateAccess), 403);
        assertSafe(send(port, protectedRead, null, memberAccess), 200);
        assertThat((List<String>) JsonPath.read(send(port, permissions, null, memberAccess).body(), "$.permissionCodes"))
                .containsExactly("user-role-grant.assign", "user.read", "user.suspend");
        assertSafe(send(port, "/v1/platform/groups", null, memberAccess), 403);
        assertSafe(send(port, "/v1/identity/users/" + target + "/role-grants" + selector, requestBody, memberAccess), 403);
        assertSafe(send(port, suspend, reason, memberAccess), 200);
        assertSafe(send(port, base + "/memberships", memberBody(foreign), access), 409);
        assertSafe(send(port, permissions, null, foreignAccess), 403);
        assertSafe(send(port, base + "/memberships", memberBody(second), delegateAccess), 403);
        var secondMembershipResponse = send(port, base + "/memberships", memberBody(second), access);
        assertSafe(secondMembershipResponse, 201);
        long secondMembership = ((Number) JsonPath.read(secondMembershipResponse.body(), "$.id")).longValue();
        assertSafe(send(port, protectedRead, null, secondAccess), 200);
        long directGrant = addCompanyFixtureGrant(admin, second, role, company, now.minusSeconds(5), null);
        assertThat((List<String>) JsonPath.read(send(port, permissions, null, secondAccess).body(), "$.permissionCodes"))
                .containsExactly("user-role-grant.assign", "user.read", "user.suspend");
        assertSafe(send(port, base + "/memberships/" + secondMembership + "/revoke", "", access), 204);
        assertSafe(send(port, protectedRead, null, secondAccess), 200);
        assertSafe(send(port, "/v1/identity/users/" + second + "/role-grants/" + directGrant + "/revoke" + selector, "", access), 204);
        assertSafe(send(port, protectedRead, null, secondAccess), 403);

        jdbc.execute("CREATE TRIGGER hyflo_test_group_grant_update BEFORE UPDATE ON T_10_01_16 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT='synthetic rollback probe'");
        try {
            assertSafe(send(port, revoke, "", access), 503);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_group_grant_update");
        }
        assertSafe(send(port, protectedRead, null, memberAccess), 200);
        assertSafe(send(port, revoke, "", access), 204);
        assertSafe(send(port, revoke, "", access), 409);
        assertSafe(send(port, protectedRead, null, memberAccess), 403);
        assertThat(count("T_10_01_16")).isEqualTo(before + 1);

        // Isolate membership validity: the role grant is active, but the member has no active
        // membership or direct grant. Test both HTTP reads and mutations with the same JWT.
        long temporalGroup = createGroup("INHERITED_TEMPORAL", access);
        String temporalBase = "/v1/platform/groups/" + temporalGroup;
        var temporalGrant = send(port, temporalBase + "/role-grants" + selector, requestBody, access);
        assertSafe(temporalGrant, 201);
        long temporalGrantId = ((Number) JsonPath.read(temporalGrant.body(), "$.id")).longValue();
        assertSafe(send(port, protectedRead, null, secondAccess), 403);
        long expiredMembership = groupMembershipFixture(admin, second, temporalGroup,
                now.minusSeconds(7200), now.minusSeconds(3600));
        assertSafe(send(port, protectedRead, null, secondAccess), 403);
        assertSafe(send(port, suspend, reason, secondAccess), 403);
        long futureMembership = groupMembershipFixture(admin, second, temporalGroup,
                now.plusSeconds(3600), now.plusSeconds(7200));
        assertSafe(send(port, protectedRead, null, secondAccess), 403);
        assertSafe(send(port, suspend, reason, secondAccess), 403);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var expired = entityManager.find(GroupMembership.class, expiredMembership);
            var future = entityManager.find(GroupMembership.class, futureMembership);
            var activeGrant = entityManager.find(GroupRoleGrant.class, temporalGrantId);
            assertThat(activeGrant.isActiveAt(Instant.now())).isTrue();
            assertThat(expired.isActiveAt(expired.getValidFrom())).isTrue();
            assertThat(expired.isActiveAt(expired.getValidTo())).isFalse();
            assertThat(future.isActiveAt(future.getValidFrom().minusNanos(1))).isFalse();
            assertThat(future.isActiveAt(future.getValidFrom())).isTrue();
            assertThat(activeGrant.isActiveAt(activeGrant.getValidFrom())).isTrue();
            assertThat(activeGrant.isActiveAt(activeGrant.getValidTo())).isFalse();
        });
        assertSafe(send(port, temporalBase + "/role-grants/" + temporalGrantId + "/revoke" + selector, "", access), 204);

        // Stored inactive and resource-scoped grants must not confer company authority.
        groupMembershipFixture(admin, member, otherGroup, now.minusSeconds(10), null);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var g = entityManager.find(Group.class, otherGroup);
            var r = entityManager.find(Role.class, role);
            var actor = entityManager.find(User.class, admin);
            entityManager.persist(new GroupRoleGrant(g, r, AuthorizationScope.global(company), "expired",
                    now.minusSeconds(7200), now.minusSeconds(3600), actor));
            entityManager.persist(new GroupRoleGrant(g, r, AuthorizationScope.global(company), "future",
                    now.plusSeconds(3600), now.plusSeconds(7200), actor));
            entityManager.persist(new GroupRoleGrant(g, r, AuthorizationScope.pipeline(company, 1), "resource scope",
                    now.minusSeconds(10), end, actor));
        });
        assertSafe(send(port, protectedRead, null, memberAccess), 403);
        assertSafe(send(port, otherBase + "/role-grants" + selector, requestBody, access), 409);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var persisted = entityManager.find(GroupRoleGrant.class, grant);
            assertThat(persisted.getGrantedBy().getId()).isEqualTo(admin);
            assertThat(persisted.getRevokedBy().getId()).isEqualTo(admin);
            assertThat(persisted.getReason()).isEqualTo("synthetic group grant");
            assertThat(persisted.isActiveAt(persisted.getRevokedAt())).isFalse();
            assertThat(persisted.overlapsInterval(persisted.getRevokedAt(), end)).isFalse();
        });
        assertSafe(send(port, grants + selector, requestBody, access), 201);
        assertSafe(send(port, protectedRead, null, memberAccess), 200);
        assertSafe(send(port, base + "/memberships/" + membership + "/revoke", "", access), 204);
        assertSafe(send(port, protectedRead, null, memberAccess), 403);
        assertSafe(send(port, base + "/memberships", memberBody(member), access), 201);
        assertSafe(send(port, protectedRead, null, memberAccess), 200);
        assertSafe(send(port, changePath(role, permissionId("role.read"), true), reason, access), 409);
        // The existing resolver excludes injected platform permission links from company inheritance.
        jdbc.update("INSERT INTO T_10_01_14 (F_01,F_02) VALUES (?,?)", role, permissionId("platform-role.read"));
        assertThat((List<String>) JsonPath.read(send(port, permissions, null, memberAccess).body(), "$.permissionCodes"))
                .containsExactly("user-role-grant.assign", "user.read", "user.suspend");
        assertSafe(send(port, "/v1/platform/roles", null, memberAccess), 403);
        assertSafe(send(port, changePath(role, permissionId("platform-role.read"), false), reason, access), 204);

        // Unsupported but structurally valid and malformed company permissions must not
        // leak through inheritance. Preserve the valid links and remove only the test probes.
        long[] invalidLinks = new TransactionTemplate(transactions).execute(tx -> {
            var action = new Action("inherit-probe", "Synthetic inheritance probe");
            entityManager.persist(action);
            var resource = new ResourceType("inherit-probe", "Synthetic inheritance probe");
            entityManager.persist(resource);
            var unsupported = new Permission("inherit-probe.inherit-probe", "Unsupported", action, resource);
            // Each permission must retain a distinct persisted action/resource pair (UK_02).
            var malformedResource = new ResourceType("malformed-inherit-probe", "Synthetic malformed inheritance probe");
            entityManager.persist(malformedResource);
            var malformed = new Permission("wrong-inherit.inherit-probe", "Malformed", action, malformedResource);
            entityManager.persist(unsupported);
            entityManager.persist(malformed);
            var inheritedRole = entityManager.find(Role.class, role);
            entityManager.persist(new RolePermission(inheritedRole, unsupported));
            entityManager.persist(new RolePermission(inheritedRole, malformed));
            return new long[]{unsupported.getId(), malformed.getId()};
        });
        try {
            var resolved = send(port, permissions, null, memberAccess);
            assertSafe(resolved, 200);
            assertThat((List<String>) JsonPath.read(resolved.body(), "$.permissionCodes"))
                    .containsExactly("user-role-grant.assign", "user.read", "user.suspend");
            assertSafe(send(port, protectedRead, null, memberAccess), 200);
        } finally {
            for (long permission : invalidLinks) {
                assertThat(jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", role, permission))
                        .isEqualTo(1);
            }
        }

        // No cached inherited authority survives account, company or session ineligibility.
        jdbc.update("UPDATE T_10_01_08 SET F_07=(SELECT F_00 FROM T_10_01_02 WHERE F_01='DISABLED') WHERE F_00=?", member);
        try {
            assertSafe(send(port, protectedRead, null, memberAccess), 401);
            assertSafe(send(port, suspend, reason, memberAccess), 401);
        } finally {
            jdbc.update("UPDATE T_10_01_08 SET F_07=(SELECT F_00 FROM T_10_01_02 WHERE F_01='ACTIVE') WHERE F_00=?", member);
        }
        assertSafe(send(port, protectedRead, null, memberAccess), 200);
        jdbc.update("UPDATE T_10_02_01 SET F_08=FALSE WHERE F_01='COMPANY'");
        try {
            assertSafe(send(port, permissions, null, memberAccess), 403);
            assertSafe(send(port, protectedRead, null, memberAccess), 403);
            assertSafe(send(port, suspend, reason, memberAccess), 403);
        } finally {
            jdbc.update("UPDATE T_10_02_01 SET F_08=TRUE WHERE F_01='COMPANY'");
        }
        assertSafe(send(port, protectedRead, null, memberAccess), 200);
        assertSafe(send(port, "/v1/auth/logout", "", memberAccess), 204);
        assertSafe(send(port, protectedRead, null, memberAccess), 401);
        assertSafe(send(port, suspend, reason, memberAccess), 401);
        memberAccess = loginAccess("inherit-member");
        assertSafe(send(port, protectedRead, null, memberAccess), 200);

        long raceGroup = createGroup("INHERITED_RACE", access);
        String raceBase = "/v1/platform/groups/" + raceGroup;
        var grantRace = client.sendAsync(request(port, raceBase + "/role-grants" + selector, requestBody, access),
                HttpResponse.BodyHandlers.ofString());
        var memberRace = client.sendAsync(request(port, raceBase + "/memberships", memberBody(second), access),
                HttpResponse.BodyHandlers.ofString());
        assertThat(grantRace.get(60, TimeUnit.SECONDS).statusCode()).isEqualTo(201);
        assertThat(memberRace.get(60, TimeUnit.SECONDS).statusCode()).isEqualTo(201);
        assertSafe(send(port, protectedRead, null, secondAccess), 200);
        assertSafe(send(port, raceBase + "/retire", reason, access), 200);
        assertSafe(send(port, protectedRead, null, secondAccess), 403);

        // Revoke inherited authority concurrently with a company write; either ordered outcome is safe.
        long writeGroup = createGroup("INHERITED_WRITE_RACE", access);
        String writeBase = "/v1/platform/groups/" + writeGroup;
        assertSafe(send(port, writeBase + "/memberships", memberBody(second), access), 201);
        var writeGrant = send(port, writeBase + "/role-grants" + selector, requestBody, access);
        assertSafe(writeGrant, 201);
        long writeGrantId = ((Number) JsonPath.read(writeGrant.body(), "$.id")).longValue();
        assertSafe(send(port, "/v1/identity/users/" + target + "/reactivate" + selector, reason, access), 200);
        long priorSuspensions = jdbc.queryForObject(
                "SELECT COUNT(*) FROM T_10_01_18 WHERE F_01='SUSPENDED' AND F_08=? AND F_09=?", Long.class, target, second);
        var mutation = client.sendAsync(request(port, suspend, reason, secondAccess), HttpResponse.BodyHandlers.ofString());
        var removal = client.sendAsync(request(port, writeBase + "/role-grants/" + writeGrantId + "/revoke" + selector,
                "", access), HttpResponse.BodyHandlers.ofString());
        assertThat(removal.get(60, TimeUnit.SECONDS).statusCode()).isEqualTo(204);
        int mutationStatus = mutation.get(60, TimeUnit.SECONDS).statusCode();
        assertThat(mutationStatus).isIn(200, 403, 503);
        assertSafe(send(port, suspend, reason, secondAccess), 403);
        // A successful ordered write must persist its state and exactly one actor-attributed
        // event. A denied or rolled-back write must persist neither.
        var persistedTarget = send(port, protectedRead, null, access);
        assertSafe(persistedTarget, 200);
        assertThat((String) JsonPath.read(persistedTarget.body(), "$.userStatusCode"))
                .isEqualTo(mutationStatus == 200 ? "SUSPENDED" : "ACTIVE");
        assertThat(jdbc.queryForObject(
                "SELECT COUNT(*) FROM T_10_01_18 WHERE F_01='SUSPENDED' AND F_08=? AND F_09=?", Long.class, target, second))
                .isEqualTo(priorSuspensions + (mutationStatus == 200 ? 1L : 0L));
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var removed = entityManager.find(GroupRoleGrant.class, writeGrantId);
            assertThat(removed.getRevokedAt()).isNotNull();
            assertThat(removed.getRevokedBy().getId()).isEqualTo(admin);
        });
        assertSafe(send(port, writeBase + "/role-grants" + selector, requestBody, access), 201);
        assertSafe(send(port, protectedRead, null, secondAccess), 200);
        // Promotion invalidates inherited company authority without deleting membership.
        assertSafe(send(port, platformGrantPath(second), platformGrantBody(limited), access), 201);
        assertSafe(send(port, protectedRead, null, secondAccess), 403);

        assertSafe(send(port, changePath(role, permissionId("user.read"), false), reason, access), 204);
        assertSafe(send(port, protectedRead, null, memberAccess), 403);
        assertSafe(send(port, "/v1/platform/roles/" + role + "/retire", reason, access), 200);
        assertSafe(send(port, permissions, null, memberAccess), 403);
        var paths = (Map<String,Object>) JsonPath.read(send(port, "/api-docs", null, access).body(), "$.paths");
        assertThat(paths.get("/v1/platform/groups/{groupId}/role-grants").toString()).contains("get=", "post=");
        assertThat(paths.get("/v1/platform/groups/{groupId}/role-grants/{grantId}/revoke").toString()).contains("post=");
    }

    @Test
    void consultsOwnProfileAndChangesOnlyContactEmailWithLiveEvidence() throws Exception {
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("PROFILE_COMPANY", "profile-admin",
                "PROFILE_AUTHORITY"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='profile-admin'", Long.class);
        long member = createCompositionUser(admin, company, "profile-member");
        long second = createCompositionUser(admin, company, "profile-second");
        long foreignCompany = new TransactionTemplate(transactions).execute(tx -> {
            var type = entityManager.createQuery("select t from OrganizationUnitType t where t.code='COMPANY'",
                    OrganizationUnitType.class).getSingleResult();
            var active = entityManager.createQuery("select s from OrganizationUnitStatus s where s.code='ACTIVE'",
                    OrganizationUnitStatus.class).getSingleResult();
            var unit = new OrganizationUnit("PROFILE_FOREIGN", null, null, "Synthetic foreign company",
                    type, null, LocalDate.now(ZoneOffset.UTC).minusDays(1), null, active);
            entityManager.persist(unit);
            return unit.getId();
        });
        long foreign = createCompositionUser(admin, foreignCompany, "profile-foreign");
        long service = new TransactionTemplate(transactions).execute(tx -> {
            User owner = entityManager.find(User.class, admin);
            User user = new User(owner.getIdentityProvider(), "profile-service", "service@example.invalid", null, company,
                    entityManager.createQuery("select s from UserStatus s where s.code='PENDING'", UserStatus.class).getSingleResult(),
                    entityManager.createQuery("select t from UserType t where t.code='SERVICE'", UserType.class).getSingleResult());
            entityManager.persist(user);
            var credential = new LocalCredential(user, passwordEncoder.encode(SYNTHETIC_PASSWORD), Instant.now());
            entityManager.persist(credential);
            user.activate(credential, entityManager.createQuery("select s from UserStatus s where s.code='ACTIVE'",
                    UserStatus.class).getSingleResult());
            return user.getId();
        });
        String access = loginAccess("profile-member");
        String secondAccess = loginAccess("profile-second");
        String foreignAccess = loginAccess("profile-foreign");
        String adminAccess = loginAccess("profile-admin");
        String serviceAccess = loginAccess("profile-service");
        String path = "/v1/me/profile", emailPath = path + "/email";
        assertSafe(send(port, path, null, null), 401);
        assertSafe(send(port, emailPath, profileEmailBody("new@example.invalid", SYNTHETIC_PASSWORD), "invalid.jwt"), 401);
        assertSafe(send(port, path + "?userId=" + foreign, null, access), 400);
        assertSafe(send(port, emailPath + "?companyId=" + foreignCompany,
                profileEmailBody("new@example.invalid", SYNTHETIC_PASSWORD), access), 400);
        var read = send(port, path, null, access);
        assertSafe(read, 200);
        Map<String, Object> projection = JsonPath.read(read.body(), "$");
        assertThat(projection.keySet()).containsExactlyInAnyOrder("userId", "username", "email", "companyId",
                "userTypeCode", "userStatusCode");
        assertThat(((Number) projection.get("userId")).longValue()).isEqualTo(member);
        assertThat(((Number) projection.get("companyId")).longValue()).isEqualTo(company);
        assertThat(projection.get("username")).isEqualTo("profile-member");
        assertThat(projection.get("email")).isEqualTo("profile-member@example.invalid");
        assertThat(projection.get("userTypeCode")).isEqualTo("HUMAN");
        assertThat(projection.get("userStatusCode")).isEqualTo("ACTIVE");
        assertSafe(send(port, path, null, adminAccess), 200);
        var serviceProfile = send(port, path, null, serviceAccess);
        assertSafe(serviceProfile, 200);
        assertThat((String) JsonPath.read(serviceProfile.body(), "$.userTypeCode")).isEqualTo("SERVICE");
        assertThat(((Number) JsonPath.read(serviceProfile.body(), "$.userId")).longValue()).isEqualTo(service);
        assertSafe(send(port, emailPath, profileEmailBody("new@example.invalid", SYNTHETIC_PASSWORD), serviceAccess), 403);
        for (String malformed : List.of("{}", "{\"email\":\"x@example.invalid\",\"currentPassword\":\"x\",\"userId\":" + foreign + "}",
                "{\"email\":\"x@example.invalid\",\"currentPassword\":\"x\",\"profilePictureFileId\":1}",
                profileEmailBody("not-an-email", SYNTHETIC_PASSWORD),
                profileEmailBody("HYFLO-BOOTSTRAP@hyflo.invalid", SYNTHETIC_PASSWORD),
                profileEmailBody(" bad@example.invalid", SYNTHETIC_PASSWORD),
                profileEmailBody("a".repeat(101) + "@example.invalid", SYNTHETIC_PASSWORD),
                profileEmailBody("x@example.invalid", "é".repeat(37)))) {
            assertSafe(send(port, emailPath, malformed, access), 400);
        }
        var before = jdbc.queryForMap("SELECT F_01,F_03,F_04,F_05,F_06,F_07,F_08 FROM T_10_01_08 WHERE F_00=?", member);
        var credentialBefore = jdbc.queryForMap("SELECT * FROM T_10_01_10 WHERE F_03=?", member);
        var sessionsBefore = jdbc.queryForList("SELECT * FROM T_10_01_11 WHERE F_04=?", member);
        long grants = count("T_10_01_15");
        long changed = profileEventCount("PROFILE_EMAIL_CHANGED");
        long failed = profileEventCount("PROFILE_EMAIL_FAILED");
        assertSafe(send(port, emailPath, profileEmailBody("Wrong@example.invalid", "wrong-password"), access), 401);
        assertThat(profileEventCount("PROFILE_EMAIL_FAILED")).isEqualTo(failed + 1);
        assertThat(jdbc.queryForObject("SELECT F_05 FROM T_10_01_12 WHERE F_01='PROFILE_EMAIL_FAILED'", Long.class)).isEqualTo(member);
        var updated = send(port, emailPath, profileEmailBody("New.Contact@example.invalid", SYNTHETIC_PASSWORD), access);
        assertSafe(updated, 200);
        assertThat((String) JsonPath.read(updated.body(), "$.email")).isEqualTo("New.Contact@example.invalid");
        assertThat((String) JsonPath.read(send(port, path, null, access).body(), "$.email")).isEqualTo("New.Contact@example.invalid");
        assertThat(profileEventCount("PROFILE_EMAIL_CHANGED")).isEqualTo(changed + 1);
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_08 WHERE F_00=?", String.class, member)).isEqualTo("New.Contact@example.invalid");
        assertThat(jdbc.queryForObject("SELECT F_03 FROM T_10_01_12 WHERE F_01='PROFILE_EMAIL_CHANGED'", String.class))
                .matches("[0-9a-f-]{36}");
        assertThat(jdbc.queryForObject("SELECT F_05 FROM T_10_01_12 WHERE F_01='PROFILE_EMAIL_CHANGED'", Long.class)).isEqualTo(member);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_12 WHERE F_01 LIKE 'PROFILE_EMAIL_%' AND F_06 IS NOT NULL", Long.class)).isZero();
        assertSafe(send(port, emailPath, profileEmailBody("New.Contact@example.invalid", SYNTHETIC_PASSWORD), access), 200);
        var conflict = send(port, emailPath, profileEmailBody("profile-foreign@example.invalid", SYNTHETIC_PASSWORD), access);
        assertSafe(conflict, 409);
        assertThat(conflict.body()).contains("ACCOUNT_OPERATION_REJECTED").doesNotContain("profile-foreign@example.invalid");
        assertThat(profileEventCount("PROFILE_EMAIL_CHANGED")).isEqualTo(changed + 1);
        assertThat(jdbc.queryForMap("SELECT F_01,F_03,F_04,F_05,F_06,F_07,F_08 FROM T_10_01_08 WHERE F_00=?", member)).isEqualTo(before);
        assertThat(jdbc.queryForMap("SELECT * FROM T_10_01_10 WHERE F_03=?", member)).isEqualTo(credentialBefore);
        assertThat(jdbc.queryForList("SELECT * FROM T_10_01_11 WHERE F_04=?", member)).isEqualTo(sessionsBefore);
        assertThat(count("T_10_01_15")).isEqualTo(grants);

        for (String trigger : List.of("BEFORE UPDATE ON T_10_01_08", "BEFORE INSERT ON T_10_01_12")) {
            jdbc.execute("CREATE TRIGGER hyflo_test_profile_failure " + trigger
                    + " FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
            try {
                assertSafe(send(port, emailPath, profileEmailBody("rollback@example.invalid", SYNTHETIC_PASSWORD), access), 503);
                assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_08 WHERE F_00=?", String.class, member)).isEqualTo("New.Contact@example.invalid");
                assertThat(profileEventCount("PROFILE_EMAIL_CHANGED")).isEqualTo(changed + 1);
            } finally {
                jdbc.execute("DROP TRIGGER hyflo_test_profile_failure");
            }
        }
        try (var executor = Executors.newFixedThreadPool(2)) {
            var ready = new CountDownLatch(2);
            var start = new CountDownLatch(1);
            var one = executor.submit(() -> {
                ready.countDown(); start.await();
                return send(port, emailPath, profileEmailBody("race@example.invalid", SYNTHETIC_PASSWORD), secondAccess);
            });
            var two = executor.submit(() -> {
                ready.countDown(); start.await();
                return send(port, emailPath, profileEmailBody("race@example.invalid", SYNTHETIC_PASSWORD), foreignAccess);
            });
            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();
            assertThat(List.of(one.get(60, TimeUnit.SECONDS).statusCode(), two.get(60, TimeUnit.SECONDS).statusCode()))
                    .containsExactlyInAnyOrder(200, 409);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_08 WHERE F_02='race@example.invalid'", Long.class)).isEqualTo(1L);
            assertThat(profileEventCount("PROFILE_EMAIL_CHANGED")).isEqualTo(changed + 2);
            var first = executor.submit(() -> send(port, emailPath, profileEmailBody("same-one@example.invalid", SYNTHETIC_PASSWORD), secondAccess));
            var last = executor.submit(() -> send(port, emailPath, profileEmailBody("same-two@example.invalid", SYNTHETIC_PASSWORD), secondAccess));
            assertSafe(first.get(60, TimeUnit.SECONDS), 200);
            assertSafe(last.get(60, TimeUnit.SECONDS), 200);
            assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_08 WHERE F_00=?", String.class, second))
                    .isIn("same-one@example.invalid", "same-two@example.invalid");
            assertThat(profileEventCount("PROFILE_EMAIL_CHANGED")).isEqualTo(changed + 4);
        }
        assertSafe(send(port, emailPath, profileEmailBody("Admin.Contact@example.invalid", SYNTHETIC_PASSWORD), adminAccess), 200);
        assertSafe(send(port, "/v1/auth/logout", "", access), 204);
        assertSafe(send(port, path, null, access), 401);
        assertSafe(send(port, emailPath, profileEmailBody("denied@example.invalid", SYNTHETIC_PASSWORD), access), 401);
        new TransactionTemplate(transactions).executeWithoutResult(tx ->
                entityManager.find(User.class, foreign).suspend(entityManager.createQuery(
                        "select s from UserStatus s where s.code='SUSPENDED'", UserStatus.class).getSingleResult()));
        assertSafe(send(port, path, null, foreignAccess), 401);
        var api = send(port, "/api-docs", null, adminAccess);
        assertThat(api.statusCode()).isEqualTo(200);
        Map<String, Object> apiPaths = JsonPath.read(api.body(), "$.paths");
        assertThat(((Map<?, ?>) apiPaths.get(path)).keySet().stream().map(Object::toString).toList()).containsExactly("get");
        assertThat(((Map<?, ?>) apiPaths.get(emailPath)).keySet().stream().map(Object::toString).toList()).containsExactly("post");
        assertSafe(send(port, path, "{}", adminAccess), 403);
        assertSafe(send(port, emailPath, null, adminAccess), 403);
        var limiter = applicationContext.getBean(dz.sh.hyflo.platform.security.LoginAttemptLimiter.class);
        Instant now = Instant.now();
        for (int n = 0; n < 10; n++) limiter.retryAfter("synthetic-test-peer", "profile-admin", now);
        assertSafe(send(port, emailPath, profileEmailBody("limited@example.invalid", SYNTHETIC_PASSWORD), adminAccess), 429);
    }


    @Test
    void consultsAuthorizedSecurityHistoryWithoutDisclosingOtherScopes() throws Exception {
        assertSafe(send(port, "/v1/setup/initialize", configuredBody("HISTORY_COMPANY", "history-admin",
                "HISTORY_ADMIN"), null, SETUP_SECRET), 204);
        long company = jdbc.queryForObject("SELECT F_00 FROM T_10_02_11", Long.class);
        long admin = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='history-admin'", Long.class);
        long reserved = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        String access = loginAccess("history-admin");
        long reader = createCompositionUser(admin, company, "history-reader");
        long inherited = createCompositionUser(admin, company, "history-inherited");
        long platformReader = createCompositionUser(admin, company, "history-platform");
        long observer = createCompositionUser(admin, company, "history-observer");
        long target = createCompositionUser(admin, company, "history-target");
        long[] companies = new TransactionTemplate(transactions).execute(tx -> {
            var type = entityManager.createQuery("select t from OrganizationUnitType t where t.code='COMPANY'",
                    OrganizationUnitType.class).getSingleResult();
            var active = entityManager.createQuery("select s from OrganizationUnitStatus s where s.code='ACTIVE'",
                    OrganizationUnitStatus.class).getSingleResult();
            var foreign = new OrganizationUnit("HISTORY_FOREIGN", null, null, "Synthetic foreign company",
                    type, null, LocalDate.now(ZoneOffset.UTC).minusDays(2), null, active);
            var expired = new OrganizationUnit("HISTORY_EXPIRED", null, null, "Synthetic expired company",
                    type, null, LocalDate.now(ZoneOffset.UTC).minusDays(3), LocalDate.now(ZoneOffset.UTC).minusDays(1), active);
            entityManager.persist(foreign);
            entityManager.persist(expired);
            return new long[]{foreign.getId(), expired.getId()};
        });
        long foreign = createCompositionUser(admin, companies[0], "history-foreign");
        long service = new TransactionTemplate(transactions).execute(tx -> {
            User owner = entityManager.find(User.class, admin);
            User user = new User(owner.getIdentityProvider(), "history-service", "history-service@example.invalid",
                    null, company, entityManager.createQuery("select s from UserStatus s where s.code='PENDING'", UserStatus.class).getSingleResult(),
                    entityManager.createQuery("select t from UserType t where t.code='SERVICE'", UserType.class).getSingleResult());
            entityManager.persist(user);
            var credential = new LocalCredential(user, passwordEncoder.encode(SYNTHETIC_PASSWORD), Instant.now());
            entityManager.persist(credential);
            user.activate(credential, entityManager.createQuery("select s from UserStatus s where s.code='ACTIVE'", UserStatus.class).getSingleResult());
            return user.getId();
        });
        String readerAccess = loginAccess("history-reader");
        String inheritedAccess = loginAccess("history-inherited");
        String platformAccess = loginAccess("history-platform");
        String observerAccess = loginAccess("history-observer");
        String foreignAccess = loginAccess("history-foreign");
        String serviceAccess = loginAccess("history-service");
        String selector = "?companyId=" + company;
        String companyBase = "/v1/identity/security-history/";
        String platformBase = "/v1/platform/security-history/";
        String auth = companyBase + "authentication";
        String reason = "{\"reason\":\"private synthetic history reason\"}";
        String rename = "{\"displayName\":\"Historical new name\",\"reason\":\"private synthetic history reason\"}";
        long historyRole = createRuntimeRole("HISTORY_READER", false, access);
        long catalogRole = createRuntimeRole("HISTORY_CATALOG", false, access);
        long platformRole = createRuntimeRole("HISTORY_PLATFORM_READER", true, access);
        long limitedRole = createRuntimeRole("HISTORY_PLATFORM_LIMITED", true, access);
        assertSafe(send(port, changePath(historyRole, permissionId("security-history.read"), true), reason, access), 204);
        assertSafe(send(port, changePath(catalogRole, permissionId("identity-catalog.read"), true), reason, access), 204);
        assertSafe(send(port, changePath(platformRole, permissionId("platform-security-history.read"), true), reason, access), 204);
        assertSafe(send(port, changePath(limitedRole, permissionId("platform-role.read"), true), reason, access), 204);
        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS);
        Instant end = now.plusSeconds(3600);
        addCompanyFixtureGrant(admin, admin, historyRole, company, now.minusSeconds(5), now.plusSeconds(7200));
        addCompanyFixtureGrant(admin, observer, catalogRole, company, now.minusSeconds(5), end);
        addCompanyFixtureGrant(admin, foreign, historyRole, companies[0], now.minusSeconds(5), end);
        addCompanyFixtureGrant(admin, admin, historyRole, companies[1], now.minusSeconds(5), end);
        assertSafe(send(port, auth + selector, null, readerAccess), 403);
        long direct = addCompanyFixtureGrant(admin, reader, historyRole, company, now.minusSeconds(5), end);
        assertSafe(send(port, auth + selector, null, readerAccess), 200);
        assertSafe(send(port, auth + selector, null, inheritedAccess), 403);
        long group = createGroup("HISTORY_GROUP", access);
        String groupPath = "/v1/platform/groups/" + group;
        var membership = send(port, groupPath + "/memberships", memberBody(inherited), access);
        assertSafe(membership, 201);
        long membershipId = ((Number) JsonPath.read(membership.body(), "$.id")).longValue();
        var grant = send(port, groupPath + "/role-grants" + selector, groupGrantBody(historyRole, end), access);
        assertSafe(grant, 201);
        assertSafe(send(port, auth + selector, null, inheritedAccess), 200);
        var platformGrant = send(port, platformGrantPath(platformReader), platformGrantBody(platformRole), access);
        assertSafe(platformGrant, 201);
        long platformGrantId = ((Number) JsonPath.read(platformGrant.body(), "$.id")).longValue();
        assertSafe(send(port, platformGrantPath(observer), platformGrantBody(limitedRole), access), 201);

        // Generate real evidence through existing workflows and interleave scoped synthetic records.
        assertSafe(send(port, "/v1/identity/users/" + target + "/suspend" + selector, reason, access), 200);
        assertSafe(send(port, "/v1/identity/users/" + target + "/disable" + selector, reason, access), 200);
        long firstSession = jdbc.queryForObject("SELECT MAX(F_00) FROM T_10_01_11 WHERE F_04=?", Long.class, reader);
        String readerCurrent = loginAccess("history-reader");
        assertSafe(send(port, "/v1/auth/sessions/" + firstSession + "/revoke", "", readerCurrent), 204);
        assertSafe(send(port, auth + selector, null, readerAccess), 401);
        assertSafe(send(port, "/v1/auth/login", "{\"username\":\"unknown-history\",\"password\":\"wrong-password\"}", null), 401);
        long renamedRole = createRuntimeRole("HISTORY_RENAMED", false, access);
        String rolePath = "/v1/platform/roles/" + renamedRole;
        assertSafe(send(port, rolePath + "/rename", rename, access), 200);
        assertSafe(send(port, rolePath + "/retire", reason, access), 200);
        assertSafe(send(port, groupPath + "/rename", rename, access), 200);
        var reconcile = send(port, "/v1/platform/capabilities/reconcile", reason, access);
        assertSafe(reconcile, 200);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            User actor = entityManager.find(User.class, admin);
            User own = entityManager.find(User.class, reader);
            User other = entityManager.find(User.class, foreign);
            User bootstrap = entityManager.find(User.class, reserved);
            var provider = actor.getIdentityProvider();
            for (int i = 0; i < 105; i++) {
                entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                        "LOGIN_FAILED", now, UUID.randomUUID().toString(), provider, own));
                entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                        "LOGIN_FAILED", now, UUID.randomUUID().toString(), provider, other));
            }
            entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent(
                    "LOGIN_FAILED", now, UUID.randomUUID().toString(), provider, bootstrap));
            own.linkEmployee(9001L);
            entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.UserEmployeeAssociationEvent(
                    "LINKED", now, UUID.randomUUID().toString(), "private synthetic history reason", null, own, actor));
            own.unlinkEmployee();
            entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.UserEmployeeAssociationEvent(
                    "UNLINKED", now, UUID.randomUUID().toString(), "private synthetic history reason", 9001L, own, actor));
            other.linkEmployee(9002L);
            entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.UserEmployeeAssociationEvent(
                    "LINKED", now, UUID.randomUUID().toString(), "private synthetic history reason", null, other, actor));
            other.unlinkEmployee();
            other.suspend(entityManager.createQuery("select s from UserStatus s where s.code='SUSPENDED'", UserStatus.class).getSingleResult());
            entityManager.persist(new dz.sh.hyflo.modules.foundation.identity.domain.model.UserLifecycleEvent(
                    "SUSPENDED", now, UUID.randomUUID().toString(), "private synthetic history reason", "ACTIVE", other, actor));
            entityManager.persist(new CapabilityReconciliationEvent(actor, now, UUID.randomUUID().toString(),
                    "private synthetic history reason", "historical-manifest", "a".repeat(64),
                    List.of("read"), List.of("historical-resource"), List.of("historical-resource.read")));
        });
        String[] companyRoutes = {"authentication", "account-lifecycle", "employee-associations"};
        String[] platformRoutes = {"role-definitions", "group-definitions", "capability-reconciliations", "unattributed-authentication"};
        List<String> tables = List.of("T_10_01_08","T_10_01_10","T_10_01_11","T_10_01_12","T_10_01_13",
                "T_10_01_14","T_10_01_15","T_10_01_16","T_10_01_18","T_10_01_19","T_10_01_20","T_10_01_21","T_10_01_22","T_10_01_23");
        Map<String,List<Map<String,Object>>> before = new HashMap<>();
        for (String table : tables) before.put(table, jdbc.queryForList("SELECT * FROM " + table + " ORDER BY F_00"));
        for (String route : companyRoutes) {
            String path = companyBase + route;
            assertSafe(send(port, path + selector, null, null), 401);
            assertSafe(send(port, path + selector, null, "invalid.jwt"), 401);
            assertSafe(send(port, path + selector, null, observerAccess), 403);
            assertSafe(send(port, path + selector, null, platformAccess), 403);
            assertSafe(send(port, path + selector, null, serviceAccess), 403);
            assertSafe(send(port, path + selector, null, foreignAccess), 401);
            assertSafe(send(port, path + "?companyId=" + companies[0], null, readerCurrent), 403);
            assertSafe(send(port, path + "?companyId=" + Long.MAX_VALUE, null, readerCurrent), 403);
            assertSafe(send(port, path + "?companyId=" + companies[1], null, access), 403);
            for (String invalid : List.of("", "?companyId=0", selector + "&companyId=" + company,
                    selector + "&afterId=-1", selector + "&afterId=99999999999999999999",
                    selector + "&afterId=1&afterId=2", selector + "&userId=" + foreign))
                assertSafe(send(port, path + invalid, null, readerCurrent), 400);
            assertSafe(send(port, path + selector, "{}", readerCurrent), 403);
            assertSafe(send(port, path + selector, null, inheritedAccess), 200);
        }
        for (String route : platformRoutes) {
            String path = platformBase + route;
            assertSafe(send(port, path, null, readerCurrent), 403);
            assertSafe(send(port, path, null, observerAccess), 403);
            assertSafe(send(port, path, null, serviceAccess), 403);
            assertSafe(send(port, path + selector, null, platformAccess), 400);
            assertSafe(send(port, path + "?afterId=-1", null, platformAccess), 400);
            assertSafe(send(port, path + "?afterId=1&afterId=2", null, platformAccess), 400);
            assertSafe(send(port, path, "{}", platformAccess), 403);
            assertSafe(send(port, path, null, platformAccess), 200);
        }
        var getWithBody = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/hyflo/api" + auth + selector))
                .header("Authorization", "Bearer " + readerCurrent).header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.ofString("{}")).build();
        assertSafe(client.send(getWithBody, HttpResponse.BodyHandlers.ofString()), 400);
        assertSafe(send(port, companyBase + "unregistered" + selector, null, access), 403);
        assertSafe(send(port, platformBase + "unregistered", null, access), 403);
        var first = send(port, auth + selector, null, readerCurrent);
        assertSafe(first, 200);
        List<Map<String,Object>> firstRows = JsonPath.read(first.body(), "$");
        assertThat(firstRows).hasSize(100).allSatisfy(row -> assertThat(row.keySet()).containsExactlyInAnyOrder(
                "id", "occurredAt", "correlationId", "eventCode", "userId", "identityProviderCode", "loginSessionId"));
        List<Long> ids = new java.util.ArrayList<>();
        firstRows.forEach(row -> ids.add(((Number) row.get("id")).longValue()));
        var tail = send(port, auth + selector + "&afterId=" + ids.getLast(), null, readerCurrent);
        assertSafe(tail, 200);
        List<Map<String,Object>> tailRows = JsonPath.read(tail.body(), "$");
        assertThat(tailRows).isNotEmpty();
        tailRows.forEach(row -> ids.add(((Number) row.get("id")).longValue()));
        assertThat(ids).isSorted().doesNotHaveDuplicates().containsExactlyElementsOf(jdbc.queryForList(
                "SELECT e.F_00 FROM T_10_01_12 e JOIN T_10_01_08 u ON e.F_05=u.F_00 WHERE u.F_05=? AND u.F_01<>'hyflo-bootstrap' ORDER BY e.F_00",
                Long.class, company));
        assertThat(send(port, auth + selector + "&afterId=" + ids.getLast(), null, readerCurrent).body()).isEqualTo("[]");
        assertThat(firstRows.stream().filter(row -> "SESSION_REVOKED".equals(row.get("eventCode"))).toList())
                .singleElement().satisfies(row -> assertThat(((Number)row.get("loginSessionId")).longValue()).isEqualTo(firstSession));
        var lifecycle = send(port, companyBase + "account-lifecycle" + selector, null, readerCurrent);
        assertSafe(lifecycle, 200);
        List<Map<String,Object>> life = JsonPath.read(lifecycle.body(), "$");
        assertThat(life).hasSize(2).allSatisfy(row -> {
            assertThat(row.keySet()).containsExactlyInAnyOrder("id","occurredAt","correlationId","eventCode","companyId","userId",
                    "performedByUserId","previousStatusCode","resultingStatusCode");
            assertThat(((Number)row.get("userId")).longValue()).isEqualTo(target);
            assertThat(((Number)row.get("performedByUserId")).longValue()).isEqualTo(admin);
            assertThat(((Number)row.get("companyId")).longValue()).isEqualTo(company);
        });
        assertThat(life.stream().map(row -> row.get("previousStatusCode")).toList()).containsExactly("ACTIVE","SUSPENDED");
        assertThat(life.stream().map(row -> row.get("resultingStatusCode")).toList()).containsExactly("SUSPENDED","DISABLED");
        var associations = send(port, companyBase + "employee-associations" + selector, null, readerCurrent);
        assertSafe(associations, 200);
        List<Map<String,Object>> links = JsonPath.read(associations.body(), "$");
        assertThat(links).hasSize(2).allSatisfy(row -> assertThat(row.keySet()).containsExactlyInAnyOrder(
                "id","occurredAt","correlationId","eventCode","companyId","userId","performedByUserId","previousEmployeeId","resultingEmployeeId"));
        assertThat(links.getFirst().get("previousEmployeeId")).isNull();
        assertThat(((Number)links.getFirst().get("resultingEmployeeId")).longValue()).isEqualTo(9001L);
        assertThat(((Number)links.getLast().get("previousEmployeeId")).longValue()).isEqualTo(9001L);
        assertThat(links.getLast().get("resultingEmployeeId")).isNull();
        var roles = send(port, platformBase + "role-definitions", null, platformAccess);
        assertSafe(roles, 200);
        List<Map<String,Object>> roleRows = JsonPath.read(roles.body(), "$");
        assertThat(roleRows).allSatisfy(row -> assertThat(row.keySet()).containsExactlyInAnyOrder(
                "id","occurredAt","correlationId","eventCode","roleId","roleCode","displayName","platformRole","performedByUserId","permissionId"));
        var renamed = roleRows.stream().filter(row -> ((Number)row.get("roleId")).longValue() == renamedRole).toList();
        assertThat(renamed.stream().map(row -> row.get("eventCode")).toList()).containsExactly("CREATED","RENAMED_FROM","RENAMED","RETIRED");
        assertThat(renamed.get(1).get("correlationId")).isEqualTo(renamed.get(2).get("correlationId"));
        assertThat(renamed.get(1).get("occurredAt")).isEqualTo(renamed.get(2).get("occurredAt"));
        assertThat(renamed.get(2).get("displayName")).isEqualTo("Historical new name");
        assertThat(roleRows.stream().anyMatch(row -> Boolean.TRUE.equals(row.get("platformRole")))).isTrue();
        var groups = send(port, platformBase + "group-definitions", null, platformAccess);
        assertSafe(groups, 200);
        List<Map<String,Object>> groupRows = JsonPath.read(groups.body(), "$");
        assertThat(groupRows).hasSize(3).allSatisfy(row -> assertThat(row.keySet()).containsExactlyInAnyOrder(
                "id","occurredAt","correlationId","eventCode","groupId","groupCode","displayName","enabled","performedByUserId"));
        assertThat(groupRows.get(1).get("correlationId")).isEqualTo(groupRows.get(2).get("correlationId"));
        var capabilities = send(port, platformBase + "capability-reconciliations", null, platformAccess);
        assertSafe(capabilities, 200);
        List<Map<String,Object>> capRows = JsonPath.read(capabilities.body(), "$");
        assertThat(capRows).hasSize(2).allSatisfy(row -> assertThat(row.keySet()).containsExactlyInAnyOrder(
                "id","occurredAt","correlationId","performedByUserId","manifestId","manifestDigest","insertedActionCodes",
                "insertedResourceTypeCodes","insertedPermissionCodes"));
        assertThat(capRows.getFirst().get("manifestDigest")).isEqualTo((String)JsonPath.read(reconcile.body(), "$.manifestDigest"));
        assertThat(capRows.getLast().get("insertedPermissionCodes")).isEqualTo(List.of("historical-resource.read"));
        assertThat(capRows.getLast().get("manifestId")).isEqualTo("historical-manifest");
        var unattributed = send(port, platformBase + "unattributed-authentication", null, platformAccess);
        assertSafe(unattributed, 200);
        List<Map<String,Object>> unknown = JsonPath.read(unattributed.body(), "$");
        assertThat(unknown).isNotEmpty().allSatisfy(row -> {
            assertThat(row.keySet()).containsExactlyInAnyOrder("id","occurredAt","correlationId","eventCode","identityProviderCode");
            assertThat(row.get("eventCode")).isEqualTo("LOGIN_FAILED");
        });
        assertThat(unknown.stream().map(row -> ((Number)row.get("id")).longValue()).toList()).containsExactlyElementsOf(
                jdbc.queryForList("SELECT F_00 FROM T_10_01_12 WHERE F_05 IS NULL AND F_01='LOGIN_FAILED' ORDER BY F_00", Long.class));
        for (var response : List.of(first,tail,lifecycle,associations,roles,groups,capabilities,unattributed))
            assertThat(response.body()).doesNotContain("private synthetic history reason","unknown-history","history-reader@example.invalid",
                    "history-foreign@example.invalid","passwordHash","refreshToken","\"reason\"");
        for (String table : tables) assertThat(jdbc.queryForList("SELECT * FROM " + table + " ORDER BY F_00")).as(table)
                .usingRecursiveComparison().isEqualTo(before.get(table));

        // Permission loss without JWT refresh and retained group retirement history.
        assertSafe(send(port, groupPath + "/memberships/" + membershipId + "/revoke", "", access), 204);
        assertSafe(send(port, auth + selector, null, inheritedAccess), 403);
        assertSafe(send(port, groupPath + "/retire", reason, access), 200);
        assertThat((List<String>)JsonPath.read(send(port, platformBase + "group-definitions", null, platformAccess).body(), "$[*].eventCode"))
                .contains("RETIRED");
        assertSafe(send(port, platformGrantPath(platformReader) + "/" + platformGrantId + "/revoke", reason, access), 204);
        assertSafe(send(port, platformBase + "role-definitions", null, platformAccess), 403);
        assertSafe(send(port, "/v1/identity/users/" + reader + "/role-grants/" + direct + "/revoke" + selector, "", access), 204);
        assertSafe(send(port, auth + selector, null, readerCurrent), 403);

        // A corrupted persisted snapshot must not produce a partial success or disclose its contents.
        long capId = ((Number)capRows.getLast().get("id")).longValue();
        jdbc.update("UPDATE T_10_01_22 SET F_08=? WHERE F_00=?", "invalid snapshot secret", capId);
        try {
            var bad = send(port, platformBase + "capability-reconciliations", null, access);
            assertSafe(bad, 503);
            assertThat(bad.body()).doesNotContain("invalid snapshot secret");
        } finally {
            jdbc.update("UPDATE T_10_01_22 SET F_08=? WHERE F_00=?", "historical-resource.read", capId);
        }
        var openapi = send(port, "/api-docs", null, access);
        assertThat(openapi.statusCode()).isEqualTo(200);
        Map<String,Object> apiPaths = JsonPath.read(openapi.body(), "$.paths");
        Map<String,Object> schemas = JsonPath.read(openapi.body(), "$.components.schemas");
        String[] names = {"Authentication","AccountLifecycle","EmployeeAssociation","RoleDefinition","GroupDefinition",
                "CapabilityReconciliation","UnattributedAuthentication"};
        List<String> allPaths = new java.util.ArrayList<>();
        for (String route : companyRoutes) allPaths.add(companyBase + route);
        for (String route : platformRoutes) allPaths.add(platformBase + route);
        List<List<String>> fields = List.of(
                List.of("id","occurredAt","correlationId","eventCode","userId","identityProviderCode","loginSessionId"),
                List.of("id","occurredAt","correlationId","eventCode","companyId","userId","performedByUserId","previousStatusCode","resultingStatusCode"),
                List.of("id","occurredAt","correlationId","eventCode","companyId","userId","performedByUserId","previousEmployeeId","resultingEmployeeId"),
                List.of("id","occurredAt","correlationId","eventCode","roleId","roleCode","displayName","platformRole","performedByUserId","permissionId"),
                List.of("id","occurredAt","correlationId","eventCode","groupId","groupCode","displayName","enabled","performedByUserId"),
                List.of("id","occurredAt","correlationId","performedByUserId","manifestId","manifestDigest","insertedActionCodes","insertedResourceTypeCodes","insertedPermissionCodes"),
                List.of("id","occurredAt","correlationId","eventCode","identityProviderCode"));
        for (int i = 0; i < allPaths.size(); i++) {
            var path = (Map<?,?>)apiPaths.get(allPaths.get(i));
            assertThat(path.keySet().stream().map(Object::toString).toList()).containsExactly("get");
            var operation = (Map<?,?>)path.get("get");
            var parameters = (List<?>)operation.get("parameters");
            assertThat(parameters.stream().map(value -> ((Map<?,?>)value).get("name").toString()).toList())
                    .containsExactlyInAnyOrderElementsOf(i < 3 ? List.of("companyId","afterId") : List.of("afterId"));
            var responseSchema = (Map<?,?>)schemas.get("SecurityHistory" + names[i]);
            assertThat(((Map<?,?>)responseSchema.get("properties")).keySet().stream().map(Object::toString).toList())
                    .containsExactlyInAnyOrderElementsOf(fields.get(i));
            assertThat(operation.get("responses").toString()).contains("SecurityHistory" + names[i]);
        }
    }

    private String profileEmailBody(String email, String password) {
        return "{\"email\":\"" + email + "\",\"currentPassword\":\"" + password + "\"}";
    }

    private long profileEventCount(String code) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_12 WHERE F_01=?", Long.class, code);
    }

    private String groupGrantBody(long role, Instant end) {
        return "{\"roleId\":" + role + ",\"validTo\":\"" + end + "\",\"reason\":\"synthetic group grant\"}";
    }

    private long createRuntimeRole(String code, boolean platform, String access) throws Exception {
        var response = send(port, "/v1/platform/roles", roleBody(code, platform), access);
        assertSafe(response, 201);
        Number id = JsonPath.read(response.body(), "$.id");
        return id.longValue();
    }

    private String changePath(long role, long permission, boolean associate) {
        return "/v1/platform/roles/" + role + "/permissions/" + permission + (associate ? "/associate" : "/remove");
    }

    private void assertCompositionRace(String path, String reason, String access) throws Exception {
        long events = count("T_10_01_21");
        var a = client.sendAsync(request(port, path, reason, access), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(request(port, path, reason, access), HttpResponse.BodyHandlers.ofString());
        assertThat(List.of(a.get(60, TimeUnit.SECONDS).statusCode(), b.get(60, TimeUnit.SECONDS).statusCode()))
                .containsExactlyInAnyOrder(204, 409);
        assertThat(count("T_10_01_21")).isEqualTo(events + 1);
    }

    private long createCompositionUser(long adminId, long companyId, String username) {
        return new TransactionTemplate(transactions).execute(tx -> {
            User admin = entityManager.find(User.class, adminId);
            User user = new User(admin.getIdentityProvider(), username, username + "@example.invalid", null, companyId,
                    entityManager.createQuery("select s from UserStatus s where s.code='PENDING'", UserStatus.class).getSingleResult(),
                    admin.getUserType());
            entityManager.persist(user);
            var credential = new LocalCredential(user, passwordEncoder.encode(SYNTHETIC_PASSWORD), Instant.now());
            entityManager.persist(credential);
            user.activate(credential, entityManager.createQuery("select s from UserStatus s where s.code='ACTIVE'",
                    UserStatus.class).getSingleResult());
            return user.getId();
        });
    }

    private long addCompanyFixtureGrant(long actor, long recipient, long role, long company,
            Instant from, Instant to) {
        return new TransactionTemplate(transactions).execute(tx -> {
            var grant = new UserRoleGrant(entityManager.find(User.class, recipient), entityManager.find(Role.class, role),
                    AuthorizationScope.global(company), "synthetic fixture", from, to, entityManager.find(User.class, actor));
            entityManager.persist(grant);
            return grant.getId();
        });
    }

    private void assertProtectedTarget(long companyId, long target, long companyRole, long grantId, String access) throws Exception {
        String base = "/v1/identity/users/" + target;
        String selector = "?companyId=" + companyId;
        String reason = "{\"reason\":\"synthetic boundary probe\"}";
        for (String operation : List.of("/activate", "/suspend", "/reactivate", "/disable", "/sessions/revoke", "/employee/unlink")) {
            assertSafe(send(port, base + operation + selector, reason, access), 403);
        }
        for (String operation : List.of("/credential/initialize", "/credential/reset")) {
            assertSafe(send(port, base + operation + selector,
                    "{\"password\":\"Synthetic-replacement-2026!\",\"reason\":\"synthetic boundary probe\"}", access), 403);
        }
        assertSafe(send(port, base + "/employee/link" + selector, "{\"employeeId\":1,\"reason\":\"synthetic\"}", access), 403);
        assertSafe(send(port, base + "/role-grants" + selector, "{\"roleId\":" + companyRole
                + ",\"validTo\":\"" + Instant.now().plusSeconds(3600).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
                + "\",\"reason\":\"synthetic\"}", access), 403);
        assertSafe(send(port, base + "/role-grants/" + grantId + "/revoke" + selector, "", access), 403);
    }

    private String loginAccess(String username) throws Exception {
        var response = send(port, "/v1/auth/login", "{\"username\":\"" + username + "\",\"password\":\""
                + SYNTHETIC_PASSWORD + "\"}", null);
        assertThat(response.statusCode()).isEqualTo(200);
        return JsonPath.read(response.body(), "$.accessToken");
    }

    private String roleBody(String code, boolean platform) {
        return "{\"code\":\"" + code + "\",\"displayName\":\"Runtime chosen role\",\"platformRole\":" + platform
                + ",\"reason\":\"synthetic runtime role\"}";
    }

    private String configuredBody(String company, String username, String roleCode) {
        String base = body(company, username);
        return base.substring(0, base.length() - 1) + ",\"initialPlatformRoleCode\":\"" + roleCode
                + "\",\"initialPlatformRoleName\":\"Installer chosen authority\"}";
    }

    private String body(String code, String username) {
        return "{\"companyCode\":\"" + code + "\",\"companyNameEn\":\"Synthetic company\","
                + "\"username\":\"" + username + "\",\"email\":\"" + username + "@example.invalid\","
                + "\"password\":\"" + SYNTHETIC_PASSWORD + "\"}";
    }

    private HttpResponse<String> status(int targetPort) throws Exception {
        return send(targetPort, "/v1/setup/status", null, null);
    }

    private HttpRequest request(int targetPort, String path, String body, String token, String... secrets) {
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + targetPort + "/hyflo/api" + path))
                .timeout(Duration.ofSeconds(60));
        for (String secret : secrets) builder.header("X-Hyflo-Setup-Secret", secret);
        if (token != null) builder.header("Authorization", "Bearer " + token);
        if (body == null) return builder.GET().build();
        return builder.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }

    private HttpResponse<String> send(int targetPort, String path, String body, String token, String... secrets)
            throws Exception {
        return client.send(request(targetPort, path, body, token, secrets), HttpResponse.BodyHandlers.ofString());
    }

    private void assertSafe(HttpResponse<String> response, int expected) {
        assertThat(response.statusCode()).isEqualTo(expected);
        assertThat(response.headers().firstValue("Cache-Control")).contains("no-store");
        assertThat(response.body()).doesNotContain(SETUP_SECRET, WRONG_SECRET, SYNTHETIC_PASSWORD,
                "synthetic rollback probe", "SQLState", "passwordHash");
    }

    private void assertEmptyOrganization() {
        for (String table : List.of("T_10_02_01", "T_10_02_06", "T_10_02_11")) {
            assertThat(count(table)).as(table).isZero();
        }
    }

    private Fixture createOrganizationFixture() {
        return new TransactionTemplate(transactions).execute(transaction -> {
            var date = LocalDate.now(ZoneOffset.UTC);
            var companyType = new OrganizationUnitType("COMPANY", null, null, "Company");
            var divisionType = new OrganizationUnitType("DIVISION", null, null, "Division");
            var active = new OrganizationUnitStatus("ACTIVE", null, null, "Active");
            var inactive = new OrganizationUnitStatus("INACTIVE", null, null, "Inactive");
            for (var catalog : List.of(companyType, divisionType, active, inactive)) {
                entityManager.persist(catalog);
            }
            var company = new OrganizationUnit("BOOT_COMPANY", null, null, "Synthetic company", companyType, null,
                    date.minusDays(2), null, active);
            entityManager.persist(company);
            var division = new OrganizationUnit("BOOT_DIVISION", null, null, "Synthetic division", divisionType, company,
                    date.minusDays(2), null, active);
            var inactiveUnit = new OrganizationUnit("BOOT_INACTIVE", null, null, "Inactive company", companyType, null,
                    date.minusDays(2), null, inactive);
            var expired = new OrganizationUnit("BOOT_EXPIRED", null, null, "Expired company", companyType, null,
                    date.minusDays(3), date, active);
            var future = new OrganizationUnit("BOOT_FUTURE", null, null, "Future company", companyType, null,
                    date.plusDays(2), null, active);
            for (var unit : List.of(division, inactiveUnit, expired, future)) {
                entityManager.persist(unit);
            }
            entityManager.flush();
            return new Fixture(company.getId(), companyType.getId(), division.getId(), inactiveUnit.getId(),
                    expired.getId(), future.getId());
        });
    }

    private void assertEmptyIdentity() {
        for (int index = 1; index <= 23; index++) {
            assertThat(count(String.format("T_10_01_%02d", index))).as("Identity table %02d is empty", index).isZero();
        }
    }

    private long count(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Long.class);
    }

    private List<String> codes(String table) {
        return jdbc.queryForList("SELECT F_01 FROM " + table, String.class);
    }

    private record Fixture(long companyId, long companyTypeId, long divisionId, long inactiveId,
            long expiredId, long futureId) {
    }
}
