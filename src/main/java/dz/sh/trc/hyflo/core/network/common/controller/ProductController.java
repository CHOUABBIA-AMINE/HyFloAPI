package dz.sh.trc.hyflo.core.network.common.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductSummary;
import dz.sh.trc.hyflo.core.network.common.model.Product;
import dz.sh.trc.hyflo.core.network.common.service.ProductService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/products")
@Tag(name = "Product API", description = "Endpoints for managing hydrocarbon products")
public class ProductController extends BaseController<CreateProductRequest, UpdateProductRequest, ProductResponse, ProductSummary> {

    public ProductController(ProductService service) {
        super(service);
    }

    @Override
    protected Page<ProductSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}
