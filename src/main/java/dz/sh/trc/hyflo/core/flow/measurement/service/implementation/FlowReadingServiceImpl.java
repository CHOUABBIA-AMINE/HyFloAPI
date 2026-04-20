package dz.sh.trc.hyflo.core.flow.measurement.service.implementation;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.core.flow.measurement.dto.request.CreateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.FlowReadingFilterDTO;
import dz.sh.trc.hyflo.core.flow.measurement.dto.request.UpdateFlowReadingRequest;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingResponse;
import dz.sh.trc.hyflo.core.flow.measurement.dto.response.FlowReadingSummary;
import dz.sh.trc.hyflo.core.flow.measurement.mapper.FlowReadingMapper;
import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import dz.sh.trc.hyflo.core.flow.measurement.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.core.flow.measurement.service.FlowReadingService;
import dz.sh.trc.hyflo.core.flow.reference.model.DataSource;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSlot;
import dz.sh.trc.hyflo.core.flow.reference.model.ValidationStatus;
import dz.sh.trc.hyflo.core.flow.reference.repository.ValidationStatusRepository;
import dz.sh.trc.hyflo.core.flow.workflow.dto.request.WorkflowActionDTO;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowInstanceResponse;
import dz.sh.trc.hyflo.core.flow.workflow.repository.WorkflowInstanceRepository;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowService;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;

@Service
public class FlowReadingServiceImpl extends AbstractCrudService<CreateFlowReadingRequest, UpdateFlowReadingRequest, FlowReadingResponse, FlowReadingSummary, FlowReading> implements FlowReadingService {

    private final WorkflowService workflowService;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final ValidationStatusRepository validationStatusRepository;
    private final FlowReadingRepository flowReadingRepository;

