/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowInstance
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow instance data in Coordination.
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_06")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 255)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 255)
    private String targetRevisionReference;

    @Column(name = "F_02", nullable = false, updatable = false)
    private Instant startedAt;

    @Column(name = "F_03", nullable = true, updatable = true)
    private Instant completedAt;

    @Positive
    @Column(name = "F_04", nullable = false, updatable = false)
    private long targetId;

    @Positive
    @Column(name = "F_05", nullable = false, updatable = false)
    private long initiatedByActorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_06_FK_01"))
    private WorkflowDefinition workflowDefinition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", nullable = false, updatable = true, foreignKey = @ForeignKey(name = "T_20_03_06_FK_02"))
    private WorkflowState workflowState;

    @Version
    @Column(name = "A_01", nullable = false)
    private Long version;

    public WorkflowInstance(String targetRevisionReference, Instant startedAt, Instant completedAt, long targetId,
            long initiatedByActorId, WorkflowDefinition workflowDefinition,
            WorkflowState workflowState) {
        if (targetRevisionReference == null || targetRevisionReference.isBlank() || targetRevisionReference.length() > 255) {
            throw new IllegalArgumentException("Invalid targetRevisionReference");
        }
        Objects.requireNonNull(startedAt, "startedAt is required");
        if (targetId <= 0) {
            throw new IllegalArgumentException("Invalid targetId");
        }
        if (initiatedByActorId <= 0) {
            throw new IllegalArgumentException("Invalid initiatedByActorId");
        }
        Objects.requireNonNull(workflowDefinition, "workflowDefinition is required");
        Objects.requireNonNull(workflowState, "workflowState is required");
        if (!workflowDefinition.matches(workflowState.getWorkflowDefinition())) {
            throw new IllegalArgumentException("Initial instance state must belong to its definition revision");
        }
        if (workflowDefinition.getPublishedAt() == null || startedAt.isBefore(workflowDefinition.getPublishedAt())
                || (workflowDefinition.getRetiredAt() != null && !startedAt.isBefore(workflowDefinition.getRetiredAt()))
                || !workflowDefinition.getWorkflowTargetType().isActive()) {
            throw new IllegalArgumentException("Instance requires a published, eligible definition revision");
        }
        if (completedAt != null && completedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Completion cannot precede the instance start");
        }
        this.targetRevisionReference = targetRevisionReference;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.targetId = targetId;
        this.initiatedByActorId = initiatedByActorId;
        this.workflowDefinition = workflowDefinition;
        this.workflowState = workflowState;
    }

    boolean matches(WorkflowInstance other) {
        return this == other || (other != null && getId() != null && getId().equals(other.getId()));
    }
}
