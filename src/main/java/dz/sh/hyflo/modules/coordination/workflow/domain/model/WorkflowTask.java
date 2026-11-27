/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowTask
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow task data in Coordination.
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
import jakarta.persistence.Version;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_07")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Column(name = "F_01", nullable = true, updatable = false)
    private Instant dueAt;

    @Column(name = "F_02", nullable = true, updatable = true)
    private Instant completedAt;

    @Positive
    @Column(name = "F_03", nullable = true, updatable = true)
    private Long assignedActorId;

    @Positive
    @Column(name = "F_04", nullable = true, updatable = true)
    private Long assignedRoleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_07_FK_01"))
    private WorkflowInstance workflowInstance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_07_FK_02"))
    private WorkflowState workflowState;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public WorkflowTask(Instant dueAt, Instant completedAt, Long assignedActorId, Long assignedRoleId,
            WorkflowInstance workflowInstance, WorkflowState workflowState) {
        if (assignedActorId != null && assignedActorId <= 0) {
            throw new IllegalArgumentException("Invalid assignedActorId");
        }
        if (assignedRoleId != null && assignedRoleId <= 0) {
            throw new IllegalArgumentException("Invalid assignedRoleId");
        }
        Objects.requireNonNull(workflowInstance, "workflowInstance is required");
        Objects.requireNonNull(workflowState, "workflowState is required");
        if (assignedActorId == null && assignedRoleId == null) {
            throw new IllegalArgumentException("A task requires an actor or role assignment");
        }
        if (!workflowInstance.getWorkflowDefinition().matches(workflowState.getWorkflowDefinition())) {
            throw new IllegalArgumentException("Task state must belong to the instance definition revision");
        }
        if ((dueAt != null && dueAt.isBefore(workflowInstance.getStartedAt()))
                || (completedAt != null && completedAt.isBefore(workflowInstance.getStartedAt()))) {
            throw new IllegalArgumentException("Task dates cannot precede the instance start");
        }
        this.dueAt = dueAt;
        this.completedAt = completedAt;
        this.assignedActorId = assignedActorId;
        this.assignedRoleId = assignedRoleId;
        this.workflowInstance = workflowInstance;
        this.workflowState = workflowState;
    }
}
