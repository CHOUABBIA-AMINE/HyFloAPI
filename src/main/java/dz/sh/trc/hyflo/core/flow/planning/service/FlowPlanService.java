package dz.sh.trc.hyflo.core.flow.planning.service;

import dz.sh.trc.hyflo.platform.kernel.BaseService;
import dz.sh.trc.hyflo.core.flow.planning.dto.*;

public interface FlowPlanService extends BaseService<CreateFlowPlanRequest, UpdateFlowPlanRequest, FlowPlanResponse, FlowPlanSummary> {

    /**
     * Approves a flow plan, transitioning its status from DRAFT to APPROVED.
     * Sets the approvedAt timestamp and the approvedBy employee.
     *
     * @param id                 the plan ID
     * @param approverEmployeeId the employee performing the approval
     * @return the updated plan response
     */
    FlowPlanResponse approvePlan(Long id, Long approverEmployeeId);
}
