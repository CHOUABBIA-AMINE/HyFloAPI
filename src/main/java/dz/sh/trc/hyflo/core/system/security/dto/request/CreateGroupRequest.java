package dz.sh.trc.hyflo.core.system.security.dto.request;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new group")
public record CreateGroupRequest(
        @NotBlank(message = "Group name cannot be blank")
        String name,
        
        String description,
        
        Set<Long> roleIds
) {}
