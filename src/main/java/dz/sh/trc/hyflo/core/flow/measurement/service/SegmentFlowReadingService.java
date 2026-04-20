package dz.sh.trc.hyflo.core.flow.measurement.service;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateSegmentFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.SegmentFlowReadingSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;
public interface SegmentFlowReadingService extends BaseService<CreateSegmentFlowReadingRequest, UpdateSegmentFlowReadingRequest, SegmentFlowReadingResponse, SegmentFlowReadingSummary> {}