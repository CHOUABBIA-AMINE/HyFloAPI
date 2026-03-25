/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;

public final class FlowAnomalyMapper {

    private FlowAnomalyMapper() {}

    public static FlowAnomalyReadDto toReadDto(FlowAnomaly entity) {
        if (entity == null) return null;

        return FlowAnomalyReadDto.builder()
                .id(entity.getId())
                .anomalyType(entity.getAnomalyType())
                .severityScore(entity.getSeverityScore())
                .confidenceScore(entity.getConfidenceScore())
                .modelName(entity.getModelName())
                .explanation(entity.getExplanation())
                .detectedAt(entity.getDetectedAt())
                .readingId(entity.getReading() != null ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null
                        ? entity.getDerivedReading().getId() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getCode() : null)
                .build();
    }
}
