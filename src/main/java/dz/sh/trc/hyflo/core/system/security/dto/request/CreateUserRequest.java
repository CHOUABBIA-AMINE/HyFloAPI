package dz.sh.trc.hyflo.core.system.security.dto.request;

import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload to create a new user")
public record CreateUserRequest(
        @NotBlank(message = "Username cannot be blank")
        String username,
        
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Must be a valid email")
        String email,
        
        @NotBlank(message = "Password cannot be blank")
        String password,
        
        Long employeeId,
        
        Set<Long> roleIds,
        
        Set<Long> groupIds
) {}
