package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;

public record UpdatePipelineSegmentRequest(
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long ownerId,
        Double diameter,
        Double length,
        Double thickness,
        Double roughness,
        Double startPoint,
        Double endPoint,
        Long constructionMaterialId,
        Long exteriorCoatingId,
        Long interiorCoatingId,
        Long pipelineId,
        Long departureFacilityId,
        Long arrivalFacilityId,
        String terrainType,
        String environmentSensitivity,
        Double corrosionIndex,
        String criticality
) {}