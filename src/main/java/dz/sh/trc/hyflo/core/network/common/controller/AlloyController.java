package dz.sh.trc.hyflo.core.network.common.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloyResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloySummary;
import dz.sh.trc.hyflo.core.network.common.model.Alloy;
import dz.sh.trc.hyflo.core.network.common.service.AlloyService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/alloys")
@Tag(name = "Alloy API", description = "Endpoints for managing alloys and materials")
public class AlloyController extends BaseController<CreateAlloyRequest, UpdateAlloyRequest, AlloyResponse, AlloySummary> {

    public AlloyController(AlloyService service) {
        super(service);
    }

    @Override
    protected Page<AlloySummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
