package dz.sh.trc.hyflo.core.system.setting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update an existing system parameter")
public record UpdateParameterRequest(
        String value,
        String type,
        String description
) {}
