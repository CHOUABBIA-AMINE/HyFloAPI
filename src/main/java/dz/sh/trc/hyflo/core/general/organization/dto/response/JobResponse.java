package dz.sh.trc.hyflo.core.general.organization.dto.response;

public record JobResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr,
        Long structureId,
        String structureDesignationFr
) {}
