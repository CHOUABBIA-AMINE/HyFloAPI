/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowThresholdDTO
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowThreshold;
import dz.sh.trc.hyflo.flow.type.dto.MeasurementTypeDTO;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
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
public class FlowThresholdDTO extends GenericDTO<FlowThreshold> {
    
    @NotBlank(message = "Threshold name is required")
    private String name;
    
    @NotNull(message = "Measurement type ID is required")
    private Long measurementTypeId;
    
    private MeasurementTypeDTO measurementType;
    
    @NotBlank(message = "Parameter is required")
    @Pattern(regexp = "FLOW_RATE|PRESSURE|TEMPERATURE|DENSITY|VOLUME")
    private String parameter;
    
    private Double minValue;
    private Double maxValue;
    
    @NotBlank(message = "Severity is required")
    @Pattern(regexp = "INFO|WARNING|CRITICAL|EMERGENCY")
    private String severity;
    
    @NotNull(message = "Infrastructure ID is required")
    private Long infrastructureId;
    
    private InfrastructureDTO infrastructure;
    
    private Long productId;
    private ProductDTO product;
    
    @NotNull(message = "Active status is required")
    private Boolean isActive;
    
    private String description;
    private Integer notificationDelayMinutes;
    
    @Override
    public FlowThreshold toEntity() {
        FlowThreshold entity = new FlowThreshold();
        entity.setId(getId());
        entity.setName(this.name);
        entity.setParameter(this.parameter);
        entity.setMinValue(this.minValue);
        entity.setMaxValue(this.maxValue);
        entity.setSeverity(this.severity);
        entity.setIsActive(this.isActive);
        entity.setDescription(this.description);
        entity.setNotificationDelayMinutes(this.notificationDelayMinutes);
        return entity;
    }
    
    @Override
    public void updateEntity(FlowThreshold entity) {
        if (this.name != null) {
            entity.setName(this.name);
        }
        if (this.parameter != null) {
            entity.setParameter(this.parameter);
        }
        if (this.minValue != null) {
            entity.setMinValue(this.minValue);
        }
        if (this.maxValue != null) {
            entity.setMaxValue(this.maxValue);
        }
        if (this.severity != null) {
            entity.setSeverity(this.severity);
        }
        if (this.isActive != null) {
            entity.setIsActive(this.isActive);
        }
        if (this.description != null) {
            entity.setDescription(this.description);
        }
        if (this.notificationDelayMinutes != null) {
            entity.setNotificationDelayMinutes(this.notificationDelayMinutes);
        }
    }
    
    public static FlowThresholdDTO fromEntity(FlowThreshold entity) {
        if (entity == null) return null;
        
        return FlowThresholdDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .parameter(entity.getParameter())
                .minValue(entity.getMinValue())
                .maxValue(entity.getMaxValue())
                .severity(entity.getSeverity())
                .isActive(entity.getIsActive())
                .description(entity.getDescription())
                .notificationDelayMinutes(entity.getNotificationDelayMinutes())
                .build();
    }
}
