package dz.sh.trc.hyflo.core.flow.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeSummary;
import dz.sh.trc.hyflo.core.flow.type.model.EventType;
import dz.sh.trc.hyflo.core.flow.type.service.EventTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/event-types")
@Tag(name = "EventType API", description = "Endpoints for managing EventType")
public class EventTypeController extends BaseController<CreateEventTypeRequest, UpdateEventTypeRequest, EventTypeResponse, EventTypeSummary> {

    public EventTypeController(EventTypeService service) {
        super(service);
    }

    @Override
    protected Page<EventTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}