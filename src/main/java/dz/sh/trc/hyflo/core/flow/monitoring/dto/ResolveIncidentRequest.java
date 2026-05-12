package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResolveIncidentRequest(
        @NotNull(message = "Resolved by employee ID is mandatory")
        Long resolvedByEmployeeId,
        @NotBlank(message = "Resolution notes are mandatory")
        String resolutionNotes,
        String correctiveActionTaken
) {}
