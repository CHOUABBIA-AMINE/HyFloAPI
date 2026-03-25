/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: Pipeline
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
 * Represents a physical hydrocarbon transport pipeline (trunk line).
 */
@Schema(description = "Hydrocarbon transport pipeline")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Pipeline")
@Table(name = "T_03_01_01")
public class Pipeline extends GenericModel {

    @Schema(description = "Unique pipeline code", example = "GZ1-HASSI-ARZEW")
    @NotBlank
    @Size(max = 50)
    @Column(name = "F_01", length = 50, nullable = false, unique = true)
    private String code;

    @Schema(description = "Descriptive name of the pipeline", example = "GZ1 Hassi R'Mel – Arzew")
    @Size(max = 200)
    @Column(name = "F_02", length = 200)
    private String designation;

    @Schema(description = "Type of hydrocarbon transported", example = "GAS")
    @Size(max = 50)
    @Column(name = "F_03", length = 50)
    private String hydrocarbonType;

    @Schema(description = "Total length of the pipeline in km", example = "562.0")
    @Column(name = "F_04")
    private Double totalLengthKm;

    @Schema(description = "Nominal diameter in inches", example = "48")
    @Column(name = "F_05")
    private Integer nominalDiameterInch;

    @Schema(description = "Maximum operating pressure in bar", example = "100.0")
    @Column(name = "F_06")
    private Double maxOperatingPressureBar;

    @Schema(description = "Design capacity in MSCFD", example = "35000.0")
    @Column(name = "F_07")
    private Double designCapacityMscfd;

    @Schema(description = "Year the pipeline was commissioned", example = "1984")
    @Column(name = "F_08")
    private Integer commissioningYear;

    @Schema(description = "Whether the pipeline is currently active")
    @Column(name = "F_09")
    private Boolean active;
}
