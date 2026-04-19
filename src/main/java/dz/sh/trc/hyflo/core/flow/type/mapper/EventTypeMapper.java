package dz.sh.trc.hyflo.core.flow.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateEventTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.EventTypeSummary;
import dz.sh.trc.hyflo.core.flow.type.model.EventType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface EventTypeMapper extends BaseMapper<CreateEventTypeRequest, UpdateEventTypeRequest, EventTypeResponse, EventTypeSummary, EventType> {
}