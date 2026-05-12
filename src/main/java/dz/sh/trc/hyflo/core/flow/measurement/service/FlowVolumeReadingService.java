package dz.sh.trc.hyflo.core.flow.measurement.service;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowVolumeReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowVolumeReadingSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;
public interface FlowVolumeReadingService extends BaseService<CreateFlowVolumeReadingRequest, UpdateFlowVolumeReadingRequest, FlowVolumeReadingResponse, FlowVolumeReadingSummary> {}