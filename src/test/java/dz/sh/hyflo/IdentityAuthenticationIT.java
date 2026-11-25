/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityAuthenticationIT
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Test
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo
 *
 * @Description : Exercises persistent local authentication and live bearer enforcement through HTTP and real MySQL.
 */

package dz.sh.hyflo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import dz.sh.hyflo.bootstrap.IdentityBootstrapCommand;
import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.platform.security.JwtTokenService;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
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

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "hyflo.identity.login-window-seconds=3600",
        "hyflo.identity.login-global-limit=200",
        "hyflo.identity.login-ip-limit=100",
        "hyflo.identity.login-username-limit=10"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class IdentityAuthenticationIT {

    private static final String PASSWORD = "Synthetic-auth-password-2026!";
    private static final String ADMIN = "pilot-admin";

    @SuppressWarnings("resource")
    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("hyflo_authentication_test")
            .withUsername("hyflo_test")
            .withPassword("synthetic-db-password")
            .withCommand("--log-bin-trust-function-creators=1");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @LocalServerPort int port;
    @Autowired IdentityBootstrapCommand bootstrap;
    @Autowired IdentityPasswordEncoder encoder;
    @Autowired JwtTokenService tokens;
    @Autowired JdbcTemplate jdbc;
    @Autowired PlatformTransactionManager transactions;
    @Autowired ApplicationContext context;
    @PersistenceContext EntityManager entityManager;

    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    @Test
    void authenticatesOnlyPersistentEligibleAccountsAndEnforcesLiveSessions() throws Exception {
        assertThat(context.getBeansOfType(UserDetailsService.class)).isEmpty();
        assertThat(get("/actuator/health", null).statusCode()).isEqualTo(200);
        assertThat(get("/api-docs", null).statusCode()).isEqualTo(401);
        assertThat(get("/v1/me", null).statusCode()).isEqualTo(401);
        assertThat(get("/v1/me", "Basic " + Base64.getEncoder().encodeToString(
                "hyflo-test:synthetic-test-password".getBytes(StandardCharsets.UTF_8))).statusCode()).isEqualTo(401);
        assertProblem(login(ADMIN, PASSWORD), 503, "IDENTITY_UNAVAILABLE");
        assertThat(count("T_10_01_12")).isZero();

        long companyId = createCompany();
        long adminId = bootstrap.execute(new String[]{IdentityBootstrapCommand.FLAG}, Map.of(
                "HYFLO_BOOTSTRAP_COMPANY_ID", Long.toString(companyId),
                "HYFLO_BOOTSTRAP_USERNAME", ADMIN,
                "HYFLO_BOOTSTRAP_EMAIL", ADMIN + "@example.invalid",
                "HYFLO_BOOTSTRAP_PASSWORD", PASSWORD,
                "HYFLO_BOOTSTRAP_OPERATOR", "synthetic-operator"));
        createAccount("pending-user", "PENDING", true);
        createAccount("suspended-user", "SUSPENDED", true);
        createAccount("disabled-user", "DISABLED", true);
        createAccount("missing-credential", "ACTIVE", false);
        createAccount("unsupported-hash", "ACTIVE", true);
        jdbc.update("UPDATE T_10_01_10 SET F_01 = '{noop}synthetic-auth-password-2026!' WHERE F_03 = "
                + "(SELECT F_00 FROM T_10_01_08 WHERE F_01 = 'unsupported-hash')");

        var invalid = post("{\"username\":\"bad username\",\"password\":\"synthetic\"}");
        assertProblem(invalid, 400, "INVALID_REQUEST");
        assertProblem(post("{\"username\":\"pilot-admin\"}"), 400, "INVALID_REQUEST");
        assertProblem(post("{"), 400, "INVALID_REQUEST");
        assertProblem(login(ADMIN, "é".repeat(37)), 400, "INVALID_REQUEST");
        assertThat(count("T_10_01_12")).isZero();

        var wrong = login(ADMIN, "wrong-password");
        assertProblem(wrong, 401, "AUTHENTICATION_REQUIRED");
        String generic = field(wrong, "$.detail");
        for (String username : List.of("unknown-user", "pending-user", "suspended-user", "disabled-user",
                "missing-credential", "unsupported-hash", "hyflo-bootstrap")) {
            var rejected = login(username, PASSWORD);
            assertProblem(rejected, 401, "AUTHENTICATION_REQUIRED");
            assertThat(field(rejected, "$.detail")).isEqualTo(generic);
        }
        assertThat(count("T_10_01_11")).isZero();
        assertThat(count("T_10_01_17")).isZero();
        assertThat(eventCount("LOGIN_FAILED")).isEqualTo(8);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_12 WHERE F_01='LOGIN_FAILED' AND F_05 IS NULL",
                Long.class)).isEqualTo(1L);
        String failureCorrelation = field(wrong, "$.correlationId");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_12 WHERE F_01='LOGIN_FAILED' AND F_03=? AND F_05=?",
                Long.class, failureCorrelation, adminId)).isEqualTo(1L);

        var successful = login("  PILOT-ADMIN  ", PASSWORD);
        assertThat(successful.statusCode()).isEqualTo(200);
        assertThat(successful.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertThat(successful.headers().firstValue("Pragma").orElse("")).contains("no-cache");
        assertThat(successful.headers().allValues("Set-Cookie")).isEmpty();
        assertThat(field(successful, "$.tokenType")).isEqualTo("Bearer");
        String access = field(successful, "$.accessToken");
        String refresh = field(successful, "$.refreshToken");
        var identity = tokens.verify(access, Instant.now());
        assertThat(identity.userId()).isEqualTo(adminId);
        assertThat(Base64.getUrlDecoder().decode(refresh)).hasSize(32);
        String digest = JwtTokenService.digestRefreshSecret(refresh);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_17 WHERE F_04=?", String.class, identity.sessionId()))
                .isEqualTo(digest);
        assertThat(jdbc.queryForObject("SELECT F_04 FROM T_10_01_11 WHERE F_00=?", Long.class, identity.sessionId()))
                .isEqualTo(adminId);
        assertThat(count("T_10_01_11")).isEqualTo(1);
        assertThat(count("T_10_01_17")).isEqualTo(1);
        assertThat(eventCount("LOGIN_SUCCEEDED")).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_12 WHERE F_01='LOGIN_SUCCEEDED' AND F_05=? AND F_03 IS NOT NULL",
                Long.class, adminId)).isEqualTo(1L);
        var sessionTimes = jdbc.queryForMap("SELECT TIMESTAMPDIFF(SECOND,F_01,F_02) AS lifetime FROM T_10_01_11 WHERE F_00=?",
                identity.sessionId());
        assertThat(((Number) sessionTimes.get("lifetime")).longValue()).isEqualTo(28800);
        assertThat(Instant.parse(field(successful, "$.accessExpiresAt")))
                .isBeforeOrEqualTo(Instant.parse(field(successful, "$.sessionExpiresAt")));
        assertThat(successful.body()).doesNotContain(PASSWORD, digest, "{bcrypt}", "passwordHash", "tokenDigest");

        var me = get("/v1/me", "Bearer " + access);
        assertThat(me.statusCode()).isEqualTo(200);
        assertThat(number(me, "$.userId")).isEqualTo(adminId);
        assertThat(number(me, "$.sessionId")).isEqualTo(identity.sessionId());
        assertThat(field(me, "$.username")).isEqualTo(ADMIN);
        assertThat(me.body()).doesNotContain("permissions", "roles", "password", "digest", "companyId");
        assertThat(me.headers().allValues("Set-Cookie")).isEmpty();
        var openapi = get("/api-docs", "Bearer " + access);
        assertThat(openapi.statusCode()).isEqualTo(200);
        assertThat(openapi.body()).contains("\"openapi\"", "/v1/auth/login", "/v1/me");
        assertProblem(get("/v1/users", "Bearer " + access), 403, "ACCESS_DENIED");
        assertProblem(get("/actuator/prometheus", "Bearer " + access), 403, "ACCESS_DENIED");
        assertProblem(get("/v1/me", "Bearer " + refresh), 401, "AUTHENTICATION_REQUIRED");
        int signature = access.lastIndexOf('.') + 1;
        String tampered = access.substring(0, signature) + (access.charAt(signature) == 'A' ? "B" : "A")
                + access.substring(signature + 1);
        assertProblem(get("/v1/me", "Bearer " + tampered), 401, "AUTHENTICATION_REQUIRED");
        var wrongOwner = tokens.issue(adminId + 10000, identity.sessionId(), Instant.now(), Instant.now().plusSeconds(30));
        assertProblem(get("/v1/me", "Bearer " + wrongOwner.value()), 401, "AUTHENTICATION_REQUIRED");
        var nonexistentSession = tokens.issue(adminId, identity.sessionId() + 10000, Instant.now(), Instant.now().plusSeconds(30));
        assertProblem(get("/v1/me", "Bearer " + nonexistentSession.value()), 401, "AUTHENTICATION_REQUIRED");
        var expiredToken = tokens.issue(adminId, identity.sessionId(), Instant.now().minusSeconds(60), Instant.now().minusSeconds(30));
        assertProblem(get("/v1/me", "Bearer " + expiredToken.value()), 401, "AUTHENTICATION_REQUIRED");

        jdbc.update("UPDATE T_10_01_01 SET F_03=FALSE WHERE F_01='LOCAL'");
        try {
            assertProblem(get("/v1/me", "Bearer " + access), 401, "AUTHENTICATION_REQUIRED");
            assertProblem(login(ADMIN, PASSWORD), 401, "AUTHENTICATION_REQUIRED");
        } finally {
            jdbc.update("UPDATE T_10_01_01 SET F_03=TRUE WHERE F_01='LOCAL'");
        }
        assertThat(eventCount("LOGIN_FAILED")).isEqualTo(9);

        long beforeSessions = count("T_10_01_11");
        long beforeRefresh = count("T_10_01_17");
        long beforeEvents = count("T_10_01_12");
        jdbc.execute("CREATE TRIGGER hyflo_test_auth_failure BEFORE INSERT ON T_10_01_12 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic login rollback'");
        try {
            assertProblem(login(ADMIN, PASSWORD), 503, "IDENTITY_UNAVAILABLE");
            assertProblem(login("unknown-user", PASSWORD), 503, "IDENTITY_UNAVAILABLE");
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_auth_failure");
        }
        assertThat(count("T_10_01_11")).isEqualTo(beforeSessions);
        assertThat(count("T_10_01_17")).isEqualTo(beforeRefresh);
        assertThat(count("T_10_01_12")).isEqualTo(beforeEvents);

        jdbc.update("UPDATE T_10_01_11 SET F_03=? WHERE F_00=?", Timestamp.from(Instant.now()), identity.sessionId());
        assertProblem(get("/v1/me", "Bearer " + access), 401, "AUTHENTICATION_REQUIRED");

        var fresh = login(ADMIN, PASSWORD);
        assertThat(fresh.statusCode()).isEqualTo(200);
        String secondAccess = field(fresh, "$.accessToken");
        var secondIdentity = tokens.verify(secondAccess, Instant.now());
        jdbc.update("UPDATE T_10_01_11 SET F_01=?,F_02=? WHERE F_00=?",
                Timestamp.from(Instant.now().minusSeconds(120)), Timestamp.from(Instant.now().minusSeconds(60)),
                secondIdentity.sessionId());
        assertProblem(get("/v1/me", "Bearer " + secondAccess), 401, "AUTHENTICATION_REQUIRED");

        var active = login(ADMIN, PASSWORD);
        assertThat(active.statusCode()).isEqualTo(200);
        String thirdAccess = field(active, "$.accessToken");
        new TransactionTemplate(transactions).executeWithoutResult(tx ->
                entityManager.find(User.class, adminId).suspend(status("SUSPENDED")));
        assertProblem(get("/v1/me", "Bearer " + thirdAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(login(ADMIN, PASSWORD), 401, "AUTHENTICATION_REQUIRED");

        long failuresBeforeLimit = eventCount("LOGIN_FAILED");
        for (int attempt = 0; attempt < 10; attempt++) {
            assertProblem(login("limited-user", PASSWORD), 401, "AUTHENTICATION_REQUIRED");
        }
        var throttled = login("LIMITED-USER", PASSWORD);
        assertProblem(throttled, 429, "LOGIN_RATE_LIMITED");
        assertThat(Long.parseLong(throttled.headers().firstValue("Retry-After").orElseThrow())).isPositive();
        assertThat(eventCount("LOGIN_FAILED")).isEqualTo(failuresBeforeLimit + 10);
    }

    private long createCompany() {
        return new TransactionTemplate(transactions).execute(tx -> {
            var type = new OrganizationUnitType("COMPANY", null, null, "Company");
            var status = new OrganizationUnitStatus("ACTIVE", null, null, "Active");
            entityManager.persist(type);
            entityManager.persist(status);
            var company = new OrganizationUnit("AUTH_COMPANY", null, null, "Synthetic company", type, null,
                    LocalDate.now(ZoneOffset.UTC).minusDays(1), null, status);
            entityManager.persist(company);
            entityManager.flush();
            return company.getId();
        });
    }

    private void createAccount(String username, String target, boolean persistCredential) {
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var provider = entityManager.createQuery("select p from IdentityProvider p where p.code='LOCAL'", IdentityProvider.class)
                    .getSingleResult();
            var human = entityManager.createQuery("select t from UserType t where t.code='HUMAN'", UserType.class)
                    .getSingleResult();
            var user = new User(provider, username, username + "@example.invalid", null, status("PENDING"), human);
            entityManager.persist(user);
            var credential = new LocalCredential(user, encoder.encode(PASSWORD), Instant.now());
            if (persistCredential) {
                entityManager.persist(credential);
            }
            if ("ACTIVE".equals(target) || "SUSPENDED".equals(target)) {
                // The intentionally missing credential fixture tests fail-closed handling of incomplete stored data.
                user.activate(credential, status("ACTIVE"));
            }
            if ("SUSPENDED".equals(target)) {
                user.suspend(status("SUSPENDED"));
            }
            if ("DISABLED".equals(target)) {
                user.disable(status("DISABLED"));
            }
        });
    }

    private UserStatus status(String code) {
        return entityManager.createQuery("select s from UserStatus s where s.code=:code", UserStatus.class)
                .setParameter("code", code).getSingleResult();
    }

    private long count(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Long.class);
    }

    private long eventCount(String event) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_12 WHERE F_01=?", Long.class, event);
    }

    private HttpResponse<String> login(String username, String password) throws Exception {
        // All fixture inputs are controlled and contain no JSON quoting characters.
        return post("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
    }

    private HttpResponse<String> post(String json) throws Exception {
        return client.send(HttpRequest.newBuilder(uri("/v1/auth/login")).timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json").header("Accept", "application/json, application/problem+json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build(), HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String path, String authorization) throws Exception {
        var builder = HttpRequest.newBuilder(uri(path)).timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json, application/problem+json").GET();
        if (authorization != null) builder.header("Authorization", authorization);
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + "/hyflo/api" + path);
    }

    private static String field(HttpResponse<String> response, String path) {
        return JsonPath.read(response.body(), path);
    }

    private static long number(HttpResponse<String> response, String path) {
        Number value = JsonPath.read(response.body(), path);
        return value.longValue();
    }

    private static void assertProblem(HttpResponse<String> response, int status, String code) {
        assertThat(response.statusCode()).as("Expected safe HTTP %s", status).isEqualTo(status);
        assertThat(field(response, "$.code")).isEqualTo(code);
        assertThat(field(response, "$.correlationId")).isNotBlank();
        assertThat(response.body()).doesNotContain("accessToken", "refreshToken", PASSWORD, "passwordHash", "Exception", "SQLState");
        assertThat(response.headers().allValues("Set-Cookie")).isEmpty();
    }
}
