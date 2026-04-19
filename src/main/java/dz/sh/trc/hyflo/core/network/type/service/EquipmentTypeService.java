package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface EquipmentTypeService extends BaseService<CreateEquipmentTypeRequest, UpdateEquipmentTypeRequest, EquipmentTypeResponse, EquipmentTypeSummary> {
}