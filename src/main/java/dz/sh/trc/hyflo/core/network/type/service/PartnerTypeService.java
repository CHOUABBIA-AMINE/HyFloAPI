package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PartnerTypeService extends BaseService<CreatePartnerTypeRequest, UpdatePartnerTypeRequest, PartnerTypeResponse, PartnerTypeSummary> {
}