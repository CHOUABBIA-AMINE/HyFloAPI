package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateVendorTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.VendorTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.VendorTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.VendorType;
import dz.sh.trc.hyflo.core.network.type.repository.VendorTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.VendorTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class VendorTypeServiceImpl extends AbstractCrudService<CreateVendorTypeRequest, UpdateVendorTypeRequest, VendorTypeResponse, VendorTypeSummary, VendorType, Long> implements VendorTypeService {

    public VendorTypeServiceImpl(VendorTypeRepository repository, VendorTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<VendorType> getEntityClass() {
        return VendorType.class;
    }
}