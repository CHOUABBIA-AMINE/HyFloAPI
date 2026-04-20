package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeverityResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeveritySummary;
import dz.sh.trc.hyflo.core.flow.reference.service.SeverityService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/severities")
@Tag(name = "Severity API", description = "Endpoints for managing Severity")
public class SeverityController extends BaseController<CreateSeverityRequest, UpdateSeverityRequest, SeverityResponse, SeveritySummary> {

    public SeverityController(SeverityService service) {
        super(service);
    }

    @Override
    protected Page<SeveritySummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}