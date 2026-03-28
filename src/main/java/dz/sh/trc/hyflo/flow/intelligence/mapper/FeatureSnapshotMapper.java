/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FeatureSnapshotMapper
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class (static utility)
 *  @Layer      : Mapper
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.mapper;

import dz.sh.trc.hyflo.flow.intelligence.dto.FeatureSnapshotReadDTO;
import dz.sh.trc.hyflo.flow.intelligence.model.FeatureSnapshot;

public final class FeatureSnapshotMapper {

    private FeatureSnapshotMapper() {}

    public static FeatureSnapshotReadDTO toReadDTO(FeatureSnapshot entity) {
        if (entity == null) return null;
        return FeatureSnapshotReadDTO.builder()
                .id(entity.getId())
                .readingId(entity.getReading() != null
                        ? entity.getReading().getId() : null)
                .derivedReadingId(entity.getDerivedReading() != null
                        ? entity.getDerivedReading().getId() : null)
                .pipelineSegmentId(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getId() : null)
                .pipelineSegmentCode(entity.getPipelineSegment() != null
                        ? entity.getPipelineSegment().getCode() : null)
                .modelName(entity.getModelName())
                .generatedAt(entity.getGeneratedAt())
                .featureVector(entity.getFeatureVector())
                .featureSchemaVersion(entity.getFeatureSchemaVersion())
                .build();
    }
}
