/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdService
 *  @CreatedOn  : 01-23-2026
 *  @UpdatedOn  : 03-26-2026 — H3: Marked @Deprecated — superseded by FlowThresholdCommandService / FlowThresholdQueryService
 *
 *  @Type       : Class
 *  @Layer      : Service (LEGACY)
 *  @Package    : Flow / Core
 *
 *  @Deprecated : This monolithic service is superseded by the command/query split:
 *                - FlowThresholdCommandService (writes)
 *                - FlowThresholdQueryService (reads)
 *                No active v2 path injects this service.
 *                Will be deleted in a future cleanup phase.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowThresholdMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @deprecated Superseded by {@link FlowThresholdCommandService} and {@link FlowThresholdQueryService}.
 *             No active v2 path should inject this service.
 *             Schedule for removal in next cleanup phase.
 */
@Deprecated(since = "v2", forRemoval = true)
@Service
@Transactional(readOnly = true)
@Slf4j
@SuppressWarnings({"squid:S1133", "java:S1133"})
public class FlowThresholdService extends GenericService<FlowThreshold, FlowThresholdDTO, Long> {

    public FlowThresholdService(FlowThresholdRepository flowThresholdRepository) {
        super(flowThresholdRepository,
              FlowThresholdMapper::toDto,
              FlowThresholdMapper::toEntity,
              "FlowThreshold");
    }
}
