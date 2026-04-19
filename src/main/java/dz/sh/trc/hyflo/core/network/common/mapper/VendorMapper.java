package dz.sh.trc.hyflo.core.network.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorSummary;
import dz.sh.trc.hyflo.core.network.common.model.Vendor;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface VendorMapper extends BaseMapper<CreateVendorRequest, UpdateVendorRequest, VendorResponse, VendorSummary, Vendor> {

    @Override
    @Mapping(target = "vendorType", ignore = true)
    @Mapping(target = "country", ignore = true)
    Vendor toEntity(CreateVendorRequest dto);

    @Override
    @Mapping(target = "vendorTypeId", source = "vendorType.id")
    @Mapping(target = "vendorTypeDesignationFr", source = "vendorType.designationFr")
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryDesignationFr", source = "country.designationFr")
    VendorResponse toResponse(Vendor entity);

    @Override
    @Mapping(target = "vendorTypeDesignationFr", source = "vendorType.designationFr")
    VendorSummary toSummary(Vendor entity);

    @Override
    @Mapping(target = "vendorType", ignore = true)
    @Mapping(target = "country", ignore = true)
    void updateEntityFromRequest(UpdateVendorRequest dto, @MappingTarget Vendor entity);
}
