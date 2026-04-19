package dz.sh.trc.hyflo.core.system.security.dto.response;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        Boolean enabled,
        Boolean accountNonLocked,
        Boolean accountNonExpired,
        Boolean credentialsNonExpired,
        String employeeFullName,
        List<RoleSummary> roles,
        List<GroupSummary> groups
) {}
