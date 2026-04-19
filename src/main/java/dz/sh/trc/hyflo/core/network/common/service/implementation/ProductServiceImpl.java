package dz.sh.trc.hyflo.core.network.common.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateProductRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.ProductSummary;
import dz.sh.trc.hyflo.core.network.common.mapper.ProductMapper;
import dz.sh.trc.hyflo.core.network.common.model.Product;
import dz.sh.trc.hyflo.core.network.common.repository.ProductRepository;
import dz.sh.trc.hyflo.core.network.common.service.ProductService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ProductServiceImpl extends AbstractCrudService<CreateProductRequest, UpdateProductRequest, ProductResponse, ProductSummary, Product> implements ProductService {

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Product> getEntityClass() {
        return Product.class;
    }
}
