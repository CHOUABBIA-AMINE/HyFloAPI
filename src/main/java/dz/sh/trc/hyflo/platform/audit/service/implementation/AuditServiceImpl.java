package dz.sh.trc.hyflo.platform.audit.service.implementation;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dz.sh.trc.hyflo.core.system.audit.model.Audited;
import dz.sh.trc.hyflo.core.system.audit.repository.AuditedRepository;
import dz.sh.trc.hyflo.platform.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditedRepository auditedRepository;
    private final ObjectMapper objectMapper;

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
