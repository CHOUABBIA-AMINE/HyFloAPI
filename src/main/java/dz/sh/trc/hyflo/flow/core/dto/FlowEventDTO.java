/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowEventDTO
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FlowEventDTO extends GenericDTO<FlowEvent> {
    
    @NotNull(message = "Event timestamp is required")
    private LocalDateTime eventTimestamp;
    
    @NotBlank(message = "Event type is required")
    @Pattern(regexp = "VALVE_OPENED|VALVE_CLOSED|PUMP_STARTED|PUMP_STOPPED|MAINTENANCE_START|MAINTENANCE_END|LEAK_DETECTED|PRESSURE_SURGE|FLOW_INTERRUPTION|FLOW_RESTORED|SYSTEM_STARTUP|SYSTEM_SHUTDOWN|EMERGENCY_STOP|CALIBRATION|INSPECTION|OTHER")
    private String eventType;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Severity is required")
    @Pattern(regexp = "INFO|WARNING|CRITICAL|EMERGENCY")
    private String severity;
    
    @NotNull(message = "Infrastructure ID is required")
    private Long infrastructureId;
    
    private InfrastructureDTO infrastructure;
    
    @NotNull(message = "Reporter ID is required")
    private Long reportedById;
    
    private EmployeeDTO reportedBy;
    
    private Long relatedReadingId;
    private FlowReadingDTO relatedReading;
    
    private Long relatedAlertId;
    private FlowAlertDTO relatedAlert;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "OPEN|IN_PROGRESS|COMPLETED|CANCELLED")
    private String status;
    
    private String actionTaken;
    
    @NotNull(message = "Impact on flow flag is required")
    private Boolean impactOnFlow;
    
    @Override
    public FlowEvent toEntity() {
        FlowEvent entity = new FlowEvent();
        entity.setId(getId());
        entity.setEventTimestamp(this.eventTimestamp);
        entity.setEventType(this.eventType);
        entity.setTitle(this.title);
        entity.setDescription(this.description);
        entity.setSeverity(this.severity);
        entity.setStartTime(this.startTime);
        entity.setEndTime(this.endTime);
        entity.setStatus(this.status);
        entity.setActionTaken(this.actionTaken);
        entity.setImpactOnFlow(this.impactOnFlow);
        return entity;
    }
    
    @Override
    public void updateEntity(FlowEvent entity) {
        if (this.eventTimestamp != null) {
            entity.setEventTimestamp(this.eventTimestamp);
        }
        if (this.eventType != null) {
            entity.setEventType(this.eventType);
        }
        if (this.title != null) {
            entity.setTitle(this.title);
        }
        if (this.description != null) {
            entity.setDescription(this.description);
        }
        if (this.severity != null) {
            entity.setSeverity(this.severity);
        }
        if (this.startTime != null) {
            entity.setStartTime(this.startTime);
        }
        if (this.endTime != null) {
            entity.setEndTime(this.endTime);
        }
        if (this.status != null) {
            entity.setStatus(this.status);
        }
        if (this.actionTaken != null) {
            entity.setActionTaken(this.actionTaken);
        }
        if (this.impactOnFlow != null) {
            entity.setImpactOnFlow(this.impactOnFlow);
        }
    }
    
    public static FlowEventDTO fromEntity(FlowEvent entity) {
        if (entity == null) return null;
        
        return FlowEventDTO.builder()
                .id(entity.getId())
                .eventTimestamp(entity.getEventTimestamp())
                .eventType(entity.getEventType())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .severity(entity.getSeverity())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .status(entity.getStatus())
                .actionTaken(entity.getActionTaken())
                .impactOnFlow(entity.getImpactOnFlow())
                .build();
    }
}
