package dz.sh.trc.hyflo.core.network.common.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.general.localization.model.Country;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateVendorRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.VendorSummary;
import dz.sh.trc.hyflo.core.network.common.mapper.VendorMapper;
import dz.sh.trc.hyflo.core.network.common.model.Vendor;
import dz.sh.trc.hyflo.core.network.common.repository.VendorRepository;
import dz.sh.trc.hyflo.core.network.common.service.VendorService;
import dz.sh.trc.hyflo.core.network.type.model.VendorType;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class VendorServiceImpl extends AbstractCrudService<CreateVendorRequest, UpdateVendorRequest, VendorResponse, VendorSummary, Vendor, Long> implements VendorService {

    public VendorServiceImpl(VendorRepository repository, VendorMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Vendor> getEntityClass() {
        return Vendor.class;
    }

    @Override
    protected void beforeCreate(CreateVendorRequest request, Vendor entity) {
        entity.setVendorType(referenceResolver.resolve(request.vendorTypeId(), VendorType.class));
        entity.setCountry(referenceResolver.resolve(request.countryId(), Country.class));
    }

    @Override
    protected void beforeUpdate(UpdateVendorRequest request, Vendor entity) {
        if (request.vendorTypeId() != null) {
            entity.setVendorType(referenceResolver.resolve(request.vendorTypeId(), VendorType.class));
        }
        if (request.countryId() != null) {
            entity.setCountry(referenceResolver.resolve(request.countryId(), Country.class));
        }
    }
}
