package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeSummary;
import dz.sh.trc.hyflo.core.system.security.service.ResourceTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/system/resource-types")
@Tag(name = "Resource Type API", description = "Endpoints for managing resource types")
public class ResourceTypeController extends BaseController<CreateResourceTypeRequest, UpdateResourceTypeRequest, ResourceTypeResponse, ResourceTypeSummary> {

    public ResourceTypeController(ResourceTypeService service) {
        super(service);
    }

    @Override
    protected Page<ResourceTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }
}
