package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FixAnomalyRequest(
        @NotNull(message = "Fixed by employee ID is mandatory")
        Long fixedByEmployeeId,
        @NotBlank(message = "Fix description is mandatory")
        String fixDescription
) {}
