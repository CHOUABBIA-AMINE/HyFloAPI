package dz.sh.trc.hyflo.core.network.topology.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record PipelineResponse(
        Long id,
        String code,
        String name,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        String operationalStatusDesignationFr,
        Long ownerId,
        String ownerDesignationFr,
        
        String nominalDiameter,
        Double length,
        String nominalThickness,
        Double nominalRoughness,
        Double designMaxServicePressure,
        Double operationalMaxServicePressure,
        Double designMinServicePressure,
        Double operationalMinServicePressure,
        Double designCapacity,
        Double operationalCapacity,
        
        Long nominalConstructionMaterialId,
        String nominalConstructionMaterialDesignationFr,
        Long nominalExteriorCoatingId,
        String nominalExteriorCoatingDesignationFr,
        Long nominalInteriorCoatingId,
        String nominalInteriorCoatingDesignationFr,
        
        Long pipelineSystemId,
        String pipelineSystemName,
        Long departureTerminalId,
        String departureTerminalName,
        Long arrivalTerminalId,
        String arrivalTerminalName,
        
        Long managerId,
        String managerDesignationFr,
        
        Set<String> vendorNames
) {}
