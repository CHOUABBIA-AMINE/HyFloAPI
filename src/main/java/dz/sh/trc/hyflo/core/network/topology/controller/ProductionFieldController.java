package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldSummary;
import dz.sh.trc.hyflo.core.network.topology.service.ProductionFieldService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/production-fields")
@Tag(name = "ProductionField API", description = "Endpoints for managing ProductionField")
public class ProductionFieldController extends BaseController<CreateProductionFieldRequest, UpdateProductionFieldRequest, ProductionFieldResponse, ProductionFieldSummary> {

    public ProductionFieldController(ProductionFieldService service) {
        super(service);
    }

    @Override
    protected Page<ProductionFieldSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}