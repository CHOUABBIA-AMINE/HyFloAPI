package dz.sh.trc.hyflo.core.flow.measurement.dto.request;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
public record CreateFlowVolumeReadingRequest(
    @NotNull LocalDate readingDate,
    @NotNull BigDecimal volume,
    String notes,
    @NotNull Long infrastructureId,
    @NotNull Long productId,
    @NotNull Long typeId,
    @NotNull Long recordedById
) {}