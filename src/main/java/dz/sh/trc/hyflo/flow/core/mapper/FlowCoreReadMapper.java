/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: FlowCoreReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class (Utility / Static Mapper)
 * 	@Layer		: Mapper
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDto;
import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDto;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Static utility mapper for the Flow / Core read layer.
 * Delegates directly to the static fromEntity() methods on each DTO
 * to keep a single authoritative mapping path.
 */
public final class FlowCoreReadMapper {

    private FlowCoreReadMapper() {
        // Utility class — no instantiation.
    }

    // ========== FlowReading ==========

    public static FlowReadingReadDto toDto(FlowReading entity) {
        return FlowReadingReadDto.fromEntity(entity);
    }

    // ========== FlowAnomaly ==========

    public static FlowAnomalyReadDto toDto(FlowAnomaly entity) {
        return FlowAnomalyReadDto.fromEntity(entity);
    }

    // ========== DataQualityIssue ==========

    public static DataQualityIssueReadDto toDto(DataQualityIssue entity) {
        return DataQualityIssueReadDto.fromEntity(entity);
    }
}
