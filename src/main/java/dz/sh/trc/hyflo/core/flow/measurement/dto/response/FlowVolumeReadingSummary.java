package dz.sh.trc.hyflo.core.flow.measurement.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
public record FlowVolumeReadingSummary(
    Long id,
    LocalDate readingDate,
    BigDecimal volume,
    String infrastructureName,
    String productName,
    String typeName,
    String validationStatusName
) {}