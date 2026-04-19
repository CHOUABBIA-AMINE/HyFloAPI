package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.PartnerType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface PartnerTypeMapper extends BaseMapper<CreatePartnerTypeRequest, UpdatePartnerTypeRequest, PartnerTypeResponse, PartnerTypeSummary, PartnerType> {
}