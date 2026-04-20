package dz.sh.trc.hyflo.core.crisis.reference.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.crisis.reference.dto.request.CreateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.request.UpdateIncidentSeverityRequest;
import dz.sh.trc.hyflo.core.crisis.reference.dto.response.IncidentSeverityResponse;
import dz.sh.trc.hyflo.core.crisis.reference.dto.summary.IncidentSeveritySummary;
import dz.sh.trc.hyflo.core.crisis.reference.model.IncidentSeverity;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface IncidentSeverityMapper extends BaseMapper<CreateIncidentSeverityRequest, UpdateIncidentSeverityRequest, IncidentSeverityResponse, IncidentSeveritySummary, IncidentSeverity> {
    

    IncidentSeverity toEntity(CreateIncidentSeverityRequest request);


    IncidentSeverityResponse toResponse(IncidentSeverity entity);

    IncidentSeveritySummary toSummary(IncidentSeverity entity);
    
    List<IncidentSeverityResponse> toResponseList(List<IncidentSeverity> entities);


    void updateEntityFromRequest(UpdateIncidentSeverityRequest request, @MappingTarget IncidentSeverity entity);
}
