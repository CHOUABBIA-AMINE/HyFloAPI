/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IdentityAuthorizationIT
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Test
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo
 *
 * @Description : Exercises direct company authorization and protected catalog reads through HTTP and isolated MySQL.
 */

package dz.sh.hyflo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Action;
import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthorizationScope;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Group;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupMembership;
import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupRoleGrant;
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
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnit;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitType;
import dz.sh.hyflo.bootstrap.IdentityBootstrapCommand;
import dz.sh.hyflo.platform.security.JwtTokenService;
import dz.sh.hyflo.platform.security.PasswordEncodingConfiguration.IdentityPasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class IdentityAuthorizationIT {

    private static final String PASSWORD = "Synthetic-authorization-password!";
    private static final String SUSPENDED_PASSWORD = "Synthetic-suspended-password!";
    private static final String RESET_PASSWORD = "Synthetic-replacement-password!";
    private static final List<String> EXPECTED = List.of("credential.initialize", "credential.reset",
            "identity-catalog.read", "login-session.revoke", "permission.read", "role.read",
            "user-role-grant.assign", "user-role-grant.read", "user-role-grant.revoke", "user.activate",
            "user.create", "user.disable", "user.reactivate", "user.read", "user.suspend");

    @SuppressWarnings("resource")
    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("hyflo_authorization_test")
            .withUsername("hyflo_test").withPassword("synthetic-db-password")
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
    @PersistenceContext EntityManager entityManager;

    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    @Test
    void evaluatesCompanyPermissionsAndRevocationsWithTheSameLiveJwt() throws Exception {
        var companies = createCompanies();
        long adminId = bootstrap.execute(new String[]{IdentityBootstrapCommand.FLAG}, Map.of(
                "HYFLO_BOOTSTRAP_COMPANY_ID", Long.toString(companies.first()),
                "HYFLO_BOOTSTRAP_USERNAME", "authz-admin",
                "HYFLO_BOOTSTRAP_EMAIL", "authz-admin@example.invalid",
                "HYFLO_BOOTSTRAP_PASSWORD", PASSWORD,
                "HYFLO_BOOTSTRAP_OPERATOR", "synthetic-operator"));
        long otherId = createAccount(companies.second());
        String admin = login("authz-admin");
        String other = login("authz-user");
        assertProblem(get(permissions(companies.first()), null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get(catalog(companies.first()), "invalid.jwt"), 401, "AUTHENTICATION_REQUIRED");
        for (String query : List.of("", "?companyId=0", "?companyId=-1", "?companyId=invalid",
                "?companyId=99999999999999999999999999")) {
            assertProblem(get("/v1/me/permissions" + query, admin), 400, "INVALID_REQUEST");
            assertProblem(get("/v1/identity/user-statuses" + query, admin), 400, "INVALID_REQUEST");
        }
        assertCodes(get(permissions(companies.first()), admin), companies.first(), EXPECTED);
        assertCatalog(get(catalog(companies.first()), admin));
        assertThat(get("/api-docs", admin).body()).contains("/v1/me/permissions", "/v1/identity/user-statuses");
        assertProblem(get("/v1/users", admin), 403, "ACCESS_DENIED");
        assertProblem(get("/v1/pipelines", admin), 403, "ACCESS_DENIED");
        assertProblem(get("/v1/authorization/check", admin), 403, "ACCESS_DENIED");
        assertProblem(get(permissions(companies.second()), admin), 403, "ACCESS_DENIED");
        String denial = detail(get(catalog(companies.first()), other));
        assertProblem(get(catalog(companies.first()), other), 403, "ACCESS_DENIED");

        var fixture = createGrantFixtures(companies, adminId, otherId);
        exerciseAccountAdministration(companies, adminId, otherId, admin, other, fixture);
        assertCodes(get(permissions(companies.first()), admin), companies.first(), EXPECTED);
        assertCodes(get(permissions(companies.second()), admin), companies.second(), List.of("synthetic.observe", "user.read"));
        assertProblem(get(catalog(companies.second()), admin), 403, "ACCESS_DENIED");
        assertCodes(get(permissions(companies.first()), other), companies.first(), List.of("identity-catalog.read"));
        assertCatalog(get(catalog(companies.first()), other));

        // These stored targets all carry an apparent direct grant but are not eligible company boundaries.
        for (long target : List.of(companies.division(), companies.inactive(), companies.expired(),
                companies.future(), companies.retired(), Long.MAX_VALUE)) {
            var rejected = get(catalog(target), admin);
            assertProblem(rejected, 403, "ACCESS_DENIED");
            assertThat(detail(rejected)).isEqualTo(denial);
            assertProblem(get(permissions(target), admin), 403, "ACCESS_DENIED");
        }
        // Group-only and unsupported target grants cannot confer authority in IDN-03.
        assertProblem(get(permissions(companies.third()), other), 403, "ACCESS_DENIED");
        assertProblem(get(catalog(companies.third()), other), 403, "ACCESS_DENIED");

        // Different actual permissions, malformed code-pairs and inactive grants do not leak extra codes.
        assertCodes(get(permissions(companies.second()), other), companies.second(), List.of("synthetic.observe", "user.read"));
        assertProblem(get(catalog(companies.second()), other), 403, "ACCESS_DENIED");

        jdbc.update("UPDATE T_10_01_15 SET F_03=?,F_11=? WHERE F_00=?",
                Timestamp.from(Instant.now().minusSeconds(1)), adminId, fixture.otherCatalogGrant());
        assertProblem(get(catalog(companies.first()), other), 403, "ACCESS_DENIED");
        assertProblem(get(permissions(companies.first()), other), 403, "ACCESS_DENIED");

        // Force a real repository read failure on this isolated disposable schema.
        jdbc.execute("RENAME TABLE T_10_01_14 TO AUTHZ_TEST_ROLE_PERMISSION");
        try {
            assertProblem(get(permissions(companies.first()), admin), 503, "IDENTITY_UNAVAILABLE");
            assertProblem(get(catalog(companies.first()), admin), 503, "IDENTITY_UNAVAILABLE");
        } finally {
            jdbc.execute("RENAME TABLE AUTHZ_TEST_ROLE_PERMISSION TO T_10_01_14");
        }
        assertCatalog(get(catalog(companies.first()), admin));

        // A retired COMPANY type denies even previously authorized users.
        jdbc.update("UPDATE T_10_02_01 SET F_08=FALSE WHERE F_01='COMPANY'");
        try {
            assertProblem(get(catalog(companies.first()), admin), 403, "ACCESS_DENIED");
            assertProblem(get(permissions(companies.second()), other), 403, "ACCESS_DENIED");
        } finally {
            jdbc.update("UPDATE T_10_02_01 SET F_08=TRUE WHERE F_01='COMPANY'");
        }

        // Current account state is authoritative even with a valid JWT.
        jdbc.update("UPDATE T_10_01_08 SET F_07=(SELECT F_00 FROM T_10_01_02 WHERE F_01='SUSPENDED') WHERE F_00=?", otherId);
        assertProblem(get(permissions(companies.second()), other), 401, "AUTHENTICATION_REQUIRED");
        jdbc.update("UPDATE T_10_01_08 SET F_07=(SELECT F_00 FROM T_10_01_02 WHERE F_01='ACTIVE') WHERE F_00=?", otherId);
        assertCodes(get(permissions(companies.second()), other), companies.second(), List.of("synthetic.observe", "user.read"));

        // Role retirement and permission removal immediately affect subsequent reads with the same access token.
        jdbc.update("UPDATE T_10_01_04 SET F_03=FALSE WHERE F_00=?", fixture.readRole());
        assertProblem(get(permissions(companies.second()), admin), 403, "ACCESS_DENIED");
        assertProblem(get(permissions(companies.second()), other), 403, "ACCESS_DENIED");
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), fixture.catalogPermission());
        assertProblem(get(catalog(companies.first()), admin), 403, "ACCESS_DENIED");
        assertCodes(get(permissions(companies.first()), admin), companies.first(),
                EXPECTED.stream().filter(code -> !code.equals("identity-catalog.read")).toList());

        // Current company eligibility is rechecked, not cached with a previously issued token.
        jdbc.update("UPDATE T_10_02_11 SET F_06=? WHERE F_00=?",
                java.sql.Date.valueOf(LocalDate.now(ZoneOffset.UTC)), companies.first());
        assertProblem(get(permissions(companies.first()), admin), 403, "ACCESS_DENIED");

        var session = tokens.verify(admin, Instant.now());
        jdbc.update("UPDATE T_10_01_11 SET F_03=? WHERE F_00=?",
                Timestamp.from(Instant.now().minusSeconds(1)), session.sessionId());
        assertProblem(get(permissions(companies.first()), admin), 401, "AUTHENTICATION_REQUIRED");
        jdbc.update("UPDATE T_10_01_01 SET F_03=FALSE WHERE F_01='LOCAL'");
        assertProblem(get(permissions(companies.second()), other), 401, "AUTHENTICATION_REQUIRED");
        jdbc.update("UPDATE T_10_01_01 SET F_03=TRUE WHERE F_01='LOCAL'");
        jdbc.update("UPDATE T_10_01_08 SET F_07=(SELECT F_00 FROM T_10_01_02 WHERE F_01='DISABLED') WHERE F_00=?", otherId);
        assertProblem(get(permissions(companies.second()), other), 401, "AUTHENTICATION_REQUIRED");
    }


    private void exerciseAccountAdministration(Companies c, long adminId, long otherId, String admin, String other,
            GrantFixture fixture) throws Exception {
        String root = "/v1/identity/users";
        String first = "?companyId=" + c.first();
        String second = "?companyId=" + c.second();
        String reason = "{\"reason\":\"synthetic lifecycle check\"}";
        String create = "{\"username\":\"Managed.User\",\"email\":\"managed@example.invalid\",\"userTypeCode\":\"HUMAN\",\"reason\":\"synthetic registration\"}";
        String service = "{\"username\":\"managed-service\",\"email\":\"managed-service@example.invalid\",\"userTypeCode\":\"SERVICE\",\"reason\":\"synthetic registration\"}";
        assertProblem(get(root + first, null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get(root + first, other), 403, "ACCESS_DENIED");
        assertProblem(get(root, admin), 400, "INVALID_REQUEST");
        assertProblem(post(root + first, create, other), 403, "ACCESS_DENIED");
        var created = post(root + first, create, admin);
        assertThat(created.statusCode()).isEqualTo(201);
        Number managedNumber = JsonPath.read(created.body(), "$.id");
        long managed = managedNumber.longValue();
        String managedPath = root + "/" + managed;
        assertThat((String) JsonPath.read(created.body(), "$.username")).isEqualTo("managed.user");
        assertThat((String) JsonPath.read(created.body(), "$.userStatusCode")).isEqualTo("PENDING");
        assertThat(((Number) JsonPath.read(created.body(), "$.companyId")).longValue()).isEqualTo(c.first());
        assertThat(created.body()).doesNotContain("password", "credential", "grant", "employee", "reason");
        var serviceCreated = post(root + first, service, admin);
        assertThat(serviceCreated.statusCode()).isEqualTo(201);
        long serviceId = ((Number) JsonPath.read(serviceCreated.body(), "$.id")).longValue();
        String servicePath = root + "/" + serviceId;
        assertProblem(post(root + first, create, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertProblem(post(root + first, "{\"username\":\"hyflo-bootstrap\",\"email\":\"reserved@example.invalid\",\"userTypeCode\":\"SERVICE\",\"reason\":\"reserved\"}", admin), 400, "INVALID_REQUEST");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=?", Long.class, managed)).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_10 WHERE F_03=?", Long.class, managed)).isZero();
        assertThat(jdbc.queryForObject("SELECT F_05 FROM T_10_01_08 WHERE F_00=?", Long.class, managed)).isEqualTo(c.first());

        assertThat(get(managedPath + first, admin).statusCode()).isEqualTo(200);
        var page = get(root + first, admin);
        assertThat(page.statusCode()).isEqualTo(200);
        assertThat(page.body()).contains("managed.user", "managed-service", "authz-admin").doesNotContain("hyflo-bootstrap", "authz-user");
        assertThat(get(root + first + "&afterId=" + managed, admin).body()).doesNotContain("managed.user");
        assertThat(get(root + second, admin).body()).contains("authz-user").doesNotContain("managed.user", "authz-admin");
        var missing = get(root + "/" + Long.MAX_VALUE + first, admin);
        var foreign = get(root + "/" + otherId + first, admin);
        assertProblem(missing, 403, "ACCESS_DENIED");
        assertProblem(foreign, 403, "ACCESS_DENIED");
        assertThat(detail(missing)).isEqualTo(detail(foreign));
        assertProblem(get(managedPath + second, admin), 403, "ACCESS_DENIED");
        assertProblem(post(root + "/" + adminId + "/disable" + first, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post(managedPath + "/activate" + first, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");

        String initialize = managedPath + "/credential/initialize" + first;
        String reset = managedPath + "/credential/reset" + first;
        String credential = credentialBody(PASSWORD);
        assertProblem(post(initialize, credential, null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post(initialize, credential, other), 403, "ACCESS_DENIED");
        assertProblem(post(root + "/" + adminId + "/credential/reset" + first, credential, admin), 403, "ACCESS_DENIED");
        long bootstrapId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        assertProblem(post(root + "/" + bootstrapId + "/credential/initialize" + first, credential, admin), 403, "ACCESS_DENIED");
        assertProblem(post(root + "/" + Long.MAX_VALUE + "/credential/initialize" + first, credential, admin), 403, "ACCESS_DENIED");
        assertProblem(post(root + "/" + otherId + "/credential/reset" + first, credential, admin), 403, "ACCESS_DENIED");
        assertProblem(post(initialize, credentialBody("short"), admin), 400, "INVALID_REQUEST");
        assertProblem(post(initialize, credentialBody("é".repeat(37)), admin), 400, "INVALID_REQUEST");
        assertProblem(post(initialize, "{\"password\":\"" + PASSWORD + "\",\"reason\":\"" + PASSWORD + "\"}", admin),
                400, "INVALID_REQUEST");
        assertProblem(post(reset, credential, admin), 409, "ACCOUNT_OPERATION_REJECTED");

        // Give the administrator the exact disable permission in the second company for a synthetic interval.
        // It still cannot mutate a first-company target via that company selector.
        long extraGrant = new TransactionTemplate(transactions).execute(tx -> {
            var actor = entityManager.find(User.class, adminId);
            var role = role("TEST_SECOND_DISABLE", permission("user.disable"));
            entityManager.persist(new RolePermission(role, permission("credential.initialize")));
            entityManager.persist(new RolePermission(role, permission("credential.reset")));
            return grant(actor, role, AuthorizationScope.global(c.second()), Instant.now().minusSeconds(5), null, actor).getId();
        });
        assertProblem(post(managedPath + "/disable" + second, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post(managedPath + "/credential/initialize" + second, credential, admin), 403, "ACCESS_DENIED");
        assertProblem(post(managedPath + "/credential/reset" + second, credential, admin), 403, "ACCESS_DENIED");
        jdbc.update("UPDATE T_10_01_15 SET F_03=?,F_11=? WHERE F_00=?",
                Timestamp.from(Instant.now().minusSeconds(1)), adminId, extraGrant);
        assertProblem(post(root + "/" + otherId + "/disable" + second, reason, admin), 403, "ACCESS_DENIED");

        // Exact initialization authority is independent of reset and account activation.
        long initializePermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='credential.initialize'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), initializePermission);
        assertProblem(post(initialize, credential, admin), 403, "ACCESS_DENIED");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), initializePermission);
        var initA = client.sendAsync(postRequest(initialize, credential, admin), HttpResponse.BodyHandlers.ofString());
        var initB = client.sendAsync(postRequest(initialize, credential, admin), HttpResponse.BodyHandlers.ofString());
        var initResults = List.of(initA.get(), initB.get());
        assertThat(initResults.stream().map(HttpResponse::statusCode).toList()).containsExactlyInAnyOrder(204, 409);
        var initSuccess = initResults.stream().filter(response -> response.statusCode() == 204).findFirst().orElseThrow();
        assertThat(initSuccess.body()).isEmpty();
        assertThat(initSuccess.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertProblem(post(initialize, credential, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_10 WHERE F_03=?", Long.class, managed)).isEqualTo(1L);
        long credentialId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_10 WHERE F_03=?", Long.class, managed);
        String initialHash = storedHash(managed);
        assertThat(LocalCredential.supportsPasswordHash(initialHash)).isTrue();
        assertThat(initialHash).isNotEqualTo(PASSWORD);
        assertThat(encoder.matches(PASSWORD, initialHash)).isTrue();
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_10 WHERE F_03=?", Timestamp.class, managed)).isNotNull();
        assertThat((String) JsonPath.read(get(managedPath + first, admin).body(), "$.userStatusCode")).isEqualTo("PENDING");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=?", Long.class, managed)).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=?", Long.class, managed)).isZero();
        assertProblem(post("/v1/auth/login", loginBody("managed.user", PASSWORD), null), 401, "AUTHENTICATION_REQUIRED");

        // A pending SERVICE account can initialize and reset without activation.
        assertThat(post(servicePath + "/credential/initialize" + first, credential, admin).statusCode()).isEqualTo(204);
        assertThat(post(servicePath + "/credential/reset" + first, credentialBody(RESET_PASSWORD), admin).statusCode()).isEqualTo(204);
        assertThat(encoder.matches(RESET_PASSWORD, storedHash(serviceId))).isTrue();
        assertThat((String) JsonPath.read(get(servicePath + first, admin).body(), "$.userStatusCode")).isEqualTo("PENDING");
        assertProblem(post(managedPath + "/reactivate" + first, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(post(managedPath + "/activate" + first, reason, admin).statusCode()).isEqualTo(200);
        var managedTokens = loginTokens("managed.user");
        String managedAccess = JsonPath.read(managedTokens, "$.accessToken");
        String managedRefresh = JsonPath.read(managedTokens, "$.refreshToken");
        String secondAccess = login("managed.user");
        long session = tokens.verify(managedAccess, Instant.now()).sessionId();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NULL", Long.class, managed)).isEqualTo(2L);

        jdbc.execute("CREATE TRIGGER hyflo_test_lifecycle_failure BEFORE INSERT ON T_10_01_18 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic rollback probe'");
        try {
            assertProblem(post(managedPath + "/suspend" + first, reason, admin), 503, "IDENTITY_UNAVAILABLE");
            assertThat(get("/v1/me", managedAccess).statusCode()).isEqualTo(200);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NULL", Long.class, managed)).isEqualTo(2L);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_lifecycle_failure");
        }

        // Two identical transitions must not both commit. The actor and target locks serialize the operations.
        var a = client.sendAsync(postRequest(managedPath + "/suspend" + first, reason, admin), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(postRequest(managedPath + "/suspend" + first, reason, admin), HttpResponse.BodyHandlers.ofString());
        assertThat(List.of(a.get().statusCode(), b.get().statusCode())).containsExactlyInAnyOrder(200, 409);
        assertProblem(get("/v1/me", managedAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get("/v1/me", secondAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + managedRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NOT NULL", Long.class, managed)).isEqualTo(2L);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_00=? AND F_03 IS NOT NULL", Long.class, session)).isEqualTo(1L);

        assertThat(post(reset, credentialBody(SUSPENDED_PASSWORD), admin).statusCode()).isEqualTo(204);
        assertThat(encoder.matches(SUSPENDED_PASSWORD, storedHash(managed))).isTrue();
        assertThat((String) JsonPath.read(get(managedPath + first, admin).body(), "$.userStatusCode")).isEqualTo("SUSPENDED");

        // Remove only the reactivation permission, then restore the exact link.
        long reactivatePermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='user.reactivate'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), reactivatePermission);
        assertProblem(post(managedPath + "/reactivate" + first, reason, admin), 403, "ACCESS_DENIED");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), reactivatePermission);
        assertProblem(post(managedPath + "/activate" + first, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(post(managedPath + "/reactivate" + first, reason, admin).statusCode()).isEqualTo(200);
        assertProblem(get("/v1/me", managedAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + managedRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        var beforeReset = loginTokens("managed.user", SUSPENDED_PASSWORD);
        String beforeResetAccess = JsonPath.read(beforeReset, "$.accessToken");
        String beforeResetRefresh = JsonPath.read(beforeReset, "$.refreshToken");
        var independentTargetSession = loginTokens("managed.user", SUSPENDED_PASSWORD);
        String secondBeforeResetAccess = JsonPath.read(independentTargetSession, "$.accessToken");
        String secondBeforeResetRefresh = JsonPath.read(independentTargetSession, "$.refreshToken");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NULL", Long.class, managed)).isEqualTo(2L);
        String beforeResetHash = storedHash(managed);
        Timestamp beforeResetTime = jdbc.queryForObject("SELECT F_02 FROM T_10_01_10 WHERE F_03=?", Timestamp.class, managed);
        long resetPermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='credential.reset'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), resetPermission);
        assertProblem(post(reset, credentialBody(RESET_PASSWORD), admin), 403, "ACCESS_DENIED");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), resetPermission);
        assertProblem(post(reset, credentialBody(SUSPENDED_PASSWORD), admin), 409, "ACCOUNT_OPERATION_REJECTED");
        long eventCount = jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_18 WHERE F_08=?", Long.class, managed);
        jdbc.execute("CREATE TRIGGER hyflo_test_credential_failure BEFORE INSERT ON T_10_01_18 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic credential rollback probe'");
        try {
            assertProblem(post(reset, credentialBody(RESET_PASSWORD), admin), 503, "IDENTITY_UNAVAILABLE");
            assertThat(storedHash(managed)).isEqualTo(beforeResetHash);
            assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_10 WHERE F_03=?", Timestamp.class, managed)).isEqualTo(beforeResetTime);
            assertThat(get("/v1/me", beforeResetAccess).statusCode()).isEqualTo(200);
            assertThat(get("/v1/me", secondBeforeResetAccess).statusCode()).isEqualTo(200);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NULL", Long.class, managed)).isEqualTo(2L);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_18 WHERE F_08=?", Long.class, managed)).isEqualTo(eventCount);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_credential_failure");
        }
        var resetSuccess = post(reset, credentialBody(RESET_PASSWORD), admin);
        assertThat(resetSuccess.statusCode()).isEqualTo(204);
        assertThat(resetSuccess.body()).isEmpty();
        assertThat(resetSuccess.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertThat(storedHash(managed)).isNotEqualTo(beforeResetHash);
        assertThat(encoder.matches(RESET_PASSWORD, storedHash(managed))).isTrue();
        assertThat(jdbc.queryForObject("SELECT F_00 FROM T_10_01_10 WHERE F_03=?", Long.class, managed)).isEqualTo(credentialId);
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_10 WHERE F_03=?", Timestamp.class, managed)).isAfterOrEqualTo(beforeResetTime);
        assertProblem(get("/v1/me", beforeResetAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get("/v1/me", secondBeforeResetAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + secondBeforeResetRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NULL", Long.class, managed)).isZero();
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + beforeResetRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/login", loginBody("managed.user", SUSPENDED_PASSWORD), null), 401, "AUTHENTICATION_REQUIRED");
        assertThat(get("/v1/me", other).statusCode()).isEqualTo(200);
        var newTokens = loginTokens("managed.user", RESET_PASSWORD);
        String newAccess = JsonPath.read(newTokens, "$.accessToken");
        String newRefresh = JsonPath.read(newTokens, "$.refreshToken");
        assertThat(post(managedPath + "/disable" + first, reason, admin).statusCode()).isEqualTo(200);
        assertProblem(get("/v1/me", newAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + newRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/login", loginBody("managed.user", RESET_PASSWORD), null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post(reset, credentialBody(PASSWORD), admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertProblem(post(managedPath + "/reactivate" + first, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertProblem(post(managedPath + "/disable" + first, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(jdbc.queryForList("SELECT F_01 FROM T_10_01_18 WHERE F_08=? ORDER BY F_00", String.class, managed))
                .containsExactly("CREATED", "CREDENTIAL_INITIALIZED", "ACTIVATED", "SUSPENDED", "CREDENTIAL_RESET", "REACTIVATED", "CREDENTIAL_RESET", "DISABLED");
        assertThat(jdbc.queryForList("SELECT F_06 FROM T_10_01_18 WHERE F_08=? ORDER BY F_00", String.class, managed))
                .containsExactly("PENDING", "PENDING", "ACTIVE", "SUSPENDED", "SUSPENDED", "ACTIVE", "ACTIVE", "DISABLED");
        assertThat(jdbc.queryForList("SELECT F_05 FROM T_10_01_18 WHERE F_08=? ORDER BY F_00", String.class, managed))
                .containsExactly(null, "PENDING", "PENDING", "ACTIVE", "SUSPENDED", "SUSPENDED", "ACTIVE", "ACTIVE");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_18 WHERE F_08=? AND F_09=? AND F_07=? AND F_03 IS NOT NULL AND F_04 IS NOT NULL",
                Long.class, managed, adminId, c.first())).isEqualTo(8L);
        assertThat(jdbc.queryForList("SELECT F_04 FROM T_10_01_18 WHERE F_08=?", String.class, managed))
                .allSatisfy(value -> assertThat(value).doesNotContain(PASSWORD, SUSPENDED_PASSWORD, RESET_PASSWORD, initialHash));
        assertThat(get("/api-docs", admin).body()).contains("/v1/identity/users/{userId}/credential/initialize",
                "/v1/identity/users/{userId}/credential/reset");
        assertThat(get("/api-docs", admin).body()).contains("/v1/identity/users", "/v1/identity/users/{userId}/activate",
                "/v1/identity/users/{userId}/suspend", "/v1/identity/users/{userId}/reactivate", "/v1/identity/users/{userId}/disable");
        assertProblem(post(managedPath + "/reset" + first, reason, admin), 403, "ACCESS_DENIED");
    }

    private HttpRequest postRequest(String path, String body, String access) {
        var builder = HttpRequest.newBuilder(uri(path)).timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json").header("Accept", "application/json, application/problem+json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (access != null) builder.header("Authorization", "Bearer " + access);
        return builder.build();
    }

    private HttpResponse<String> post(String path, String body, String access) throws Exception {
        return client.send(postRequest(path, body, access), HttpResponse.BodyHandlers.ofString());
    }

    private Companies createCompanies() {
        return new TransactionTemplate(transactions).execute(tx -> {
            var today = LocalDate.now(ZoneOffset.UTC);
            var company = new OrganizationUnitType("COMPANY", null, null, "Company");
            var division = new OrganizationUnitType("DIVISION", null, null, "Division");
            var active = new OrganizationUnitStatus("ACTIVE", null, null, "Active");
            var inactive = new OrganizationUnitStatus("INACTIVE", null, null, "Inactive");
            for (Object catalog : List.of(company, division, active, inactive)) entityManager.persist(catalog);
            long first = unit("AUTHZ_FIRST", company, active, today.minusDays(5), null);
            long second = unit("AUTHZ_SECOND", company, active, today.minusDays(5), null);
            long third = unit("AUTHZ_THIRD", company, active, today.minusDays(5), null);
            long wrong = unit("AUTHZ_DIVISION", division, active, today.minusDays(5), null);
            long disabled = unit("AUTHZ_INACTIVE", company, inactive, today.minusDays(5), null);
            long expired = unit("AUTHZ_EXPIRED", company, active, today.minusDays(5), today);
            long future = unit("AUTHZ_FUTURE", company, active, today.plusDays(5), null);
            // There can be only one COMPANY type; test its retirement separately by temporarily toggling it.
            // This additional synthetic non-company unit is attached to a retired type.
            var retiredType = new OrganizationUnitType("RETIRED_TYPE", null, null, "Retired");
            retiredType.retire();
            entityManager.persist(retiredType);
            long retired = unit("AUTHZ_RETIRED", retiredType, active, today.minusDays(5), null);
            return new Companies(first, second, third, wrong, disabled, expired, future, retired);
        });
    }

    private long unit(String code, OrganizationUnitType type, OrganizationUnitStatus status,
            LocalDate from, LocalDate to) {
        var value = new OrganizationUnit(code, null, null, code, type, null, from, to, status);
        entityManager.persist(value);
        entityManager.flush();
        return value.getId();
    }

    private long createAccount(long companyId) {
        return new TransactionTemplate(transactions).execute(tx -> {
            var provider = entityManager.createQuery("select p from IdentityProvider p where p.code='LOCAL'", IdentityProvider.class)
                    .getSingleResult();
            var type = entityManager.createQuery("select t from UserType t where t.code='HUMAN'", UserType.class)
                    .getSingleResult();
            var pending = status("PENDING");
            var user = new User(provider, "authz-user", "authz-user@example.invalid", null, companyId, pending, type);
            entityManager.persist(user);
            var credential = new LocalCredential(user, encoder.encode(PASSWORD), Instant.now());
            entityManager.persist(credential);
            user.activate(credential, status("ACTIVE"));
            entityManager.flush();
            return user.getId();
        });
    }

    private UserStatus status(String code) {
        return entityManager.createQuery("select s from UserStatus s where s.code=:code", UserStatus.class)
                .setParameter("code", code).getSingleResult();
    }

    private GrantFixture createGrantFixtures(Companies c, long adminId, long otherId) {
        return new TransactionTemplate(transactions).execute(tx -> {
            User admin = entityManager.find(User.class, adminId);
            User other = entityManager.find(User.class, otherId);
            Role adminRole = entityManager.createQuery("select r from Role r where r.code='IDENTITY_ADMIN'", Role.class).getSingleResult();
            Permission catalog = permission("identity-catalog.read");
            Role catalogRole = role("TEST_CATALOG", catalog);
            Role readRole = role("TEST_READ", permission("user.read"));
            Instant now = Instant.now().minusSeconds(5);
            Instant future = Instant.now().plusSeconds(3600);
            grant(admin, adminRole, AuthorizationScope.global(c.first()), now, null, admin);
            grant(admin, readRole, AuthorizationScope.global(c.second()), now, null, admin);
            grant(other, readRole, AuthorizationScope.global(c.second()), now, null, admin);
            long otherCatalog = grant(other, catalogRole, AuthorizationScope.global(c.first()), now, null, admin).getId();
            for (long target : List.of(c.division(), c.inactive(), c.expired(), c.future(), c.retired(), Long.MAX_VALUE)) {
                grant(admin, catalogRole, AuthorizationScope.global(target), now, null, admin);
            }
            grant(other, catalogRole, AuthorizationScope.global(c.second()), future, null, admin);
            grant(other, catalogRole, AuthorizationScope.global(c.second()), now.minusSeconds(60), now, admin);
            var revoked = grant(other, catalogRole, AuthorizationScope.global(c.second()), now, null, admin);
            revoked.revoke(now.plusSeconds(1), admin);
            var retired = role("TEST_RETIRED", catalog);
            retired.retire();
            grant(other, retired, AuthorizationScope.global(c.second()), now, null, admin);
            grant(other, catalogRole, AuthorizationScope.region(c.third(), 1), now, null, admin);
            grant(other, catalogRole, AuthorizationScope.pipeline(c.third(), 1), now, null, admin);
            grant(other, catalogRole, AuthorizationScope.infrastructure(c.third(), 1), now, null, admin);
            var group = new Group("TEST_GROUP", "Synthetic group");
            entityManager.persist(group);
            entityManager.persist(new GroupMembership(other, group, now, null, admin));
            entityManager.persist(new GroupRoleGrant(group, catalogRole, AuthorizationScope.global(c.third()),
                    "synthetic", now, null, admin));
            var action = new Action("inspect", "Inspect");
            var resource = new ResourceType("synthetic", "Synthetic");
            entityManager.persist(action);
            entityManager.persist(resource);
            var observe = new Action("observe", "Observe");
            entityManager.persist(observe);
            var extra = new Permission("synthetic.observe", "Synthetic observe", observe, resource);
            entityManager.persist(extra);
            entityManager.persist(new RolePermission(readRole, extra));
            var malformed = new Permission("SYNTHETIC.inspect", "Malformed case", action, resource);
            entityManager.persist(malformed);
            grant(other, role("TEST_MALFORMED", malformed), AuthorizationScope.global(c.second()), now, null, admin);
            entityManager.flush();
            return new GrantFixture(otherCatalog, readRole.getId(), adminRole.getId(), catalog.getId());
        });
    }

    private Permission permission(String code) {
        return entityManager.createQuery("select p from Permission p where p.code=:code", Permission.class)
                .setParameter("code", code).getSingleResult();
    }

    private Role role(String code, Permission permission) {
        Role role = new Role(code, code);
        entityManager.persist(role);
        entityManager.persist(new RolePermission(role, permission));
        return role;
    }

    private UserRoleGrant grant(User user, Role role, AuthorizationScope scope, Instant from, Instant to, User actor) {
        var grant = new UserRoleGrant(user, role, scope, "synthetic", from, to, actor);
        entityManager.persist(grant);
        return grant;
    }

    private String login(String username) throws Exception {
        return JsonPath.read(loginTokens(username), "$.accessToken");
    }

    private String storedHash(long userId) {
        return jdbc.queryForObject("SELECT F_01 FROM T_10_01_10 WHERE F_03=?", String.class, userId);
    }

    private static String loginBody(String username, String password) {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }

    private static String credentialBody(String password) {
        return "{\"password\":\"" + password + "\",\"reason\":\"synthetic credential administration\"}";
    }

    private String loginTokens(String username) throws Exception {
        return loginTokens(username, PASSWORD);
    }

    private String loginTokens(String username, String password) throws Exception {
        var response = client.send(HttpRequest.newBuilder(uri("/v1/auth/login")).timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json").header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginBody(username, password)))
                .build(), HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        return response.body();
    }

    private HttpResponse<String> get(String path, String access) throws Exception {
        var builder = HttpRequest.newBuilder(uri(path)).timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json, application/problem+json").GET();
        if (access != null) builder.header("Authorization", "Bearer " + access);
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String path) { return URI.create("http://localhost:" + port + "/hyflo/api" + path); }
    private String permissions(long id) { return "/v1/me/permissions?companyId=" + id; }
    private String catalog(long id) { return "/v1/identity/user-statuses?companyId=" + id; }

    private static void assertCodes(HttpResponse<String> response, long companyId, List<String> expected) {
        assertThat(response.statusCode()).isEqualTo(200);
        Number actualId = JsonPath.read(response.body(), "$.companyId");
        List<String> codes = JsonPath.read(response.body(), "$.permissionCodes");
        assertThat(actualId.longValue()).isEqualTo(companyId);
        assertThat(codes).containsExactlyElementsOf(expected);
        assertThat(response.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertThat(response.body()).doesNotContain("grantedBy", "token", "password", "organizationUnit");
    }

    private static void assertCatalog(HttpResponse<String> response) {
        assertThat(response.statusCode()).isEqualTo(200);
        List<String> codes = JsonPath.read(response.body(), "$[*].code");
        List<Number> ids = JsonPath.read(response.body(), "$[*].id");
        List<String> names = JsonPath.read(response.body(), "$[*].displayName");
        assertThat(codes).containsExactly("ACTIVE", "DISABLED", "PENDING", "SUSPENDED");
        assertThat(ids).hasSize(4).allSatisfy(id -> assertThat(id.longValue()).isPositive());
        assertThat(names).hasSize(4);
        assertThat(response.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
    }

    private static String detail(HttpResponse<String> response) { return JsonPath.read(response.body(), "$.detail"); }

    private static void assertProblem(HttpResponse<String> response, int status, String code) {
        assertThat(response.statusCode()).isEqualTo(status);
        String actual = JsonPath.read(response.body(), "$.code");
        assertThat(actual).isEqualTo(code);
        String correlation = JsonPath.read(response.body(), "$.correlationId");
        assertThat(correlation).isNotBlank();
        assertThat(response.body()).doesNotContain("permissionCodes", "passwordHash", "SQLState", "Exception");
    }

    private record Companies(long first, long second, long third, long division, long inactive,
            long expired, long future, long retired) {}
    private record GrantFixture(long otherCatalogGrant, long readRole, long adminRole, long catalogPermission) {}
}
