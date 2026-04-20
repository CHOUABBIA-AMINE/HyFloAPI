package dz.sh.trc.hyflo.core.flow.planning.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.flow.planning.dto.*;
import dz.sh.trc.hyflo.core.flow.planning.mapper.FlowPlanMapper;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowPlan;
import dz.sh.trc.hyflo.core.flow.planning.repository.FlowPlanRepository;
import dz.sh.trc.hyflo.core.flow.planning.service.FlowPlanService;
import dz.sh.trc.hyflo.core.flow.reference.model.PlanStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.PlanStatusRepository;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowPlanServiceImpl extends AbstractCrudService<CreateFlowPlanRequest, UpdateFlowPlanRequest, FlowPlanResponse, FlowPlanSummary, FlowPlan> implements FlowPlanService {

    private final PlanStatusRepository planStatusRepository;
    private final FlowPlanRepository flowPlanRepository;

    public FlowPlanServiceImpl(FlowPlanRepository repository,
                               FlowPlanMapper mapper,
                               ReferenceResolver referenceResolver,
                               ApplicationEventPublisher eventPublisher,
                               PlanStatusRepository planStatusRepository) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.planStatusRepository = planStatusRepository;
        this.flowPlanRepository = repository;
    }

    @Override
    protected Class<FlowPlan> getEntityClass() {
        return FlowPlan.class;
    }

    @Override
    protected void beforeCreate(CreateFlowPlanRequest request, FlowPlan entity) {
        // Resolve mandatory FKs
        if (request.getPipelineId() != null) {
            entity.setPipeline(referenceResolver.resolve(request.getPipelineId(), Pipeline.class));
        }

        // Auto-set initial status to DRAFT
        PlanStatus draftStatus = planStatusRepository.findByCode("DRAFT")
                .orElseThrow(() -> new ResourceNotFoundException("PlanStatus 'DRAFT' not found. Please seed reference data."));
        entity.setStatus(draftStatus);

        // Resolve optional FKs
        if (request.getSubmittedById() != null) {
            entity.setSubmittedBy(referenceResolver.resolve(request.getSubmittedById(), Employee.class));
        }
        if (request.getRevisedFromId() != null) {
            entity.setRevisedFrom(referenceResolver.resolve(request.getRevisedFromId(), FlowPlan.class));
        }
    }

    @Override
    protected void beforeUpdate(UpdateFlowPlanRequest request, FlowPlan entity) {
        // Guard: only DRAFT plans can be modified
        String currentStatus = entity.getStatus() != null ? entity.getStatus().getCode() : "UNKNOWN";
        if (!"DRAFT".equals(currentStatus)) {
            throw new IllegalStateException(
                    "Cannot update plan (id=" + entity.getId() + "): current status is '" + currentStatus + "', must be 'DRAFT'");
        }
    }

    @Override
    @Transactional
    public FlowPlanResponse approvePlan(Long id, Long approverEmployeeId) {
        FlowPlan plan = findEntityById(id);

        // Guard: can only approve a DRAFT plan
        String currentStatus = plan.getStatus() != null ? plan.getStatus().getCode() : "UNKNOWN";
        if (!"DRAFT".equals(currentStatus)) {
            throw new IllegalStateException(
                    "Cannot approve plan (id=" + id + "): current status is '" + currentStatus + "', expected 'DRAFT'");
        }

        // Transition status: DRAFT → APPROVED
        PlanStatus approvedStatus = planStatusRepository.findByCode("APPROVED")
                .orElseThrow(() -> new ResourceNotFoundException("PlanStatus 'APPROVED' not found"));

        plan.setStatus(approvedStatus);
        plan.setApprovedAt(LocalDateTime.now());
        plan.setApprovedBy(referenceResolver.resolve(approverEmployeeId, Employee.class));

        flowPlanRepository.save(plan);
        return mapper.toResponse(plan);
    }
}
