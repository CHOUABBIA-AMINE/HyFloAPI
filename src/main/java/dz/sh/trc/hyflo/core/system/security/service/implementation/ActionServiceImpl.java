package dz.sh.trc.hyflo.core.system.security.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionSummary;
import dz.sh.trc.hyflo.core.system.security.mapper.ActionMapper;
import dz.sh.trc.hyflo.core.system.security.model.Action;
import dz.sh.trc.hyflo.core.system.security.repository.ActionRepository;
import dz.sh.trc.hyflo.core.system.security.service.ActionService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class ActionServiceImpl extends AbstractCrudService<CreateActionRequest, UpdateActionRequest, ActionResponse, ActionSummary, Action, Long> implements ActionService {

    public ActionServiceImpl(ActionRepository repository, ActionMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }

    @Override
    protected Class<Action> getEntityClass() {
        return Action.class;
    }
}
