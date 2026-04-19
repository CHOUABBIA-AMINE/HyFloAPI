package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.EventStatusMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.EventStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.EventStatusRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.EventStatusService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class EventStatusServiceImpl extends AbstractCrudService<CreateEventStatusRequest, UpdateEventStatusRequest, EventStatusResponse, EventStatusSummary, EventStatus, Long> implements EventStatusService {

    public EventStatusServiceImpl(EventStatusRepository repository, EventStatusMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<EventStatus> getEntityClass() {
        return EventStatus.class;
    }
}