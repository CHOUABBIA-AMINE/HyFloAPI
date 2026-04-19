package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.QualityFlagMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.QualityFlag;
import dz.sh.trc.hyflo.core.flow.reference.repository.QualityFlagRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.QualityFlagService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class QualityFlagServiceImpl extends AbstractCrudService<CreateQualityFlagRequest, UpdateQualityFlagRequest, QualityFlagResponse, QualityFlagSummary, QualityFlag> implements QualityFlagService {

    public QualityFlagServiceImpl(QualityFlagRepository repository, QualityFlagMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<QualityFlag> getEntityClass() {
        return QualityFlag.class;
    }
}