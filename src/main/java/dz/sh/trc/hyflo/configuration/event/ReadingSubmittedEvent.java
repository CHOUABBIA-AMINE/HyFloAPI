/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ReadingSubmittedEvent
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
 * Event published when a Reader submits operational readings
 * Triggers notification to all Validators
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
public class ReadingSubmittedEvent {

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
}
