package dz.sh.trc.hyflo.core.crisis.emergency.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentSummary;
import dz.sh.trc.hyflo.core.crisis.emergency.model.Incident;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface IncidentMapper extends BaseMapper<CreateIncidentRequest, UpdateIncidentRequest, IncidentResponse, IncidentSummary, Incident> {
    
    @Mapping(target="severity.id", source="severityId")
    @Mapping(target="pipelineSegment.id", source="pipelineSegmentId")

    Incident toEntity(CreateIncidentRequest request);

    @Mapping(target="severityId", source="severity.id")
    @Mapping(target="pipelineSegmentId", source="pipelineSegment.id")
    @Mapping(target="severityLabel", source="severity.label")
    @Mapping(target="pipelineSegmentName", source="pipelineSegment.name")

    IncidentResponse toResponse(Incident entity);

    IncidentSummary toSummary(Incident entity);
    
    List<IncidentResponse> toResponseList(List<Incident> entities);

    @Mapping(target="severity.id", source="severityId")
    @Mapping(target="pipelineSegment.id", source="pipelineSegmentId")

    void updateEntityFromRequest(UpdateIncidentRequest request, @MappingTarget Incident entity);
}
