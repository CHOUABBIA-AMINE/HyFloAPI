/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PlanStatus
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Model
 *  @Package    : Flow / Common
 *
 *  @Description: Reference entity for FlowPlan lifecycle status.
 *                Valid codes: DRAFT, APPROVED, SUPERSEDED.
 *                Follows ValidationStatus structure (T_03_02_06) exactly.
 *
 **/

package dz.sh.trc.hyflo.api.flow.common.model;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference entity for FlowPlan governance status.
 * Codes: DRAFT, APPROVED, SUPERSEDED.
 * Owned by flow.common because it is a shared reference type.
 */
@Schema(description = "Reference status for flow plan lifecycle governance")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PlanStatus")
@Table(
        name = "T_03_02_10",
        uniqueConstraints = {
                @UniqueConstraint(name = "T_03_02_10_UK_01", columnNames = {"F_01"}),
                @UniqueConstraint(name = "T_03_02_10_UK_02", columnNames = {"F_04"})
        }
)
public class PlanStatus extends GenericModel {

    @Schema(description = "Unique status code", example = "DRAFT",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(description = "Designation in Arabic", example = "مسودة", maxLength = 100)
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(description = "Designation in English", example = "Draft", maxLength = 100)
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(description = "Designation in French (required)", example = "Brouillon",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(description = "Description in Arabic", maxLength = 255)
    @Column(name = "F_05", length = 255)
    private String descriptionAr;

    @Schema(description = "Description in English", maxLength = 255)
    @Column(name = "F_06", length = 255)
    private String descriptionEn;

    @Schema(description = "Description in French", maxLength = 255)
    @Column(name = "F_07", length = 255)
    private String descriptionFr;
}
