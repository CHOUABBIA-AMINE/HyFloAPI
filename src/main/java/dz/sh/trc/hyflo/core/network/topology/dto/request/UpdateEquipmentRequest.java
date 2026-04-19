package dz.sh.trc.hyflo.core.network.topology.dto.request;

import java.time.LocalDate;

public record UpdateEquipmentRequest(
        String name,
        String modelNumber,
        String serialNumber,
        LocalDate manufacturingDate,
        LocalDate installationDate,
        LocalDate commissioningDate,
        LocalDate lastMaintenanceDate,
        LocalDate decommissioningDate,
        Long operationalStatusId,
        Long equipmentTypeId,
        Long facilityId,
        Long manufacturerId
) {}