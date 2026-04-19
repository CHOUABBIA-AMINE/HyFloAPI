package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PermissionService extends BaseService<CreatePermissionRequest, UpdatePermissionRequest, PermissionResponse, PermissionSummary> {
}
