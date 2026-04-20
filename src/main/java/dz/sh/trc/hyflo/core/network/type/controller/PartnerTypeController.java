package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdatePartnerTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.PartnerTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.PartnerType;
import dz.sh.trc.hyflo.core.network.type.service.PartnerTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/partner-types")
@Tag(name = "PartnerType API", description = "Endpoints for managing PartnerType")
public class PartnerTypeController extends BaseController<CreatePartnerTypeRequest, UpdatePartnerTypeRequest, PartnerTypeResponse, PartnerTypeSummary> {

    public PartnerTypeController(PartnerTypeService service) {
        super(service);
    }

    @Override
    protected Page<PartnerTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}