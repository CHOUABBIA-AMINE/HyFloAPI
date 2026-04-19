package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ReadingSlotService extends BaseService<CreateReadingSlotRequest, UpdateReadingSlotRequest, ReadingSlotResponse, ReadingSlotSummary> {
}