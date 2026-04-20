package dz.sh.trc.hyflo.core.network.common.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorSummary;
import dz.sh.trc.hyflo.core.network.common.model.Vendor;
import dz.sh.trc.hyflo.core.network.common.service.VendorService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/vendors")
@Tag(name = "Vendor API", description = "Endpoints for managing vendors and contractors")
public class VendorController extends BaseController<CreateVendorRequest, UpdateVendorRequest, VendorResponse, VendorSummary> {

    public VendorController(VendorService service) {
        super(service);
    }

    @Override
    protected Page<VendorSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
