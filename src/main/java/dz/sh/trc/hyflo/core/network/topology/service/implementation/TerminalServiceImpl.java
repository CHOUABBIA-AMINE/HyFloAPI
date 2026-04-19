package dz.sh.trc.hyflo.core.network.topology.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalSummary;
import dz.sh.trc.hyflo.core.network.topology.mapper.TerminalMapper;
import dz.sh.trc.hyflo.core.network.topology.model.Terminal;
import dz.sh.trc.hyflo.core.network.topology.repository.TerminalRepository;
import dz.sh.trc.hyflo.core.network.topology.service.TerminalService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class TerminalServiceImpl extends AbstractCrudService<CreateTerminalRequest, UpdateTerminalRequest, TerminalResponse, TerminalSummary, Terminal> implements TerminalService {

    public TerminalServiceImpl(TerminalRepository repository, TerminalMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Terminal> getEntityClass() {
        return Terminal.class;
    }
}