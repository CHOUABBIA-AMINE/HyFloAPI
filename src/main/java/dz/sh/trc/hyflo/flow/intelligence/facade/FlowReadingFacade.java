package dz.sh.trc.hyflo.flow.intelligence.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import dz.sh.trc.hyflo.flow.common.repository.ReadingSlotRepository;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Facade providing intelligence module with controlled access to flow readings
 * and reading slots, wrapping FlowReadingRepository and ReadingSlotRepository.
 *
 * This keeps repository access logic in a single place and avoids scattering
 * direct repository dependencies across multiple intelligence services.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FlowReadingFacade {

    private final FlowReadingRepository flowReadingRepository;
    private final ReadingSlotRepository readingSlotRepository;

    /**
     * Find latest reading for a specific pipeline.
     */
    public Optional<FlowReading> findLatestByPipeline(Long pipelineId) {
        log.debug("Finding latest flow reading for pipeline: {}", pipelineId);
        return flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId);
    }

    /**
     * Find all readings for a specific pipeline and date.
     */
    public List<FlowReading> findByPipelineAndDate(Long pipelineId, LocalDate readingDate) {
        log.debug("Finding flow readings for pipeline {} on date {}", pipelineId, readingDate);
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate);
    }

    /**
     * Find readings within date range ordered chronologically for time-series analysis.
     */
    public List<FlowReading> findByPipelineAndDateRangeOrdered(
            Long pipelineId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding ordered flow readings for pipeline {} between {} and {}", 
                  pipelineId, startDate, endDate);
        return flowReadingRepository
                .findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
                        pipelineId, startDate, endDate);
    }

    /**
     * Get all reading slots ordered by display order.
     */
    public List<ReadingSlot> findAllSlotsOrdered() {
        log.debug("Finding all reading slots ordered by display order");
        return readingSlotRepository.findAllByOrderByDisplayOrder();
    }
}
