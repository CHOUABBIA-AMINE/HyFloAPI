package dz.sh.hyflo;

import dz.sh.hyflo.modules.coordination.audit.domain.model.AuditAccessRecord;
import dz.sh.hyflo.modules.coordination.audit.domain.model.AuditEvent;
import dz.sh.hyflo.modules.coordination.audit.domain.model.AuditFieldChange;
import dz.sh.hyflo.modules.coordination.configuration.domain.model.FeatureFlag;
import dz.sh.hyflo.modules.coordination.configuration.domain.model.Parameter;
import dz.sh.hyflo.modules.coordination.configuration.domain.model.ParameterValue;
import dz.sh.hyflo.modules.coordination.integration.domain.model.ExternalSystem;
import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationExchangeMessage;
import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationJobRun;
import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationRejectedRecord;
import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationDeliveryAttempt;
import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationMessage;
import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationRequest;
import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationTemplate;
import dz.sh.hyflo.modules.coordination.notification.domain.model.NotificationType;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowDecisionType;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowDefinition;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowInstance;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowState;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowStateHistory;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowTargetType;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowTask;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowTransition;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.support.Repositories;
import org.springframework.jdbc.core.JdbcTemplate;
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
class CoordinationPersistenceIT {

    private static final Instant START = Instant.parse("2026-11-27T12:00:00Z");
    private static final Instant LATER = START.plusSeconds(60);
    private static final Map<Class<?>, String> TABLES = Map.ofEntries(
        Map.entry(Parameter.class, "T_20_01_01"),
        Map.entry(ParameterValue.class, "T_20_01_02"),
        Map.entry(FeatureFlag.class, "T_20_01_03"),
        Map.entry(AuditEvent.class, "T_20_02_01"),
        Map.entry(AuditFieldChange.class, "T_20_02_02"),
        Map.entry(AuditAccessRecord.class, "T_20_02_03"),
        Map.entry(WorkflowTargetType.class, "T_20_03_01"),
        Map.entry(WorkflowDecisionType.class, "T_20_03_02"),
        Map.entry(WorkflowDefinition.class, "T_20_03_03"),
        Map.entry(WorkflowState.class, "T_20_03_04"),
        Map.entry(WorkflowTransition.class, "T_20_03_05"),
        Map.entry(WorkflowInstance.class, "T_20_03_06"),
        Map.entry(WorkflowTask.class, "T_20_03_07"),
        Map.entry(WorkflowStateHistory.class, "T_20_03_08"),
        Map.entry(NotificationType.class, "T_20_04_01"),
        Map.entry(NotificationTemplate.class, "T_20_04_02"),
        Map.entry(NotificationRequest.class, "T_20_04_03"),
        Map.entry(NotificationMessage.class, "T_20_04_04"),
        Map.entry(NotificationDeliveryAttempt.class, "T_20_04_05"),
        Map.entry(ExternalSystem.class, "T_20_05_01"),
        Map.entry(IntegrationJobRun.class, "T_20_05_02"),
        Map.entry(IntegrationExchangeMessage.class, "T_20_05_03"),
        Map.entry(IntegrationRejectedRecord.class, "T_20_05_04")
    );

    @SuppressWarnings("resource")
    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("hyflo_coordination_test")
            .withUsername("hyflo_test")
            .withPassword("synthetic-db-password");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    PlatformTransactionManager transactions;

    @Test
    void mapsEveryCoordinationTableConstraintAndOwnerRepository() {
        var metamodel = entityManager.getMetamodel().getEntities().stream()
                .<Class<?>>map(entity -> entity.getJavaType())
                .filter(type -> type.getPackageName().startsWith("dz.sh.hyflo.modules.coordination."))
                .toList();
        assertThat(metamodel).containsExactlyInAnyOrderElementsOf(TABLES.keySet());
        var repositories = new Repositories(applicationContext);
        for (var mapping : TABLES.entrySet()) {
            var model = mapping.getKey();
            var table = mapping.getValue();
            assertThat(model.getAnnotation(Table.class).name()).isEqualTo(table);
            var repository = repositories.getRepositoryInformationFor(model).orElseThrow();
            assertThat(repository.getIdType()).isEqualTo(Long.class);
            assertThat(repository.getRepositoryInterface().getName()).isEqualTo(
                    model.getPackageName().replace(".domain.model", ".infrastructure.persistence.repository")
                            + "." + model.getSimpleName() + "Repository");
            assertThat(repositories.getRepositoryFor(model)).isPresent();
            assertPhysicalMapping(model, table);
        }
    }

