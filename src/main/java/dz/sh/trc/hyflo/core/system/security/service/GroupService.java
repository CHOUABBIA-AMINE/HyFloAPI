package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateGroupRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateGroupRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.GroupResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.GroupSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface GroupService extends BaseService<CreateGroupRequest, UpdateGroupRequest, GroupResponse, GroupSummary> {
    
    void assignRole(Long groupId, Long roleId);
    
    void removeRole(Long groupId, Long roleId);
}
