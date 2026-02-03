package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDateTime;

public interface SlotCoverageProjection {
    Long getPipelineId();
    String getPipelineCode();
    String getPipelineName();
    Long getReadingId();
    String getValidationStatusCode(); // Returns "NOT_RECORDED", "DRAFT", "SUBMITTED", etc.
    LocalDateTime getRecordedAt();
    LocalDateTime getValidatedAt();
    String getRecordedByName();
    String getValidatedByName();
    Boolean getHasReading();
}