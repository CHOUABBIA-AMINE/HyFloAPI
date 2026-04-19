package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.ReadingSourceNatureMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSourceNature;
import dz.sh.trc.hyflo.core.flow.reference.repository.ReadingSourceNatureRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.ReadingSourceNatureService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ReadingSourceNatureServiceImpl extends AbstractCrudService<CreateReadingSourceNatureRequest, UpdateReadingSourceNatureRequest, ReadingSourceNatureResponse, ReadingSourceNatureSummary, ReadingSourceNature, Long> implements ReadingSourceNatureService {

    public ReadingSourceNatureServiceImpl(ReadingSourceNatureRepository repository, ReadingSourceNatureMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ReadingSourceNature> getEntityClass() {
        return ReadingSourceNature.class;
    }
}