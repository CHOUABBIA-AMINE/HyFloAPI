package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Equipment;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper extends BaseMapper<CreateEquipmentRequest, UpdateEquipmentRequest, EquipmentResponse, EquipmentSummary, Equipment> {
}