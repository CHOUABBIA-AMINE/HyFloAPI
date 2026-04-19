package dz.sh.trc.hyflo.core.general.organization.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureSummary;
import dz.sh.trc.hyflo.core.general.organization.model.Structure;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface StructureMapper extends BaseMapper<CreateStructureRequest, UpdateStructureRequest, StructureResponse, StructureSummary, Structure> {
    
    @Override
    @Mapping(target = "structureType", ignore = true)
    @Mapping(target = "parentStructure", ignore = true)
    Structure toEntity(CreateStructureRequest dto);

    @Override
    @Mapping(target = "structureTypeId", source = "structureType.id")
    @Mapping(target = "structureTypeDesignationFr", source = "structureType.designationFr")
    @Mapping(target = "parentStructureId", source = "parentStructure.id")
    @Mapping(target = "parentStructureCode", source = "parentStructure.code")
    StructureResponse toResponse(Structure entity);

    @Override
    @Mapping(target = "structureTypeDesignationFr", source = "structureType.designationFr")
    StructureSummary toSummary(Structure entity);

    @Override
    @Mapping(target = "structureType", ignore = true)
    @Mapping(target = "parentStructure", ignore = true)
    void updateEntityFromRequest(UpdateStructureRequest dto, @MappingTarget Structure entity);
}
