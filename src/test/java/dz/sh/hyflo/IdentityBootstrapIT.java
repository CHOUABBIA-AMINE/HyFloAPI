/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityBootstrapIT
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
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
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
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
            "identity-catalog.read");

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
                        java.util.stream.Stream.of("platform-role.read", "platform-role.create")).toList());
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
        assertThat(paths.keySet().stream().filter(p -> p.startsWith("/v1/platform")).toList()).containsExactly(endpoint);
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
        for (int index = 1; index <= 21; index++) {
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
