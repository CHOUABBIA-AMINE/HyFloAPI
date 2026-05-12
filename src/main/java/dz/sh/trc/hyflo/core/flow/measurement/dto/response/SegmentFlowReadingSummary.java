package dz.sh.trc.hyflo.core.flow.measurement.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
public record SegmentFlowReadingSummary(
    Long id,
    LocalDate readingDate,
    BigDecimal containedVolume,
    String pipelineSegmentName,
    String validationStatusName
) {}