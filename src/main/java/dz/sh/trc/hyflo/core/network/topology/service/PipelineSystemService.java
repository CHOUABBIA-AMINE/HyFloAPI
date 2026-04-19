package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface PipelineSystemService extends BaseService<CreatePipelineSystemRequest, UpdatePipelineSystemRequest, PipelineSystemResponse, PipelineSystemSummary> {
}