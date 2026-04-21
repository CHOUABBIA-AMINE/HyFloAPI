package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.VendorTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/network/vendor-types")
@Tag(name = "VendorType API", description = "Endpoints for managing VendorType")
public class VendorTypeController extends BaseController<CreateVendorTypeRequest, UpdateVendorTypeRequest, VendorTypeResponse, VendorTypeSummary> {

    public VendorTypeController(VendorTypeService service) {
        super(service);
    }

    @Override
    protected Page<VendorTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}