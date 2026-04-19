package dz.sh.trc.hyflo.core.network.common.service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PartnerService extends BaseService<CreatePartnerRequest, UpdatePartnerRequest, PartnerResponse, PartnerSummary> {
}
