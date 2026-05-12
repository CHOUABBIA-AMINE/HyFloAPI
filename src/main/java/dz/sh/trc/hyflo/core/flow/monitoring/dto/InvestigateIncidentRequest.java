package dz.sh.trc.hyflo.core.flow.monitoring.dto;

import jakarta.validation.constraints.NotNull;

public record InvestigateIncidentRequest(
        @NotNull(message = "Investigator employee ID is mandatory")
        Long investigatorEmployeeId,
        String investigationNotes
) {}
