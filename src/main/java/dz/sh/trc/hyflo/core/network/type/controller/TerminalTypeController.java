package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.TerminalTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/terminal-types")
@Tag(name = "TerminalType API", description = "Endpoints for managing TerminalType")
public class TerminalTypeController extends BaseController<CreateTerminalTypeRequest, UpdateTerminalTypeRequest, TerminalTypeResponse, TerminalTypeSummary> {

    public TerminalTypeController(TerminalTypeService service) {
        super(service);
    }

    @Override
    protected Page<TerminalTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}