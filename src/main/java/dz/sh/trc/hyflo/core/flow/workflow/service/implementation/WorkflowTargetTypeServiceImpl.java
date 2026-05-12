package dz.sh.trc.hyflo.core.flow.workflow.service.implementation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.CreateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.UpdateWorkflowTargetTypeRequest;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTargetTypeSummary;
import dz.sh.trc.hyflo.core.flow.workflow.mapper.WorkflowTargetTypeMapper;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowTargetType;
import dz.sh.trc.hyflo.core.flow.workflow.repository.WorkflowTargetTypeRepository;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowTargetTypeService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class WorkflowTargetTypeServiceImpl extends AbstractCrudService<CreateWorkflowTargetTypeRequest, UpdateWorkflowTargetTypeRequest, WorkflowTargetTypeResponse, WorkflowTargetTypeSummary, WorkflowTargetType> implements WorkflowTargetTypeService {
    public WorkflowTargetTypeServiceImpl(WorkflowTargetTypeRepository repository, WorkflowTargetTypeMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
    }
    @Override
    protected Class<WorkflowTargetType> getEntityClass() { return WorkflowTargetType.class; }
}