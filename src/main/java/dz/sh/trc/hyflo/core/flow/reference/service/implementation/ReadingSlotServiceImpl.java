package dz.sh.trc.hyflo.core.flow.reference.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotSummary;
import dz.sh.trc.hyflo.core.flow.reference.mapper.ReadingSlotMapper;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSlot;
import dz.sh.trc.hyflo.core.flow.reference.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.core.flow.reference.service.ReadingSlotService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ReadingSlotServiceImpl extends AbstractCrudService<CreateReadingSlotRequest, UpdateReadingSlotRequest, ReadingSlotResponse, ReadingSlotSummary, ReadingSlot> implements ReadingSlotService {

    public ReadingSlotServiceImpl(ReadingSlotRepository repository, ReadingSlotMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<ReadingSlot> getEntityClass() {
        return ReadingSlot.class;
    }
}