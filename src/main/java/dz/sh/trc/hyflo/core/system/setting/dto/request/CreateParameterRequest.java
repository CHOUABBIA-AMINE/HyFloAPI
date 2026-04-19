package dz.sh.trc.hyflo.core.system.setting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to create a new system parameter")
public record CreateParameterRequest(
        @NotBlank(message = "Key is mandatory")
        String key,
        
        @NotBlank(message = "Value is mandatory")
        String value,
        
        @NotBlank(message = "Type is mandatory")
        String type,
        
        String description
) {}
