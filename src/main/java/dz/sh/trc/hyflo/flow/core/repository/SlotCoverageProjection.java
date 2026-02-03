/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: SlotCoverageProjection
 * 	@CreatedOn	: 01-28-2026
 * 	@UpdatedOn	: 02-03-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Projection for slot coverage query
 * FIX #4: Added slot timing fields for deadline calculation
 */
public interface SlotCoverageProjection {
    
    Long getPipelineId();
    String getPipelineCode();
    String getPipelineName();
    Long getReadingId();
    String getValidationStatusCode();
    LocalDateTime getRecordedAt();
    LocalDateTime getValidatedAt();
    String getRecordedByName();
    String getValidatedByName();
    Boolean getHasReading();
    
    // FIX #4: Slot timing for deadline calculation
    LocalDate getReadingDate();
    LocalTime getSlotStartTime();
    LocalTime getSlotEndTime();
}
