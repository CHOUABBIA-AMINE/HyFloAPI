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
    private static final List<String> EXPECTED = List.of("credential.initialize", "credential.reset",
            "identity-catalog.read", "login-session.revoke", "permission.read", "role.read",
            "user-role-grant.assign", "user-role-grant.read", "user-role-grant.revoke", "user.activate",
            "user.create", "user.disable", "user.reactivate", "user.read", "user.suspend");

    @SuppressWarnings("resource")
    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("hyflo_authorization_test")
            .withUsername("hyflo_test").withPassword("synthetic-db-password");

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
        long otherId = createAccount();
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
        jdbc.update("UPDATE T_10_01_08 SET F_06=(SELECT F_00 FROM T_10_01_02 WHERE F_01='SUSPENDED') WHERE F_00=?", otherId);
        assertProblem(get(permissions(companies.second()), other), 401, "AUTHENTICATION_REQUIRED");
        jdbc.update("UPDATE T_10_01_08 SET F_06=(SELECT F_00 FROM T_10_01_02 WHERE F_01='ACTIVE') WHERE F_00=?", otherId);
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
        jdbc.update("UPDATE T_10_01_08 SET F_06=(SELECT F_00 FROM T_10_01_02 WHERE F_01='DISABLED') WHERE F_00=?", otherId);
        assertProblem(get(permissions(companies.second()), other), 401, "AUTHENTICATION_REQUIRED");
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

    private long createAccount() {
        return new TransactionTemplate(transactions).execute(tx -> {
            var provider = entityManager.createQuery("select p from IdentityProvider p where p.code='LOCAL'", IdentityProvider.class)
                    .getSingleResult();
            var type = entityManager.createQuery("select t from UserType t where t.code='HUMAN'", UserType.class)
                    .getSingleResult();
            var pending = status("PENDING");
            var user = new User(provider, "authz-user", "authz-user@example.invalid", null, pending, type);
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
        var response = client.send(HttpRequest.newBuilder(uri("/v1/auth/login")).timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json").header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"" + username + "\",\"password\":\"" + PASSWORD + "\"}"))
                .build(), HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        return JsonPath.read(response.body(), "$.accessToken");
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
