/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ReadingValidatedEvent
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
 * Event published when a Validator approves a reading
 * Triggers notification to the original Reader
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
public class ReadingValidatedEvent {

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
}
