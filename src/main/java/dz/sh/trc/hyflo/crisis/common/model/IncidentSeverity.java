/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: IncidentSeverity
 * 	@CreatedOn	: 03-25-2026
 *	@UpdatedOn	: 03-26-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 *  @Package    : Crisis / Common
 *
 **/

package dz.sh.trc.hyflo.crisis.common.model;

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
 * Reference entity for incident severity levels (P1–P4, CRITICAL, HIGH, MEDIUM, LOW).
 */
@Schema(description = "Incident severity reference")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "IncidentSeverity")
@Table(name = "T_05_00_01")
public class IncidentSeverity extends GenericModel {

    @Schema(description = "Severity code", example = "P1")
    @NotBlank
    @Size(max = 20)
    @Column(name = "F_01", length = 20, nullable = false, unique = true)
    private String code;

    @Schema(description = "Severity label", example = "Critical")
    @Size(max = 100)
    @Column(name = "F_02", length = 100)
    private String label;

    @Schema(description = "Priority rank (1 = highest)", example = "1")
    @Column(name = "F_03")
    private Integer rank;
}
