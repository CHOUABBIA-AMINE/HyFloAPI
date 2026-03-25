/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadableTargetValidationServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : Flow / Core
 *
 *  Phase 3 — Commit 20
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.flow.core.dto.command.FlowReadingCommandDto;
import dz.sh.trc.hyflo.flow.core.service.ReadableTargetValidationService;
import dz.sh.trc.hyflo.network.core.repository.PipelineSegmentRepository;
import dz.sh.trc.hyflo.network.core.repository.StationRepository;
import dz.sh.trc.hyflo.network.core.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates readable infrastructure targets for FlowReading commands.
 *
 * Rules enforced:
 * 1. pipelineId is always required — enforced separately in FlowReadingCommandServiceImpl.
 * 2. If a secondary target is provided (pipelineSegmentId, stationId, terminalId),
 *    it must exist in the network module.
 * 3. Only ONE secondary target may be specified per reading.
 * 4. PipelineSegment must belong to the specified pipeline (cross-domain guard).
 *
 * Phase 3 — Commit 20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReadableTargetValidationServiceImpl implements ReadableTargetValidationService {

    private final PipelineSegmentRepository pipelineSegmentRepository;
    private final StationRepository stationRepository;
    private final TerminalRepository terminalRepository;

    @Override
    public void validateCommandTarget(FlowReadingCommandDto command) {
        int secondaryTargetCount = 0;

        if (command.getPipelineSegmentId() != null) {
            secondaryTargetCount++;
            validateSegmentBelongsToPipeline(command.getPipelineSegmentId(), command.getPipelineId());
        }

        if (command.getStationId() != null) {
            secondaryTargetCount++;
            if (!stationRepository.existsById(command.getStationId())) {
                throw new IllegalArgumentException(
                        "Station not found: " + command.getStationId());
            }
        }

        if (command.getTerminalId() != null) {
            secondaryTargetCount++;
            if (!terminalRepository.existsById(command.getTerminalId())) {
                throw new IllegalArgumentException(
                        "Terminal not found: " + command.getTerminalId());
            }
        }

        if (secondaryTargetCount > 1) {
            throw new IllegalArgumentException(
                    "A FlowReading may target at most one secondary infrastructure element "
                    + "(PIPELINE_SEGMENT, STATION, or TERMINAL). "
                    + secondaryTargetCount + " targets were specified.");
        }

        log.debug("Readable target validation passed for pipeline ID: {}", command.getPipelineId());
    }

    private void validateSegmentBelongsToPipeline(Long segmentId, Long pipelineId) {
        pipelineSegmentRepository.findById(segmentId).ifPresentOrElse(
                segment -> {
                    if (segment.getPipeline() == null
                            || !pipelineId.equals(segment.getPipeline().getId())) {
                        throw new IllegalArgumentException(
                                "PipelineSegment " + segmentId
                                + " does not belong to Pipeline " + pipelineId);
                    }
                },
                () -> {
                    throw new IllegalArgumentException(
                            "PipelineSegment not found: " + segmentId);
                }
        );
    }
}
