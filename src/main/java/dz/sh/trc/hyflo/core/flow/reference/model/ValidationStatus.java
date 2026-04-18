/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ValidationStatus
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.core.flow.reference.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Validation status for flow data approval workflow.
 * Tracks the approval state of flow readings, operations, and forecasts.
 */
@Schema(description = "Validation status for flow data approval and quality control workflow")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ValidationStatus")
@Table(name = "T_03_02_06", uniqueConstraints = {
        @UniqueConstraint(name = "T_03_02_06_UK_01", columnNames = {"F_01"}),
        @UniqueConstraint(name = "T_03_02_06_UK_02", columnNames = {"F_04"})
})
public class ValidationStatus extends GenericModel {

    @Schema(
            description = "Unique code identifying the validation status",
            example = "PENDING",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(
            description = "Validation status designation in Arabic",
            example = "قيد الانتظار",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(
            description = "Validation status designation in English",
            example = "Pending Validation",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(
            description = "Validation status designation in French (required)",
            example = "En Attente de Validation",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(
            description = "Detailed description in Arabic",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_05", length = 255)
    private String descriptionAr;

    @Schema(
            description = "Detailed description in English",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_06", length = 255)
    private String descriptionEn;

    @Schema(
            description = "Detailed description in French",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 255
    )
    @Column(name = "F_07", length = 255)
    private String descriptionFr;
}
