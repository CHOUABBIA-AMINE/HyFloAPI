package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import jakarta.validation.constraints.NotNull;

public record AnalyzeAnomalyRequest(
        @NotNull(message = "Analyst employee ID is mandatory")
        Long analystEmployeeId,
        String analysisNotes
) {}
