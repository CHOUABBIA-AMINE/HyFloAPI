package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilityResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilitySummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface FacilityService extends BaseService<CreateFacilityRequest, UpdateFacilityRequest, FacilityResponse, FacilitySummary> {
}