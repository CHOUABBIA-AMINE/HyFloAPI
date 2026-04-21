package dz.sh.trc.hyflo.core.crisis.emergency.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentImpactResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentImpactSummary;
import dz.sh.trc.hyflo.core.crisis.emergency.service.IncidentImpactService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;

@RestController
@RequestMapping("/crisis/impacts")
public class IncidentImpactController extends BaseController<CreateIncidentImpactRequest, UpdateIncidentImpactRequest, IncidentImpactResponse, IncidentImpactSummary> {

    public IncidentImpactController(IncidentImpactService service) {
        super(service);
    }

    @Override
    protected Page<IncidentImpactSummary> performSearch(String search, Pageable pageable) {
        return null;
    }
}
