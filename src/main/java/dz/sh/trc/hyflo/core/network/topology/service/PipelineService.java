package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PipelineService extends BaseService<CreatePipelineRequest, UpdatePipelineRequest, PipelineResponse, PipelineSummary> {
}
