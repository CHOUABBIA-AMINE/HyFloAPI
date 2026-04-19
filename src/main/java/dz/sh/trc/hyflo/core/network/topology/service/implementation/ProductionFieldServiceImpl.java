package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.ProductionFieldMapper;
import dz.sh.trc.hyflo.core.network.topology.model.ProductionField;
import dz.sh.trc.hyflo.core.network.topology.repository.ProductionFieldRepository;
import dz.sh.trc.hyflo.core.network.topology.service.ProductionFieldService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ProductionFieldServiceImpl extends AbstractCrudService<CreateProductionFieldRequest, UpdateProductionFieldRequest, ProductionFieldResponse, ProductionFieldSummary, ProductionField, Long> implements ProductionFieldService {

    public ProductionFieldServiceImpl(ProductionFieldRepository repository, ProductionFieldMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ProductionField> getEntityClass() {
        return ProductionField.class;
    }
}