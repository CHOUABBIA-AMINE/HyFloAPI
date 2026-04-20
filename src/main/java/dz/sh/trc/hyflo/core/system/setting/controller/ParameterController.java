package dz.sh.trc.hyflo.core.system.setting.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.setting.dto.request.CreateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.request.UpdateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterResponse;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterSummary;
import dz.sh.trc.hyflo.core.system.setting.model.Parameter;
import dz.sh.trc.hyflo.core.system.setting.service.ParameterService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/parameters")
@Tag(name = "Parameter API", description = "Endpoints for managing system settings and parameters")
public class ParameterController extends BaseController<CreateParameterRequest, UpdateParameterRequest, ParameterResponse, ParameterSummary> {

    public ParameterController(ParameterService service) {
        super(service);
    }

    @Override
    protected Page<ParameterSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }
}
