package dz.sh.trc.hyflo.core.flow.monitoring.service;

import dz.sh.trc.hyflo.platform.kernel.BaseService;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;

public interface FlowIncidentService extends BaseService<CreateFlowIncidentRequest, UpdateFlowIncidentRequest, FlowIncidentResponse, FlowIncidentSummary> {
    void investigateIncident(Long id, InvestigateIncidentRequest request);
    void resolveIncident(Long id, ResolveIncidentRequest request);
}
