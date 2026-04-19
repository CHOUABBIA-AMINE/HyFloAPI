package dz.sh.trc.hyflo.core.system.security.dto.request;

import java.util.Set;
import jakarta.validation.constraints.Email;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to update an existing user")
public record UpdateUserRequest(
        @Email(message = "Must be a valid email")
        String email,
        
        Boolean enabled,
        
        Boolean accountNonLocked,
        
        Set<Long> roleIds,
        
        Set<Long> groupIds
) {}
