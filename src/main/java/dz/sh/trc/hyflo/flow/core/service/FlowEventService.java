/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowEventService
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.flow.core.dto.FlowEventDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.repository.EmployeeRepository;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import dz.sh.trc.hyflo.network.core.repository.InfrastructureRepository;

@Service
public class FlowEventService extends GenericService<FlowEvent, FlowEventDTO, Long> {
    
    private final FlowEventRepository flowEventRepository;
    private final InfrastructureRepository infrastructureRepository;
    private final EmployeeRepository employeeRepository;
    private final FlowReadingRepository flowReadingRepository;
    private final FlowAlertRepository flowAlertRepository;
    private final FlowReadingService flowReadingService;
    private final FlowAlertService flowAlertService;
    
    public FlowEventService(
            FlowEventRepository flowEventRepository,
            InfrastructureRepository infrastructureRepository,
            EmployeeRepository employeeRepository,
            FlowReadingRepository flowReadingRepository,
            FlowAlertRepository flowAlertRepository,
            FlowReadingService flowReadingService,
            FlowAlertService flowAlertService) {
        this.flowEventRepository = flowEventRepository;
        this.infrastructureRepository = infrastructureRepository;
        this.employeeRepository = employeeRepository;
        this.flowReadingRepository = flowReadingRepository;
        this.flowAlertRepository = flowAlertRepository;
        this.flowReadingService = flowReadingService;
        this.flowAlertService = flowAlertService;
    }
    
    @Override
    protected JpaRepository<FlowEvent, Long> getRepository() {
        return flowEventRepository;
    }
    
    @Override
    protected String getEntityName() {
        return "FlowEvent";
    }
    
    @Override
    protected FlowEventDTO toDTO(FlowEvent entity) {
        FlowEventDTO dto = FlowEventDTO.fromEntity(entity);
        
        if (entity.getInfrastructure() != null) {
            dto.setInfrastructureId(entity.getInfrastructure().getId());
            dto.setInfrastructure(InfrastructureDTO.fromEntity(entity.getInfrastructure()));
        }
        
        if (entity.getReportedBy() != null) {
            dto.setReportedById(entity.getReportedBy().getId());
            dto.setReportedBy(EmployeeDTO.fromEntity(entity.getReportedBy()));
        }
        
        if (entity.getRelatedReading() != null) {
            dto.setRelatedReadingId(entity.getRelatedReading().getId());
            dto.setRelatedReading(flowReadingService.toDTO(entity.getRelatedReading()));
        }
        
        if (entity.getRelatedAlert() != null) {
            dto.setRelatedAlertId(entity.getRelatedAlert().getId());
            dto.setRelatedAlert(flowAlertService.toDTO(entity.getRelatedAlert()));
        }
        
        return dto;
    }
    
    @Override
    protected FlowEvent toEntity(FlowEventDTO dto) {
        FlowEvent entity = dto.toEntity();
        
        entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
            .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        
        entity.setReportedBy(employeeRepository.findById(dto.getReportedById())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found")));
        
        if (dto.getRelatedReadingId() != null) {
            entity.setRelatedReading(flowReadingRepository.findById(dto.getRelatedReadingId())
                .orElseThrow(() -> new ResourceNotFoundException("Flow reading not found")));
        }
        
        if (dto.getRelatedAlertId() != null) {
            entity.setRelatedAlert(flowAlertRepository.findById(dto.getRelatedAlertId())
                .orElseThrow(() -> new ResourceNotFoundException("Flow alert not found")));
        }
        
        return entity;
    }
    
    @Override
    protected void updateEntityFromDTO(FlowEvent entity, FlowEventDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getInfrastructureId() != null) {
            entity.setInfrastructure(infrastructureRepository.findById(dto.getInfrastructureId())
                .orElseThrow(() -> new ResourceNotFoundException("Infrastructure not found")));
        }
        
        if (dto.getReportedById() != null) {
            entity.setReportedBy(employeeRepository.findById(dto.getReportedById())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found")));
        }
        
        if (dto.getRelatedReadingId() != null) {
            entity.setRelatedReading(flowReadingRepository.findById(dto.getRelatedReadingId())
                .orElseThrow(() -> new ResourceNotFoundException("Flow reading not found")));
        }
        
        if (dto.getRelatedAlertId() != null) {
            entity.setRelatedAlert(flowAlertRepository.findById(dto.getRelatedAlertId())
                .orElseThrow(() -> new ResourceNotFoundException("Flow alert not found")));
        }
    }
    
    public List<FlowEventDTO> findByInfrastructure(Long infrastructureId) {
        return flowEventRepository.findByInfrastructure(infrastructureId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<FlowEventDTO> findActiveEventsWithFlowImpact() {
        return flowEventRepository.findActiveEventsWithFlowImpact()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}
