package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdatePlanStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.PlanStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.service.PlanStatusService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flow/plan-statuses")
@Tag(name = "PlanStatus API", description = "Endpoints for managing PlanStatus")
public class PlanStatusController extends BaseController<CreatePlanStatusRequest, UpdatePlanStatusRequest, PlanStatusResponse, PlanStatusSummary> {

    public PlanStatusController(PlanStatusService service) {
        super(service);
    }

    @Override
    protected Page<PlanStatusSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}