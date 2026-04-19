package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.EventStatus;
import dz.sh.trc.hyflo.core.flow.reference.service.EventStatusService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/event-statuses")
@Tag(name = "EventStatus API", description = "Endpoints for managing EventStatus")
public class EventStatusController extends BaseController<CreateEventStatusRequest, UpdateEventStatusRequest, EventStatusResponse, EventStatusSummary, EventStatus, Long> {

    public EventStatusController(EventStatusService service) {
        super(service);
    }

    @Override
    protected Page<EventStatusSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}