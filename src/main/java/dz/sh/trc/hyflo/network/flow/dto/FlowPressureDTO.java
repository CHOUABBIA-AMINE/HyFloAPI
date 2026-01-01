/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowPressureDTO
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Data Transfer Object
 *	@Layer		: Network / DTO
 *	@Package	: Network / DTO
 *
 **/

package dz.sh.trc.hyflo.network.flow.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.flow.model.FlowPressure;
import dz.sh.trc.hyflo.network.flow.model.MeasurementHour;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * FlowPressure Data Transfer Object - Extends GenericDTO
 * Maps FlowPressure entity which extends Facility which extends Infrastructure
 * 
 * Inherited from Infrastructure:
 * - code, name, installationDate, commissioningDate, decommissioningDate, operationalStatusId
 * 
 * Inherited from Facility:
 * - vendor, location (vendorId, locationId)
 * 
 * FlowPressure specific:
 * - stationType, pipelines
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowPressureDTO extends GenericDTO<FlowPressure> {

    @NotBlank(message = "Name is required")
    private double pressure;

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
    public FlowPressure toEntity() {
        FlowPressure flow = new FlowPressure();
        flow.setId(getId());
        flow.setPressure(this.pressure);
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
    public void updateEntity(FlowPressure flow) {
        if (this.pressure != 0) flow.setPressure(this.pressure);
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

    public static FlowPressureDTO fromEntity(FlowPressure flow) {
        if (flow == null) return null;
        
        return FlowPressureDTO.builder()
                .id(flow.getId())
                .pressure(flow.getPressure())
                .measurementDate(flow.getMeasurementDate())
                .measurementHourId(flow.getMeasurementHour() != null ? flow.getMeasurementHour().getId() : null)
                .pipelineId(flow.getPipeline() != null ? flow.getPipeline().getId() : null)
                
                .measurementHour(flow.getMeasurementHour() != null ? MeasurementHourDTO.fromEntity(flow.getMeasurementHour()) : null)
                .pipeline(flow.getPipeline() != null ? PipelineDTO.fromEntity(flow.getPipeline()) : null)
                .build();
    }
}
