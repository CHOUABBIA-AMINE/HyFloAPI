package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ResourceService extends BaseService<CreateResourceRequest, UpdateResourceRequest, ResourceResponse, ResourceSummary> {
}
