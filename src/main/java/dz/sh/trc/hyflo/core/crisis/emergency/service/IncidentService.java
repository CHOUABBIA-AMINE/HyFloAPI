package dz.sh.trc.hyflo.core.crisis.emergency.service;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface IncidentService extends BaseService<CreateIncidentRequest, UpdateIncidentRequest, IncidentResponse, IncidentSummary> {
}
