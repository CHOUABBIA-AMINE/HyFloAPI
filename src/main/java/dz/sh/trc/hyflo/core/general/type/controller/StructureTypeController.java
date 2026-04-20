package dz.sh.trc.hyflo.core.general.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.general.type.dto.request.CreateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.request.UpdateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeResponse;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeSummary;
import dz.sh.trc.hyflo.core.general.type.model.StructureType;
import dz.sh.trc.hyflo.core.general.type.service.StructureTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/general/types/structures")
@Tag(name = "Structure Type API", description = "Endpoints for managing structure types")
public class StructureTypeController extends BaseController<CreateStructureTypeRequest, UpdateStructureTypeRequest, StructureTypeResponse, StructureTypeSummary> {

    public StructureTypeController(StructureTypeService service) {
        super(service);
    }

    @Override
    protected Page<StructureTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
