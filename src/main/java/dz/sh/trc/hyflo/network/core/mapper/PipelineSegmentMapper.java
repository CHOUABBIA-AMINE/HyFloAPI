/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Phase A: Align all entity field access to actual
 *                             PipelineSegment model.
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Network / Core
 *
 *  Entity source-of-truth (PipelineSegment extends Infrastructure):
 *    Infrastructure : code, name, installationDate, commissioningDate,
 *                     decommissioningDate, operationalStatus, owner (Structure)
 *    PipelineSegment: diameter, length, thickness, roughness,
 *                     startPoint, endPoint,          ← NOT startKm / endKm
 *                     constructionMaterial (Alloy),
 *                     exteriorCoating (Alloy),
 *                     interiorCoating  (Alloy),
 *                     pipeline (Pipeline),
 *                     departureFacility (Facility),  ← NOT departureNode
 *                     arrivalFacility   (Facility),  ← NOT arrivalNode
 *                     coordinates, terrainType,
 *                     environmentSensitivity,
 *                     corrosionIndex, criticality
 *
 *  CommandDto note:
 *    PipelineSegmentCommandDto keeps field names startKm/endKm and
 *    departureNodeId/arrivalNodeId — those are DTO-level names only.
 *    The mapper reads them from the DTO and writes to the entity using
 *    the real entity setters (setStartPoint / setEndPoint /
 *    setDepartureFacility / setArrivalFacility).
 **/

package dz.sh.trc.hyflo.network.core.mapper;

import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.dto.command.PipelineSegmentCommandDto;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDto;
import dz.sh.trc.hyflo.network.core.model.Facility;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;

public final class PipelineSegmentMapper {

    private PipelineSegmentMapper() {}

    // =====================================================================
    // entity → PipelineSegmentReadDto
    // =====================================================================

    public static PipelineSegmentReadDto toReadDto(PipelineSegment entity) {
        if (entity == null) return null;

        return PipelineSegmentReadDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .length(entity.getLength())
                // startPoint / endPoint  (entity)  → startKm / endKm  (DTO label)
                .startKm(entity.getStartPoint())
                .endKm(entity.getEndPoint())
                .pipelineId(entity.getPipeline() != null
                        ? entity.getPipeline().getId() : null)
                .pipelineCode(entity.getPipeline() != null
                        ? entity.getPipeline().getCode() : null)
                .pipelineName(entity.getPipeline() != null
                        ? entity.getPipeline().getName() : null)
                .operationalStatusId(entity.getOperationalStatus() != null
                        ? entity.getOperationalStatus().getId() : null)
                .operationalStatusCode(entity.getOperationalStatus() != null
                        ? entity.getOperationalStatus().getCode() : null)
                .constructionMaterialId(entity.getConstructionMaterial() != null
                        ? entity.getConstructionMaterial().getId() : null)
                .constructionMaterialCode(entity.getConstructionMaterial() != null
                        ? entity.getConstructionMaterial().getCode() : null)
                // departureFacility / arrivalFacility (entity) → departureNode / arrivalNode (DTO label)
                .departureNodeId(entity.getDepartureFacility() != null
                        ? entity.getDepartureFacility().getId() : null)
                .departureNodeCode(entity.getDepartureFacility() != null
                        ? entity.getDepartureFacility().getCode() : null)
                .arrivalNodeId(entity.getArrivalFacility() != null
                        ? entity.getArrivalFacility().getId() : null)
                .arrivalNodeCode(entity.getArrivalFacility() != null
                        ? entity.getArrivalFacility().getCode() : null)
                .build();
    }

    // =====================================================================
    // PipelineSegmentCommandDto → new PipelineSegment entity
    // =====================================================================

    public static PipelineSegment toEntity(PipelineSegmentCommandDto dto) {
        if (dto == null) return null;

        PipelineSegment entity = new PipelineSegment();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setLength(dto.getLength());
        // DTO startKm / endKm → entity startPoint / endPoint
        entity.setStartPoint(dto.getStartKm());
        entity.setEndPoint(dto.getEndKm());

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
        // DTO departureNodeId / arrivalNodeId → entity departureFacility / arrivalFacility
        if (dto.getDepartureNodeId() != null) {
            Facility f = new Facility();
            f.setId(dto.getDepartureNodeId());
            entity.setDepartureFacility(f);
        }
        if (dto.getArrivalNodeId() != null) {
            Facility f = new Facility();
            f.setId(dto.getArrivalNodeId());
            entity.setArrivalFacility(f);
        }

        return entity;
    }
}
