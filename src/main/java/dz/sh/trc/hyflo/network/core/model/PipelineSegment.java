/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegment
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import dz.sh.trc.hyflo.network.common.model.Alloy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents individual segments of a pipeline with specific physical characteristics.
 * Pipelines are subdivided into segments for detailed technical management, inspection,
 * and maintenance tracking. Each segment may have different materials or specifications.
 */
@Schema(description = "Individual pipeline segment with specific physical and material properties for detailed asset management")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="PipelineSegment")
@Table(name="T_02_03_09")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_00"))
public class PipelineSegment extends Infrastructure {

	@Schema(
		description = "Internal diameter of this pipeline segment in millimeters",
		example = "1219.2",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Segment diameter is mandatory")
	@Positive(message = "Segment diameter must be positive")
	@Column(name="F_08", nullable=false)
	private Double diameter;

	@Schema(
		description = "Length of this pipeline segment in kilometers",
		example = "25.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Segment length is mandatory")
	@Positive(message = "Segment length must be positive")
	@Column(name="F_09", nullable=false)
	private Double length;

	@Schema(
		description = "Wall thickness of this pipeline segment in millimeters",
		example = "12.7",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Segment thickness is mandatory")
	@Positive(message = "Segment thickness must be positive")
	@Column(name="F_10", nullable=false)
	private Double thickness;

	@Schema(
		description = "Internal surface roughness of this pipeline segment in millimeters",
		example = "0.045",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Segment roughness is mandatory")
	@PositiveOrZero(message = "Segment roughness must be zero or positive")
	@Column(name="F_11", nullable=false)
	private Double roughness;

	@Schema(
		description = "Chainage or kilometer post marking the start point of this segment along the pipeline route",
		example = "150.0",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Start point is mandatory")
	@PositiveOrZero(message = "Start point must be zero or positive")
	@Column(name="F_12", nullable=false)
	private Double startPoint;

	@Schema(
		description = "Chainage or kilometer post marking the end point of this segment along the pipeline route",
		example = "175.5",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "End point is mandatory")
	@PositiveOrZero(message = "End point must be zero or positive")
	@Column(name="F_13", nullable=false)
	private Double endPoint;

	@Schema(
		description = "Alloy or material used for constructing this specific segment (may differ from nominal pipeline material)",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_14", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_01"), nullable=true)
	private Alloy constructionMaterial;

	@Schema(
		description = "Alloy or material used for exterior coating of this specific segment",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_15", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_02"), nullable=true)
	private Alloy exteriorCoating;

	@Schema(
		description = "Alloy or material used for interior coating of this specific segment",
		requiredMode = Schema.RequiredMode.NOT_REQUIRED
	)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_16", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_03"), nullable=true)
	private Alloy interiorCoating;

	@Schema(
		description = "Parent pipeline to which this segment belongs",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	@NotNull(message = "Pipeline is mandatory")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_17", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_04"), nullable=false)
	private Pipeline pipeline;
    
}
