package dz.sh.trc.hyflo.core.system.security.service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ResourceTypeService extends BaseService<CreateResourceTypeRequest, UpdateResourceTypeRequest, ResourceTypeResponse, ResourceTypeSummary> {
}
