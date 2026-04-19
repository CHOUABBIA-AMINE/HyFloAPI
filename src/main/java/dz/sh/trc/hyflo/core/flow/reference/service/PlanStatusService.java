package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PlanStatusService extends BaseService<CreatePlanStatusRequest, UpdatePlanStatusRequest, PlanStatusResponse, PlanStatusSummary> {
}