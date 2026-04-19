package dz.sh.trc.hyflo.core.network.common.service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface VendorService extends BaseService<CreateVendorRequest, UpdateVendorRequest, VendorResponse, VendorSummary> {
}
