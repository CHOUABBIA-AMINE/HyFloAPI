package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface EquipmentService extends BaseService<CreateEquipmentRequest, UpdateEquipmentRequest, EquipmentResponse, EquipmentSummary> {
}