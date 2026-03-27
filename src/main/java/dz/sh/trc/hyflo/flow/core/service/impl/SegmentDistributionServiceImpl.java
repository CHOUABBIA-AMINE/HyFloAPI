/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : SegmentDistributionServiceImpl
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — H4: add @Async asyncGenerateDerivedReadings()
 *  @UpdatedOn  : 03-26-2026 — H5: publish DerivedReadingGenerationFailedEvent on async failure
 *  @UpdatedOn  : 03-26-2026 — fix: getLengthKm() → getLength() (PipelineSegment field is 'length')
 *
 *  @Type       : Class
 *  @Layer      : Service (Internal Orchestration Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Generates one DerivedFlowReading per active PipelineSegment.
 *
 *                Distribution strategy:
 *                  Proportional-length: each segment receives a share of total pipeline volume
 *                  proportional to its length relative to the total pipeline length.
 *                  Falls back to equal distribution if segment lengths are not configured.
 *
 *                Idempotency:
 *                  rebuildForSourceReading() deletes existing derived readings
 *                  before persisting new batch — safe on repeat approval.
 *
 *  Phase 3 — Commit 19
 *  Phase 4 — H4: async wrapper method added
 *  Phase 4 — H5: DerivedReadingGenerationFailedEvent published on failure
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.DerivedFlowReadingReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.DerivedFlowReadingCommandDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.service.DerivedFlowReadingCommandService;
import dz.sh.trc.hyflo.flow.core.service.SegmentDistributionService;
import dz.sh.trc.hyflo.flow.workflow.event.DerivedReadingGenerationFailedEvent;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Generates one DerivedFlowReading per active PipelineSegment.
 *
 * Proportional distribution strategy:
 *   Each segment receives a proportional share of total pipeline volume
 *   based on segment.getLength() / totalLength.
 *   Falls back to equal distribution if total length is zero.
 *
 * Idempotency: rebuildForSourceReading() deletes existing derived readings
 *   before persisting new batch — safe on repeat approval.
 *
 * H4: asyncGenerateDerivedReadings() executes on 'taskExecutor' thread pool
 *   so the approval HTTP thread is freed after state persistence.
 *
 * H5: On async failure, DerivedReadingGenerationFailedEvent is published via
 *   ApplicationEventPublisher. Approval is never rolled back.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SegmentDistributionServiceImpl implements SegmentDistributionService {

    private final PipelineSegmentRepository pipelineSegmentRepository;
    private final DerivedFlowReadingCommandService derivedFlowReadingCommandService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<DerivedFlowReadingReadDTO> generateDerivedReadings(FlowReading sourceReading) {
        Long pipelineId = sourceReading.getPipeline().getId();
        log.info("Generating derived readings for source reading ID: {} on pipeline: {}",
                sourceReading.getId(), pipelineId);

        List<PipelineSegment> segments = pipelineSegmentRepository.findByPipelineId(pipelineId);

        if (segments.isEmpty()) {
            log.warn("Pipeline {} has no segments — no derived readings generated for reading {}",
                    pipelineId, sourceReading.getId());
            return List.of();
        }

        // FIX: PipelineSegment.length is type Double, field name is 'length' (not 'lengthKm')
        double totalLength = segments.stream()
                .mapToDouble(s -> s.getLength() != null ? s.getLength() : 0.0)
                .sum();

        boolean useEqualDistribution = totalLength == 0.0;
        double equalShare = 1.0 / segments.size();

        log.debug("Distribution strategy: {}, total length: {} km, segments: {}",
                useEqualDistribution ? "EQUAL" : "PROPORTIONAL_LENGTH",
                totalLength, segments.size());

        List<DerivedFlowReadingCommandDTO> commands = segments.stream()
                .map(segment -> buildDerivedCommand(
                        sourceReading,
                        segment,
                        useEqualDistribution ? equalShare
                                : (segment.getLength() != null
                                        ? segment.getLength() / totalLength
                                        : equalShare)))
                .collect(Collectors.toList());

        List<DerivedFlowReadingReadDTO> result =
                derivedFlowReadingCommandService.rebuildForSourceReading(
                        sourceReading.getId(), commands);

        log.info("Generated {} derived readings for source reading ID: {}",
                result.size(), sourceReading.getId());
        return result;
    }

    /**
     * H4: Async entry point for derived reading generation.
     * H5: On failure, publishes DerivedReadingGenerationFailedEvent.
     *
     * Runs on 'taskExecutor' thread pool — does not block the approval HTTP thread.
     * Failure is logged and published but does NOT propagate to the caller.
     */
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<DerivedFlowReadingReadDTO>> asyncGenerateDerivedReadings(
            FlowReading sourceReading) {
        log.info("[ASYNC] Starting derived reading generation for reading ID: {} on thread: {}",
                sourceReading.getId(), Thread.currentThread().getName());
        try {
            List<DerivedFlowReadingReadDTO> result = generateDerivedReadings(sourceReading);
            log.info("[ASYNC] Derived reading generation complete: {} records for reading ID: {}",
                    result.size(), sourceReading.getId());
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            Long pipelineId = null;
            try {
                pipelineId = sourceReading.getPipeline() != null
                        ? sourceReading.getPipeline().getId() : null;
            } catch (Exception ignored) {
                // pipeline may be detached in async context
            }
            log.error("[ASYNC] Derived reading generation FAILED for reading ID: {}. "
                    + "Approval remains valid. Manual regeneration may be needed.",
                    sourceReading.getId(), e);
            // H5: Publish failure event — enables monitoring without re-throwing
            eventPublisher.publishEvent(
                    DerivedReadingGenerationFailedEvent.create(
                            sourceReading.getId(),
                            pipelineId,
                            e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            return CompletableFuture.failedFuture(e);
        }
    }

    private DerivedFlowReadingCommandDTO buildDerivedCommand(
            FlowReading source, PipelineSegment segment, double weight) {

        BigDecimal containedVolume = null;
        if (source.getVolumeM3() != null) {
            containedVolume = source.getVolumeM3()
                    .multiply(BigDecimal.valueOf(weight))
                    .setScale(6, java.math.RoundingMode.HALF_UP);
        }

        return DerivedFlowReadingCommandDTO.builder()
                .sourceReadingId(source.getId())
                .pipelineSegmentId(segment.getId())
                .readingDate(source.getReadingDate())
                .readingSlotId(source.getReadingSlot() != null
                        ? source.getReadingSlot().getId() : null)
                .dataSourceId(source.getDataSource() != null
                        ? source.getDataSource().getId() : null)
                .calculatedAt(LocalDateTime.now())
                .calculationMethod("PROPORTIONAL_LENGTH")
                .pressure(source.getInletPressureBar())
                .temperature(source.getTemperatureCelsius())
                .flowRate(source.getVolumeM3())
                .containedVolume(containedVolume)
                .validationStatusId(source.getValidationStatus() != null
                        ? source.getValidationStatus().getId() : null)
                .build();
    }
}
