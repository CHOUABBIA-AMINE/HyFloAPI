/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentDistributionServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Workflow Orchestration)
 *  @Package    : Flow / Workflow / Impl
 *
 *  @Description: Implements SegmentDistributionService.
 *                Distributes an approved FlowReading into per-segment
 *                DerivedFlowReading records using a proportional-length
 *                (PROPORTIONAL_LENGTH) distribution strategy.
 *
 *                Distribution algorithm:
 *                  1. Load all PipelineSegment records for the reading's pipeline.
 *                  2. Compute total pipeline length = sum of all segment lengths.
 *                  3. For each segment, derive:
 *                       ratio     = segment.lengthKm / totalLength
 *                       flowRate  = source.flowRate  * ratio
 *                       volume    = source.containedVolume * ratio
 *                       pressure  = source.pressure  (same across segments — not proportional)
 *                       temperature = source.temperature (same — ambient, not proportional)
 *                  4. Delete any prior DerivedFlowReading for this source reading
 *                     (idempotent rebuild — safe to call again if re-approved).
 *                  5. Persist all new DerivedFlowReading records.
 *                  6. Map to DerivedFlowReadingReadDTO and return.
 *
 *                asyncGenerateDerivedReadings() wraps the synchronous method
 *                in a CompletableFuture using @Async (Spring TaskExecutor).
 *                It runs in a new transaction to avoid sharing the caller's
 *                ReadingWorkflowService transaction context.
 *
 *  @Fix        : Resolves Spring boot startup failure:
 *                "required a bean of type SegmentDistributionService that could not be found"
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service.impl;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.mapper.DerivedFlowReadingMapper;
import dz.sh.trc.hyflo.flow.core.model.DerivedFlowReading;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.DerivedFlowReadingRepository;
import dz.sh.trc.hyflo.flow.workflow.service.SegmentDistributionService;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Proportional-length segment distribution engine.
 *
 * Registered as {@code @Service} to satisfy the SegmentDistributionService
 * constructor injection in ReadingWorkflowService.
 *
 * Transaction notes:
 * - generateDerivedReadings      : requires its own transaction for the delete-then-save
 *                                  idempotency pattern.
 * - asyncGenerateDerivedReadings : wrapped in @Async for non-blocking approval;
 *                                  @Transactional ensures a fresh transaction independent
 *                                  of the caller.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SegmentDistributionServiceImpl implements SegmentDistributionService {

    private static final String CALC_METHOD       = "PROPORTIONAL_LENGTH";
    private static final MathContext MATH_CTX      = new MathContext(18, RoundingMode.HALF_UP);
    private static final int         SCALE         = 4;

    private final PipelineSegmentRepository  segmentRepository;
    private final DerivedFlowReadingRepository derivedRepository;

    // ----------------------------------------------------------------
    //  SegmentDistributionService contract — synchronous
    // ----------------------------------------------------------------

    @Override
    @Transactional
    public List<DerivedFlowReadingReadDTO> generateDerivedReadings(FlowReading sourceReading) {
        if (sourceReading == null) {
            log.warn("generateDerivedReadings called with null sourceReading — skipping");
            return List.of();
        }

        Long pipelineId = resolvePipelineId(sourceReading);
        if (pipelineId == null) {
            log.warn("generateDerivedReadings: sourceReading ID={} has no pipeline — skipping",
                    sourceReading.getId());
            return List.of();
        }

        List<PipelineSegment> segments = segmentRepository.findByPipelineId(pipelineId);
        if (segments.isEmpty()) {
            log.warn("generateDerivedReadings: no segments found for pipeline ID={} — skipping",
                    pipelineId);
            return List.of();
        }

        // Compute total pipeline length
        BigDecimal totalLength = segments.stream()
                .map(this::segmentLength)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalLength.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("generateDerivedReadings: totalLength=0 for pipeline ID={} — skipping proportional distribution",
                    pipelineId);
            return List.of();
        }

        // Idempotent rebuild: delete any prior derived readings for this source
        derivedRepository.deleteBySourceReadingId(sourceReading.getId());
        log.debug("generateDerivedReadings: deleted prior derived readings for sourceReading ID={}",
                sourceReading.getId());

        // Build derived readings
        List<DerivedFlowReading> derived = new ArrayList<>();
        LocalDateTime calculatedAt = LocalDateTime.now();

        for (PipelineSegment segment : segments) {
            BigDecimal segLen = segmentLength(segment);
            BigDecimal ratio  = segLen.divide(totalLength, MATH_CTX);

            DerivedFlowReading dr = new DerivedFlowReading();
            dr.setReadingDate(sourceReading.getReadingDate());
            dr.setPipelineSegment(segment);
            dr.setSourceReading(sourceReading);
            dr.setReadingSlot(sourceReading.getReadingSlot());
            dr.setValidationStatus(sourceReading.getValidationStatus());
            dr.setCalculatedAt(calculatedAt);
            dr.setCalculationMethod(CALC_METHOD);

            // Proportional volume and flow rate
            dr.setFlowRate(scale(multiply(sourceReading.getFlowRate(), ratio)));
            dr.setContainedVolume(scale(multiply(sourceReading.getContainedVolume(), ratio)));

            // Pressure and temperature are pipeline-level averages — not proportional
            dr.setPressure(scale(sourceReading.getPressure()));
            dr.setTemperature(scale(sourceReading.getTemperature()));

            // Denormalised scalar provenance
            if (sourceReading.getReadingSlot() != null) {
                dr.setReadingSlotId(sourceReading.getReadingSlot().getId());
            }

            derived.add(dr);
        }

        List<DerivedFlowReading> saved = derivedRepository.saveAll(derived);
        log.info("generateDerivedReadings: created {} derived readings for sourceReading ID={}",
                saved.size(), sourceReading.getId());

        return saved.stream()
                .map(DerivedFlowReadingMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    //  SegmentDistributionService contract — async
    // ----------------------------------------------------------------

    @Async
    @Override
    @Transactional
    public CompletableFuture<List<DerivedFlowReadingReadDTO>> asyncGenerateDerivedReadings(
            FlowReading sourceReading) {
        log.debug("asyncGenerateDerivedReadings: starting for sourceReading ID={}",
                sourceReading != null ? sourceReading.getId() : "null");
        try {
            List<DerivedFlowReadingReadDTO> result = generateDerivedReadings(sourceReading);
            log.debug("asyncGenerateDerivedReadings: completed with {} records", result.size());
            return CompletableFuture.completedFuture(result);
        } catch (Exception ex) {
            log.error("asyncGenerateDerivedReadings: failed for sourceReading ID={}",
                    sourceReading != null ? sourceReading.getId() : "null", ex);
            CompletableFuture<List<DerivedFlowReadingReadDTO>> failed = new CompletableFuture<>();
            failed.completeExceptionally(ex);
            return failed;
        }
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    /**
     * Resolve the pipeline ID from the FlowReading.
     * FlowReading.pipeline is a @ManyToOne — guard against null.
     */
    private Long resolvePipelineId(FlowReading reading) {
        if (reading.getPipeline() == null) return null;
        return reading.getPipeline().getId();
    }

    /**
     * Get segment length in km. PipelineSegment.lengthKm is the canonical length field.
     * Returns ZERO if null to avoid NPE in sum/divide operations.
     */
    private BigDecimal segmentLength(PipelineSegment segment) {
        BigDecimal len = segment.getLengthKm();
        return (len != null) ? len : BigDecimal.ZERO;
    }

    /** Null-safe multiply: returns ZERO if either operand is null. */
    private BigDecimal multiply(BigDecimal value, BigDecimal ratio) {
        if (value == null || ratio == null) return BigDecimal.ZERO;
        return value.multiply(ratio, MATH_CTX);
    }

    /** Apply standard scale to a nullable BigDecimal; returns null if input is null. */
    private BigDecimal scale(BigDecimal value) {
        if (value == null) return null;
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
