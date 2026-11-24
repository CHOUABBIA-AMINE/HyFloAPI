package dz.sh.hyflo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import dz.sh.hyflo.modules.foundation.identity.domain.model.IdentityProvider;
import dz.sh.hyflo.modules.foundation.identity.domain.model.LocalCredential;
import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import dz.sh.hyflo.modules.foundation.identity.domain.model.UserStatus;
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
import org.springframework.transaction.annotation.Transactional;
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
        assertThat(account.getStatus().getCode()).isEqualTo(UserStatus.PENDING_CODE);
        account.activate(credential, active);
        account.suspend(suspended);
        account.activate(credential, active);

        entityManager.flush();
        entityManager.clear();

        var savedAccount = entityManager.find(User.class, account.getId());
        var savedCredential = entityManager.find(LocalCredential.class, credential.getId());
        assertThat(savedAccount.getUsername()).isEqualTo("synthetic.user");
        assertThat(savedAccount.getStatus().getCode()).isEqualTo(UserStatus.ACTIVE_CODE);
        assertThat(savedAccount.getProvider().getCode()).isEqualTo(IdentityProvider.LOCAL_CODE);
        assertThat(savedCredential.getUser().getId()).isEqualTo(savedAccount.getId());
        assertThat(savedCredential.getPasswordHash()).isEqualTo(hash);
        assertThat(jdbc.queryForObject("SELECT F_01 FROM T_00_02_02 WHERE F_00 = ?", String.class,
                savedAccount.getId())).isEqualTo("synthetic.user");
        assertThat(jdbc.queryForObject("SELECT F_02 FROM T_00_02_09 WHERE F_00 = ?", String.class,
                savedCredential.getId())).isEqualTo(hash);

        assertIdentityColumns("T_00_02_02", "F_00", "A_01", "F_01", "F_02", "F_08", "F_09", "F_10");
        assertIdentityColumns("T_00_02_08", "F_00", "F_01", "F_02", "F_03");
        assertIdentityColumns("T_00_02_09", "F_00", "F_01", "F_02", "F_03");
        assertIdentityColumns("T_00_02_10", "F_00", "F_01", "F_02");
        assertIdentityConstraints("T_00_02_02", "T_00_02_02_UK_01", "T_00_02_02_UK_02",
                "T_00_02_02_FK_02", "T_00_02_02_FK_03");
        assertIdentityConstraints("T_00_02_08", "T_00_02_08_UK_01");
        assertIdentityConstraints("T_00_02_09", "T_00_02_09_UK_01", "T_00_02_09_FK_01");
        assertIdentityConstraints("T_00_02_10", "T_00_02_10_UK_01");

        savedAccount.disable(entityManager.find(UserStatus.class, disabled.getId()));
        assertThatThrownBy(() -> savedAccount.activate(savedCredential, entityManager.find(UserStatus.class, active.getId())))
                .isInstanceOf(IllegalStateException.class);
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
