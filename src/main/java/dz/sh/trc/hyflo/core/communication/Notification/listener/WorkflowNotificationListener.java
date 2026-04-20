package dz.sh.trc.hyflo.core.communication.Notification.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dz.sh.trc.hyflo.core.communication.Notification.service.NotificationService;
import dz.sh.trc.hyflo.platform.event.WorkflowTransitionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listens for WorkflowTransitionEvent and dispatches notifications asynchronously.
 *
 * This listener bridges the workflow engine and the communication layer:
 * - Workflow publishes events (fire-and-forget, no coupling to notifications)
 * - This listener receives events and calls NotificationService
 *
 * The @Async annotation ensures notification delivery does not block the workflow transaction.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WorkflowNotificationListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void onWorkflowTransition(WorkflowTransitionEvent event) {
        log.info("Workflow event received: action={}, entity={}/{}, transition={} → {}",
                event.getAction(), event.getEntityType(), event.getEntityId(),
                event.getFromState(), event.getToState());

        try {
            // Determine notification type code from the action
            String notificationTypeCode = resolveNotificationTypeCode(event);

            // Send to recipients (if specified in the event)
            if (event.hasSpecificRecipients()) {
                // Future: resolve usernames → user IDs and send individually
                log.debug("Skipping user-targeted notification (not yet implemented for username resolution)");
            } else if (event.hasRoleBasedRecipients()) {
                // Future: resolve role → users and send individually
                log.debug("Skipping role-targeted notification (not yet implemented for role resolution)");
            } else {
                log.debug("No specific recipients for event type={}, notification logged only", notificationTypeCode);
            }
        } catch (Exception e) {
            // Never let notification failure break the workflow
            log.error("Failed to process workflow notification: action={}, entity={}/{}",
                    event.getAction(), event.getEntityType(), event.getEntityId(), e);
        }
    }

    /**
     * Maps workflow action to NotificationType code.
     * Convention: entity type + action (e.g., FLOW_READING + SUBMIT → READING_SUBMITTED)
     */
    private String resolveNotificationTypeCode(WorkflowTransitionEvent event) {
        String entityShort = event.getEntityType() != null
                ? event.getEntityType().replace("FLOW_", "")
                : "UNKNOWN";
        String action = event.getAction() != null ? event.getAction() : "UNKNOWN";

        return switch (action.toUpperCase()) {
            case "SUBMIT" -> entityShort + "_SUBMITTED";
            case "VALIDATE" -> entityShort + "_VALIDATED";
            case "APPROVE" -> entityShort + "_APPROVED";
            case "REJECT" -> entityShort + "_REJECTED";
            default -> entityShort + "_" + action.toUpperCase();
        };
    }
}
