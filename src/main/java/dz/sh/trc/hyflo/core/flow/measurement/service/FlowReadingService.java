package dz.sh.trc.hyflo.core.flow.measurement.service;

import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.FlowReadingFilterDTO;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.WorkflowActionDTO;
import dz.sh.trc.hyflo.platform.kernel.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlowReadingService extends BaseService<CreateFlowReadingRequest, UpdateFlowReadingRequest, FlowReadingResponse, FlowReadingSummary> {
    FlowReadingResponse submitReading(Long id, WorkflowActionDTO action);
    FlowReadingResponse validateReading(Long id, WorkflowActionDTO action);
    FlowReadingResponse approveReading(Long id, WorkflowActionDTO action);
    FlowReadingResponse rejectReading(Long id, WorkflowActionDTO action);
    Page<FlowReadingSummary> findByFilter(FlowReadingFilterDTO filter, Pageable pageable);
}