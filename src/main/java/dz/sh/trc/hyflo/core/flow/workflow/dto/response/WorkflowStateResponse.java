package dz.sh.trc.hyflo.core.flow.workflow.dto.response;

public record WorkflowStateResponse(
        Long id,
        String code,
        String designationAr,
        String designationEn,
        String designationFr
) {}