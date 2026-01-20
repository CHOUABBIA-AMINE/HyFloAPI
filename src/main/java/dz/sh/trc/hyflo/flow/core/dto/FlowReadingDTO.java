/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReadingDTO
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.dto;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.dto.DataSourceDTO;
import dz.sh.trc.hyflo.flow.common.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.type.dto.MeasurementTypeDTO;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.core.dto.InfrastructureDTO;
import jakarta.validation.constraints.NotNull;
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
public class FlowReadingDTO extends GenericDTO<FlowReading> {
    
    // ==================== TEMPORAL ====================
    @NotNull(message = "Reading timestamp is required")
    private LocalDateTime readingTimestamp;
    
    // ==================== MEASUREMENT TYPE ====================
    @NotNull(message = "Measurement type ID is required")
    private Long measurementTypeId;
    
    private MeasurementTypeDTO measurementType;
    
    // ==================== MEASUREMENT VALUES ====================
    private Double flowRate;
    private Double pressure;
    private Double temperature;
    private Double density;
    private Double cumulativeVolume;
    
    // ==================== VALIDATION ====================
    @NotNull(message = "Validation status ID is required")
    private Long validationStatusId;
    
    private ValidationStatusDTO validationStatus;
    
    private Long qualityFlagId;
    private QualityFlagDTO qualityFlag;
    
    private String notes;
    private LocalDateTime validatedAt;
    
    // ==================== EMPLOYEES ====================
    @NotNull(message = "Operator ID is required")
    private Long recordedById;
    
    private EmployeeDTO recordedBy;
    
    private Long validatedById;
    private EmployeeDTO validatedBy;
    
    // ==================== STRUCTURE (Derived from operator's job) ====================
    private StructureDTO structure;
    
    // ==================== INFRASTRUCTURE ====================
    @NotNull(message = "Infrastructure ID is required")
    private Long infrastructureId;
    
    private InfrastructureDTO infrastructure;
    
    // ==================== PRODUCT ====================
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private ProductDTO product;
    
    // ==================== DATA SOURCE ====================
    private Long dataSourceId;
    private DataSourceDTO dataSource;
    
    private String scadaTagId;
    
    @Override
    public FlowReading toEntity() {
        FlowReading entity = new FlowReading();
        entity.setId(getId());
        entity.setReadingTimestamp(this.readingTimestamp);
        entity.setFlowRate(this.flowRate);
        entity.setPressure(this.pressure);
        entity.setTemperature(this.temperature);
        entity.setDensity(this.density);
        entity.setCumulativeVolume(this.cumulativeVolume);
        entity.setNotes(this.notes);
        entity.setValidatedAt(this.validatedAt);
        entity.setScadaTagId(this.scadaTagId);
        return entity;
    }
    
    @Override
    public void updateEntity(FlowReading entity) {
        if (this.readingTimestamp != null) {
            entity.setReadingTimestamp(this.readingTimestamp);
        }
        if (this.flowRate != null) {
            entity.setFlowRate(this.flowRate);
        }
        if (this.pressure != null) {
            entity.setPressure(this.pressure);
        }
        if (this.temperature != null) {
            entity.setTemperature(this.temperature);
        }
        if (this.density != null) {
            entity.setDensity(this.density);
        }
        if (this.cumulativeVolume != null) {
            entity.setCumulativeVolume(this.cumulativeVolume);
        }
        if (this.notes != null) {
            entity.setNotes(this.notes);
        }
        if (this.validatedAt != null) {
            entity.setValidatedAt(this.validatedAt);
        }
        if (this.scadaTagId != null) {
            entity.setScadaTagId(this.scadaTagId);
        }
    }
    
    public static FlowReadingDTO fromEntity(FlowReading entity) {
        if (entity == null) return null;
        
        return FlowReadingDTO.builder()
                .id(entity.getId())
                .readingTimestamp(entity.getReadingTimestamp())
                .flowRate(entity.getFlowRate())
                .pressure(entity.getPressure())
                .temperature(entity.getTemperature())
                .density(entity.getDensity())
                .cumulativeVolume(entity.getCumulativeVolume())
                .notes(entity.getNotes())
                .validatedAt(entity.getValidatedAt())
                .scadaTagId(entity.getScadaTagId())
                .build();
    }
}
