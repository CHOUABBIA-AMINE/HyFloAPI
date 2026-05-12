package dz.sh.trc.hyflo.core.flow.measurement.dto.request;

import java.time.LocalDate;

public record FlowReadingFilterDTO(
        LocalDate dateFrom,
        LocalDate dateTo,
        Long pipelineId,
        Long validationStatusId,
        Long readingSlotId
) {}