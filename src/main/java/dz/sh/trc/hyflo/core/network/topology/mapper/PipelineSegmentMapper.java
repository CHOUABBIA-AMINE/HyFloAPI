package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdatePipelineSegmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.PipelineSegmentSummary;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSegment;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface PipelineSegmentMapper extends BaseMapper<CreatePipelineSegmentRequest, UpdatePipelineSegmentRequest, PipelineSegmentResponse, PipelineSegmentSummary, PipelineSegment> {
}