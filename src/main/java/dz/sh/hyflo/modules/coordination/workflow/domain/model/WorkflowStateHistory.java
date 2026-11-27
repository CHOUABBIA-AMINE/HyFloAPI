/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowStateHistory
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow state history data in Coordination.
 */

package dz.sh.hyflo.modules.coordination.workflow.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_08", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_03_08_UK_01", columnNames = {"F_07", "F_01"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowStateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Positive
    @Column(name = "F_01", nullable = false, updatable = false)
    private int sequence;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_02", nullable = false, updatable = false, length = 255)
    private String actorNameSnapshot;

    @Size(max = 255)
    @Column(name = "F_03", nullable = true, updatable = false, length = 255)
    private String reason;

    @Column(name = "F_04", nullable = false, updatable = false)
    private Instant decidedAt;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_05", nullable = false, updatable = false, length = 255)
    private String correlationId;

    @Positive
    @Column(name = "F_06", nullable = false, updatable = false)
    private long decidedByActorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_08_FK_01"))
    private WorkflowInstance workflowInstance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_08_FK_02"))
    private WorkflowTransition workflowTransition;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", nullable = true, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_08_FK_03"))
    private WorkflowTask workflowTask;

    public WorkflowStateHistory(int sequence, String actorNameSnapshot, String reason, Instant decidedAt,
            String correlationId, long decidedByActorId, WorkflowInstance workflowInstance,
            WorkflowTransition workflowTransition, WorkflowTask workflowTask) {
        if (sequence <= 0) {
            throw new IllegalArgumentException("Invalid sequence");
        }
        if (actorNameSnapshot == null || actorNameSnapshot.isBlank() || actorNameSnapshot.length() > 255) {
            throw new IllegalArgumentException("Invalid actorNameSnapshot");
        }
        if (reason != null && (reason.length() > 255)) {
            throw new IllegalArgumentException("Invalid reason");
        }
        Objects.requireNonNull(decidedAt, "decidedAt is required");
        if (correlationId == null || correlationId.isBlank() || correlationId.length() > 255) {
            throw new IllegalArgumentException("Invalid correlationId");
        }
        if (decidedByActorId <= 0) {
            throw new IllegalArgumentException("Invalid decidedByActorId");
        }
        Objects.requireNonNull(workflowInstance, "workflowInstance is required");
        Objects.requireNonNull(workflowTransition, "workflowTransition is required");
        if (!workflowInstance.getWorkflowDefinition().matches(workflowTransition.getWorkflowDefinition())) {
            throw new IllegalArgumentException("Transition must belong to the instance definition revision");
        }
        if (decidedAt.isBefore(workflowInstance.getStartedAt())
                || (workflowInstance.getCompletedAt() != null && decidedAt.isAfter(workflowInstance.getCompletedAt()))) {
            throw new IllegalArgumentException("Decision time must be within the instance lifetime");
        }
        if (workflowTransition.isReasonRequired() && (reason == null || reason.isBlank())) {
            throw new IllegalArgumentException("A reason is required for this transition");
        }
        if (workflowTask != null && (!workflowInstance.matches(workflowTask.getWorkflowInstance())
                || !workflowTransition.getFromWorkflowState().matches(workflowTask.getWorkflowState()))) {
            throw new IllegalArgumentException("Task must belong to the instance and transition source state");
        }
        this.sequence = sequence;
        this.actorNameSnapshot = actorNameSnapshot;
        this.reason = reason;
        this.decidedAt = decidedAt;
        this.correlationId = correlationId;
        this.decidedByActorId = decidedByActorId;
        this.workflowInstance = workflowInstance;
        this.workflowTransition = workflowTransition;
        this.workflowTask = workflowTask;
    }
}
