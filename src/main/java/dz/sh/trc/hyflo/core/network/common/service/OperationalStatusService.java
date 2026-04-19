package dz.sh.trc.hyflo.core.network.common.service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface OperationalStatusService extends BaseService<CreateOperationalStatusRequest, UpdateOperationalStatusRequest, OperationalStatusResponse, OperationalStatusSummary> {
}
