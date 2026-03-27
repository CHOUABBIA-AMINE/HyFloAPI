/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowCoreReadMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 - Phase 2: rewritten to own mapping logic directly
 *                             No longer delegates to DTO.fromEntity() (anti-pattern removed)
 *
 *  @Type       : Class (Utility / Static Mapper — Facade)
 *  @Layer      : Mapper
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.mapper;

import dz.sh.trc.hyflo.flow.core.dto.DataQualityIssueReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.FlowAnomalyReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.model.DataQualityIssue;
import dz.sh.trc.hyflo.flow.core.model.FlowAnomaly;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;

/**
 * Facade mapper for the Flow / Core read layer.
 *
 * Delegates to the dedicated mapper for each entity.
 * Does NOT call any DTO.fromEntity() method — that anti-pattern
 * has been removed in Phase 2.
 */
public final class FlowCoreReadMapper {

    private FlowCoreReadMapper() {}

    public static FlowReadingReadDTO toDTO(FlowReading entity) {
        return FlowReadingMapper.toReadDTO(entity);
    }

    public static FlowAnomalyReadDTO toDTO(FlowAnomaly entity) {
        return FlowAnomalyMapper.toReadDTO(entity);
    }

    public static DataQualityIssueReadDTO toDTO(DataQualityIssue entity) {
        return DataQualityIssueMapper.toReadDTO(entity);
    }
}
