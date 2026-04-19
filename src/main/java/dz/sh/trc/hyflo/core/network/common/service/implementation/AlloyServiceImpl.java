package dz.sh.trc.hyflo.core.network.common.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloyResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloySummary;
import dz.sh.trc.hyflo.core.network.common.mapper.AlloyMapper;
import dz.sh.trc.hyflo.core.network.common.model.Alloy;
import dz.sh.trc.hyflo.core.network.common.repository.AlloyRepository;
import dz.sh.trc.hyflo.core.network.common.service.AlloyService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class AlloyServiceImpl extends AbstractCrudService<CreateAlloyRequest, UpdateAlloyRequest, AlloyResponse, AlloySummary, Alloy, Long> implements AlloyService {

    public AlloyServiceImpl(AlloyRepository repository, AlloyMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Alloy> getEntityClass() {
        return Alloy.class;
    }
}
