/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ReadingSubmittedEvent
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026 - Refactored to extend BaseNotificationEvent
 *
 *  @Type       : Class
 *  @Layer      : Event
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.event;

import dz.sh.trc.hyflo.configuration.event.BaseNotificationEvent;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Event published when a Reader submits operational readings
 * Triggers notification to all Validators
 * 
 * Now extends BaseNotificationEvent for generic notification handling
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class ReadingSubmittedEvent extends BaseNotificationEvent {

    /**
     * ID of the submitted reading
     */
    private final Long readingId;

    /**
     * Username of the Reader who submitted the reading
     */
    private final String submittedBy;

    /**
     * Identifier/description of the reading (e.g., "Meter #12345")
     */
    private final String readingIdentifier;

    /**
     * Timestamp when the reading was submitted
     */
    private final String submittedAt;

    /**
     * Factory method to create ReadingSubmittedEvent with proper notification setup
     * 
     * @param readingId ID of the submitted reading
     * @param submittedBy Username of the submitter
     * @param readingIdentifier Description of the reading
     * @param submittedAt Timestamp
     * @return Configured ReadingSubmittedEvent
     */
    public static ReadingSubmittedEvent create(
            Long readingId,
            String submittedBy,
            String readingIdentifier,
            String submittedAt) {
        
        return ReadingSubmittedEvent.builder()
                // Event-specific fields
                .readingId(readingId)
                .submittedBy(submittedBy)
                .readingIdentifier(readingIdentifier)
                .submittedAt(submittedAt)
                // Base notification fields
                .notificationTypeCode("READING_SUBMITTED")
                .notificationTitle("New Reading Awaiting Validation")
                .notificationMessage(String.format(
                        "Reader %s submitted a new operational reading for %s",
                        submittedBy,
                        readingIdentifier
                ))
                .relatedEntityId(readingId.toString())
                .relatedEntityType("READING")
                .recipientRole("ROLE_VALIDATOR") // Send to all validators
                .occurredAt(submittedAt)
                .priority(BaseNotificationEvent.NotificationPriority.NORMAL)
                .build();
    }
}
