/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ReadingRejectedEvent
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Event
 *  @Package    : System / Notification / Event
 *
 **/

package dz.sh.trc.hyflo.configuration.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Event published when a Validator rejects a reading
 * Triggers notification to the original Reader
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
public class ReadingRejectedEvent {

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
}
