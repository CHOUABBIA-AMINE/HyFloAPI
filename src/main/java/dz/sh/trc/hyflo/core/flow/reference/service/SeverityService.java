package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeverityResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeveritySummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface SeverityService extends BaseService<CreateSeverityRequest, UpdateSeverityRequest, SeverityResponse, SeveritySummary> {
}