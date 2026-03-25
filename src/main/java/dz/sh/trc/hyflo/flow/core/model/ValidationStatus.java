/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: ValidationStatus
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference entity for the validation lifecycle status of a FlowReading.
 * Codes: DRAFT, SUBMITTED, VALIDATED, APPROVED, REJECTED.
 */
@Schema(description = "Reference for the validation lifecycle status of a flow reading")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ValidationStatus")
@Table(name = "T_04_00_01")
public class ValidationStatus extends GenericModel {

    @Schema(description = "Status code", example = "APPROVED")
    @NotBlank
    @Size(max = 30)
    @Column(name = "F_01", length = 30, nullable = false, unique = true)
    private String code;

    @Schema(description = "Human-readable label", example = "Approved")
    @Size(max = 100)
    @Column(name = "F_02", length = 100)
    private String label;
}
