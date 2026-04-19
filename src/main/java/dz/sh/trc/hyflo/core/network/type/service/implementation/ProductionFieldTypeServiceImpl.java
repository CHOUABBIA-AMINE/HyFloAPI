package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.ProductionFieldTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.ProductionFieldType;
import dz.sh.trc.hyflo.core.network.type.repository.ProductionFieldTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.ProductionFieldTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ProductionFieldTypeServiceImpl extends AbstractCrudService<CreateProductionFieldTypeRequest, UpdateProductionFieldTypeRequest, ProductionFieldTypeResponse, ProductionFieldTypeSummary, ProductionFieldType, Long> implements ProductionFieldTypeService {

    public ProductionFieldTypeServiceImpl(ProductionFieldTypeRepository repository, ProductionFieldTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ProductionFieldType> getEntityClass() {
        return ProductionFieldType.class;
    }
}