package dz.sh.trc.hyflo.core.network.common.service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloyResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloySummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface AlloyService extends BaseService<CreateAlloyRequest, UpdateAlloyRequest, AlloyResponse, AlloySummary> {
}
