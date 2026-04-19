package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateEventStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.EventStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.EventStatus;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface EventStatusMapper extends BaseMapper<CreateEventStatusRequest, UpdateEventStatusRequest, EventStatusResponse, EventStatusSummary, EventStatus> {
}