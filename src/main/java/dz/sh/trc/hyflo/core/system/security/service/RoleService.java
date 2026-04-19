package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateRoleRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateRoleRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.RoleResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.RoleSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface RoleService extends BaseService<CreateRoleRequest, UpdateRoleRequest, RoleResponse, RoleSummary> {
    
    void assignPermission(Long roleId, Long permissionId);
    
    void removePermission(Long roleId, Long permissionId);
}
