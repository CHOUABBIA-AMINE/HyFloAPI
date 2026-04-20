package dz.sh.trc.hyflo.core.crisis.emergency.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.CreateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.request.UpdateIncidentImpactRequest;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.response.IncidentImpactResponse;
import dz.sh.trc.hyflo.core.crisis.emergency.dto.summary.IncidentImpactSummary;
import dz.sh.trc.hyflo.core.crisis.emergency.model.IncidentImpact;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface IncidentImpactMapper extends BaseMapper<CreateIncidentImpactRequest, UpdateIncidentImpactRequest, IncidentImpactResponse, IncidentImpactSummary, IncidentImpact> {
    
    @Mapping(target="incident.id", source="incidentId")

    IncidentImpact toEntity(CreateIncidentImpactRequest request);

    @Mapping(target="incidentId", source="incident.id")
    @Mapping(target="incidentCode", source="incident.code")

    IncidentImpactResponse toResponse(IncidentImpact entity);

    IncidentImpactSummary toSummary(IncidentImpact entity);
    
    List<IncidentImpactResponse> toResponseList(List<IncidentImpact> entities);

    @Mapping(target="incident.id", source="incidentId")

    void updateEntityFromRequest(UpdateIncidentImpactRequest request, @MappingTarget IncidentImpact entity);
}
