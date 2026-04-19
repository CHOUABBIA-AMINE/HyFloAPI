package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.AlertStatusMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.AlertStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.AlertStatusRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.AlertStatusService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class AlertStatusServiceImpl extends AbstractCrudService<CreateAlertStatusRequest, UpdateAlertStatusRequest, AlertStatusResponse, AlertStatusSummary, AlertStatus, Long> implements AlertStatusService {

    public AlertStatusServiceImpl(AlertStatusRepository repository, AlertStatusMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<AlertStatus> getEntityClass() {
        return AlertStatus.class;
    }
}