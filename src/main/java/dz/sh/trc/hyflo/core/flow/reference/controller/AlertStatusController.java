package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.service.AlertStatusService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flow/alert-statuses")
@Tag(name = "AlertStatus API", description = "Endpoints for managing AlertStatus")
public class AlertStatusController extends BaseController<CreateAlertStatusRequest, UpdateAlertStatusRequest, AlertStatusResponse, AlertStatusSummary> {

    public AlertStatusController(AlertStatusService service) {
        super(service);
    }

    @Override
    protected Page<AlertStatusSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}