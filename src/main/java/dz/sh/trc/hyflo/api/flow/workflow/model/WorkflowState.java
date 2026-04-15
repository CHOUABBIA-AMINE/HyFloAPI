/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: WorkflowState
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.api.flow.workflow.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference entity representing a valid state in the workflow lifecycle.
 * <p>
 * Typical codes: DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, CANCELLED.
 * <p>
 * This entity replaces the raw {@code String currentState} field that was
 * previously embedded in {@link WorkflowInstance}, giving full referential
 * integrity, traceability and multilingual display support to workflow states.
 */
@Schema(description = "Reference entity for a workflow lifecycle state (e.g., DRAFT, APPROVED, REJECTED)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WorkflowState")
@Table(name = "T_03_04_03", uniqueConstraints = {
        @UniqueConstraint(name = "T_03_04_03_UK_01", columnNames = {"F_01"}),
        @UniqueConstraint(name = "T_03_04_03_UK_02", columnNames = {"F_04"})
})
public class WorkflowState extends GenericModel {

    @Schema(
            description = "Unique code identifying the workflow state",
            example = "APPROVED",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @NotBlank(message = "Workflow state code is mandatory")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(
            description = "Workflow state designation in Arabic",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(
            description = "Workflow state designation in English",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(
            description = "Workflow state designation in French (required)",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "French designation is mandatory")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(
            description = "Whether this is a terminal state (no further transitions possible)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_05", nullable = false)
    private Boolean terminal = Boolean.FALSE;

    @Schema(
            description = "Whether this workflow state is currently active in the system",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_06", nullable = false)
    private Boolean active = Boolean.TRUE;
}
