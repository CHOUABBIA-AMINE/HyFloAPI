package dz.sh.trc.hyflo.core.crisis.reference.service;

import dz.sh.trc.hyflo.core.crisis.reference.dto.request.CreateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.request.UpdateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.response.IncidentSeverityResponse;
import dz.sh.trc.hyflo.core.crisis.reference.dto.summary.IncidentSeveritySummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface IncidentSeverityService extends BaseService<CreateIncidentSeverityRequest, UpdateIncidentSeverityRequest, IncidentSeverityResponse, IncidentSeveritySummary> {
}
