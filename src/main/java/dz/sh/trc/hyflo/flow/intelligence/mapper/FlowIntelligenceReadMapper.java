/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowIntelligenceReadMapper
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class (static utility mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Intelligence
 *
 *  @Description: Maps flow.intelligence.model entities to their read DTOs.
 *                Replaces FlowCoreReadMapper references for intelligence entities.
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.mapper;

import dz.sh.trc.hyflo.flow.intelligence.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.intelligence.model.FlowAnomaly;

/**
 * Static utility mapper for flow.intelligence entities → DTOs.
 */
public final class FlowIntelligenceReadMapper {

    private FlowIntelligenceReadMapper() {}

    public static FlowAnomalyReadDTO toAnomalyDTO(FlowAnomaly entity) {
        if (entity == null) return null;
        return FlowAnomalyReadDTO.builder()
                .id(entity.getId())
                .anomalyType(entity.getAnomalyType())
                .severityScore(entity.getSeverityScore())
                .confidenceScore(entity.getConfidenceScore())
                .modelName(entity.getModelName())
                .explanation(entity.getExplanation())
                .detectedAt(entity.getDetectedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getCode() : null)
                .build();
    }

    public static DataQualityIssueReadDTO toQualityIssueDTO(DataQualityIssue entity) {
        if (entity == null) return null;
        return DataQualityIssueReadDTO.builder()
                .id(entity.getId())
                .issueType(entity.getIssueType())
                .qualityScore(entity.getQualityScore())
                .details(entity.getDetails())
                .acknowledged(entity.getAcknowledged())
                .raisedAt(entity.getRaisedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null)
                .build();
    }
}
