package dz.sh.trc.hyflo.core.network.topology.dto.response;

import java.time.LocalDate;

public record EquipmentResponse(
        Long id,
        String code,
        String name,
        String modelNumber,
        String serialNumber,
        LocalDate manufacturingDate,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate lastMaintenanceDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        String operationalStatusDesignationFr,
        Long equipmentTypeId,
        String equipmentTypeDesignationFr,
        Long facilityId,
        String facilityDesignationFr,
        Long manufacturerId,
        String manufacturerDesignationFr
) {}