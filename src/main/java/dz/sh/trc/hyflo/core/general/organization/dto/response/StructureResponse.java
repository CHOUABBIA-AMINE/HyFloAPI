package dz.sh.trc.hyflo.core.general.organization.dto.response;

public record StructureResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr,
        Long structureTypeId,
        String structureTypeDesignationFr,
        Long parentStructureId,
        String parentStructureCode
) {}
