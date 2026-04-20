package dz.sh.trc.hyflo.core.flow.workflow.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dz.sh.trc.hyflo.core.flow.workflow.dto.request.WorkflowActionDTO;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowInstanceResponse;
import dz.sh.trc.hyflo.core.flow.workflow.dto.response.WorkflowTransitionDTO;
import dz.sh.trc.hyflo.core.flow.workflow.mapper.WorkflowInstanceMapper;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowState;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowTargetType;
import dz.sh.trc.hyflo.core.flow.workflow.repository.WorkflowInstanceRepository;
import dz.sh.trc.hyflo.core.flow.workflow.repository.WorkflowStateRepository;
import dz.sh.trc.hyflo.core.flow.workflow.repository.WorkflowTargetTypeRepository;
import dz.sh.trc.hyflo.core.flow.workflow.service.WorkflowService;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.platform.audit.service.AuditService;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final WorkflowStateRepository workflowStateRepository;
    private final WorkflowTargetTypeRepository workflowTargetTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkflowInstanceMapper mapper;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final AuditService auditService;

    /**
     * State transition validation matrix.
     * Key = current state code, Value = set of allowed actions from that state.
     */
    private static final Map<String, Set<String>> ALLOWED_TRANSITIONS = Map.of(
            "DRAFT",      Set.of("SUBMIT", "CANCEL"),
            "SUBMITTED",  Set.of("VALIDATE", "REJECT", "CANCEL"),
            "VALIDATED",  Set.of("APPROVE", "REJECT", "CANCEL"),
            "APPROVED",   Set.of(),   // terminal
            "REJECTED",   Set.of(),   // terminal
            "CANCELLED",  Set.of()    // terminal
    );

    public WorkflowServiceImpl(WorkflowInstanceRepository workflowInstanceRepository,
                               WorkflowStateRepository workflowStateRepository,
                               WorkflowTargetTypeRepository workflowTargetTypeRepository,
                               EmployeeRepository employeeRepository,
                               WorkflowInstanceMapper mapper,
                               ObjectMapper objectMapper,
                               ApplicationEventPublisher eventPublisher,
                               AuditService auditService) {
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.workflowStateRepository = workflowStateRepository;
        this.workflowTargetTypeRepository = workflowTargetTypeRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public WorkflowInstanceResponse initiateWorkflow(String targetTypeCode, Long initiatorEmployeeId) {
        WorkflowTargetType targetType = workflowTargetTypeRepository.findByCode(targetTypeCode)
                .orElseThrow(() -> new ResourceNotFoundException("WorkflowTargetType not found: " + targetTypeCode));

        WorkflowState draftState = workflowStateRepository.findByCode("DRAFT")
                .orElseThrow(() -> new ResourceNotFoundException("WorkflowState DRAFT not found"));

        Employee initiator = null;
        if (initiatorEmployeeId != null) {
            initiator = employeeRepository.findById(initiatorEmployeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + initiatorEmployeeId));
        }

        WorkflowInstance instance = new WorkflowInstance();
        instance.setTargetType(targetType);
        instance.setCurrentState(draftState);
        instance.setStartedAt(LocalDateTime.now());
        instance.setInitiatedBy(initiator);
        instance.setLastActor(initiator);
        instance.setUpdatedAt(LocalDateTime.now());
        
        List<WorkflowTransitionDTO> history = new ArrayList<>();
        history.add(new WorkflowTransitionDTO(LocalDateTime.now(), null, "DRAFT", "INITIATE", initiator != null ? initiator.getFullNameLt() : "SYSTEM", "Workflow initiated"));
        
        try {
            instance.setHistory(objectMapper.writeValueAsString(history));
        } catch (JsonProcessingException e) {
            instance.setHistory("[]");
        }

        instance = workflowInstanceRepository.save(instance);

        // Publish event for workflow initiation
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(instance.getId())
                .entityType(instance.getTargetType().getCode())
                .fromState(null)
                .toState("DRAFT")
                .action("INITIATE")
                .actorEmployeeId(initiatorEmployeeId)
                .build());

        // Audit log
        auditService.logWorkflow("WorkflowInstance", instance.getId(), null, "DRAFT", "INITIATE", initiatorEmployeeId);

        return mapper.toResponse(instance);
    }

    @Override
    @Transactional
    public WorkflowInstanceResponse transitionState(Long instanceId, WorkflowActionDTO actionDTO) {
        WorkflowInstance instance = workflowInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkflowInstance not found: " + instanceId));

        // --- State transition validation ---
        String currentStateCode = instance.getCurrentState().getCode();
        String action = actionDTO.action().toUpperCase();

        validateTransition(currentStateCode, action);

        Employee actor = employeeRepository.findById(actionDTO.actorEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + actionDTO.actorEmployeeId()));

        String nextStateCode = determineNextState(action);
        
        WorkflowState nextState = workflowStateRepository.findByCode(nextStateCode)
                .orElseThrow(() -> new ResourceNotFoundException("WorkflowState not found: " + nextStateCode));

        instance.setCurrentState(nextState);
        instance.setLastActor(actor);
        instance.setComment(actionDTO.comment());
        instance.setUpdatedAt(LocalDateTime.now());
        
        if (Boolean.TRUE.equals(nextState.getTerminal())) {
            instance.setCompletedAt(LocalDateTime.now());
        }

        List<WorkflowTransitionDTO> history = getWorkflowHistory(instanceId);
        history.add(new WorkflowTransitionDTO(LocalDateTime.now(), currentStateCode, nextStateCode, action, actor.getFullNameLt(), actionDTO.comment()));
        
        try {
            instance.setHistory(objectMapper.writeValueAsString(history));
        } catch (JsonProcessingException e) {
            // keep old history
        }

        instance = workflowInstanceRepository.save(instance);

        // Publish event for state transition
        eventPublisher.publishEvent(WorkflowTransitionEvent.builder()
                .entityId(instance.getId())
                .entityType(instance.getTargetType().getCode())
                .fromState(currentStateCode)
                .toState(nextStateCode)
                .action(action)
                .actorEmployeeId(actionDTO.actorEmployeeId())
                .build());

        // Audit log
        auditService.logWorkflow("WorkflowInstance", instance.getId(), currentStateCode, nextStateCode, action, actionDTO.actorEmployeeId());

        return mapper.toResponse(instance);
    }

    /**
     * Validates that the requested action is allowed from the current state.
     * Throws IllegalStateException if the transition is invalid.
     */
    private void validateTransition(String currentStateCode, String action) {
        Set<String> allowed = ALLOWED_TRANSITIONS.get(currentStateCode);
        if (allowed == null || !allowed.contains(action)) {
            throw new IllegalStateException(
                    "Invalid workflow transition: cannot perform '" + action + "' from state '" + currentStateCode + "'");
        }
    }

    private String determineNextState(String action) {
        return switch (action) {
            case "SUBMIT" -> "SUBMITTED";
            case "VALIDATE" -> "VALIDATED";
            case "APPROVE" -> "APPROVED";
            case "REJECT" -> "REJECTED";
            case "CANCEL" -> "CANCELLED";
            default -> throw new IllegalArgumentException("Unknown workflow action: " + action);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowTransitionDTO> getWorkflowHistory(Long instanceId) {
        WorkflowInstance instance = workflowInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkflowInstance not found: " + instanceId));
        
        if (instance.getHistory() == null || instance.getHistory().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(instance.getHistory(), new TypeReference<List<WorkflowTransitionDTO>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WorkflowInstanceResponse getById(Long instanceId) {
        WorkflowInstance instance = workflowInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkflowInstance not found: " + instanceId));
        return mapper.toResponse(instance);
    }
}
