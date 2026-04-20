package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureSummary;
import dz.sh.trc.hyflo.core.network.topology.service.InfrastructureService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/infrastructures")
@Tag(name = "Infrastructure API", description = "Endpoints for managing infrastructure")
public class InfrastructureController extends BaseController<CreateInfrastructureRequest, UpdateInfrastructureRequest, InfrastructureResponse, InfrastructureSummary> {

    public InfrastructureController(InfrastructureService service) {
        super(service);
    }

    @Override
    protected Page<InfrastructureSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
