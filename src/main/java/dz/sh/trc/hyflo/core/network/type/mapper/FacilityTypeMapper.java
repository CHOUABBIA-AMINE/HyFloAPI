package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateFacilityTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.FacilityTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.FacilityType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface FacilityTypeMapper extends BaseMapper<CreateFacilityTypeRequest, UpdateFacilityTypeRequest, FacilityTypeResponse, FacilityTypeSummary, FacilityType> {
}