package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface EventStatusService extends BaseService<CreateEventStatusRequest, UpdateEventStatusRequest, EventStatusResponse, EventStatusSummary> {
}