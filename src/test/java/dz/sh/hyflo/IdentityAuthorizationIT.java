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
import dz.sh.hyflo.modules.foundation.identity.domain.model.LoginSession;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import dz.sh.hyflo.modules.foundation.identity.domain.model.Role;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserType;
import dz.sh.hyflo.modules.foundation.organization.application.service.EmployeeAssignmentService;
import dz.sh.hyflo.modules.foundation.organization.domain.model.AssignmentStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.AssignmentType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Employee;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeAssignment;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeStatus;
import dz.sh.hyflo.modules.foundation.organization.domain.model.EmployeeType;
import dz.sh.hyflo.modules.foundation.organization.domain.model.Position;
import dz.sh.hyflo.modules.foundation.organization.domain.model.PositionLevel;
import dz.sh.hyflo.modules.foundation.organization.domain.model.PositionStatus;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "hyflo.identity.login-global-limit=200", "hyflo.identity.login-ip-limit=100",
        "hyflo.identity.login-window-seconds=3600"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class IdentityAuthorizationIT {

    private static final String PASSWORD = "Synthetic-authorization-password!";
    private static final String SUSPENDED_PASSWORD = "Synthetic-suspended-password!";
    private static final String RESET_PASSWORD = "Synthetic-replacement-password!";
    private static final List<String> EXPECTED = List.of("credential.initialize", "credential.reset",
            "identity-catalog.read", "login-session.revoke", "permission.read", "role.read",
            "user-employee.associate", "user-employee.read", "user-role-grant.assign", "user-role-grant.read", "user-role-grant.revoke", "user.activate",
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
    @Autowired EmployeeAssignmentService employeeAssignments;
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

        exerciseDefinitionCatalogs(companies, admin, other);
        var fixture = createGrantFixtures(companies, adminId, otherId);
        exerciseAccountAdministration(companies, adminId, otherId, admin, other, fixture);
        exerciseAccountSearch(companies, admin, other, fixture);
        exerciseEmployeeAssociation(companies, adminId, otherId, admin, other, fixture);
        var malformedCatalog = get("/v1/identity/permissions?companyId=" + companies.first(), admin);
        assertThat(malformedCatalog.statusCode()).isEqualTo(200);
        List<Map<String, Object>> definitions = JsonPath.read(malformedCatalog.body(), "$[*]");
        assertThat(definitions).anySatisfy(definition -> {
            assertThat(definition.get("code")).isEqualTo("SYNTHETIC.inspect");
            assertThat(definition.get("validDefinition")).isEqualTo(false);
        });
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
            assertProblem(get("/v1/identity/roles?companyId=" + companies.first(), admin), 503, "IDENTITY_UNAVAILABLE");
            assertProblem(get("/v1/identity/permissions?companyId=" + companies.first(), admin), 503, "IDENTITY_UNAVAILABLE");
            assertProblem(get("/v1/identity/users?companyId=" + companies.first() + "&usernamePrefix=managed", admin),
                    503, "IDENTITY_UNAVAILABLE");
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


    private void exerciseEmployeeAssociation(Companies c, long adminId, long otherId, String admin, String other,
            GrantFixture fixture) throws Exception {
        var f = createEmployeeAssociationFixtures(c, adminId);
        long a = userId("search_0");
        long b = userId("search_1");
        long disabled = userId("managed.user");
        long service = userId("managed-service");
        long bootstrapId = userId("hyflo-bootstrap");
        String first = "?companyId=" + c.first();
        String second = "?companyId=" + c.second();
        String root = "/v1/identity/users/";
        String live = root + f.liveUser() + "/employee";
        String reason = "{\"reason\":\"synthetic unlink\"}";
        String liveAccess = login("employee-live");
        String beforeHash = storedHash(f.liveUser());
        long beforeSessions = jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=?", Long.class, f.liveUser());
        long beforeGrants = jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=?", Long.class, f.liveUser());

        assertProblem(get(live + first, null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post(live + "/link" + first, linkBody(f.descendant()), null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get(live + first, other), 403, "ACCESS_DENIED");
        assertProblem(post(live + "/link" + first, linkBody(f.descendant()), other), 403, "ACCESS_DENIED");
        assertProblem(get(live, admin), 400, "INVALID_REQUEST");
        assertProblem(get(live + second, admin), 403, "ACCESS_DENIED");
        assertProblem(post(live + "/link" + first, "{\"employeeId\":0,\"reason\":\"invalid\"}", admin), 400, "INVALID_REQUEST");
        assertProblem(post(live + "/link" + first, "{\"employeeId\":null,\"reason\":\"invalid\"}", admin), 400, "INVALID_REQUEST");
        assertProblem(post(live + "/link" + first, "{\"employeeId\":" + f.descendant() + ",\"reason\":\" \"}", admin), 400, "INVALID_REQUEST");
        assertProblem(post(live + "/unlink" + first, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertAssociation(get(live + first, admin), f.liveUser(), null);
        String denial = null;
        for (long target : List.of(adminId, otherId, disabled, service, bootstrapId, Long.MAX_VALUE)) {
            var rejected = post(root + target + "/employee/link" + first, linkBody(f.descendant()), admin);
            assertProblem(rejected, 403, "ACCESS_DENIED");
            if (denial == null) denial = detail(rejected);
            else assertThat(detail(rejected)).isEqualTo(denial);
        }
        for (long employee : f.deniedEmployees()) {
            var rejected = post(live + "/link" + first, linkBody(employee), admin);
            assertProblem(rejected, 403, "ACCESS_DENIED");
            assertThat(detail(rejected)).isEqualTo(denial);
        }
        assertProblem(post(live + "/link" + first, linkBody(Long.MAX_VALUE), admin), 403, "ACCESS_DENIED");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_19", Long.class)).isZero();

        long write = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='user-employee.associate'", Long.class);
        long read = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='user-employee.read'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), write);
        try {
            assertAssociation(get(live + first, admin), f.liveUser(), null);
            assertProblem(post(live + "/link" + first, linkBody(f.descendant()), admin), 403, "ACCESS_DENIED");
        } finally {
            jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), write);
        }

        jdbc.execute("CREATE TRIGGER hyflo_test_employee_event_failure BEFORE INSERT ON T_10_01_19 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic association evidence failure'");
        try {
            assertProblem(post(live + "/link" + first, linkBody(f.descendant()), admin), 503, "IDENTITY_UNAVAILABLE");
            assertAssociation(get(live + first, admin), f.liveUser(), null);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_19", Long.class)).isZero();
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_employee_event_failure");
        }

        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), read);
        try {
            assertProblem(get(live + first, admin), 403, "ACCESS_DENIED");
            var linked = post(live + "/link" + first, linkBody(f.descendant()), admin);
            assertThat(linked.statusCode()).isEqualTo(204);
            assertThat(linked.body()).isEmpty();
            assertThat(linked.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        } finally {
            jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), read);
        }
        assertAssociation(get(live + first, admin), f.liveUser(), f.descendant());
        assertProblem(post(live + "/link" + first, linkBody(f.direct()), admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertProblem(post(root + a + "/employee/link" + first, linkBody(f.descendant()), admin), 409, "ACCOUNT_OPERATION_REJECTED");

        // Database constraints independently reject dangling and duplicated scalar references.
        assertThatThrownBy(() -> jdbc.update("UPDATE T_10_01_08 SET F_03=? WHERE F_00=?", Long.MAX_VALUE, a))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE T_10_01_08 SET F_03=? WHERE F_00=?", f.descendant(), a))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
        assertAssociation(get(root + a + "/employee" + first, admin), a, null);
        assertAssociation(get(root + b + "/employee" + first, admin), b, null);

        // Transfer through the existing Organization service; Identity must not follow the assignment.
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var assignment = entityManager.createQuery(
                    "select a from EmployeeAssignment a where a.employee.id=:id", EmployeeAssignment.class)
                    .setParameter("id", f.descendant()).getSingleResult();
            var ended = entityManager.createQuery("select s from AssignmentStatus s where s.code='ENDED'", AssignmentStatus.class)
                    .getSingleResult();
            employeeAssignments.transfer(f.descendant(), entityManager.find(OrganizationUnit.class, c.second()),
                    assignment.getPosition(), LocalDate.now(ZoneOffset.UTC), assignment.getAssignmentType(),
                    assignment.getAssignmentStatus(), ended);
        });
        assertAssociation(get(live + first, admin), f.liveUser(), f.descendant());
        assertThat(jdbc.queryForObject("SELECT F_05 FROM T_10_01_08 WHERE F_00=?", Long.class, f.liveUser())).isEqualTo(c.first());
        assertThat(post(live + "/unlink" + first, reason, admin).statusCode()).isEqualTo(204);
        assertAssociation(get(live + first, admin), f.liveUser(), null);
        assertProblem(post(live + "/link" + first, linkBody(f.descendant()), admin), 403, "ACCESS_DENIED");
        assertThat(jdbc.queryForList("SELECT F_01 FROM T_10_01_19 WHERE F_08=? ORDER BY F_00", String.class, f.liveUser()))
                .containsExactly("LINKED", "UNLINKED");
        assertThat(jdbc.queryForList("SELECT F_06 FROM T_10_01_19 WHERE F_08=? ORDER BY F_00", Long.class, f.liveUser()))
                .containsExactly(null, f.descendant());
        assertThat(jdbc.queryForList("SELECT F_07 FROM T_10_01_19 WHERE F_08=? ORDER BY F_00", Long.class, f.liveUser()))
                .containsExactly(f.descendant(), null);
        assertThat(jdbc.queryForObject("""
                SELECT COUNT(*) FROM T_10_01_19 WHERE F_08=? AND F_09=? AND F_05=?
                    AND F_02 IS NOT NULL AND F_03 IS NOT NULL AND F_04 IN ('synthetic link','synthetic unlink')
                """, Long.class, f.liveUser(), adminId, c.first())).isEqualTo(2L);
        assertThat(storedHash(f.liveUser())).isEqualTo(beforeHash);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_04=? AND F_03 IS NULL", Long.class, f.liveUser()))
                .isEqualTo(beforeSessions);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=?", Long.class, f.liveUser()))
                .isEqualTo(beforeGrants);
        assertThat(get("/v1/me", liveAccess).statusCode()).isEqualTo(200);
        assertCodes(get(permissions(c.first()), liveAccess), c.first(), List.of("user.read"));
        assertThat((String) JsonPath.read(get(root + f.liveUser() + first, admin).body(), "$.userStatusCode")).isEqualTo("ACTIVE");

        String pa = root + a + "/employee";
        String pb = root + b + "/employee";
        var x = client.sendAsync(postRequest(pa + "/link" + first, linkBody(f.direct()), admin), HttpResponse.BodyHandlers.ofString());
        var y = client.sendAsync(postRequest(pb + "/link" + first, linkBody(f.direct()), admin), HttpResponse.BodyHandlers.ofString());
        var results = List.of(x.get(30, java.util.concurrent.TimeUnit.SECONDS), y.get(30, java.util.concurrent.TimeUnit.SECONDS));
        assertThat(results.stream().map(HttpResponse::statusCode).toList()).containsExactlyInAnyOrder(204, 409);
        long winner = results.getFirst().statusCode() == 204 ? a : b;
        long loser = winner == a ? b : a;
        assertAssociation(get(root + winner + "/employee" + first, admin), winner, f.direct());
        assertAssociation(get(root + loser + "/employee" + first, admin), loser, null);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_19 WHERE F_07=?", Long.class, f.direct())).isEqualTo(1L);
        // A disabled linked account retains its reference until an explicit authorized unlink.
        assertThat(post(root + winner + "/disable" + first, reason, admin).statusCode()).isEqualTo(200);
        assertProblem(post(root + loser + "/employee/link" + first, linkBody(f.direct()), admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(post(root + winner + "/employee/unlink" + first, reason, admin).statusCode()).isEqualTo(204);
        assertThat(post(root + loser + "/employee/link" + first, linkBody(f.direct()), admin).statusCode()).isEqualTo(204);
        assertThat(post(root + loser + "/employee/unlink" + first, reason, admin).statusCode()).isEqualTo(204);
        assertThat(get("/api-docs", admin).body()).contains("/v1/identity/users/{userId}/employee",
                "/v1/identity/users/{userId}/employee/link", "/v1/identity/users/{userId}/employee/unlink");
    }

    private long userId(String username) {
        return jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01=?", Long.class, username);
    }

    private static String linkBody(long employee) {
        return "{\"employeeId\":" + employee + ",\"reason\":\"synthetic link\"}";
    }

    private static void assertAssociation(HttpResponse<String> response, long userId, Long employeeId) {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        Map<String, Object> value = JsonPath.read(response.body(), "$");
        assertThat(value).containsOnlyKeys("userId", "employeeId");
        assertThat(((Number) value.get("userId")).longValue()).isEqualTo(userId);
        if (employeeId == null) assertThat(value.get("employeeId")).isNull();
        else assertThat(((Number) value.get("employeeId")).longValue()).isEqualTo(employeeId);
    }

    private EmployeeLinkFixture createEmployeeAssociationFixtures(Companies c, long adminId) {
        var fixture = new TransactionTemplate(transactions).execute(tx -> {
            LocalDate today = LocalDate.now(ZoneOffset.UTC);
            var first = entityManager.find(OrganizationUnit.class, c.first());
            var second = entityManager.find(OrganizationUnit.class, c.second());
            var divisionType = entityManager.find(OrganizationUnit.class, c.division()).getOrganizationUnitType();
            var activeUnit = first.getOrganizationUnitStatus();
            var nested = new OrganizationUnit("LINK_NESTED", null, null, "Nested company",
                    first.getOrganizationUnitType(), first, today.minusDays(10), null, activeUnit);
            var child = new OrganizationUnit("LINK_CHILD", null, null, "Child unit", divisionType, first,
                    today.minusDays(10), null, activeUnit);
            var cyclic = new OrganizationUnit("LINK_CYCLE", null, null, "Cycle probe", divisionType, first,
                    today.minusDays(10), null, activeUnit);
            var inactiveChild = new OrganizationUnit("LINK_INACTIVE", null, null, "Inactive child", divisionType, first,
                    today.minusDays(10), null, entityManager.find(OrganizationUnit.class, c.inactive()).getOrganizationUnitStatus());
            var type = new EmployeeType(EmployeeType.PERMANENT_CODE, null, null, "Permanent");
            var active = new EmployeeStatus(EmployeeStatus.ACTIVE_CODE, null, null, "Active");
            var suspended = new EmployeeStatus(EmployeeStatus.SUSPENDED_CODE, null, null, "Suspended");
            var level = new PositionLevel(PositionLevel.ENGINEER_CODE, null, null, "Engineer");
            var positionStatus = new PositionStatus(PositionStatus.ACTIVE_CODE, null, null, "Active");
            var assignmentType = new AssignmentType(AssignmentType.PRIMARY_CODE, null, null, "Primary");
            var assigned = new AssignmentStatus(AssignmentStatus.ACTIVE_CODE, null, null, "Active");
            var ended = new AssignmentStatus(AssignmentStatus.ENDED_CODE, null, null, "Ended");
            var suspendedAssignment = new AssignmentStatus(AssignmentStatus.SUSPENDED_CODE, null, null, "Suspended");
            for (var item : List.of(nested, child, cyclic, inactiveChild, type, active, suspended, level,
                    positionStatus, assignmentType, assigned, ended, suspendedAssignment)) entityManager.persist(item);
            var position = new Position("LINK_POSITION", null, null, "Engineer", null, null, "Engineer", level, positionStatus);
            entityManager.persist(position);
            var descendant = employee("LINK-DESC", type, active, today.minusDays(10), null);
            var direct = employee("LINK-DIRECT", type, active, today.minusDays(10), null);
            var foreign = employee("LINK-FOREIGN", type, active, today.minusDays(10), null);
            var inactive = employee("LINK-STATUS", type, suspended, today.minusDays(10), null);
            var expired = employee("LINK-EMP-END", type, active, today.minusDays(10), today);
            var future = employee("LINK-FUTURE", type, active, today.minusDays(10), null);
            var old = employee("LINK-OLD", type, active, today.minusDays(10), null);
            var ambiguous = employee("LINK-AMBIG", type, active, today.minusDays(10), null);
            var badAssignment = employee("LINK-ASSIGN-STATUS", type, active, today.minusDays(10), null);
            var nestedEmployee = employee("LINK-NEST", type, active, today.minusDays(10), null);
            var cycleEmployee = employee("LINK-CYCLE", type, active, today.minusDays(10), null);
            var noCompany = employee("LINK-NOCOMPANY", type, active, today.minusDays(10), null);
            var inactiveUnitEmployee = employee("LINK-INACTIVE-UNIT", type, active, today.minusDays(10), null);
            var unassigned = employee("LINK-UNASSIGNED", type, active, today.minusDays(10), null);
            var notHired = employee("LINK-NOTHIRED", type, active, today.plusDays(1), null);
            var start = today.minusDays(2);
            for (var pair : List.of(new Object[]{descendant, child}, new Object[]{direct, first},
                    new Object[]{foreign, second}, new Object[]{inactive, first}, new Object[]{expired, first},
                    new Object[]{ambiguous, first}, new Object[]{badAssignment, first}, new Object[]{nestedEmployee, nested},
                    new Object[]{cycleEmployee, cyclic}, new Object[]{noCompany, entityManager.find(OrganizationUnit.class, c.division())},
                    new Object[]{inactiveUnitEmployee, inactiveChild})) {
                entityManager.persist(new EmployeeAssignment((Employee) pair[0], (OrganizationUnit) pair[1], position,
                        start, assignmentType, assigned));
            }
            entityManager.persist(new EmployeeAssignment(ambiguous, child, position, today.minusDays(1), assignmentType, assigned));
            entityManager.persist(new EmployeeAssignment(future, first, position, today.plusDays(1), assignmentType, assigned));
            var endedAssignment = new EmployeeAssignment(old, first, position, start, assignmentType, assigned);
            endedAssignment.endOn(today.minusDays(1), ended);
            entityManager.persist(endedAssignment);
            var admin = entityManager.find(User.class, adminId);
            var live = new User(admin.getIdentityProvider(), "employee-live", "employee-live@example.invalid", null,
                    c.first(), status("PENDING"), admin.getUserType());
            entityManager.persist(live);
            var credential = new LocalCredential(live, encoder.encode(PASSWORD), Instant.now());
            entityManager.persist(credential);
            live.activate(credential, status("ACTIVE"));
            grant(live, role("EMPLOYEE_LINK_READER", permission("user.read")), AuthorizationScope.global(c.first()),
                    Instant.now().minusSeconds(1), null, admin);
            entityManager.flush();
            return new EmployeeLinkFixture(live.getId(), descendant.getId(), direct.getId(), cyclic.getId(),
                    badAssignment.getId(), suspendedAssignment.getId(), List.of(foreign.getId(), inactive.getId(),
                    expired.getId(), future.getId(), old.getId(), ambiguous.getId(), badAssignment.getId(), nestedEmployee.getId(),
                    cycleEmployee.getId(), noCompany.getId(), inactiveUnitEmployee.getId(), unassigned.getId(), notHired.getId()));
        });
        // Intentional malformed database fixtures, never exposed as production mutation APIs.
        jdbc.update("UPDATE T_10_02_11 SET F_08=F_00 WHERE F_00=?", fixture.cycleUnit());
        jdbc.update("UPDATE T_10_02_12 SET F_07=? WHERE F_05=?", fixture.suspendedStatus(), fixture.suspendedEmployee());
        return fixture;
    }

    private Employee employee(String number, EmployeeType type, EmployeeStatus status, LocalDate hire, LocalDate end) {
        var employee = new Employee(number, null, null, "Synthetic", "Employee", hire, end, type, status);
        entityManager.persist(employee);
        return employee;
    }

    private record EmployeeLinkFixture(long liveUser, long descendant, long direct, long cycleUnit,
            long suspendedEmployee, long suspendedStatus, List<Long> deniedEmployees) {}

    private void exerciseAccountSearch(Companies c, String admin, String other, GrantFixture fixture) throws Exception {
        String root = "/v1/identity/users";
        String first = root + "?companyId=" + c.first();
        String second = root + "?companyId=" + c.second();

        assertProblem(get(first + "&usernamePrefix=managed", null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get(first + "&usernamePrefix=managed", other), 403, "ACCESS_DENIED");
        assertProblem(get(root + "?usernamePrefix=managed", admin), 400, "INVALID_REQUEST");
        assertProblem(get(root + "?companyId=0&usernamePrefix=managed", admin), 400, "INVALID_REQUEST");
        for (String invalid : List.of("&usernamePrefix=", "&usernamePrefix=%25", "&usernamePrefix=a%20b",
                "&usernamePrefix=" + "a".repeat(21), "&email=", "&email=a%20b",
                "&email=" + "a".repeat(101), "&userStatusCode=", "&userStatusCode=active",
                "&userStatusCode=UNKNOWN", "&userTypeCode=", "&userTypeCode=human",
                "&userTypeCode=UNKNOWN", "&afterId=", "&afterId=-1", "&afterId=9223372036854775808")) {
            assertProblem(get(first + invalid, admin), 400, "INVALID_REQUEST");
        }
        assertSearchNames(get(first + "&usernamePrefix=%20MANAGED.%20", admin), "managed.user");
        assertSearchNames(get(first + "&usernamePrefix=managed&userTypeCode=HUMAN", admin), "managed.user");
        assertSearchNames(get(first + "&usernamePrefix=managed&userTypeCode=SERVICE", admin), "managed-service");
        assertSearchNames(get(first + "&usernamePrefix=managed&userStatusCode=DISABLED", admin), "managed.user");
        assertSearchNames(get(first + "&email=managed%40example.invalid&userStatusCode=DISABLED&userTypeCode=HUMAN",
                admin), "managed.user");
        assertSearchNames(get(first + "&email=managed%40example.invalid&userTypeCode=SERVICE", admin));
        assertSearchNames(get(first + "&usernamePrefix=nonexistent", admin));
        assertSearchNames(get(first + "&usernamePrefix=hyflo-bootstrap", admin));
        assertSearchNames(get(first + "&email=authz-user%40example.invalid", admin));
        assertSearchNames(get(second + "&usernamePrefix=managed", admin));
        assertSearchNames(get(second + "&usernamePrefix=AUTHZ", admin), "authz-user");
        assertSearchNames(get(second + "&userStatusCode=ACTIVE&userTypeCode=HUMAN", other), "authz-user");

        long permissionId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='user.read'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), permissionId);
        try {
            assertProblem(get(first + "&usernamePrefix=managed", admin), 403, "ACCESS_DENIED");
            assertSearchNames(get(second + "&usernamePrefix=AUTHZ", admin), "authz-user");
        } finally {
            jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), permissionId);
        }
        assertSearchNames(get(first + "&usernamePrefix=managed.", admin), "managed.user");

        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var provider = entityManager.createQuery("select p from IdentityProvider p where p.code='LOCAL'",
                    IdentityProvider.class).getSingleResult();
            var human = entityManager.createQuery("select t from UserType t where t.code='HUMAN'",
                    UserType.class).getSingleResult();
            var pending = status("PENDING");
            for (int i = 0; i < 101; i++) {
                String name = "search_" + i;
                entityManager.persist(new User(provider, name, name + "@example.invalid", null, c.first(), pending, human));
                if (i == 0) {
                    entityManager.persist(new User(provider, "searchx0", "searchx0@example.invalid",
                            null, c.first(), pending, human));
                    entityManager.persist(new User(provider, "search_foreign", "search-foreign@example.invalid",
                            null, c.second(), pending, human));
                }
            }
            entityManager.flush();
        });
        String filtered = first + "&usernamePrefix=search_&userStatusCode=PENDING&userTypeCode=HUMAN";
        var page = get(filtered, admin);
        assertThat(page.statusCode()).isEqualTo(200);
        assertThat(page.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        List<Map<String, Object>> rows = JsonPath.read(page.body(), "$[*]");
        assertThat(rows).hasSize(100).allSatisfy(row -> {
            assertThat(row).containsOnlyKeys("id", "username", "email", "companyId", "userTypeCode", "userStatusCode");
            assertThat((String) row.get("username")).startsWith("search_").isNotEqualTo("search_foreign");
            assertThat(((Number) row.get("companyId")).longValue()).isEqualTo(c.first());
            assertThat(row.get("userStatusCode")).isEqualTo("PENDING");
            assertThat(row.get("userTypeCode")).isEqualTo("HUMAN");
        });
        List<Number> ids = JsonPath.read(page.body(), "$[*].id");
        assertThat(ids.stream().map(Number::longValue).toList()).isSorted();
        var next = get(filtered + "&afterId=" + ids.getLast(), admin);
        assertSearchNames(next, "search_100");
        List<Number> nextIds = JsonPath.read(next.body(), "$[*].id");
        assertThat(nextIds.getFirst().longValue()).isGreaterThan(ids.getLast().longValue());
        assertSearchNames(get(filtered + "&afterId=" + nextIds.getFirst(), admin));
        assertSearchNames(get(second + "&usernamePrefix=search_", admin), "search_foreign");
        var openapi = get("/api-docs", admin);
        assertThat(openapi.statusCode()).isEqualTo(200);
        assertThat(openapi.body()).contains("usernamePrefix", "email", "userStatusCode", "userTypeCode", "afterId");
    }

    private static void assertSearchNames(HttpResponse<String> response, String... expected) {
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        List<String> names = JsonPath.read(response.body(), "$[*].username");
        assertThat(names).containsExactly(expected);
    }

    private void exerciseDefinitionCatalogs(Companies c, String admin, String other) throws Exception {
        String first = "?companyId=" + c.first();
        String rolesPath = "/v1/identity/roles";
        String permissionsPath = "/v1/identity/permissions";
        long adminRole = jdbc.queryForObject("SELECT F_00 FROM T_10_01_04 WHERE F_01='IDENTITY_ADMIN'", Long.class);
        String bundlePath = rolesPath + "/" + adminRole + "/permissions";
        for (String path : List.of(rolesPath, permissionsPath, bundlePath)) {
            assertProblem(get(path + first, null), 401, "AUTHENTICATION_REQUIRED");
            assertProblem(get(path + first, other), 403, "ACCESS_DENIED");
            assertProblem(get(path + "?companyId=" + c.second(), admin), 403, "ACCESS_DENIED");
            assertProblem(get(path, admin), 400, "INVALID_REQUEST");
            for (String cursor : List.of("-1", "x", "9223372036854775808")) {
                assertProblem(get(path + first + "&afterId=" + cursor, admin), 400, "INVALID_REQUEST");
            }
            assertThat(get(path + first + "&afterId=" + Long.MAX_VALUE, admin).body()).isEqualTo("[]");
        }
        assertProblem(get(rolesPath + "/0/permissions" + first, admin), 400, "INVALID_REQUEST");
        assertThat(get(rolesPath + "/" + Long.MAX_VALUE + "/permissions" + first, admin).body()).isEqualTo("[]");
        var roleResponse = get(rolesPath + first, admin);
        assertThat(roleResponse.statusCode()).isEqualTo(200);
        List<Map<String, Object>> roleEntries = JsonPath.read(roleResponse.body(), "$[*]");
        assertThat(roleEntries).hasSize(1);
        assertThat(roleEntries.getFirst()).containsOnlyKeys("id", "code", "displayName", "enabled");
        assertThat(roleEntries.getFirst().get("code")).isEqualTo("IDENTITY_ADMIN");
        assertThat(roleEntries.getFirst().get("enabled")).isEqualTo(true);
        assertThat(roleResponse.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        for (String path : List.of(permissionsPath, bundlePath)) {
            var response = get(path + first, admin);
            assertThat(response.statusCode()).isEqualTo(200);
            List<Map<String, Object>> entries = JsonPath.read(response.body(), "$[*]");
            assertThat(entries).hasSize(EXPECTED.size()).allSatisfy(entry -> {
                assertThat(entry).containsOnlyKeys("id", "code", "displayName", "actionCode",
                        "resourceTypeCode", "validDefinition");
                assertThat(entry.get("validDefinition")).isEqualTo(true);
                assertThat(entry.get("code")).isEqualTo(entry.get("resourceTypeCode") + "." + entry.get("actionCode"));
            });
            assertThat(entries.stream().map(entry -> (String) entry.get("code")).toList())
                    .containsExactlyInAnyOrderElementsOf(EXPECTED);
            List<Number> ids = JsonPath.read(response.body(), "$[*].id");
            assertThat(ids.stream().map(Number::longValue).toList()).isSorted();
            assertThat(response.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
            List<Number> later = JsonPath.read(get(path + first + "&afterId=" + ids.getFirst(), admin).body(), "$[*].id");
            assertThat(later).containsExactlyElementsOf(ids.subList(1, ids.size()));
        }
        // Remove one exact permission at a time; the same JWT must not bypass either gate.
        for (String code : List.of("role.read", "permission.read")) {
            long id = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01=?", Long.class, code);
            jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", adminRole, id);
            try {
                assertProblem(get((code.equals("role.read") ? rolesPath : permissionsPath) + first, admin),
                        403, "ACCESS_DENIED");
                assertThat(get((code.equals("role.read") ? permissionsPath : rolesPath) + first, admin).statusCode())
                        .isEqualTo(200);
                assertProblem(get(bundlePath + first, admin), 403, "ACCESS_DENIED");
            } finally {
                jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", adminRole, id);
            }
        }
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            for (int i = 0; i < 101; i++) {
                Role definition = new Role("TEST_CATALOG_PAGE_" + i, "Synthetic catalog page " + i);
                if (i == 0) definition.retire();
                entityManager.persist(definition);
            }
            entityManager.flush();
        });
        var firstPage = get(rolesPath + first, admin);
        assertThat(firstPage.statusCode()).isEqualTo(200);
        List<Number> firstIds = JsonPath.read(firstPage.body(), "$[*].id");
        assertThat(firstIds).hasSize(100);
        assertThat(firstIds.stream().map(Number::longValue).toList()).isSorted();
        List<Boolean> enabled = JsonPath.read(firstPage.body(), "$[*].enabled");
        assertThat(enabled).contains(false);
        var secondPage = get(rolesPath + first + "&afterId=" + firstIds.getLast(), admin);
        assertThat(secondPage.statusCode()).isEqualTo(200);
        List<Number> secondIds = JsonPath.read(secondPage.body(), "$[*].id");
        assertThat(secondIds).hasSize(2).allSatisfy(id ->
                assertThat(id.longValue()).isGreaterThan(firstIds.getLast().longValue()));
        assertThat(get("/api-docs", admin).body()).contains(rolesPath, permissionsPath,
                "/v1/identity/roles/{roleId}/permissions");
        assertProblem(post(permissionsPath + first, "{}", admin), 403, "ACCESS_DENIED");
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
            entityManager.persist(new RolePermission(role, permission("login-session.revoke")));
            return grant(actor, role, AuthorizationScope.global(c.second()), Instant.now().minusSeconds(5), null, actor).getId();
        });
        assertProblem(post(managedPath + "/disable" + second, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post(managedPath + "/credential/initialize" + second, credential, admin), 403, "ACCESS_DENIED");
        assertProblem(post(managedPath + "/credential/reset" + second, credential, admin), 403, "ACCESS_DENIED");
        assertProblem(post(servicePath + "/sessions/revoke" + second, reason, admin), 403, "ACCESS_DENIED");
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
        exerciseAdministrativeSessionRevocation(c, adminId, otherId, admin, other, fixture, serviceId, servicePath);
    }

    private void exerciseAdministrativeSessionRevocation(Companies c, long adminId, long otherId,
            String admin, String other, GrantFixture fixture, long serviceId, String servicePath) throws Exception {
        String first = "?companyId=" + c.first();
        String second = "?companyId=" + c.second();
        String revoke = servicePath + "/sessions/revoke" + first;
        String reason = "{\"reason\":\"synthetic security response\"}";
        assertProblem(post(revoke, reason, null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post(revoke, reason, other), 403, "ACCESS_DENIED");
        assertProblem(post(servicePath + "/sessions/revoke", reason, admin), 400, "INVALID_REQUEST");
        assertProblem(post(revoke, "{\"reason\":\" \"}", admin), 400, "INVALID_REQUEST");
        assertProblem(post(revoke, "{\"reason\":\"" + "x".repeat(256) + "\"}", admin), 400, "INVALID_REQUEST");
        assertProblem(post("/v1/identity/users/" + adminId + "/sessions/revoke" + first, reason, admin), 403, "ACCESS_DENIED");
        long bootstrapId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        assertProblem(post("/v1/identity/users/" + bootstrapId + "/sessions/revoke" + first, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post("/v1/identity/users/" + Long.MAX_VALUE + "/sessions/revoke" + first, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post("/v1/identity/users/" + otherId + "/sessions/revoke" + first, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post(servicePath + "/sessions/revoke" + second, reason, admin), 403, "ACCESS_DENIED");
        assertProblem(post(revoke, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(post(servicePath + "/activate" + first, reason, admin).statusCode()).isEqualTo(200);

        var aTokens = loginTokens("managed-service", RESET_PASSWORD);
        var bTokens = loginTokens("managed-service", RESET_PASSWORD);
        String aAccess = JsonPath.read(aTokens, "$.accessToken");
        String bAccess = JsonPath.read(bTokens, "$.accessToken");
        String aRefresh = JsonPath.read(aTokens, "$.refreshToken");
        String bRefresh = JsonPath.read(bTokens, "$.refreshToken");
        long aId = tokens.verify(aAccess, Instant.now()).sessionId();
        long bId = tokens.verify(bAccess, Instant.now()).sessionId();
        assertThat(aId).isNotEqualTo(bId);
        String originalHash = storedHash(serviceId);
        long[] historical = new TransactionTemplate(transactions).execute(tx -> {
            User target = entityManager.find(User.class, serviceId);
            Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
            var expired = new LoginSession(target, now.minusSeconds(120), now.minusSeconds(60));
            var revoked = new LoginSession(target, now.minusSeconds(120), now.plusSeconds(600));
            revoked.revoke(now.minusSeconds(30));
            var future = new LoginSession(target, now.plusSeconds(120), now.plusSeconds(600));
            entityManager.persist(expired);
            entityManager.persist(revoked);
            entityManager.persist(future);
            entityManager.flush();
            return new long[]{expired.getId(), revoked.getId(), future.getId()};
        });
        Timestamp previousRevokedAt = jdbc.queryForObject("SELECT F_03 FROM T_10_01_11 WHERE F_00=?", Timestamp.class, historical[1]);
        long permissionId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='login-session.revoke'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), permissionId);
        assertProblem(post(revoke, reason, admin), 403, "ACCESS_DENIED");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), permissionId);
        assertThat(get("/v1/me", aAccess).statusCode()).isEqualTo(200);
        assertThat(get("/v1/me", bAccess).statusCode()).isEqualTo(200);

        jdbc.execute("CREATE TRIGGER hyflo_test_admin_revoke_failure BEFORE INSERT ON T_10_01_18 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic session rollback probe'");
        try {
            assertProblem(post(revoke, reason, admin), 503, "IDENTITY_UNAVAILABLE");
            assertThat(get("/v1/me", aAccess).statusCode()).isEqualTo(200);
            assertThat(get("/v1/me", bAccess).statusCode()).isEqualTo(200);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_00 IN (?,?) AND F_03 IS NULL",
                    Long.class, aId, bId)).isEqualTo(2L);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_18 WHERE F_08=? AND F_01='SESSIONS_REVOKED'",
                    Long.class, serviceId)).isZero();
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_admin_revoke_failure");
        }

        var a = client.sendAsync(postRequest(revoke, reason, admin), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(postRequest(revoke, reason, admin), HttpResponse.BodyHandlers.ofString());
        var results = List.of(a.get(), b.get());
        assertThat(results.stream().map(HttpResponse::statusCode).toList()).containsExactlyInAnyOrder(204, 409);
        var success = results.stream().filter(response -> response.statusCode() == 204).findFirst().orElseThrow();
        assertThat(success.body()).isEmpty();
        assertThat(success.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertProblem(get("/v1/me", aAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get("/v1/me", bAccess), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + aRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(post("/v1/auth/refresh", "{\"refreshToken\":\"" + bRefresh + "\"}", null), 401, "AUTHENTICATION_REQUIRED");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_00 IN (?,?) AND F_03 IS NOT NULL",
                Long.class, aId, bId)).isEqualTo(2L);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_11 WHERE F_00 IN (?,?) AND F_03 IS NULL",
                Long.class, historical[0], historical[2])).isEqualTo(2L);
        assertThat(jdbc.queryForObject("SELECT F_03 FROM T_10_01_11 WHERE F_00=?", Timestamp.class, historical[1]))
                .isEqualTo(previousRevokedAt);
        assertProblem(post(revoke, reason, admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(storedHash(serviceId)).isEqualTo(originalHash);
        assertThat((String) JsonPath.read(get(servicePath + first, admin).body(), "$.userStatusCode")).isEqualTo("ACTIVE");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=?", Long.class, serviceId)).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_18 WHERE F_08=? AND F_01='SESSIONS_REVOKED'",
                Long.class, serviceId)).isEqualTo(1L);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_18 WHERE F_08=? AND F_09=? AND F_07=? "
                + "AND F_01='SESSIONS_REVOKED' AND F_04='synthetic security response' AND F_05='ACTIVE' "
                + "AND F_06='ACTIVE' AND F_03 IS NOT NULL AND F_02 IS NOT NULL", Long.class,
                serviceId, adminId, c.first())).isEqualTo(1L);
        assertThat(get("/v1/me", other).statusCode()).isEqualTo(200);
        assertThat(get("/v1/me", admin).statusCode()).isEqualTo(200);
        String fresh = JsonPath.read(loginTokens("managed-service", RESET_PASSWORD), "$.accessToken");
        assertThat(get("/v1/me", fresh).statusCode()).isEqualTo(200);
        assertProblem(get("/v1/me", aAccess), 401, "AUTHENTICATION_REQUIRED");
        assertThat(get("/api-docs", admin).body()).contains("/v1/identity/users/{userId}/sessions/revoke");
        assertProblem(get(servicePath + "/sessions" + first, admin), 403, "ACCESS_DENIED");
        assertProblem(post(servicePath + "/sessions/" + aId + "/revoke" + first, reason, admin), 403, "ACCESS_DENIED");
        exerciseDirectRoleDelegation(c, adminId, otherId, admin, other, fixture, serviceId, servicePath, fresh);
    }

    private void exerciseDirectRoleDelegation(Companies c, long adminId, long otherId, String admin, String other,
            GrantFixture fixture, long serviceId, String servicePath, String delegate) throws Exception {
        String root = "/v1/identity/users/";
        String first = "?companyId=" + c.first();
        String second = "?companyId=" + c.second();
        String path = servicePath + "/role-grants";
        String foreign = root + otherId + "/role-grants";
        Instant baseTime = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
        Instant end = baseTime.plusSeconds(1800);
        String request = grantBody(fixture.adminRole(), end);
        long bootstrapId = jdbc.queryForObject("SELECT F_00 FROM T_10_01_08 WHERE F_01='hyflo-bootstrap'", Long.class);
        long bootstrapGrant = jdbc.queryForObject("SELECT F_00 FROM T_10_01_15 WHERE F_08=? AND F_10=? AND F_06=?",
                Long.class, adminId, bootstrapId, c.first());
        assertProblem(get(path + first, null), 401, "AUTHENTICATION_REQUIRED");
        assertProblem(get(path + first, other), 403, "ACCESS_DENIED");
        assertProblem(get(path + second, admin), 403, "ACCESS_DENIED");
        assertProblem(get(path, admin), 400, "INVALID_REQUEST");
        assertProblem(post(path + first, request, other), 403, "ACCESS_DENIED");
        assertProblem(post(path + second, request, admin), 403, "ACCESS_DENIED");
        assertProblem(post(root + adminId + "/role-grants" + first, request, admin), 403, "ACCESS_DENIED");
        assertProblem(post(root + bootstrapId + "/role-grants" + first, request, admin), 403, "ACCESS_DENIED");
        assertProblem(post(root + Long.MAX_VALUE + "/role-grants" + first, request, admin), 403, "ACCESS_DENIED");
        assertProblem(post(foreign + first, request, admin), 403, "ACCESS_DENIED");
        assertProblem(post(path + first, grantBody(Long.MAX_VALUE, end), admin), 403, "ACCESS_DENIED");
        assertProblem(post(path + first, grantBody(fixture.adminRole(), baseTime.minusSeconds(1)), admin), 400, "INVALID_REQUEST");
        assertProblem(post(path + first, "{\"roleId\":0,\"validTo\":\"" + end + "\",\"reason\":\"test\"}", admin),
                400, "INVALID_REQUEST");
        assertThat(get(path + first, admin).body()).isEqualTo("[]");
        assertThat(get(root + Long.MAX_VALUE + "/role-grants" + first, admin).body()).isEqualTo("[]");
        assertProblem(get(permissions(c.first()), delegate), 403, "ACCESS_DENIED");

        // A known, finite non-administrative catalog role is a synthetic fixture, not a seed.
        long[] source = new TransactionTemplate(transactions).execute(tx -> {
            User actor = entityManager.find(User.class, adminId);
            User recipient = entityManager.find(User.class, otherId);
            Role ordinary = role("TEST_DELEGABLE_CATALOG", permission("role.read"));
            var authority = grant(actor, ordinary, AuthorizationScope.global(c.first()),
                    baseTime.minusSeconds(60), end, actor);
            var expired = grant(recipient, ordinary, AuthorizationScope.global(c.first()),
                    baseTime.minusSeconds(120), baseTime.minusSeconds(60), actor);
            var future = grant(recipient, ordinary, AuthorizationScope.global(c.first()),
                    end.plusSeconds(30), end.plusSeconds(60), actor);
            entityManager.flush();
            return new long[]{ordinary.getId(), authority.getId(), expired.getId(), future.getId()};
        });
        assertProblem(post(foreign + first, grantBody(source[0], end.plusSeconds(1)), admin), 403, "ACCESS_DENIED");
        assertProblem(post(foreign + "/" + source[2] + "/revoke" + first, "", admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(post(foreign + "/" + source[3] + "/revoke" + first, "", admin).statusCode()).isEqualTo(204);

        long assignPermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='user-role-grant.assign'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), assignPermission);
        assertProblem(post(path + first, request, admin), 403, "ACCESS_DENIED");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), assignPermission);

        jdbc.execute("CREATE TRIGGER hyflo_test_grant_insert_failure BEFORE INSERT ON T_10_01_15 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic grant insert rollback'");
        try {
            assertProblem(post(path + first, request, admin), 503, "IDENTITY_UNAVAILABLE");
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_08=?", Long.class, serviceId)).isZero();
            assertProblem(get(permissions(c.first()), delegate), 403, "ACCESS_DENIED");
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_grant_insert_failure");
        }

        var a = client.sendAsync(postRequest(path + first, request, admin), HttpResponse.BodyHandlers.ofString());
        var b = client.sendAsync(postRequest(path + first, request, admin), HttpResponse.BodyHandlers.ofString());
        var results = List.of(a.get(), b.get());
        assertThat(results.stream().map(HttpResponse::statusCode).toList()).containsExactlyInAnyOrder(201, 409);
        var assigned = results.stream().filter(response -> response.statusCode() == 201).findFirst().orElseThrow();
        long grantId = ((Number) JsonPath.read(assigned.body(), "$.id")).longValue();
        assertThat(((Number) JsonPath.read(assigned.body(), "$.grantedBy")).longValue()).isEqualTo(adminId);
        assertThat(((Number) JsonPath.read(assigned.body(), "$.userId")).longValue()).isEqualTo(serviceId);
        assertThat(((Number) JsonPath.read(assigned.body(), "$.companyId")).longValue()).isEqualTo(c.first());
        assertThat((String) JsonPath.read(assigned.body(), "$.scopeType")).isEqualTo("GLOBAL");
        assertThat((String) JsonPath.read(assigned.body(), "$.roleCode")).isEqualTo("IDENTITY_ADMIN");
        assertThat((String) JsonPath.read(assigned.body(), "$.reason")).isEqualTo("synthetic delegation");
        assertThat(assigned.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_10_01_15 WHERE F_00=?", Timestamp.class, grantId).toInstant()).isEqualTo(end);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_10_01_15 WHERE F_00=?", Timestamp.class, grantId).toInstant())
                .isAfterOrEqualTo(baseTime).isBefore(end);
        assertCodes(get(permissions(c.first()), delegate), c.first(), EXPECTED);
        assertProblem(post(path + "/" + grantId + "/revoke" + first, "", delegate), 403, "ACCESS_DENIED");
        assertProblem(post(root + adminId + "/role-grants/" + bootstrapGrant + "/revoke" + first, "", delegate), 403, "ACCESS_DENIED");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_00=? AND F_03 IS NULL", Long.class, bootstrapGrant)).isEqualTo(1L);

        // The delegated administrator can act only within its own finite same-role interval.
        var child = post("/v1/identity/users" + first,
                "{\"username\":\"delegation-child\",\"email\":\"delegation-child@example.invalid\",\"userTypeCode\":\"HUMAN\",\"reason\":\"synthetic child\"}", admin);
        assertThat(child.statusCode()).isEqualTo(201);
        long childId = ((Number) JsonPath.read(child.body(), "$.id")).longValue();
        String childPath = root + childId + "/role-grants";
        assertProblem(post(childPath + first, grantBody(fixture.adminRole(), end.plusSeconds(1)), delegate), 403, "ACCESS_DENIED");
        var childGrant = post(childPath + first, grantBody(fixture.adminRole(), end.minusSeconds(60)), delegate);
        assertThat(childGrant.statusCode()).isEqualTo(201);
        long childGrantId = ((Number) JsonPath.read(childGrant.body(), "$.id")).longValue();

        // Foreign account grant permits only an explicitly classified catalog role.
        var ordinary = post(foreign + first, grantBody(source[0], end), admin);
        assertThat(ordinary.statusCode()).isEqualTo(201);
        long ordinaryId = ((Number) JsonPath.read(ordinary.body(), "$.id")).longValue();
        assertCodes(get(permissions(c.first()), other), c.first(), List.of("identity-catalog.read", "role.read"));
        assertThat(get("/v1/identity/roles" + first, other).statusCode()).isEqualTo(200);
        assertProblem(get("/v1/identity/permissions" + first, other), 403, "ACCESS_DENIED");
        assertProblem(get("/v1/identity/roles/" + fixture.adminRole() + "/permissions" + first, other),
                403, "ACCESS_DENIED");
        assertCodes(get(permissions(c.second()), other), c.second(), List.of("synthetic.observe", "user.read"));
        var page = get(foreign + first, admin);
        assertThat(page.statusCode()).isEqualTo(200);
        List<Number> ids = JsonPath.read(page.body(), "$[*].id");
        assertThat(ids.stream().map(Number::longValue).toList()).contains(ordinaryId, source[2], source[3]);
        assertThat(page.body()).doesNotContain("username", "email", "password", "owningOrganizationUnitId");
        assertThat(get(foreign + first + "&afterId=" + ordinaryId, admin).body()).isEqualTo("[]");
        assertThat(post(foreign + "/" + ordinaryId + "/revoke" + first, "", admin).statusCode()).isEqualTo(204);
        assertCodes(get(permissions(c.first()), other), c.first(), List.of("identity-catalog.read"));

        String revocation = path + "/" + grantId + "/revoke";
        assertProblem(post(revocation + second, "", admin), 403, "ACCESS_DENIED");
        assertProblem(post(foreign + "/" + grantId + "/revoke" + first, "", admin), 403, "ACCESS_DENIED");
        long revokePermission = jdbc.queryForObject("SELECT F_00 FROM T_10_01_09 WHERE F_01='user-role-grant.revoke'", Long.class);
        jdbc.update("DELETE FROM T_10_01_14 WHERE F_01=? AND F_02=?", fixture.adminRole(), revokePermission);
        assertProblem(post(revocation + first, "", admin), 403, "ACCESS_DENIED");
        jdbc.update("INSERT INTO T_10_01_14(F_01,F_02) VALUES (?,?)", fixture.adminRole(), revokePermission);
        String hash = storedHash(serviceId);
        jdbc.execute("CREATE TRIGGER hyflo_test_grant_revoke_failure BEFORE UPDATE ON T_10_01_15 "
                + "FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'synthetic grant revoke rollback'");
        try {
            assertProblem(post(revocation + first, "", admin), 503, "IDENTITY_UNAVAILABLE");
            assertCodes(get(permissions(c.first()), delegate), c.first(), EXPECTED);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_00=? AND F_03 IS NULL AND F_11 IS NULL",
                    Long.class, grantId)).isEqualTo(1L);
        } finally {
            jdbc.execute("DROP TRIGGER hyflo_test_grant_revoke_failure");
        }
        var revoked = post(revocation + first, "", admin);
        assertThat(revoked.statusCode()).isEqualTo(204);
        assertThat(revoked.body()).isEmpty();
        assertThat(revoked.headers().firstValue("Cache-Control").orElse("")).contains("no-store");
        assertProblem(get(permissions(c.first()), delegate), 403, "ACCESS_DENIED");
        assertThat(get("/v1/me", delegate).statusCode()).isEqualTo(200);
        assertThat(storedHash(serviceId)).isEqualTo(hash);
        assertThat((String) JsonPath.read(get(servicePath + first, admin).body(), "$.userStatusCode")).isEqualTo("ACTIVE");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_00=? AND F_03 IS NOT NULL AND F_11=? AND F_10=? "
                + "AND F_04='synthetic delegation'", Long.class, grantId, adminId, adminId)).isEqualTo(1L);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_15 WHERE F_00=? AND F_03 IS NULL AND F_10=?",
                Long.class, childGrantId, serviceId)).isEqualTo(1L);
        assertProblem(post(revocation + first, "", admin), 409, "ACCOUNT_OPERATION_REJECTED");
        assertThat(get(path + first, admin).body()).contains("revokedAt", "revokedBy");
        assertThat(post(childPath + "/" + childGrantId + "/revoke" + first, "", admin).statusCode()).isEqualTo(204);
        assertThat(get("/api-docs", admin).body()).contains("/v1/identity/users/{userId}/role-grants",
                "/v1/identity/users/{userId}/role-grants/{grantId}/revoke");
        assertThat(get("/v1/identity/roles" + first, admin).statusCode()).isEqualTo(200);
        assertProblem(get("/v1/identity/roles" + first, other), 403, "ACCESS_DENIED");
        assertProblem(post("/v1/identity/roles" + first, "{}", admin), 403, "ACCESS_DENIED");
    }

    private static String grantBody(long roleId, Instant end) {
        return "{\"roleId\":" + roleId + ",\"validTo\":\"" + end + "\",\"reason\":\"synthetic delegation\"}";
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
