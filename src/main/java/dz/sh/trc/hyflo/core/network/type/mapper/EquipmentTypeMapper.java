package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateEquipmentTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.EquipmentTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.EquipmentType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface EquipmentTypeMapper extends BaseMapper<CreateEquipmentTypeRequest, UpdateEquipmentTypeRequest, EquipmentTypeResponse, EquipmentTypeSummary, EquipmentType> {
}