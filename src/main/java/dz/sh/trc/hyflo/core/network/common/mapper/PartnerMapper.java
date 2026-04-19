package dz.sh.trc.hyflo.core.network.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerSummary;
import dz.sh.trc.hyflo.core.network.common.model.Partner;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface PartnerMapper extends BaseMapper<CreatePartnerRequest, UpdatePartnerRequest, PartnerResponse, PartnerSummary, Partner> {

    @Override
    @Mapping(target = "partnerType", ignore = true)
    @Mapping(target = "country", ignore = true)
    Partner toEntity(CreatePartnerRequest dto);

    @Override
    @Mapping(target = "partnerTypeId", source = "partnerType.id")
    @Mapping(target = "partnerTypeDesignationFr", source = "partnerType.designationFr")
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryDesignationFr", source = "country.designationFr")
    PartnerResponse toResponse(Partner entity);

    @Override
    @Mapping(target = "partnerTypeDesignationFr", source = "partnerType.designationFr")
    PartnerSummary toSummary(Partner entity);

    @Override
    @Mapping(target = "partnerType", ignore = true)
    @Mapping(target = "country", ignore = true)
    void updateEntityFromRequest(UpdatePartnerRequest dto, @MappingTarget Partner entity);
}
