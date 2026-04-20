package dz.sh.trc.hyflo.core.general.organization.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureSummary;
import dz.sh.trc.hyflo.core.general.organization.service.StructureService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/general/structures")
@Tag(name = "Structure API", description = "Endpoints for managing organizational structures")
public class StructureController extends BaseController<CreateStructureRequest, UpdateStructureRequest, StructureResponse, StructureSummary> {

    public StructureController(StructureService service) {
        super(service);
    }

    @Override
    protected Page<StructureSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
