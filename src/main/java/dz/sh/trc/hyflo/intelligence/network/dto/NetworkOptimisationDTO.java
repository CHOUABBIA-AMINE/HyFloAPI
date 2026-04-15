package dz.sh.trc.hyflo.intelligence.network.dto;

import java.time.Instant;
import java.util.List;

/**
 * AI-generated optimisation suggestions for a pipeline network segment.
 */
public record NetworkOptimisationDTO(

        String networkSegmentId,
        String topologyZone,

        /**
         * Prioritised list of optimisation actions suggested by the AI.
         */
        List<OptimisationActionDTO> actions,

        /**
         * Estimated efficiency gain percentage if all actions are applied.
         */
        double estimatedEfficiencyGainPercent,

        /**
         * AI-generated executive summary of the optimisation opportunity.
         */
        String summary,

        String aiProvider,
        Instant generatedAt,
        String correlationId
) {
    /**
     * A single AI-suggested optimisation action.
     */
    public record OptimisationActionDTO(

            int priority,

            /**
             * Target node for this action.
             */
            String targetNodeId,

            /**
             * Action type: REBALANCE | REROUTE | REDUCE_PRESSURE
             * | INCREASE_FLOW | SHUTDOWN_NODE | ACTIVATE_BACKUP
             */
            String actionType,

            String rationale,

            /**
             * Expected impact: MINOR | MODERATE | SIGNIFICANT
             */
            String expectedImpact
    ) {}
}