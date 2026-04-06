/**
 * 
 * 	@Author		: HyFlo v2 Mapper
 *
 * 	@Name		: FeatureSnapshotReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Mapping
 * 	@Package	: Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.mapper;

import dz.sh.trc.hyflo.intelligence.dto.FeatureSnapshotReadDTO;
import dz.sh.trc.hyflo.intelligence.model.FeatureSnapshot;

/**
 * Static mapper from FeatureSnapshot entity to read DTO.
 */
public final class FeatureSnapshotReadMapper {

    private FeatureSnapshotReadMapper() {
        // utility class
    }

    public static FeatureSnapshotReadDTO toDto(FeatureSnapshot entity) {
        if (entity == null) {
            return null;
        }
        FeatureSnapshotReadDTO dto = new FeatureSnapshotReadDTO();
        dto.setId(entity.getId());
        dto.setReadingId(entity.getReading() != null ? entity.getReading().getId() : null);
        dto.setDerivedReadingId(entity.getDerivedReading() != null ? entity.getDerivedReading().getId() : null);
        dto.setPipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null);
        dto.setModelName(entity.getModelName());
        dto.setGeneratedAt(entity.getGeneratedAt());
        dto.setFeatureVector(entity.getFeatureVector());
        dto.setFeatureSchemaVersion(entity.getFeatureSchemaVersion());
        return dto;
    }
}
