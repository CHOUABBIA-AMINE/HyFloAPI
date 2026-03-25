/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineSegment
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
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Represents a logical segment of a pipeline between two metering stations.
 */
@Schema(description = "A logical pipeline segment between two operational metering stations")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PipelineSegment")
@Table(name = "T_03_01_02")
public class PipelineSegment extends GenericModel {

    @Schema(description = "Unique segment code", example = "GZ1-SEG-01")
    @NotBlank
    @Size(max = 50)
    @Column(name = "F_01", length = 50, nullable = false, unique = true)
    private String code;

    @Schema(description = "Name of the upstream (inlet) station", example = "Hassi R'Mel CS")
    @Size(max = 200)
    @Column(name = "F_02", length = 200)
    private String upstreamStation;

    @Schema(description = "Name of the downstream (outlet) station", example = "Tilghemt CS")
    @Size(max = 200)
    @Column(name = "F_03", length = 200)
    private String downstreamStation;

    @Schema(description = "Length of the segment in km", example = "120.5")
    @Column(name = "F_04")
    private Double lengthKm;

    @Schema(description = "FK to parent pipeline")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_01_02_FK_01"), nullable = false)
    private Pipeline pipeline;
}
