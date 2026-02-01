/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ReadingRejectedEvent
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026 - Refactored to extend BaseNotificationEvent
 *
 *  @Type       : Class
 *  @Layer      : Event
 *  @Package    : Configuration / Event
 *
 **/

package dz.sh.trc.hyflo.configuration.event;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Event published when a Validator rejects a reading
 * Triggers notification to the original Reader
 * 
 * Now extends BaseNotificationEvent for generic notification handling
 */
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class ReadingRejectedEvent extends BaseNotificationEvent {

    /**
     * ID of the rejected reading
     */
    private final Long readingId;

    /**
     * Username of the Validator who rejected the reading
     */
    private final String rejectedBy;

    /**
     * Username of the Reader who originally submitted the reading
     */
    private final String originalSubmitter;

    /**
     * Identifier/description of the reading
     */
    private final String readingIdentifier;

    /**
     * Rejection reason (mandatory)
     */
    private final String rejectionReason;

    /**
     * Timestamp when rejected
     */
    private final String rejectedAt;

    /**
     * Factory method to create ReadingRejectedEvent with proper notification setup
     * 
     * @param readingId ID of the rejected reading
     * @param rejectedBy Username of the rejector
     * @param originalSubmitter Username of the original submitter
     * @param readingIdentifier Description of the reading
     * @param rejectionReason Reason for rejection
     * @param rejectedAt Timestamp
     * @return Configured ReadingRejectedEvent
     */
    public static ReadingRejectedEvent create(
            Long readingId,
            String rejectedBy,
            String originalSubmitter,
            String readingIdentifier,
            String rejectionReason,
            String rejectedAt) {
        
        String message = String.format(
                "Your reading for %s has been rejected by %s\n\nReason: %s",
                readingIdentifier,
                rejectedBy,
                rejectionReason
        );
        
        return ReadingRejectedEvent.builder()
                // Event-specific fields
                .readingId(readingId)
                .rejectedBy(rejectedBy)
                .originalSubmitter(originalSubmitter)
                .readingIdentifier(readingIdentifier)
                .rejectionReason(rejectionReason)
                .rejectedAt(rejectedAt)
                // Base notification fields
                .notificationTypeCode("READING_REJECTED")
                .notificationTitle("Reading Rejected")
                .notificationMessage(message)
                .relatedEntityId(readingId.toString())
                .relatedEntityType("READING")
                .recipientUsernames(List.of(originalSubmitter)) // Send to specific user
                .occurredAt(rejectedAt)
                .priority(BaseNotificationEvent.NotificationPriority.HIGH) // Higher priority for rejections
                .build();
    }
}
