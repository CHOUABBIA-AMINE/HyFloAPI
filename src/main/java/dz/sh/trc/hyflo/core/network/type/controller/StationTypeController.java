package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.StationTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/station-types")
@Tag(name = "StationType API", description = "Endpoints for managing StationType")
public class StationTypeController extends BaseController<CreateStationTypeRequest, UpdateStationTypeRequest, StationTypeResponse, StationTypeSummary> {

    public StationTypeController(StationTypeService service) {
        super(service);
    }

    @Override
    protected Page<StationTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}