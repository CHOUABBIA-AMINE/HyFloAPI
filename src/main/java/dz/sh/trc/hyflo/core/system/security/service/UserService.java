package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface UserService extends BaseService<CreateUserRequest, UpdateUserRequest, UserResponse, UserSummary> {
    
    void assignRole(Long userId, Long roleId);
    
    void removeRole(Long userId, Long roleId);
    
    void assignGroup(Long userId, Long groupId);
    
    void removeGroup(Long userId, Long groupId);
    
    UserResponse findByUsername(String username);
}
