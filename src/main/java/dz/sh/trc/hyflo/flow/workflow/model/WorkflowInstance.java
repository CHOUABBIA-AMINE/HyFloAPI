/**
 *
 * 	@Author		: HyFlo v2
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

package dz.sh.trc.hyflo.flow.workflow.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Employee;
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

/**
 * Represents an explicit, strongly-typed workflow instance for the
 * HyFlo governance lifecycle (record → submit → validate → approve / reject).
 *
 * <h3>Binding direction</h3>
 * {@code WorkflowInstance} does NOT hold a foreign key back to the domain entity
 * it governs. Instead, domain entities ({@code FlowReading}, {@code FlowOperation},
 * etc.) hold a nullable FK to their associated {@code WorkflowInstance}.
 * This is the correct JPA ownership pattern: the participant carries the reference;
 * the workflow aggregate remains domain-neutral and reusable.
 *
 * <h3>EAV removal</h3>
 * The previous {@code targetEntity} (String) + {@code targetId} (Long) fields
 * have been removed. They provided no referential integrity, prevented JOIN queries,
 * and made lazy-loading across the workflow boundary impossible.
 *
 * <h3>State tracking</h3>
 * {@code currentState} is now a typed FK to {@link WorkflowState}, replacing
 * the previous raw {@code String} field.
 */
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

    // ------------------------------------------------------------------
    // F_01 — Process type
    // ------------------------------------------------------------------

    @Schema(
            description = "Type of workflow process (e.g., FLOW_READING_VALIDATION, FLOW_OPERATION_VALIDATION)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_01"), nullable = false)
    private WorkflowTargetType targetType;

    // ------------------------------------------------------------------
    // F_02 — Current lifecycle state (typed reference, replaces raw String)
    // ------------------------------------------------------------------

    @Schema(
            description = "Current lifecycle state of this workflow instance (e.g., DRAFT, SUBMITTED, APPROVED)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_02", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_02"), nullable = false)
    private WorkflowState currentState;

    // ------------------------------------------------------------------
    // F_03 — Timeline
    // ------------------------------------------------------------------

    @Schema(
            description = "Timestamp when this workflow instance was opened",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_03", nullable = false)
    private LocalDateTime startedAt;

    @Schema(
            description = "Timestamp when this workflow instance reached a terminal state (null if still active)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_04")
    private LocalDateTime completedAt;

    // ------------------------------------------------------------------
    // F_05, F_06 — Actors
    // ------------------------------------------------------------------

    @Schema(
            description = "Employee who initiated (opened) this workflow instance",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_03"))
    private Employee initiatedBy;

    @Schema(
            description = "Employee who performed the most recent workflow transition",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_04"))
    private Employee lastActor;

    // ------------------------------------------------------------------
    // F_07 — Last actor comment
    // ------------------------------------------------------------------

    @Schema(
            description = "Free-text comment provided by the last actor at the time of their transition (e.g., rejection reason)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_07", length = 2000)
    private String comment;

    // ------------------------------------------------------------------
    // F_08 — Append-only audit trail
    // ------------------------------------------------------------------

    @Schema(
            description = "JSON-serialized append-only audit trail of all workflow transitions. " +
                    "Each entry records: actor, fromState, toState, timestamp, comment.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_08", columnDefinition = "TEXT")
    private String history;
}
