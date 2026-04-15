package dz.sh.trc.hyflo.intelligence.network.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

/**
 * Request DTO carrying pipeline network topology and operational context
 * for AI insight and optimisation analysis.
 */
public record NetworkInsightRequestDTO(

        @NotBlank(message = "Network segment ID must not be blank")
        String networkSegmentId,

        @NotBlank(message = "Topology zone must not be blank")
        String topologyZone,

        @NotNull(message = "Snapshot timestamp is required")
        Instant snapshotAt,

        /**
         * Current operational state of each node in the segment.
         */
        @NotNull(message = "Node states must not be null")
        List<NetworkNodeStateDTO> nodeStates,

        /**
         * Active alert IDs currently raised on this segment.
         * Included in LLM prompt as operational context.
         */
        List<String> activeAlertIds,

        /**
         * Optional operator observation injected into LLM context.
         */
        String operatorNote,

        String correlationId
) {
    /**
     * Operational state snapshot of a single network node (pump, valve, junction).
     */
    public record NetworkNodeStateDTO(

            @NotBlank(message = "Node ID must not be blank")
            String nodeId,

            /**
             * Node type: PUMP | VALVE | JUNCTION | COMPRESSOR | METER | TANK
             */
            String nodeType,

            /**
             * Operational status: ACTIVE | IDLE | FAULT | MAINTENANCE | OFFLINE
             */
            String status,

            double pressureBar,
            double flowRateM3h,
            double utilizationPercent
    ) {}
}