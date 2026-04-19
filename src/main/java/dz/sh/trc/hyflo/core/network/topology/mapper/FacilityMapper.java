package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateFacilityRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilityResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.FacilitySummary;
import dz.sh.trc.hyflo.core.network.topology.model.Facility;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface FacilityMapper extends BaseMapper<CreateFacilityRequest, UpdateFacilityRequest, FacilityResponse, FacilitySummary, Facility> {
}