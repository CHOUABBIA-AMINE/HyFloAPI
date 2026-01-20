/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: FlowReadingDTO
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.dto;

import java.time.LocalDateTime;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.flow.model.FlowReading;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FlowReadingDTO extends GenericDTO<FlowReading> {
    
    @NotNull(message = "Reading timestamp is required")
    private LocalDateTime readingTimestamp;
    
    @NotNull(message = "Measurement type ID is required")
    private Long measurementTypeId;
    
    private Double flowRate;
    private Double pressure;
    private Double temperature;
    private Double density;
    private Double cumulativeVolume;
    
    @NotNull(message = "Validation status ID is required")
    private Long validationStatusId;
    
    private Long qualityFlagId;
    private String notes;
    private LocalDateTime validatedAt;
    
    @NotNull(message = "Operator ID is required")
    private Long recordedById;
    
    private Long validatedById;
    
    @NotNull(message = "Infrastructure ID is required")
    private Long infrastructureId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    private Long dataSourceId;
    private String scadaTagId;
    
    private String measurementTypeCode;
    private String measurementTypeName;
    
    private String validationStatusCode;
    private String validationStatusName;
    
    private String qualityFlagCode;
    private String qualityFlagName;
    
    private String dataSourceCode;
    private String dataSourceName;
    
    private String infrastructureName;
    private String infrastructureCode;
    
    private String operatorName;
    private String operatorRegistrationNumber;
    
    private String validatorName;
    
    private Long structureId;
    private String structureName;
    private String structureCode;
    
    private String productName;
    
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
        if (this.readingTimestamp != null) entity.setReadingTimestamp(this.readingTimestamp);
        if (this.flowRate != null) entity.setFlowRate(this.flowRate);
        if (this.pressure != null) entity.setPressure(this.pressure);
        if (this.temperature != null) entity.setTemperature(this.temperature);
        if (this.density != null) entity.setDensity(this.density);
        if (this.cumulativeVolume != null) entity.setCumulativeVolume(this.cumulativeVolume);
        if (this.notes != null) entity.setNotes(this.notes);
        if (this.validatedAt != null) entity.setValidatedAt(this.validatedAt);
        if (this.scadaTagId != null) entity.setScadaTagId(this.scadaTagId);
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
