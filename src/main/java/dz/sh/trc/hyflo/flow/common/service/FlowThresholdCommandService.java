/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdCommandService
 *  @CreatedOn  : 03-26-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.service → flow.common.service
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command)
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import dz.sh.trc.hyflo.flow.common.dto.FlowThresholdDTO;

/**
 * Command contract for FlowThreshold write operations.
 * Business invariant: only one active threshold per pipeline at any time.
 */
public interface FlowThresholdCommandService {

    FlowThresholdDTO createThreshold(FlowThresholdDTO dto);
    FlowThresholdDTO updateThreshold(Long id, FlowThresholdDTO dto);
    void deleteThreshold(Long id);
    FlowThresholdDTO activateThreshold(Long id);
    FlowThresholdDTO deactivateThreshold(Long id);
}
