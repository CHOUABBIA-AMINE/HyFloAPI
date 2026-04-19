package dz.sh.trc.hyflo.core.network.common.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.common.dto.request.CreateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusSummary;
import dz.sh.trc.hyflo.core.network.common.mapper.OperationalStatusMapper;
import dz.sh.trc.hyflo.core.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.core.network.common.repository.OperationalStatusRepository;
import dz.sh.trc.hyflo.core.network.common.service.OperationalStatusService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class OperationalStatusServiceImpl extends AbstractCrudService<CreateOperationalStatusRequest, UpdateOperationalStatusRequest, OperationalStatusResponse, OperationalStatusSummary, OperationalStatus> implements OperationalStatusService {

    public OperationalStatusServiceImpl(OperationalStatusRepository repository, OperationalStatusMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<OperationalStatus> getEntityClass() {
        return OperationalStatus.class;
    }
}
