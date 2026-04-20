package dz.sh.trc.hyflo.core.crisis.emergency.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentSummary;
import dz.sh.trc.hyflo.core.crisis.emergency.service.IncidentService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;

@RestController
@RequestMapping("/api/v1/crisis/incidents")
public class IncidentController extends BaseController<CreateIncidentRequest, UpdateIncidentRequest, IncidentResponse, IncidentSummary> {

    public IncidentController(IncidentService service) {
        super(service);
    }

    @Override
    protected Page<IncidentSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
