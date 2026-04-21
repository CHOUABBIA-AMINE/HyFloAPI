package dz.sh.trc.hyflo.core.system.audit.service.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dz.sh.trc.hyflo.core.system.audit.dto.request.AuditFilterDTO;
import dz.sh.trc.hyflo.core.system.audit.dto.request.CreateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.request.UpdateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditResponse;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditSummary;
import dz.sh.trc.hyflo.core.system.audit.mapper.AuditMapper;
import dz.sh.trc.hyflo.core.system.audit.model.Audited;
import dz.sh.trc.hyflo.core.system.audit.repository.AuditedRepository;
import dz.sh.trc.hyflo.core.system.audit.service.AuditService;
import dz.sh.trc.hyflo.platform.kernel.AbstractCrudService;
import dz.sh.trc.hyflo.platform.kernel.ReferenceResolver;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditServiceImpl extends AbstractCrudService<CreateAuditRequest, UpdateAuditRequest, AuditResponse, AuditSummary, Audited> implements AuditService {

    private final AuditedRepository auditedRepository;
    private final ObjectMapper objectMapper;

    public AuditServiceImpl(AuditedRepository repository, ObjectMapper objectMapper, AuditMapper mapper, ReferenceResolver referenceResolver, ApplicationEventPublisher eventPublisher) {
        super(repository, mapper, referenceResolver, eventPublisher);
        this.auditedRepository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Class<Audited> getEntityClass() {
        return Audited.class;
    }

    @Override
    public AuditResponse create(CreateAuditRequest request) {
        throw new UnsupportedOperationException("Audit records are read-only and created internally");
    }

    @Override
    public AuditResponse update(Long id, UpdateAuditRequest request) {
        throw new UnsupportedOperationException("Audit records are read-only");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Audit records are read-only and cannot be deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditSummary> search(AuditFilterDTO filter, Pageable pageable) {
        Specification<Audited> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filter.entityName() != null && !filter.entityName().isBlank()) {
                predicates.add(cb.equal(root.get("entityName"), filter.entityName()));
            }
            if (filter.action() != null && !filter.action().isBlank()) {
                predicates.add(cb.equal(root.get("action"), filter.action()));
            }
            if (filter.username() != null && !filter.username().isBlank()) {
                predicates.add(cb.equal(root.get("username"), filter.username()));
            }
            if (filter.status() != null && !filter.status().isBlank()) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }
            if (filter.dateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.dateFrom()));
            }
            if (filter.dateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.dateTo()));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return auditedRepository.findAll(spec, pageable).map(mapper::toSummary);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName, Long entityId, String action, String description) {
        log(entityName, entityId, action, description, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logWorkflow(String entityName, Long entityId, String fromState, String toState, String action, Long actorId) {
        String description = String.format("Workflow transition for %s [%d]: %s -> %s (Action: %s)",
                entityName, entityId, fromState != null ? fromState : "START", toState, action);

        Map<String, Object> metadata = Map.of(
                "fromState", fromState != null ? fromState : "",
                "toState", toState,
                "workflowAction", action,
                "actorId", actorId != null ? actorId : ""
        );

        log(entityName, entityId, action, description, metadata);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityName, Long entityId, String action, String description, Map<String, Object> metadata) {
        try {
            String metadataJson = null;
            if (metadata != null) {
                metadataJson = objectMapper.writeValueAsString(metadata);
            }

            Audited audit = Audited.builder()
                    .entityName(entityName)
                    .entityId(entityId)
                    .action(action.toUpperCase())
                    .description(description)
                    .timestamp(new Date())
                    .status("SUCCESS")
                    .metadata(metadataJson)
                    .module(determineModule(entityName))
                    .build();

            auditedRepository.save(audit);
        } catch (Exception e) {
            // Audit failure should never block the main transaction
            log.error("Failed to persist audit log for entity={} action={}", entityName, action, e);
        }
    }

    private String determineModule(String entityName) {
        if (entityName.contains("Flow")) return "FLOW";
        if (entityName.contains("Pipeline") || entityName.contains("Infrastructure")) return "NETWORK";
        if (entityName.contains("User") || entityName.contains("Role")) return "SECURITY";
        return "SYSTEM";
    }
}
