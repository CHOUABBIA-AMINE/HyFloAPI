/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FeatureSnapshotReadDTO
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO (Read)
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Read DTO for a feature snapshot (AI traceability)")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeatureSnapshotReadDTO {

    private Long id;
    private Long readingId;
    private Long derivedReadingId;
    private Long pipelineSegmentId;
    private String pipelineSegmentCode;
    private String modelName;
    private LocalDateTime generatedAt;
    private String featureVector;
    private String featureSchemaVersion;
}
