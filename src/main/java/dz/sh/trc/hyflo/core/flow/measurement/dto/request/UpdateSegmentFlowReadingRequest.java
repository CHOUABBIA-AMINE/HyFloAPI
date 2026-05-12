package dz.sh.trc.hyflo.core.flow.measurement.dto.request;
import java.math.BigDecimal;
public record UpdateSegmentFlowReadingRequest(
    BigDecimal pressure,
    BigDecimal temperature,
    BigDecimal flowRate,
    BigDecimal containedVolume,
    String calculationMethod
) {}