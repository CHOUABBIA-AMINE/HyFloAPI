package dz.sh.trc.hyflo.core.flow.measurement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FlowReadingResponse(
        Long id,
        LocalDate readingDate,
        BigDecimal volumeM3,
        BigDecimal volumeMscf,
        BigDecimal inletPressureBar,
        BigDecimal outletPressureBar,
        BigDecimal temperatureCelsius,
        String notes,
        LocalDateTime submittedAt,
        LocalDateTime validatedAt,
        Long pipelineId,
        String pipelineName,
        Long validationStatusId,
        String validationStatusName,
        Long workflowInstanceId,
        Long dataSourceId,
        String dataSourceName,
        Long readingSlotId,
        Long recordedById,
        Long validatedById,
        LocalDateTime recordedAt,
        Long version
) {}