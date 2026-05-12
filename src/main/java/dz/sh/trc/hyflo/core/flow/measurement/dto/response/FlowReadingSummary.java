package dz.sh.trc.hyflo.core.flow.measurement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FlowReadingSummary(
        Long id,
        LocalDate readingDate,
        BigDecimal volumeM3,
        String pipelineName,
        String validationStatusName
) {}