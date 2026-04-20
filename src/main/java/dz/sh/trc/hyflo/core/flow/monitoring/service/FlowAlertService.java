package dz.sh.trc.hyflo.core.flow.monitoring.service;

import dz.sh.trc.hyflo.platform.kernel.BaseService;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;

public interface FlowAlertService extends BaseService<CreateFlowAlertRequest, UpdateFlowAlertRequest, FlowAlertResponse, FlowAlertSummary> {
    void acknowledgeAlert(Long id, AcknowledgeAlertRequest request);
    void resolveAlert(Long id, ResolveAlertRequest request);
}
