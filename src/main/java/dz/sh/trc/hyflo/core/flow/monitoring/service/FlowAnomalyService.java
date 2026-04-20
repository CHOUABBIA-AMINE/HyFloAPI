package dz.sh.trc.hyflo.core.flow.monitoring.service;

import dz.sh.trc.hyflo.platform.kernel.BaseService;
import dz.sh.trc.hyflo.core.flow.monitoring.dto.*;

public interface FlowAnomalyService extends BaseService<CreateFlowAnomalyRequest, UpdateFlowAnomalyRequest, FlowAnomalyResponse, FlowAnomalySummary> {
    void analyzeAnomaly(Long id, AnalyzeAnomalyRequest request);
    void fixAnomaly(Long id, FixAnomalyRequest request);
}
