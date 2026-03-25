/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : WorkflowInstanceQueryServiceImpl
 *  @CreatedOn  : Phase 4 — Commit 30
 *
 *  @Type       : Class
 *  @Layer      : Service Implementation
 *  @Package    : Flow / Workflow / Service / Impl
 *
 *  @Description: Implementation of WorkflowInstanceQueryService.
 *                Uses WorkflowInstanceRepository (Commit 26.1) and
 *                WorkflowInstanceMapper (Phase 3) for DTO projection.
 *                No business logic — pure read delegation.
 *
 *  Phase 4 — Commit 30
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service.impl;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.workflow.dto.query.WorkflowInstanceReadDto;
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
 * Phase 4 — Commit 30
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WorkflowInstanceQueryServiceImpl implements WorkflowInstanceQueryService {

    private final WorkflowInstanceRepository repository;
    private final WorkflowInstanceMapper mapper;

    @Override
    public WorkflowInstanceReadDto getById(Long id) {
        log.debug("WorkflowInstanceQueryService.getById({})", id);
        WorkflowInstance instance = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WorkflowInstance not found: " + id));
        return mapper.toReadDto(instance);
    }

    @Override
    public List<WorkflowInstanceReadDto> getByState(String stateCode) {
        log.debug("WorkflowInstanceQueryService.getByState({})", stateCode);
        return repository.findByCurrentState_Code(stateCode)
                .stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDto> getByInitiatingActor(Long employeeId) {
        log.debug("WorkflowInstanceQueryService.getByInitiatingActor({})", employeeId);
        return repository.findByInitiatedBy_Id(employeeId)
                .stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDto> getByLastActor(Long employeeId) {
        log.debug("WorkflowInstanceQueryService.getByLastActor({})", employeeId);
        return repository.findByLastActor_Id(employeeId)
                .stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDto> getByDateRange(LocalDate from, LocalDate to) {
        log.debug("WorkflowInstanceQueryService.getByDateRange({}, {})", from, to);
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.atTime(LocalTime.MAX);
        return repository.findByStartedAtBetween(fromDt, toDt)
                .stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkflowInstanceReadDto> getByTargetType(String targetTypeCode) {
        log.debug("WorkflowInstanceQueryService.getByTargetType({})", targetTypeCode);
        return repository.findByTargetType_Code(targetTypeCode)
                .stream()
                .map(mapper::toReadDto)
                .collect(Collectors.toList());
    }
}