    private void assertPhysicalMapping(Class<?> model, String table) {
        var columns = new ArrayList<String>();
        var foreignKeys = new ArrayList<String>();
        for (var field : model.getDeclaredFields()) {
            var column = field.getAnnotation(Column.class);
            if (column != null) {
                columns.add(column.name());
            }
            var join = field.getAnnotation(JoinColumn.class);
            if (join != null) {
                columns.add(join.name());
                foreignKeys.add(join.foreignKey().name() + ":" + join.name() + ":"
                        + TABLES.get(field.getType()) + ":F_00:" + (join.nullable() ? "YES" : "NO"));
            }
        }
        assertThat(jdbc.queryForList("""
                SELECT COLUMN_NAME FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?
                """, String.class, table)).containsExactlyInAnyOrderElementsOf(columns);
        assertThat(jdbc.queryForList("""
                SELECT CONCAT(K.CONSTRAINT_NAME, ':', K.COLUMN_NAME, ':', K.REFERENCED_TABLE_NAME,
                    ':', K.REFERENCED_COLUMN_NAME, ':', C.IS_NULLABLE)
                FROM information_schema.KEY_COLUMN_USAGE K
                JOIN information_schema.COLUMNS C ON C.TABLE_SCHEMA = K.TABLE_SCHEMA
                    AND BINARY C.TABLE_NAME = BINARY K.TABLE_NAME AND C.COLUMN_NAME = K.COLUMN_NAME
                WHERE K.TABLE_SCHEMA = DATABASE() AND BINARY K.TABLE_NAME = BINARY ?
                    AND K.REFERENCED_TABLE_NAME IS NOT NULL
                """, String.class, table)).containsExactlyInAnyOrderElementsOf(foreignKeys);
        var uniqueKeys = Arrays.stream(model.getAnnotation(Table.class).uniqueConstraints())
                .map(key -> key.name() + ":" + String.join(",", key.columnNames())).toList();
        assertThat(jdbc.queryForList("""
                SELECT CONCAT(INDEX_NAME, ':', GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX))
                FROM information_schema.STATISTICS
                WHERE TABLE_SCHEMA = DATABASE() AND BINARY TABLE_NAME = BINARY ?
                    AND NON_UNIQUE = 0 AND INDEX_NAME <> 'PRIMARY'
                GROUP BY INDEX_NAME
                """, String.class, table)).containsExactlyInAnyOrderElementsOf(uniqueKeys);
    }

