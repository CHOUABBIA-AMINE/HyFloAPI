package dz.sh.trc.hyflo.core.flow.measurement.dto.request;

import java.math.BigDecimal;

public record UpdateFlowReadingRequest(
        BigDecimal volumeM3,
        BigDecimal volumeMscf,
        BigDecimal inletPressureBar,
        BigDecimal outletPressureBar,
        BigDecimal temperatureCelsius,
        String notes,
        Long version
) {}