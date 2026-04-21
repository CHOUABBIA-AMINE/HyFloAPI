package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.FacilityTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/facility-types")
@Tag(name = "FacilityType API", description = "Endpoints for managing FacilityType")
public class FacilityTypeController extends BaseController<CreateFacilityTypeRequest, UpdateFacilityTypeRequest, FacilityTypeResponse, FacilityTypeSummary> {

    public FacilityTypeController(FacilityTypeService service) {
        super(service);
    }

    @Override
    protected Page<FacilityTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}