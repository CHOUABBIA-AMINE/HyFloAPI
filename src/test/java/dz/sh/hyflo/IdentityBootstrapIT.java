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

        String alpha = body("FIRST_ALPHA", "alpha-admin");
        String beta = body("FIRST_BETA", "beta-admin");
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
        assertThat(codes("T_10_01_09")).containsExactlyInAnyOrderElementsOf(EXPECTED_PERMISSIONS);
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
        for (String permission : EXPECTED_PERMISSIONS) assertThat(permissions.body()).contains("\"" + permission + "\"");
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
            assertThat(send(secondPort, "/v1/auth/login", login, null).statusCode()).isEqualTo(200);
        }
        assertThat(count("T_10_02_11")).isEqualTo(1L);
        assertThat(count("T_10_01_08")).isEqualTo(2L);
        assertThat(count("T_10_01_15")).isEqualTo(1L);
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
        for (int index = 1; index <= 19; index++) {
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
