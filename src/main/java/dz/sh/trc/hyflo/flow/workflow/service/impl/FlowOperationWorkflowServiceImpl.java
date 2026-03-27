/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationWorkflowServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation (Workflow)
 *  @Package    : Flow / Workflow
 *
 *  @Description: Implements FlowOperationWorkflowService.
 *                Approve/reject logic extracted from FlowOperationCommandServiceImpl.
 *                Delegates to FlowOperationCommandService.approve/reject
 *                to keep state-machine logic in one place and avoid duplication.
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
 * Workflow facade for FlowOperation lifecycle transitions.
 *
 * Delegates to FlowOperationCommandService which already contains
 * the full approve/reject state-machine implementation.
 * This layer enforces clean module separation: workflow concerns
 * are now routed through flow.workflow, not flow.core controllers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowOperationWorkflowServiceImpl implements FlowOperationWorkflowService {

    private final FlowOperationCommandService commandService;

    @Override
    public FlowOperationReadDTO approve(Long id, Long validatorId) {
        log.info("[WorkflowService] approve operationId={} validatorId={}", id, validatorId);
        return commandService.approve(id, validatorId);
    }

    @Override
    public FlowOperationReadDTO reject(Long id, Long validatorId, String rejectionReason) {
        log.info("[WorkflowService] reject operationId={} validatorId={} reason={}",
                id, validatorId, rejectionReason);
        return commandService.reject(id, validatorId, rejectionReason);
    }
}
