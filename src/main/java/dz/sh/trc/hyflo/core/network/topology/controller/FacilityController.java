package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilityResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilitySummary;
import dz.sh.trc.hyflo.core.network.topology.service.FacilityService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/facilities")
@Tag(name = "Facility API", description = "Endpoints for managing Facility")
public class FacilityController extends BaseController<CreateFacilityRequest, UpdateFacilityRequest, FacilityResponse, FacilitySummary> {

    public FacilityController(FacilityService service) {
        super(service);
    }

    @Override
    protected Page<FacilitySummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}