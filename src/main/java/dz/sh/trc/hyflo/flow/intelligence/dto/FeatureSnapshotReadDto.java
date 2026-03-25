/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: FeatureSnapshotReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for feature snapshots used by AI models.
 */
@Schema(description = "Read DTO for feature snapshots")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FeatureSnapshotReadDto {

    @Schema(description = "Technical identifier of the feature snapshot")
    private Long id;

    @Schema(description = "Identifier of the flow reading associated with this feature vector (if any)")
    private Long readingId;

    @Schema(description = "Identifier of the derived flow reading associated with this feature vector (if any)")
    private Long derivedReadingId;

    @Schema(description = "Identifier of the pipeline segment associated with this feature vector (if any)")
    private Long pipelineSegmentId;

    @Schema(description = "AI model name or version used when generating this feature vector")
    private String modelName;

    @Schema(description = "Timestamp when this feature vector was generated")
    private LocalDateTime generatedAt;

    @Schema(description = "Serialized feature vector (e.g., JSON array)")
    private String featureVector;

    @Schema(description = "Logical schema version for this feature vector representation")
    private String featureSchemaVersion;
}
