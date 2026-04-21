package dz.sh.trc.hyflo.core.network.common.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusSummary;
import dz.sh.trc.hyflo.core.network.common.service.OperationalStatusService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/operational-statuses")
@Tag(name = "Operational Status API", description = "Endpoints for managing operational statuses")
public class OperationalStatusController extends BaseController<CreateOperationalStatusRequest, UpdateOperationalStatusRequest, OperationalStatusResponse, OperationalStatusSummary> {

    public OperationalStatusController(OperationalStatusService service) {
        super(service);
    }

    @Override
    protected Page<OperationalStatusSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
