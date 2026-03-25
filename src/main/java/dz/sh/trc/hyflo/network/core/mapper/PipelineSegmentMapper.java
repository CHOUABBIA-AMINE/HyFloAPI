/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.mapper;

import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.dto.command.PipelineSegmentCommandDto;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDto;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

public final class PipelineSegmentMapper {

    private PipelineSegmentMapper() {}

    public static PipelineSegmentReadDto toReadDto(PipelineSegment entity) {
        if (entity == null) return null;

        return PipelineSegmentReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .length(entity.getLength())
                .startKm(entity.getStartKm())
                .endKm(entity.getEndKm())
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                .pipelineCode(entity.getPipeline() != null ? entity.getPipeline().getCode() : null)
                .pipelineName(entity.getPipeline() != null ? entity.getPipeline().getName() : null)
                .operationalStatusId(entity.getOperationalStatus() != null
                        ? entity.getOperationalStatus().getId() : null)
                .operationalStatusCode(entity.getOperationalStatus() != null
                        ? entity.getOperationalStatus().getCode() : null)
                .constructionMaterialId(entity.getConstructionMaterial() != null
                        ? entity.getConstructionMaterial().getId() : null)
                .constructionMaterialCode(entity.getConstructionMaterial() != null
                        ? entity.getConstructionMaterial().getCode() : null)
                .departureNodeId(entity.getDepartureNode() != null
                        ? entity.getDepartureNode().getId() : null)
                .departureNodeCode(entity.getDepartureNode() != null
                        ? entity.getDepartureNode().getCode() : null)
                .arrivalNodeId(entity.getArrivalNode() != null
                        ? entity.getArrivalNode().getId() : null)
                .arrivalNodeCode(entity.getArrivalNode() != null
                        ? entity.getArrivalNode().getCode() : null)
                .build();
    }

    public static PipelineSegment toEntity(PipelineSegmentCommandDto dto) {
        if (dto == null) return null;

        PipelineSegment entity = new PipelineSegment();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setLength(dto.getLength());
        entity.setStartKm(dto.getStartKm());
        entity.setEndKm(dto.getEndKm());

        if (dto.getPipelineId() != null) {
            Pipeline p = new Pipeline();
            p.setId(dto.getPipelineId());
            entity.setPipeline(p);
        }
        if (dto.getOperationalStatusId() != null) {
            OperationalStatus s = new OperationalStatus();
            s.setId(dto.getOperationalStatusId());
            entity.setOperationalStatus(s);
        }
        if (dto.getConstructionMaterialId() != null) {
            Alloy a = new Alloy();
            a.setId(dto.getConstructionMaterialId());
            entity.setConstructionMaterial(a);
        }

        return entity;
    }
}
