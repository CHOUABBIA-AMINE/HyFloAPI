package dz.sh.trc.hyflo.core.network.common.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdatePartnerRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.PartnerSummary;
import dz.sh.trc.hyflo.core.network.common.model.Partner;
import dz.sh.trc.hyflo.core.network.common.service.PartnerService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/partners")
@Tag(name = "Partner API", description = "Endpoints for managing business partners")
public class PartnerController extends BaseController<CreatePartnerRequest, UpdatePartnerRequest, PartnerResponse, PartnerSummary> {

    public PartnerController(PartnerService service) {
        super(service);
    }

    @Override
    protected Page<PartnerSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
