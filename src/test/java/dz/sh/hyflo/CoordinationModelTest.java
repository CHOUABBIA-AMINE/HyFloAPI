package dz.sh.hyflo;

import dz.sh.hyflo.modules.coordination.audit.domain.model.AuditEvent;
import dz.sh.hyflo.modules.coordination.audit.domain.model.AuditFieldChange;
import dz.sh.hyflo.modules.coordination.configuration.domain.model.Parameter;
import dz.sh.hyflo.modules.coordination.configuration.domain.model.ParameterValue;
import dz.sh.hyflo.modules.coordination.integration.domain.model.ExternalSystem;
import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationExchangeMessage;
import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationJobRun;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowDecisionType;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowDefinition;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowInstance;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowState;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowStateHistory;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowTargetType;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowTask;
import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowTransition;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CoordinationModelTest {

    private static final Instant START = Instant.parse("2026-11-27T12:00:00Z");
    private static final Instant LATER = START.plusSeconds(60);

    @Test
    void validatesParameterTypeAndEffectivePeriod() {
        var parameter = new Parameter("MAX_ATTEMPTS", "integration", "INTEGER", null, true);
        assertThat(new ParameterValue(1, "5", START, LATER, "Initial value", 12L, parameter).getValue())
                .isEqualTo("5");
        var open = new ParameterValue(2, "6", START, null, "Second value", 12L, parameter);
        assertThatIllegalArgumentException().isThrownBy(() -> open.closeEffectivePeriod(START));
        open.closeEffectivePeriod(LATER);
        assertThat(open.getEffectiveTo()).isEqualTo(LATER);
        assertThatIllegalArgumentException().isThrownBy(() -> open.closeEffectivePeriod(LATER.plusSeconds(1)));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ParameterValue(1, "1.5", START, LATER, "Initial value", 12L, parameter));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ParameterValue(1, "5", START, START, "Initial value", 12L, parameter));
    }

    @Test
    void rejectsRawValuesInMaskedAuditEvidence() {
        var event = new AuditEvent("workflow", "DECIDE", "Synthetic actor", "topology",
                "PIPELINE", 24L, "SUCCESS", START, "synthetic-audit", 12L);
        assertThat(new AuditFieldChange("status", null, null, true, event).isMasked()).isTrue();
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new AuditFieldChange("status", "raw value", null, true, event));
    }

    @Test
    void keepsWorkflowDecisionsBoundToTheirDefinitionAndTask() {
        var target = new WorkflowTargetType("PIPELINE", "topology", null, null, "Pipeline", true);
        var decision = new WorkflowDecisionType("APPROVE", null, null, "Approve", true);
        var revision = new WorkflowDefinition("PIPELINE_REVIEW", 1, START, null, target);
        var nextRevision = new WorkflowDefinition("PIPELINE_REVIEW", 2, START, null, target);
        var review = new WorkflowState("REVIEW", null, null, "Review", true, false, revision);
        var approved = new WorkflowState("APPROVED", null, null, "Approved", false, true, revision);
        var foreign = new WorkflowState("APPROVED", null, null, "Approved", false, true, nextRevision);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WorkflowTransition("APPROVE", true, revision, decision, review, foreign));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WorkflowInstance("revision-3", START, null, 24L, 12L, revision, foreign));

        var transition = new WorkflowTransition("APPROVE", true, revision, decision, review, approved);
        var instance = new WorkflowInstance("revision-3", START, null, 24L, 12L, revision, review);
        var otherInstance = new WorkflowInstance("revision-4", START, null, 24L, 12L, revision, review);
        var task = new WorkflowTask(LATER, null, 12L, null, instance, review);
        var wrongTask = new WorkflowTask(LATER, null, 12L, null, otherInstance, review);
        var wrongState = new WorkflowTask(LATER, null, 12L, null, instance, approved);
        var decisionRecord = new WorkflowStateHistory(1, "Synthetic approver", "Design checked", LATER,
                "synthetic-decision", 13L, instance, transition, task);

        assertThat(decisionRecord.getDecidedByActorId()).isEqualTo(13L);
        assertThat(task.getAssignedActorId()).isEqualTo(12L);
        assertThatIllegalArgumentException().isThrownBy(() -> new WorkflowTask(LATER, null, null, null, instance, review));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WorkflowStateHistory(1, "Synthetic approver", null, LATER,
                        "synthetic-decision", 13L, instance, transition, task));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new WorkflowStateHistory(1, "Synthetic approver", "Design checked", LATER,
                        "synthetic-decision", 0L, instance, transition, task));
        for (var invalidTask : new WorkflowTask[] {wrongTask, wrongState}) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> new WorkflowStateHistory(1, "Synthetic approver", "Design checked", LATER,
                            "synthetic-decision", 13L, instance, transition, invalidTask));
        }
    }

    @Test
    void checksIntegrationSystemDirectionAndOpaquePayloadReference() {
        var system = new ExternalSystem("SYNTHETIC", null, null, "Synthetic system", true, 5L);
        var otherSystem = new ExternalSystem("OTHER", null, null, "Other system", true, 5L);
        var run = new IntegrationJobRun("IMPORT", "INBOUND", START, null, null, 12L, system);
        assertThat(new IntegrationExchangeMessage("INBOUND", "message-1", "object-1", "a".repeat(64),
                LATER, "synthetic-exchange", system, run).getIntegrationJobRun()).isSameAs(run);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new IntegrationExchangeMessage("INBOUND", "message-1", null, null,
                        LATER, "synthetic-exchange", otherSystem, run));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new IntegrationExchangeMessage("OUTBOUND", "message-1", null, null,
                        LATER, "synthetic-exchange", system, run));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new IntegrationExchangeMessage("INBOUND", "message-1", "{inline-payload}", null,
                        LATER, "synthetic-exchange", system, run));
    }
}
