package dz.sh.trc.hyflo.core.flow.measurement.dto.request;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
public record CreateSegmentFlowReadingRequest(
    @NotNull LocalDate readingDate,
    BigDecimal pressure,
    BigDecimal temperature,
    BigDecimal flowRate,
    BigDecimal containedVolume,
    String calculationMethod,
    Long dataSourceId,
    Long readingSlotId,
    @NotNull Long pipelineSegmentId,
    Long sourceReadingId
) {}