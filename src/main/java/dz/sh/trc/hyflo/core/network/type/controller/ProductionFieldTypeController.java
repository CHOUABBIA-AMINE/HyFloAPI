package dz.sh.trc.hyflo.core.network.type.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeSummary;
import dz.sh.trc.hyflo.core.network.type.service.ProductionFieldTypeService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/production-field-types")
@Tag(name = "ProductionFieldType API", description = "Endpoints for managing ProductionFieldType")
public class ProductionFieldTypeController extends BaseController<CreateProductionFieldTypeRequest, UpdateProductionFieldTypeRequest, ProductionFieldTypeResponse, ProductionFieldTypeSummary> {

    public ProductionFieldTypeController(ProductionFieldTypeService service) {
        super(service);
    }

    @Override
    protected Page<ProductionFieldTypeSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}