package dz.sh.trc.hyflo.core.network.common.service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ProductService extends BaseService<CreateProductRequest, UpdateProductRequest, ProductResponse, ProductSummary> {
}
