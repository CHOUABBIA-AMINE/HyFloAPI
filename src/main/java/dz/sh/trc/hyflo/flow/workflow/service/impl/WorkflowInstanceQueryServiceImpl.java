/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceQueryServiceImpl
 *  @CreatedOn  : Phase 4 — Commit 30
 *  @UpdatedOn  : 03-26-2026 — fix: remove WorkflowInstanceMapper bean injection.
 *                WorkflowInstanceMapper is a final static utility class with a
 *                private constructor — it cannot be @Autowired as a Spring bean.
 *                All mapper.toReadDTO() instance calls replaced with
 *                WorkflowInstanceMapper.toReadDTO() static calls.
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation
 *  @Package    : Flow / Workflow / Service / Impl
 *
 *  @Description: Implementation of WorkflowInstanceQueryService.
 *                Uses WorkflowInstanceRepository (Commit 26.1) and
 *                WorkflowInstanceMapper (static utility) for DTO projection.
 *                No business logic — pure read delegation.
 *
 *  Phase 4 — Commit 30
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service.impl;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.workflow.dto.query.WorkflowInstanceReadDTO;
import dz.sh.trc.hyflo.flow.workflow.mapper.WorkflowInstanceMapper;
import dz.sh.trc.hyflo.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.flow.workflow.repository.WorkflowInstanceRepository;
import dz.sh.trc.hyflo.flow.workflow.service.WorkflowInstanceQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Read-only service implementation for WorkflowInstance queries.
 *
 * Delegates entirely to WorkflowInstanceRepository.
 * No write operations. No business logic.
 *
 * WorkflowInstanceMapper is a final static utility class — all calls
 * use WorkflowInstanceMapper.toReadDTO() directly (no bean injection).
 *
 * Phase 4 — Commit 30
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WorkflowInstanceQueryServiceImpl implements WorkflowInstanceQueryService {

    // FIX: WorkflowInstanceMapper is NOT a Spring bean — it is a final static
    // utility class with a private constructor. Do NOT inject it via @RequiredArgsConstructor.
    // Use WorkflowInstanceMapper.toReadDTO() static calls directly.
    private final WorkflowInstanceRepository repository;

    @Override
    public WorkflowInstanceReadDTO getById(Long id) {
        log.debug("WorkflowInstanceQueryService.getById({})", id);
        WorkflowInstance instance = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WorkflowInstance not found: " + id));
        return WorkflowInstanceMapper.toReadDTO(instance);
    }

    @Override
    public List<WorkflowInstanceReadDTO> getByState(String stateCode) {
        log.debug("WorkflowInstanceQueryService.getByState({})", stateCode);
        return repository.findByCurrentState_Code(stateCode)
                .stream()
                .map(WorkflowInstanceMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDTO> getByInitiatingActor(Long employeeId) {
        log.debug("WorkflowInstanceQueryService.getByInitiatingActor({})", employeeId);
        return repository.findByInitiatedBy_Id(employeeId)
                .stream()
                .map(WorkflowInstanceMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDTO> getByLastActor(Long employeeId) {
        log.debug("WorkflowInstanceQueryService.getByLastActor({})", employeeId);
        return repository.findByLastActor_Id(employeeId)
                .stream()
                .map(WorkflowInstanceMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDTO> getByDateRange(LocalDate from, LocalDate to) {
        log.debug("WorkflowInstanceQueryService.getByDateRange({}, {})", from, to);
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.atTime(LocalTime.MAX);
        return repository.findByStartedAtBetween(fromDt, toDt)
                .stream()
                .map(WorkflowInstanceMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDTO> getByTargetType(String targetTypeCode) {
        log.debug("WorkflowInstanceQueryService.getByTargetType({})", targetTypeCode);
        return repository.findByTargetType_Code(targetTypeCode)
                .stream()
                .map(WorkflowInstanceMapper::toReadDTO)
                .collect(Collectors.toList());
    }
}
