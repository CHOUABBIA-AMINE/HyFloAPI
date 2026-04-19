package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface VendorTypeService extends BaseService<CreateVendorTypeRequest, UpdateVendorTypeRequest, VendorTypeResponse, VendorTypeSummary> {
}