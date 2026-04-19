package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PipelineSegmentService extends BaseService<CreatePipelineSegmentRequest, UpdatePipelineSegmentRequest, PipelineSegmentResponse, PipelineSegmentSummary> {
}