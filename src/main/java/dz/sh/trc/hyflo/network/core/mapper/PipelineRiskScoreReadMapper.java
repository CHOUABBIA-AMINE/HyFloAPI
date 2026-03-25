/**
 * 
 * 	@Author		: HyFlo v2 Mapper
 *
 * 	@Name		: PipelineRiskScoreReadMapper
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Mapping
 * 	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.mapper;

import dz.sh.trc.hyflo.network.core.dto.PipelineRiskScoreReadDto;
import dz.sh.trc.hyflo.network.core.model.PipelineRiskScore;

/**
 * Static mapper from PipelineRiskScore entity to read DTO.
 */
public final class PipelineRiskScoreReadMapper {

    private PipelineRiskScoreReadMapper() {
        // utility class
    }

    public static PipelineRiskScoreReadDto toDto(PipelineRiskScore entity) {
        if (entity == null) {
            return null;
        }
        PipelineRiskScoreReadDto dto = new PipelineRiskScoreReadDto();
        dto.setId(entity.getId());
        dto.setPipelineSegmentId(entity.getPipelineSegment() != null ? entity.getPipelineSegment().getId() : null);
        dto.setCalculatedAt(entity.getCalculatedAt());
        dto.setValidUntil(entity.getValidUntil());
        dto.setRiskScore(entity.getRiskScore());
        dto.setModelName(entity.getModelName());
        dto.setDetails(entity.getDetails());
        return dto;
    }
}
