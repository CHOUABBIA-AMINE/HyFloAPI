package dz.sh.trc.hyflo.core.system.security.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionSummary;
import dz.sh.trc.hyflo.core.system.security.model.Permission;
import dz.sh.trc.hyflo.core.system.security.service.PermissionService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/system/permissions")
@Tag(name = "Permission API", description = "Endpoints for managing permissions")
public class PermissionController extends BaseController<CreatePermissionRequest, UpdatePermissionRequest, PermissionResponse, PermissionSummary> {

    public PermissionController(PermissionService service) {
        super(service);
    }

    @Override
    protected Page<PermissionSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not yet implemented");
    }
}
