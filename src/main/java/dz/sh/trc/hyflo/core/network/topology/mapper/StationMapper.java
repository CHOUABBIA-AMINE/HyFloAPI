package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Station;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface StationMapper extends BaseMapper<CreateStationRequest, UpdateStationRequest, StationResponse, StationSummary, Station> {
}