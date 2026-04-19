package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateStationTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.StationTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface StationTypeService extends BaseService<CreateStationTypeRequest, UpdateStationTypeRequest, StationTypeResponse, StationTypeSummary> {
}