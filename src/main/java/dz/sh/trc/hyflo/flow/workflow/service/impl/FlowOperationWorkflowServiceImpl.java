/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationWorkflowServiceImpl
 *  @CreatedOn  : 03-28-2026 — extracted from FlowOperationCommandServiceImpl (workflow refactor)
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation (Workflow)
 *  @Package    : Flow / Workflow
 *
 *  @Description: Implements approve and reject for FlowOperation.
 *                Delegates to FlowOperationCommandService for the actual approve/reject logic
 *                to avoid duplicating business rules.
 *                This class owns the workflow layer; core service owns the state-transition logic.
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service.impl;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowOperationCommandService;
import dz.sh.trc.hyflo.flow.workflow.service.FlowOperationWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Workflow service implementation for FlowOperation lifecycle transitions.
 * Delegates to FlowOperationCommandService.approve() and .reject().
 * Workflow layer owns the HTTP-facing contract; core layer owns the business invariants.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowOperationWorkflowServiceImpl implements FlowOperationWorkflowService {

    private final FlowOperationCommandService commandService;

    @Override
    public FlowOperationReadDTO approve(Long id, Long validatorId) {
        log.info("[workflow] approve operationId={} validatorId={}", id, validatorId);
        return commandService.approve(id, validatorId);
    }

    @Override
    public FlowOperationReadDTO reject(Long id, Long validatorId, String reason) {
        log.info("[workflow] reject operationId={} validatorId={} reason={}", id, validatorId, reason);
        return commandService.reject(id, validatorId, reason);
    }
}
