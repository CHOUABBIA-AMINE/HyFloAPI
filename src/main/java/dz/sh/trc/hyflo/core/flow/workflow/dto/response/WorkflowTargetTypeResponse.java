package dz.sh.trc.hyflo.core.flow.workflow.dto.response;

public record WorkflowTargetTypeResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr
) {}