/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ReadingValidatedEvent
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026 - Refactored to extend BaseNotificationEvent
 *
 *  @Type       : Class
 *  @Layer      : Event
 *  @Package    : Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.event;

import java.util.List;

import dz.sh.trc.hyflo.configuration.event.BaseNotificationEvent;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Event published when a Validator approves a reading
 * Triggers notification to the original Reader
 * 
 * Now extends BaseNotificationEvent for generic notification handling
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class ReadingValidatedEvent extends BaseNotificationEvent {

    /**
     * ID of the validated reading
     */
    private final Long readingId;

    /**
     * Username of the Validator who approved the reading
     */
    private final String validatedBy;

    /**
     * Username of the Reader who originally submitted the reading
     */
    private final String originalSubmitter;

    /**
     * Identifier/description of the reading
     */
    private final String readingIdentifier;

    /**
     * Optional validation comment
     */
    private final String comment;

    /**
     * Timestamp when validated
     */
    private final String validatedAt;

    /**
     * Factory method to create ReadingValidatedEvent with proper notification setup
     * 
     * @param readingId ID of the validated reading
     * @param validatedBy Username of the validator
     * @param originalSubmitter Username of the original submitter
     * @param readingIdentifier Description of the reading
     * @param comment Optional validation comment
     * @param validatedAt Timestamp
     * @return Configured ReadingValidatedEvent
     */
    public static ReadingValidatedEvent create(
            Long readingId,
            String validatedBy,
            String originalSubmitter,
            String readingIdentifier,
            String comment,
            String validatedAt) {
        
        // Build notification message
        String message = String.format(
                "Your reading for %s has been validated by %s",
                readingIdentifier,
                validatedBy
        );
        
        if (comment != null && !comment.isEmpty()) {
            message += "\n\nComment: " + comment;
        }
        
        return ReadingValidatedEvent.builder()
                // Event-specific fields
                .readingId(readingId)
                .validatedBy(validatedBy)
                .originalSubmitter(originalSubmitter)
                .readingIdentifier(readingIdentifier)
                .comment(comment)
                .validatedAt(validatedAt)
                // Base notification fields
                .notificationTypeCode("READING_VALIDATED")
                .notificationTitle("Reading Validated")
                .notificationMessage(message)
                .relatedEntityId(readingId.toString())
                .relatedEntityType("READING")
                .recipientUsernames(List.of(originalSubmitter)) // Send to specific user
                .occurredAt(validatedAt)
                .priority(BaseNotificationEvent.NotificationPriority.NORMAL)
                .build();
    }
}
