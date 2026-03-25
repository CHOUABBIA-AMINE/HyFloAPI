/**
 *
 * 	@Author		: HyFlo v2
 *
 * 	@Name		: PipelineRiskScoreReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class (Utility / Static Mapper)
 * 	@Layer		: Mapper
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.mapper;

import dz.sh.trc.hyflo.network.core.dto.PipelineRiskScoreReadDto;
import dz.sh.trc.hyflo.network.core.model.PipelineRiskScore;

/**
 * Static utility mapper for the Network / Core read layer.
 * Delegates to the static fromEntity() method on PipelineRiskScoreReadDto.
 */
public final class PipelineRiskScoreReadMapper {

    private PipelineRiskScoreReadMapper() {
        // Utility class — no instantiation.
    }

    // ========== PipelineRiskScore ==========

    public static PipelineRiskScoreReadDto toDto(PipelineRiskScore entity) {
        return PipelineRiskScoreReadDto.fromEntity(entity);
    }
}
