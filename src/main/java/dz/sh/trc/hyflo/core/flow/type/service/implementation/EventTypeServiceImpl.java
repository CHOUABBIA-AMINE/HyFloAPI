package dz.sh.trc.hyflo.core.flow.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeSummary;
import dz.sh.trc.hyflo.core.flow.type.mapper.EventTypeMapper;
import dz.sh.trc.hyflo.core.flow.type.model.EventType;
import dz.sh.trc.hyflo.core.flow.type.repository.EventTypeRepository;
import dz.sh.trc.hyflo.core.flow.type.service.EventTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class EventTypeServiceImpl extends AbstractCrudService<CreateEventTypeRequest, UpdateEventTypeRequest, EventTypeResponse, EventTypeSummary, EventType, Long> implements EventTypeService {

    public EventTypeServiceImpl(EventTypeRepository repository, EventTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<EventType> getEntityClass() {
        return EventType.class;
    }
}