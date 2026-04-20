package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Terminal;
import dz.sh.trc.hyflo.core.network.topology.service.TerminalService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/terminals")
@Tag(name = "Terminal API", description = "Endpoints for managing Terminal")
public class TerminalController extends BaseController<CreateTerminalRequest, UpdateTerminalRequest, TerminalResponse, TerminalSummary> {

    public TerminalController(TerminalService service) {
        super(service);
    }

    @Override
    protected Page<TerminalSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}