    public FlowReadingServiceImpl(FlowReadingRepository repository,
                                  FlowReadingMapper mapper,
                                  ReferenceResolver referenceResolver,
                                  ApplicationEventPublisher eventPublisher,
                                  WorkflowService workflowService,
                                  WorkflowInstanceRepository workflowInstanceRepository,
                                  ValidationStatusRepository validationStatusRepository) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.workflowService = workflowService;
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.validationStatusRepository = validationStatusRepository;
        this.flowReadingRepository = repository;
    }

    @Override
    protected Class<FlowReading> getEntityClass() { return FlowReading.class; }

    // ========== LIFECYCLE HOOKS ==========

    @Override
    protected void beforeCreate(CreateFlowReadingRequest request, FlowReading entity) {
        // Resolve mandatory FK
        entity.setPipeline(referenceResolver.resolve(request.pipelineId(), Pipeline.class));

        // Resolve optional FKs
        if (request.dataSourceId() != null) {
            entity.setDataSource(referenceResolver.resolve(request.dataSourceId(), DataSource.class));
        }
        if (request.readingSlotId() != null) {
            entity.setReadingSlot(referenceResolver.resolve(request.readingSlotId(), ReadingSlot.class));
        }
        if (request.recordedById() != null) {
            entity.setRecordedBy(referenceResolver.resolve(request.recordedById(), Employee.class));
        }

        // Set initial validation status to DRAFT
        entity.setValidationStatus(resolveValidationStatus("DRAFT"));
        entity.setRecordedAt(LocalDateTime.now());
    }

    @Override
    protected void beforeUpdate(UpdateFlowReadingRequest request, FlowReading entity) {
        // Guard: only allow updates when reading is in DRAFT state
        requireState(entity, "DRAFT", "update");
    }

    // ========== WORKFLOW TRANSITIONS ==========

    @Override
    @Transactional
    public FlowReadingResponse submitReading(Long id, WorkflowActionDTO action) {
        FlowReading entity = findEntityById(id);

        // Guard: must be in DRAFT (or no workflow yet)
        if (entity.getWorkflowInstance() != null) {
            requireWorkflowState(entity, "DRAFT", "SUBMIT");
        }

        // Open a new workflow instance for this reading
        WorkflowInstanceResponse workflow = workflowService.initiateWorkflow(
                "FLOW_READING_VALIDATION", action.actorEmployeeId());

        entity.setWorkflowInstance(workflowInstanceRepository.findById(workflow.id()).orElseThrow());

        // Transition workflow from DRAFT → SUBMITTED
        workflowService.transitionState(workflow.id(),
                new WorkflowActionDTO("SUBMIT", action.comment(), action.actorEmployeeId()));

        // Sync entity state
        entity.setValidationStatus(resolveValidationStatus("SUBMITTED"));
        entity.setSubmittedAt(LocalDateTime.now());

        flowReadingRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public FlowReadingResponse validateReading(Long id, WorkflowActionDTO action) {
        FlowReading entity = findEntityById(id);
        requireWorkflowState(entity, "SUBMITTED", "VALIDATE");

        // Transition workflow: SUBMITTED → VALIDATED
        workflowService.transitionState(entity.getWorkflowInstance().getId(), action);

        // Sync entity state
        entity.setValidationStatus(resolveValidationStatus("VALIDATED"));

        flowReadingRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public FlowReadingResponse approveReading(Long id, WorkflowActionDTO action) {
        FlowReading entity = findEntityById(id);
        requireWorkflowState(entity, "VALIDATED", "APPROVE");

        // Transition workflow: VALIDATED → APPROVED
        workflowService.transitionState(entity.getWorkflowInstance().getId(), action);

        // Sync entity state
        entity.setValidationStatus(resolveValidationStatus("APPROVED"));
        entity.setValidatedAt(LocalDateTime.now());
        entity.setValidatedBy(referenceResolver.resolve(action.actorEmployeeId(), Employee.class));

        flowReadingRepository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public FlowReadingResponse rejectReading(Long id, WorkflowActionDTO action) {
        FlowReading entity = findEntityById(id);

        // Guard: reject is allowed from SUBMITTED or VALIDATED
        String currentState = getWorkflowStateCode(entity);
        if (!"SUBMITTED".equals(currentState) && !"VALIDATED".equals(currentState)) {
            throw new IllegalStateException(
                    "Cannot REJECT a FlowReading in state '" + currentState + "'. Must be SUBMITTED or VALIDATED.");
        }

        // Transition workflow → REJECTED
        workflowService.transitionState(entity.getWorkflowInstance().getId(), action);

        // Sync entity state — note: no validatedAt set on rejection
        entity.setValidationStatus(resolveValidationStatus("REJECTED"));
        entity.setValidatedBy(referenceResolver.resolve(action.actorEmployeeId(), Employee.class));

        flowReadingRepository.save(entity);
        return mapper.toResponse(entity);
    }

    // ========== QUERY ==========

    @Override
    @Transactional(readOnly = true)
    public Page<FlowReadingSummary> findByFilter(FlowReadingFilterDTO filter, Pageable pageable) {
        // Apply real filtering based on provided criteria
        if (filter.pipelineId() != null && filter.dateFrom() != null && filter.dateTo() != null) {
            return flowReadingRepository.findByPipelineIdAndReadingDateBetween(
                    filter.pipelineId(), filter.dateFrom(), filter.dateTo(), pageable)
                    .map(mapper::toSummary);
        }
        if (filter.validationStatusId() != null) {
            return flowReadingRepository.findByValidationStatusId(filter.validationStatusId(), pageable)
                    .map(mapper::toSummary);
        }
        if (filter.pipelineId() != null) {
            return flowReadingRepository.findByPipelineId(filter.pipelineId(), pageable)
                    .map(mapper::toSummary);
        }
        if (filter.dateFrom() != null && filter.dateTo() != null) {
            return flowReadingRepository.findByReadingDateBetween(
                    filter.dateFrom(), filter.dateTo(), pageable)
                    .map(mapper::toSummary);
        }
        // No filters → return all (paginated)
        return flowReadingRepository.findAll(pageable).map(mapper::toSummary);
    }

    // ========== INTERNAL HELPERS ==========

    /**
     * Resolves a ValidationStatus entity by its code.
     */
    private ValidationStatus resolveValidationStatus(String code) {
        return validationStatusRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("ValidationStatus not found: " + code));
    }

    /**
     * Gets the current workflow state code for a reading.
     */
    private String getWorkflowStateCode(FlowReading entity) {
        if (entity.getWorkflowInstance() == null || entity.getWorkflowInstance().getCurrentState() == null) {
            return "DRAFT";
        }
        return entity.getWorkflowInstance().getCurrentState().getCode();
    }

    /**
     * Guard: ensures the reading's validation status matches the expected code.
     */
    private void requireState(FlowReading entity, String expectedCode, String operation) {
        if (entity.getValidationStatus() != null && !expectedCode.equals(entity.getValidationStatus().getCode())) {
            throw new IllegalStateException(
                    "Cannot " + operation + " a FlowReading in validation status '" +
                    entity.getValidationStatus().getCode() + "'. Expected: " + expectedCode);
        }
    }

    /**
     * Guard: ensures the reading's workflow instance is in the expected state.
     */
    private void requireWorkflowState(FlowReading entity, String expectedState, String action) {
        String currentState = getWorkflowStateCode(entity);
        if (!expectedState.equals(currentState)) {
            throw new IllegalStateException(
                    "Cannot " + action + " a FlowReading with workflow state '" + currentState +
                    "'. Expected: " + expectedState);
        }
    }
}