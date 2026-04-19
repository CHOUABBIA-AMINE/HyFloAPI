package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.StationType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface StationTypeMapper extends BaseMapper<CreateStationTypeRequest, UpdateStationTypeRequest, StationTypeResponse, StationTypeSummary, StationType> {
}