package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface FacilityTypeService extends BaseService<CreateFacilityTypeRequest, UpdateFacilityTypeRequest, FacilityTypeResponse, FacilityTypeSummary> {
}