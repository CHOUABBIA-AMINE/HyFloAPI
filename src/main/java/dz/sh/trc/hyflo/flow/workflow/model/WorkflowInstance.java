/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: WorkflowInstance
 * 	@CreatedOn	: 03-25-2026
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
 * Explicit workflow instance representing the lifecycle of a target entity
 * (FlowReading, FlowOperation, etc.). This becomes the source of truth for
 * workflow state, while ValidationStatus acts as a projection.
 */
@Schema(description = "Explicit workflow instance for flow domain entities")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WorkflowInstance")
@Table(name = "T_03_04_01")
public class WorkflowInstance extends GenericModel {

    @Schema(
            description = "Entity type this workflow instance is associated with (e.g., FlowReading, FlowOperation)",
            example = "FlowReading",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_01", length = 100, nullable = false)
    private String targetEntity;

    @Schema(
            description = "Identifier of the target entity instance",
            example = "123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_02", nullable = false)
    private Long targetId;

    @Schema(
            description = "Workflow target type (e.g., FLOW_READING_VALIDATION)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_03", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_03"), nullable = false)
    private WorkflowTargetType targetType;

    @Schema(
            description = "Current state of the workflow (e.g., PENDING, APPROVED, REJECTED)",
            example = "PENDING",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @Column(name = "F_04", length = 50, nullable = false)
    private String currentState;

    @Schema(
            description = "Timestamp when the workflow was started",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_05", nullable = false)
    private LocalDateTime startedAt;

    @Schema(
            description = "Timestamp when the workflow was completed (if applicable)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_06")
    private LocalDateTime completedAt;

    @Schema(
            description = "Employee who initiated this workflow instance",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_01"))
    private Employee initiatedBy;

    @Schema(
            description = "Employee who last acted on this workflow",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_02"))
    private Employee lastActor;

    @Schema(
            description = "JSON-serialized workflow history (transitions, comments, etc.)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_09", columnDefinition = "TEXT")
    private String history;
}
