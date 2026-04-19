package dz.sh.trc.hyflo.core.general.type.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.general.type.dto.request.CreateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.request.UpdateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeResponse;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeSummary;
import dz.sh.trc.hyflo.core.general.type.model.StructureType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface StructureTypeMapper extends BaseMapper<CreateStructureTypeRequest, UpdateStructureTypeRequest, StructureTypeResponse, StructureTypeSummary, StructureType> {
    
    @Override
    void updateEntityFromRequest(UpdateStructureTypeRequest dto, @MappingTarget StructureType entity);
}
