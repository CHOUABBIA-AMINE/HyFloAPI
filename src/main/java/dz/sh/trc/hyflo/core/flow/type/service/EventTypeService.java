package dz.sh.trc.hyflo.core.flow.type.service;

import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface EventTypeService extends BaseService<CreateEventTypeRequest, UpdateEventTypeRequest, EventTypeResponse, EventTypeSummary> {
}