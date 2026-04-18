/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: WorkflowInstance
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.core.flow.workflow.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Explicit workflow instance representing the governance lifecycle of a domain entity")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WorkflowInstance")
@Table(name = "T_03_04_01")
public class WorkflowInstance extends GenericModel {

    // F_01 — Process type
    @Schema(description = "Type of workflow process", requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_01"), nullable = false)
    private WorkflowTargetType targetType;

    // F_02 — Current lifecycle state
    @Schema(description = "Current lifecycle state", requiredMode = Schema.RequiredMode.REQUIRED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_02", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_02"), nullable = false)
    private WorkflowState currentState;

    // F_03 — Started at
    @Schema(description = "Timestamp when this workflow instance was opened",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "F_03", nullable = false)
    private LocalDateTime startedAt;

    // F_04 — Completed at
    @Schema(description = "Timestamp when this workflow instance reached a terminal state")
    @Column(name = "F_04")
    private LocalDateTime completedAt;

    // F_05 — Initiated by
    @Schema(description = "Employee who initiated this workflow instance")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_03"))
    private Employee initiatedBy;

    // F_06 — Last actor
    @Schema(description = "Employee who performed the most recent workflow transition")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_04"))
    private Employee lastActor;

    // F_07 — Last actor comment
    @Schema(description = "Free-text comment by the last actor")
    @Column(name = "F_07", length = 2000)
    private String comment;

    // F_08 — Append-only audit trail
    @Schema(description = "JSON-serialized append-only audit trail of all workflow transitions")
    @Column(name = "F_08", columnDefinition = "TEXT")
    private String history;

    // F_09 — Last updated timestamp
    @Schema(
            description = "Timestamp of the most recent state transition on this workflow instance.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_09")
    private LocalDateTime updatedAt;
}
