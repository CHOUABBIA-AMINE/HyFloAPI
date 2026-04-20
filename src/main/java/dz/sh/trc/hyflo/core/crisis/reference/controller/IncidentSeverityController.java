package dz.sh.trc.hyflo.core.crisis.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.crisis.reference.dto.request.CreateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.request.UpdateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.response.IncidentSeverityResponse;
import dz.sh.trc.hyflo.core.crisis.reference.dto.summary.IncidentSeveritySummary;
import dz.sh.trc.hyflo.core.crisis.reference.service.IncidentSeverityService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;

@RestController
@RequestMapping("/api/v1/crisis/severities")
public class IncidentSeverityController extends BaseController<CreateIncidentSeverityRequest, UpdateIncidentSeverityRequest, IncidentSeverityResponse, IncidentSeveritySummary, dz.sh.trc.hyflo.core.crisis.reference.model.IncidentSeverity, Long> {

    public IncidentSeverityController(IncidentSeverityService service) {
        super(service);
    }

    @Override
    protected Page<IncidentSeveritySummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
