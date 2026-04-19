package dz.sh.trc.hyflo.core.network.type.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeSummary;
import dz.sh.trc.hyflo.core.network.type.mapper.TerminalTypeMapper;
import dz.sh.trc.hyflo.core.network.type.model.TerminalType;
import dz.sh.trc.hyflo.core.network.type.repository.TerminalTypeRepository;
import dz.sh.trc.hyflo.core.network.type.service.TerminalTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class TerminalTypeServiceImpl extends AbstractCrudService<CreateTerminalTypeRequest, UpdateTerminalTypeRequest, TerminalTypeResponse, TerminalTypeSummary, TerminalType, Long> implements TerminalTypeService {

    public TerminalTypeServiceImpl(TerminalTypeRepository repository, TerminalTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<TerminalType> getEntityClass() {
        return TerminalType.class;
    }
}