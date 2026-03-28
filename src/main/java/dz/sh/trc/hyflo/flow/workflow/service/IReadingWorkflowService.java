/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IReadingWorkflowService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Workflow
 *
 *  @Description: Contract for the reading workflow service (approve/reject actions).
 *                Implementation: ReadingWorkflowService (existing @Service class, kept in place).
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;

/**
 * Contract for the reading workflow (approve / reject transitions).
 * Implemented by ReadingWorkflowService.
 */
public interface IReadingWorkflowService {

    FlowReadingReadDTO approve(Long id, Long approvedById);

    FlowReadingReadDTO reject(Long id, Long rejectedById, String rejectionReason);
}
