package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.VendorType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface VendorTypeMapper extends BaseMapper<CreateVendorTypeRequest, UpdateVendorTypeRequest, VendorTypeResponse, VendorTypeSummary, VendorType> {
}