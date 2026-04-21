package dz.sh.trc.hyflo.core.flow.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeSummary;
import dz.sh.trc.hyflo.core.flow.type.service.OperationTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/flow/operation-types")
@Tag(name = "OperationType API", description = "Endpoints for managing OperationType")
public class OperationTypeController extends BaseController<CreateOperationTypeRequest, UpdateOperationTypeRequest, OperationTypeResponse, OperationTypeSummary> {

    public OperationTypeController(OperationTypeService service) {
        super(service);
    }

    @Override
    protected Page<OperationTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}