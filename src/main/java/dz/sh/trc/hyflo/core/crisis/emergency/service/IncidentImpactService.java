package dz.sh.trc.hyflo.core.crisis.emergency.service;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentImpactResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentImpactSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface IncidentImpactService extends BaseService<CreateIncidentImpactRequest, UpdateIncidentImpactRequest, IncidentImpactResponse, IncidentImpactSummary> {
}
