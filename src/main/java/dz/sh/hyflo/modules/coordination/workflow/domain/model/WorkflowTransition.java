/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowTransition
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Class
 * @Layer       : Model
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.domain.model
 *
 * @Description : Represents workflow transition data in Coordination.
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
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_20_03_05", uniqueConstraints = {
        @UniqueConstraint(name = "T_20_03_05_UK_01", columnNames = {"F_03", "F_01"}),
        @UniqueConstraint(name = "T_20_03_05_UK_02", columnNames = {"F_05", "F_04"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkflowTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_00")
    private Long id;

    @Size(max = 64)
    @NotBlank
    @Column(name = "F_01", nullable = false, updatable = false, length = 64)
    private String code;

    @Column(name = "F_02", nullable = false, updatable = false)
    private boolean reasonRequired;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_05_FK_01"))
    private WorkflowDefinition workflowDefinition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_05_FK_02"))
    private WorkflowDecisionType workflowDecisionType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_05_FK_03"))
    private WorkflowState fromWorkflowState;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "T_20_03_05_FK_04"))
    private WorkflowState toWorkflowState;

    public WorkflowTransition(String code, boolean reasonRequired, WorkflowDefinition workflowDefinition,
            WorkflowDecisionType workflowDecisionType, WorkflowState fromWorkflowState,
            WorkflowState toWorkflowState) {
        if (code == null || !code.matches("[A-Za-z][A-Za-z0-9_.-]{0,63}")) {
            throw new IllegalArgumentException("Invalid code");
        }
        Objects.requireNonNull(workflowDefinition, "workflowDefinition is required");
        Objects.requireNonNull(workflowDecisionType, "workflowDecisionType is required");
        Objects.requireNonNull(fromWorkflowState, "fromWorkflowState is required");
        Objects.requireNonNull(toWorkflowState, "toWorkflowState is required");
        if (!workflowDefinition.matches(fromWorkflowState.getWorkflowDefinition())
                || !workflowDefinition.matches(toWorkflowState.getWorkflowDefinition())) {
            throw new IllegalArgumentException("Transition states must belong to the same definition revision");
        }
        this.code = code;
        this.reasonRequired = reasonRequired;
        this.workflowDefinition = workflowDefinition;
        this.workflowDecisionType = workflowDecisionType;
        this.fromWorkflowState = fromWorkflowState;
        this.toWorkflowState = toWorkflowState;
    }
}