    @Test
    @Transactional
    void roundTripsSyntheticRecordsAcrossAllFiveOwners() {
        var parameter = new Parameter("MAX_ATTEMPTS", "integration", "INTEGER", null, true);
        var value = new ParameterValue(1, "5", START, null, "Initial value", 12L, parameter);
        entityManager.persist(parameter);
        entityManager.persist(value);

        var event = new AuditEvent("workflow", "DECIDE", "Synthetic actor", "topology", "PIPELINE",
                24L, "SUCCESS", START, "synthetic-audit", 13L);
        var change = new AuditFieldChange("status", "REVIEW", "APPROVED", false, event);
        entityManager.persist(event);
        entityManager.persist(change);

        var target = new WorkflowTargetType("PIPELINE", "topology", null, null, "Pipeline", true);
        var decision = new WorkflowDecisionType("APPROVE", null, null, "Approve", true);
        entityManager.persist(target);
        entityManager.persist(decision);
        var definition = new WorkflowDefinition("PIPELINE_REVIEW", 1, START, null, target);
        entityManager.persist(definition);
        entityManager.persist(new WorkflowDefinition("PIPELINE_REVIEW", 2, LATER, null, target));
        var review = new WorkflowState("REVIEW", null, null, "Review", true, false, definition);
        var approved = new WorkflowState("APPROVED", null, null, "Approved", false, true, definition);
        entityManager.persist(review);
        entityManager.persist(approved);
        var transition = new WorkflowTransition("APPROVE", true, definition, decision, review, approved);
        entityManager.persist(transition);
        var instance = new WorkflowInstance("revision-3", START, null, 24L, 12L, definition, review);
        entityManager.persist(instance);
        var task = new WorkflowTask(LATER, null, 12L, null, instance, review);
        entityManager.persist(task);
        var history = new WorkflowStateHistory(1, "Synthetic approver", "Design checked", LATER,
                "synthetic-decision", 13L, instance, transition, task);
        entityManager.persist(history);

        var type = new NotificationType("APPROVAL", null, null, "Approval", 1, true);
        var template = new NotificationTemplate("REVIEW", 1, "en", "Review", "Review completed");
        entityManager.persist(type);
        entityManager.persist(template);
        var request = new NotificationRequest("workflow", "event-1", "pipeline-24", START, type);
        entityManager.persist(request);
        var message = new NotificationMessage("IN_APP", "Review", "Review completed", LATER, 12L, request, template);
        entityManager.persist(message);
        var attempt = new NotificationDeliveryAttempt(1, START, "DELIVERED", null, message);
        entityManager.persist(attempt);

        var system = new ExternalSystem("SYNTHETIC", null, null, "Synthetic system", true, 5L);
        entityManager.persist(system);
        var run = new IntegrationJobRun("IMPORT", "INBOUND", START, null, null, 12L, system);
        entityManager.persist(run);
        var exchange = new IntegrationExchangeMessage("INBOUND", "message-1", "object-1", "a".repeat(64),
                START, "synthetic-exchange", system, run);
        entityManager.persist(exchange);
        var rejection = new IntegrationRejectedRecord("row-1", "topology", "VALIDATION", "INVALID_DATA", LATER, null, exchange);
        entityManager.persist(rejection);

        entityManager.flush();
        entityManager.clear();
        assertThat(entityManager.find(ParameterValue.class, value.getId()).getParameter().getValueType()).isEqualTo("INTEGER");
        assertThat(entityManager.find(AuditFieldChange.class, change.getId()).getAuditEvent().getActorId()).isEqualTo(13L);
        var saved = entityManager.find(WorkflowStateHistory.class, history.getId());
        assertThat(saved.getWorkflowInstance().getTargetRevisionReference()).isEqualTo("revision-3");
        assertThat(saved.getWorkflowInstance().getWorkflowDefinition().getRevisionNumber()).isEqualTo(1);
        assertThat(saved.getWorkflowTransition().getId()).isEqualTo(transition.getId());
        assertThat(saved.getWorkflowTask().getId()).isEqualTo(task.getId());
        assertThat(saved.getDecidedByActorId()).isEqualTo(13L);
        var delivered = entityManager.find(NotificationDeliveryAttempt.class, attempt.getId());
        assertThat(delivered.getAttemptedAt()).isEqualTo(START);
        assertThat(delivered.getNotificationMessage().getReadAt()).isEqualTo(LATER);
        assertThat(delivered.getNotificationMessage().getNotificationTemplate().getRevisionNumber()).isEqualTo(1);
        var rejected = entityManager.find(IntegrationRejectedRecord.class, rejection.getId());
        assertThat(rejected.getIntegrationExchangeMessage().getIntegrationJobRun().getExternalSystem().getCode())
                .isEqualTo("SYNTHETIC");
    }

    @Test
    void rejectsDuplicateParametersAndDanglingSameOwnerReferences() {
        var transaction = new TransactionTemplate(transactions);
        assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
            String insert = "INSERT INTO T_20_01_01 (F_01, F_02, F_03, F_05) VALUES (?, ?, ?, TRUE)";
            jdbc.update(insert, "SYNTHETIC_DUPLICATE", "integration", "INTEGER");
            jdbc.update(insert, "SYNTHETIC_DUPLICATE", "integration", "INTEGER");
        })).isInstanceOf(DataIntegrityViolationException.class)
                .rootCause().hasMessageContaining("T_20_01_01_UK_01");
        assertThatThrownBy(() -> transaction.executeWithoutResult(status -> jdbc.update("""
                INSERT INTO T_20_02_02 (F_01, F_04, F_05) VALUES ('status', TRUE, -1)
                """))).isInstanceOf(DataIntegrityViolationException.class)
                .rootCause().hasMessageContaining("T_20_02_02_FK_01");
    }
}
