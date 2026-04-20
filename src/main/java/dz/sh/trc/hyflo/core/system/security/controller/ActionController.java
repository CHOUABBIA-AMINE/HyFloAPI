package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionSummary;
import dz.sh.trc.hyflo.core.system.security.service.ActionService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/actions")
@Tag(name = "Action API", description = "Endpoints for managing actions")
public class ActionController extends BaseController<CreateActionRequest, UpdateActionRequest, ActionResponse, ActionSummary> {

    public ActionController(ActionService service) {
        super(service);
    }

    @Override
    protected Page<ActionSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }
}
