/**
 * 
* 	@Author		: MEDJERAB Abir Model
 *
 * 	@Name		: WorkflowTargetType
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.core.flow.workflow.model;

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
 * Reference entity defining the type of workflow target
 * (e.g., FLOW_READING_VALIDATION, FLOW_OPERATION_VALIDATION).
 */
@Schema(description = "Reference entity defining the type of workflow target")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WorkflowTargetType")
@Table(name = "T_03_05_02", uniqueConstraints = {
        @UniqueConstraint(name = "T_03_05_02_UK_01", columnNames = {"F_01"}),
        @UniqueConstraint(name = "T_03_05_02_UK_02", columnNames = {"F_04"})
})
public class WorkflowTargetType extends GenericModel {

    @Schema(
            description = "Unique code identifying the workflow target type",
            example = "FLOW_READING_VALIDATION",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "Workflow target type code is mandatory")
    @Size(min = 2, max = 100, message = "Code must be between 2 and 100 characters")
    @Column(name = "F_01", length = 100, nullable = false)
    private String code;

    @Schema(
            description = "Workflow target type designation in Arabic",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(
            description = "Workflow target type designation in English",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(
            description = "Workflow target type designation in French (required)",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @NotBlank(message = "French designation is mandatory")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(
            description = "Indicates whether this workflow target type is active",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_05", nullable = false)
    private Boolean active = Boolean.TRUE;
}
