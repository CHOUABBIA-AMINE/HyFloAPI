package dz.sh.trc.hyflo.core.network.common.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductSummary;
import dz.sh.trc.hyflo.core.network.common.model.Product;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<CreateProductRequest, UpdateProductRequest, ProductResponse, ProductSummary, Product> {
}
