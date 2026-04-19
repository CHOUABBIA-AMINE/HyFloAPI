package dz.sh.trc.hyflo.core.system.setting.service;

import dz.sh.trc.hyflo.core.system.setting.dto.request.CreateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.request.UpdateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterResponse;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ParameterService extends BaseService<CreateParameterRequest, UpdateParameterRequest, ParameterResponse, ParameterSummary> {
}
