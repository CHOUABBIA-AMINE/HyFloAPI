package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationSummary;
import dz.sh.trc.hyflo.core.network.topology.service.StationService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/stations")
@Tag(name = "Station API", description = "Endpoints for managing Station")
public class StationController extends BaseController<CreateStationRequest, UpdateStationRequest, StationResponse, StationSummary> {

    public StationController(StationService service) {
        super(service);
    }

    @Override
    protected Page<StationSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}