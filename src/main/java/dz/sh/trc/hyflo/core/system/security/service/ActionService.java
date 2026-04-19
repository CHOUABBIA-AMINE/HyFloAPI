package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ActionService extends BaseService<CreateActionRequest, UpdateActionRequest, ActionResponse, ActionSummary> {
}
