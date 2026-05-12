package dz.sh.trc.hyflo.core.flow.workflow.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowStateRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowStateSummary;
import dz.sh.trc.hyflo.core.flow.workflow.mapper.WorkflowStateMapper;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowState;
import dz.sh.trc.hyflo.core.flow.workflow.repository.WorkflowStateRepository;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowStateService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class WorkflowStateServiceImpl extends AbstractCrudService<CreateWorkflowStateRequest, UpdateWorkflowStateRequest, WorkflowStateResponse, WorkflowStateSummary, WorkflowState> implements WorkflowStateService {
    public WorkflowStateServiceImpl(WorkflowStateRepository repository, WorkflowStateMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }
    @Override
    protected Class<WorkflowState> getEntityClass() { return WorkflowState.class; }
}