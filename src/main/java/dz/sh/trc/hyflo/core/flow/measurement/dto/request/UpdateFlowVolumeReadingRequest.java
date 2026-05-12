package dz.sh.trc.hyflo.core.flow.measurement.dto.request;
import java.math.BigDecimal;
public record UpdateFlowVolumeReadingRequest(
    BigDecimal volume,
    String notes,
    Long typeId
) {}