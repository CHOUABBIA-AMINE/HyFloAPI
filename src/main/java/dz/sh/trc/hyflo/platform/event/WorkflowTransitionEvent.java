package dz.sh.trc.hyflo.platform.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Event published after every workflow state transition.
 * The WorkflowNotificationListener consumes this to create notifications asynchronously.
 *
 * This event is decoupled from the notification system:
 * - The workflow engine publishes it (fire-and-forget)
 * - The communication layer consumes it (via @EventListener)
 */
@Getter
@SuperBuilder
public class WorkflowTransitionEvent extends BaseNotificationEvent {

    /** The ID of the domain entity (e.g., FlowReading.id, FlowPlan.id) */
    private final Long entityId;

    /** The type of entity (e.g., "FLOW_READING", "FLOW_ALERT", "FLOW_PLAN") */
    private final String entityType;

    /** The state the entity transitioned FROM (e.g., "DRAFT") */
    private final String fromState;

    /** The state the entity transitioned TO (e.g., "SUBMITTED") */
    private final String toState;

    /** The action that triggered this transition (e.g., "SUBMIT") */
    private final String action;

    /** The employee who performed the action */
    private final Long actorEmployeeId;
}
