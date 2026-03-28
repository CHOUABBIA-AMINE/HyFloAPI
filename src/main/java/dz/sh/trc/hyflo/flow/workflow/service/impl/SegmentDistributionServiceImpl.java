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
 *  Field mapping (after reading actual entity fields):
 *
 *    FlowReading source fields used:
 *      - volumeM3             → BigDecimal  → proportional per segment → DerivedFlowReading.containedVolume
 *      - volumeMscf           → BigDecimal  → proportional per segment → DerivedFlowReading.flowRate
 *                                            (MSCF is the closest available "flow rate" measure)
 *      - inletPressureBar     → BigDecimal  → averaged (inlet+outlet / 2) → DerivedFlowReading.pressure
 *      - outletPressureBar    → BigDecimal  → (part of pressure average above)
 *      - temperatureCelsius   → BigDecimal  → copied as-is             → DerivedFlowReading.temperature
 *      - readingDate          → LocalDate   → copied as-is
 *      - readingSlot          → ReadingSlot → copied as-is
 *      - validationStatus     → ValidationStatus → copied as-is
 *
 *    PipelineSegment field used:
 *      - length               → Double (km) — Lombok getter: getLength()
 *
 *  Distribution algorithm:
 *    1. Load all PipelineSegment records for the reading's pipeline.
 *    2. Compute totalLength = Σ(segment.length).
 *    3. For each segment:
 *         ratio          = segment.length / totalLength
 *         containedVolume = source.volumeM3  * ratio
 *         flowRate        = source.volumeMscf * ratio
 *         pressure        = (inletPressureBar + outletPressureBar) / 2  (uniform across segment)
 *         temperature     = source.temperatureCelsius                   (uniform across segment)
 *    4. Delete any prior DerivedFlowReading for this source (idempotent rebuild).
 *    5. saveAll() and map to DerivedFlowReadingReadDTO.
 *
 *  @Fix        : Resolves Spring boot startup failure:
 *                "required a bean of type SegmentDistributionService that could not be found"
 *  @Fix        : Corrects compiler errors — wrong field names assumed in first revision:
 *                  getLengthKm()      → getLength()         (PipelineSegment.length : Double)
 *                  getFlowRate()      → getVolumeMscf()     (FlowReading — no flowRate field)
 *                  getContainedVolume()→ getVolumeM3()      (FlowReading — no containedVolume)
 *                  getPressure()      → avg(inletPressureBar, outletPressureBar)
 *                  getTemperature()   → getTemperatureCelsius()
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
 * Registered as {@code @Service} so Spring can satisfy the
 * SegmentDistributionService constructor injection in ReadingWorkflowService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SegmentDistributionServiceImpl implements SegmentDistributionService {

    private static final String      CALC_METHOD = "PROPORTIONAL_LENGTH";
    private static final MathContext  MATH_CTX    = new MathContext(18, RoundingMode.HALF_UP);
    private static final int          SCALE       = 4;

    private final PipelineSegmentRepository    segmentRepository;
    private final DerivedFlowReadingRepository derivedRepository;

    // ----------------------------------------------------------------
    //  SegmentDistributionService — synchronous
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
            log.warn("generateDerivedReadings: no segments for pipeline ID={} — skipping",
                    pipelineId);
            return List.of();
        }

        // Total pipeline length from segment.length (Double, km)
        double totalLengthRaw = segments.stream()
                .mapToDouble(s -> s.getLength() != null ? s.getLength() : 0.0)
                .sum();

        if (totalLengthRaw == 0.0) {
            log.warn("generateDerivedReadings: totalLength=0 for pipeline ID={} — skipping",
                    pipelineId);
            return List.of();
        }

        BigDecimal totalLength = BigDecimal.valueOf(totalLengthRaw);

        // Idempotent rebuild
        derivedRepository.deleteBySourceReadingId(sourceReading.getId());
        log.debug("generateDerivedReadings: deleted prior derived readings for sourceReading ID={}",
                sourceReading.getId());

        // Compute average pressure: (inlet + outlet) / 2
        BigDecimal avgPressure = averagePressure(
                sourceReading.getInletPressureBar(),
                sourceReading.getOutletPressureBar()
        );

        // Temperature — uniform across all segments (ambient / pipeline-level)
        BigDecimal temperature = scale(sourceReading.getTemperatureCelsius());

        List<DerivedFlowReading> derived = new ArrayList<>();
        LocalDateTime calculatedAt = LocalDateTime.now();

        for (PipelineSegment segment : segments) {
            double segLenRaw = segment.getLength() != null ? segment.getLength() : 0.0;
            BigDecimal segLen = BigDecimal.valueOf(segLenRaw);
            BigDecimal ratio  = segLen.divide(totalLength, MATH_CTX);

            DerivedFlowReading dr = new DerivedFlowReading();
            dr.setReadingDate(sourceReading.getReadingDate());
            dr.setPipelineSegment(segment);
            dr.setSourceReading(sourceReading);
            dr.setReadingSlot(sourceReading.getReadingSlot());
            dr.setValidationStatus(sourceReading.getValidationStatus());
            dr.setCalculatedAt(calculatedAt);
            dr.setCalculationMethod(CALC_METHOD);

            // Proportional volume distribution
            // containedVolume ← volumeM3  (physical volume in m³, split by segment length)
            // flowRate        ← volumeMscf (MSCF flow volume, split by segment length)
            dr.setContainedVolume(scaleProportional(sourceReading.getVolumeM3(), ratio));
            dr.setFlowRate(scaleProportional(sourceReading.getVolumeMscf(), ratio));

            // Pressure and temperature — uniform (not proportional)
            dr.setPressure(avgPressure);
            dr.setTemperature(temperature);

            // Denormalised scalar provenance
            if (sourceReading.getReadingSlot() != null) {
                dr.setReadingSlotId(sourceReading.getReadingSlot().getId());
            }

            derived.add(dr);
        }

        List<DerivedFlowReading> saved = derivedRepository.saveAll(derived);
        log.info("generateDerivedReadings: persisted {} derived readings for sourceReading ID={}",
                saved.size(), sourceReading.getId());

        return saved.stream()
                .map(DerivedFlowReadingMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    //  SegmentDistributionService — async
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

    /** Navigate FlowReading → Pipeline → id. Guards against null pipeline. */
    private Long resolvePipelineId(FlowReading reading) {
        if (reading.getPipeline() == null) return null;
        return reading.getPipeline().getId();
    }

    /**
     * Compute average of inlet and outlet pressure.
     * Returns null if both are null; uses available value if only one is null.
     */
    private BigDecimal averagePressure(BigDecimal inlet, BigDecimal outlet) {
        if (inlet == null && outlet == null) return null;
        if (inlet  == null) return scale(outlet);
        if (outlet == null) return scale(inlet);
        return scale(inlet.add(outlet).divide(BigDecimal.valueOf(2), MATH_CTX));
    }

    /**
     * Multiply a source value by a ratio for proportional distribution.
     * Returns null (not ZERO) if value is null — null signals "no data"
     * rather than "zero volume", preserving semantic accuracy in the derived record.
     */
    private BigDecimal scaleProportional(BigDecimal value, BigDecimal ratio) {
        if (value == null || ratio == null) return null;
        return value.multiply(ratio, MATH_CTX).setScale(SCALE, RoundingMode.HALF_UP);
    }

    /** Apply standard scale to a nullable BigDecimal. Returns null if input is null. */
    private BigDecimal scale(BigDecimal value) {
        if (value == null) return null;
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
