package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.PlanStatus;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface PlanStatusMapper extends BaseMapper<CreatePlanStatusRequest, UpdatePlanStatusRequest, PlanStatusResponse, PlanStatusSummary, PlanStatus> {
}