package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateStationRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.StationSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface StationService extends BaseService<CreateStationRequest, UpdateStationRequest, StationResponse, StationSummary> {
}