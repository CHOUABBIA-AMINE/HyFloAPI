package dz.sh.trc.hyflo.core.flow.measurement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public record CreateFlowReadingRequest(
        @NotNull LocalDate readingDate,
        BigDecimal volumeM3,
        BigDecimal volumeMscf,
        BigDecimal inletPressureBar,
        BigDecimal outletPressureBar,
        BigDecimal temperatureCelsius,
        String notes,
        @NotNull Long pipelineId,
        Long dataSourceId,
        Long readingSlotId,
        Long recordedById
) {}