/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowAlertService
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowThresholdRepository;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;

@Service
public class FlowAlertService extends GenericService<FlowAlert, FlowAlertDTO, Long> {
    
    private final FlowAlertRepository flowAlertRepository;
    private final FlowThresholdRepository flowThresholdRepository;
    private final FlowReadingRepository flowReadingRepository;
    private final EmployeeRepository employeeRepository;
    private final FlowThresholdService flowThresholdService;
    private final FlowReadingService flowReadingService;
    
    public FlowAlertService(
            FlowAlertRepository flowAlertRepository,
            FlowThresholdRepository flowThresholdRepository,
            FlowReadingRepository flowReadingRepository,
            EmployeeRepository employeeRepository,
            FlowThresholdService flowThresholdService,
            FlowReadingService flowReadingService) {
        this.flowAlertRepository = flowAlertRepository;
        this.flowThresholdRepository = flowThresholdRepository;
        this.flowReadingRepository = flowReadingRepository;
        this.employeeRepository = employeeRepository;
        this.flowThresholdService = flowThresholdService;
        this.flowReadingService = flowReadingService;
    }
    
    @Override
    protected JpaRepository<FlowAlert, Long> getRepository() {
        return flowAlertRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "FlowAlert";
    }
    
    @Override
    protected FlowAlertDTO toDTO(FlowAlert entity) {
        FlowAlertDTO dto = FlowAlertDTO.fromEntity(entity);
        
        if (entity.getThreshold() != null) {
            dto.setThresholdId(entity.getThreshold().getId());
            dto.setThreshold(flowThresholdService.toDTO(entity.getThreshold()));
        }
        
        if (entity.getFlowReading() != null) {
            dto.setFlowReadingId(entity.getFlowReading().getId());
            dto.setFlowReading(flowReadingService.toDTO(entity.getFlowReading()));
        }
        
        if (entity.getAcknowledgedBy() != null) {
            dto.setAcknowledgedById(entity.getAcknowledgedBy().getId());
            dto.setAcknowledgedBy(EmployeeDTO.fromEntity(entity.getAcknowledgedBy()));
        }
        
        if (entity.getResolvedBy() != null) {
            dto.setResolvedById(entity.getResolvedBy().getId());
            dto.setResolvedBy(EmployeeDTO.fromEntity(entity.getResolvedBy()));
        }
        
        return dto;
    }
    
    @Override
    protected FlowAlert toEntity(FlowAlertDTO dto) {
        FlowAlert entity = dto.toEntity();
        
        entity.setThreshold(flowThresholdRepository.findById(dto.getThresholdId())
            .orElseThrow(() -> new ResourceNotFoundException("Threshold not found")));
        
        if (dto.getFlowReadingId() != null) {
            entity.setFlowReading(flowReadingRepository.findById(dto.getFlowReadingId())
                .orElseThrow(() -> new ResourceNotFoundException("Flow reading not found")));
        }
        
        if (dto.getAcknowledgedById() != null) {
            entity.setAcknowledgedBy(employeeRepository.findById(dto.getAcknowledgedById())
                .orElseThrow(() -> new ResourceNotFoundException("Acknowledged by employee not found")));
        }
        
        if (dto.getResolvedById() != null) {
            entity.setResolvedBy(employeeRepository.findById(dto.getResolvedById())
                .orElseThrow(() -> new ResourceNotFoundException("Resolved by employee not found")));
        }
        
        return entity;
    }
    
    @Override
    protected void updateEntityFromDTO(FlowAlert entity, FlowAlertDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getThresholdId() != null) {
            entity.setThreshold(flowThresholdRepository.findById(dto.getThresholdId())
                .orElseThrow(() -> new ResourceNotFoundException("Threshold not found")));
        }
        
        if (dto.getFlowReadingId() != null) {
            entity.setFlowReading(flowReadingRepository.findById(dto.getFlowReadingId())
                .orElseThrow(() -> new ResourceNotFoundException("Flow reading not found")));
        }
        
        if (dto.getAcknowledgedById() != null) {
            entity.setAcknowledgedBy(employeeRepository.findById(dto.getAcknowledgedById())
                .orElseThrow(() -> new ResourceNotFoundException("Acknowledged by employee not found")));
        }
        
        if (dto.getResolvedById() != null) {
            entity.setResolvedBy(employeeRepository.findById(dto.getResolvedById())
                .orElseThrow(() -> new ResourceNotFoundException("Resolved by employee not found")));
        }
    }
    
    @Transactional
    public FlowAlertDTO acknowledgeAlert(Long id, Long employeeId) {
        FlowAlert alert = flowAlertRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        
        alert.setStatus("ACKNOWLEDGED");
        alert.setAcknowledgedAt(LocalDateTime.now());
        alert.setAcknowledgedBy(employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found")));
        
        return toDTO(flowAlertRepository.save(alert));
    }
    
    @Transactional
    public FlowAlertDTO resolveAlert(Long id, Long employeeId, String resolutionNotes) {
        FlowAlert alert = flowAlertRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        
        alert.setStatus("RESOLVED");
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolvedBy(employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found")));
        alert.setResolutionNotes(resolutionNotes);
        
        return toDTO(flowAlertRepository.save(alert));
    }
    
    public List<FlowAlertDTO> findActiveAlerts() {
        List<String> activeStatuses = Arrays.asList("NEW", "ACKNOWLEDGED", "INVESTIGATING");
        return flowAlertRepository.findByStatusIn(activeStatuses)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowAlertDTO> findActiveAlertsByInfrastructure(Long infrastructureId) {
        List<String> activeStatuses = Arrays.asList("NEW", "ACKNOWLEDGED", "INVESTIGATING");
        return flowAlertRepository.findByInfrastructureAndStatus(infrastructureId, activeStatuses)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}
