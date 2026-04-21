package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceSummary;
import dz.sh.trc.hyflo.core.system.security.service.ResourceService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/system/resources")
@Tag(name = "Resource API", description = "Endpoints for managing resources")
public class ResourceController extends BaseController<CreateResourceRequest, UpdateResourceRequest, ResourceResponse, ResourceSummary> {

    public ResourceController(ResourceService service) {
        super(service);
    }

    @Override
    protected Page<ResourceSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }
}
