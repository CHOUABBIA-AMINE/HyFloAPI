package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSystemRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSystemSummary;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSystem;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface PipelineSystemMapper extends BaseMapper<CreatePipelineSystemRequest, UpdatePipelineSystemRequest, PipelineSystemResponse, PipelineSystemSummary, PipelineSystem> {
}