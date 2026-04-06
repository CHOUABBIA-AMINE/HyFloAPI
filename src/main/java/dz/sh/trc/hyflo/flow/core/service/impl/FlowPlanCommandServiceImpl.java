/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanCommandServiceImpl
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Service (Command Implementation)
 *  @Package    : Flow / Core
 *
 *  @Description: Command implementation for FlowPlan write operations.
 *                Status transitions:
 *                  DRAFT → APPROVED  (approve)
 *                  APPROVED → SUPERSEDED  (supersede)
 *                  delete: DRAFT only.
 *                Uses FlowPlanMapper exclusively. No raw entity returns.
 *                Exception handling mirrors FlowReadingCommandServiceImpl.
 *
 **/

package dz.sh.trc.hyflo.flow.core.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.common.repository.PlanStatusRepository;
import dz.sh.trc.hyflo.flow.core.dto.FlowPlanReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowPlanCommandDTO;
import dz.sh.trc.hyflo.flow.core.mapper.FlowPlanMapper;
import dz.sh.trc.hyflo.flow.core.model.FlowPlan;
import dz.sh.trc.hyflo.flow.core.repository.FlowPlanRepository;
import dz.sh.trc.hyflo.flow.core.service.FlowPlanCommandService;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlowPlanCommandServiceImpl implements FlowPlanCommandService {

    private final FlowPlanRepository flowPlanRepository;
    private final PlanStatusRepository planStatusRepository;
    private final PipelineRepository pipelineRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public FlowPlanReadDTO create(FlowPlanCommandDTO command) {
        log.info("Creating FlowPlan for pipeline ID: {} date: {} scenario: {}",
                command.getPipelineId(), command.getPlanDate(), command.getScenarioCode());

        pipelineRepository.findById(command.getPipelineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pipeline not found: " + command.getPipelineId()));

        planStatusRepository.findById(command.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlanStatus not found: " + command.getStatusId()));

        FlowPlan entity = FlowPlanMapper.toEntity(command);
        FlowPlan saved = flowPlanRepository.save(entity);
        log.debug("Saved FlowPlan ID: {}", saved.getId());
        return FlowPlanMapper.toReadDTO(saved);
    }

    @Override
    public FlowPlanReadDTO update(Long id, FlowPlanCommandDTO command) {
        log.info("Updating FlowPlan ID: {}", id);

        FlowPlan existing = flowPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowPlan not found: " + id));

        String currentCode = existing.getStatus() != null
                ? existing.getStatus().getCode() : "";
        if (!"DRAFT".equalsIgnoreCase(currentCode)) {
            throw new IllegalStateException(
                    "Cannot update FlowPlan (ID=" + id + ") in status " + currentCode
                    + ". Only DRAFT plans may be updated.");
        }

        FlowPlanMapper.updateEntity(command, existing);
        FlowPlan saved = flowPlanRepository.save(existing);
        log.info("Updated FlowPlan ID: {}", saved.getId());
        return FlowPlanMapper.toReadDTO(saved);
    }

    @Override
    public FlowPlanReadDTO approve(Long id, Long approverEmployeeId) {
        log.info("Approving FlowPlan ID: {} by employee: {}", id, approverEmployeeId);

        FlowPlan plan = flowPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowPlan not found: " + id));

        String currentCode = plan.getStatus() != null ? plan.getStatus().getCode() : "";
        if (!"DRAFT".equalsIgnoreCase(currentCode)) {
            throw new IllegalStateException(
                    "Cannot approve FlowPlan (ID=" + id + ") in status " + currentCode
                    + ". Only DRAFT plans may be approved.");
        }

        Employee approver = employeeRepository.findById(approverEmployeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + approverEmployeeId));

        planStatusRepository.findByCode("APPROVED")
                .ifPresent(plan::setStatus);
        plan.setApprovedBy(approver);
        plan.setApprovedAt(LocalDateTime.now());

        FlowPlan saved = flowPlanRepository.save(plan);
        log.info("FlowPlan ID: {} approved", saved.getId());
        return FlowPlanMapper.toReadDTO(saved);
    }

    @Override
    public FlowPlanReadDTO supersede(Long id) {
        log.info("Superseding FlowPlan ID: {}", id);

        FlowPlan plan = flowPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowPlan not found: " + id));

        String currentCode = plan.getStatus() != null ? plan.getStatus().getCode() : "";
        if (!"APPROVED".equalsIgnoreCase(currentCode)) {
            throw new IllegalStateException(
                    "Cannot supersede FlowPlan (ID=" + id + ") in status " + currentCode
                    + ". Only APPROVED plans may be superseded.");
        }

        planStatusRepository.findByCode("SUPERSEDED")
                .ifPresent(plan::setStatus);

        FlowPlan saved = flowPlanRepository.save(plan);
        log.info("FlowPlan ID: {} superseded", saved.getId());
        return FlowPlanMapper.toReadDTO(saved);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting FlowPlan ID: {}", id);

        FlowPlan plan = flowPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "FlowPlan not found: " + id));

        String currentCode = plan.getStatus() != null ? plan.getStatus().getCode() : "";
        if (!"DRAFT".equalsIgnoreCase(currentCode)) {
            throw new IllegalStateException(
                    "Cannot delete FlowPlan (ID=" + id + ") in status " + currentCode
                    + ". Only DRAFT plans may be deleted.");
        }

        flowPlanRepository.deleteById(id);
        log.info("Deleted FlowPlan ID: {}", id);
    }
}
