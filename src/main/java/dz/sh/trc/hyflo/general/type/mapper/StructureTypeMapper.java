/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : StructureTypeMapper
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.mapper;

import dz.sh.trc.hyflo.general.type.dto.query.StructureTypeReadDto;
import dz.sh.trc.hyflo.general.type.model.StructureType;

public final class StructureTypeMapper {

    private StructureTypeMapper() {}

    public static StructureTypeReadDto toReadDto(StructureType entity) {
        if (entity == null) return null;
        return StructureTypeReadDto.builder()
                .id(entity.getId())
                .designationFr(entity.getDesignationFr())
                .designationEn(entity.getDesignationEn())
                .designationAr(entity.getDesignationAr())
                .build();
    }
}
