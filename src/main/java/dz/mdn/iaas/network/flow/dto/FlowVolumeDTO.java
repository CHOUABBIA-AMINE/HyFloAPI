/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowVolumeDTO
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Data Transfer Object
 *	@Layer		: Network / DTO
 *	@Package	: Network / DTO
 *
 **/

package dz.mdn.iaas.network.flow.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.network.core.dto.PipelineDTO;
import dz.mdn.iaas.network.core.model.Pipeline;
import dz.mdn.iaas.network.flow.model.FlowVolume;
import dz.mdn.iaas.network.flow.model.MeasurementHour;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * FlowVolume Data Transfer Object - Extends GenericDTO
 * Maps FlowVolume entity which extends Facility which extends Infrastructure
 * 
 * Inherited from Infrastructure:
 * - code, name, installationDate, commissioningDate, decommissioningDate, operationalStatusId
 * 
 * Inherited from Facility:
 * - vendor, location (vendorId, locationId)
 * 
 * FlowVolume specific:
 * - stationType, pipelines
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowVolumeDTO extends GenericDTO<FlowVolume> {

    // Infrastructure fields
    @NotBlank(message = "Code is required")
    private double volume;

    private LocalDate measurementDate;

    @NotNull(message = "Measurement Hour ID is required")
    private Long measurementHourId;

    @NotNull(message = "Pipeline is required")
    private Long pipelineId;
    
    private MeasurementHourDTO measurementHour;
    private PipelineDTO pipeline;

    @Builder.Default
    private Set<Long> pipelineIds = new HashSet<>();

    @Override
    public FlowVolume toEntity() {
        FlowVolume flow = new FlowVolume();
        flow.setId(getId());
        flow.setVolume(this.volume);
        flow.setMeasurementDate(this.measurementDate);
        
        if (this.measurementHourId != null) {
        	MeasurementHour hour = new MeasurementHour();
            hour.setId(this.measurementHourId);
            flow.setMeasurementHour(hour);
        }
        
        if (this.pipelineId != null) {
        	Pipeline pipeline = new Pipeline();
        	pipeline.setId(this.pipelineId);
            flow.setPipeline(pipeline);
        }
        
        return flow;
    }

    @Override
    public void updateEntity(FlowVolume flow) {
        if (this.volume != 0) flow.setVolume(this.volume);
        if (this.measurementDate != null) flow.setMeasurementDate(this.measurementDate);
        
        if (this.measurementHourId != null) {
        	MeasurementHour hour = new MeasurementHour();
            hour.setId(this.measurementHourId);
            flow.setMeasurementHour(hour);
        }
        
        if (this.pipelineId != null) {
        	Pipeline pipeline = new Pipeline();
        	pipeline.setId(this.pipelineId);
            flow.setPipeline(pipeline);
        }
    }

    public static FlowVolumeDTO fromEntity(FlowVolume flow) {
        if (flow == null) return null;
        
        return FlowVolumeDTO.builder()
                .id(flow.getId())
                .volume(flow.getVolume())
                .measurementDate(flow.getMeasurementDate())
                .measurementHourId(flow.getMeasurementHour() != null ? flow.getMeasurementHour().getId() : null)
                .pipelineId(flow.getPipeline() != null ? flow.getPipeline().getId() : null)
                
                .measurementHour(flow.getMeasurementHour() != null ? MeasurementHourDTO.fromEntity(flow.getMeasurementHour()) : null)
                .pipeline(flow.getPipeline() != null ? PipelineDTO.fromEntity(flow.getPipeline()) : null)
                .build();
    }
}
