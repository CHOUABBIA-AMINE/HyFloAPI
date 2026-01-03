/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowVolumeDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.flow.model.FlowVolume;
import dz.sh.trc.hyflo.network.flow.model.MeasurementHour;
import jakarta.validation.constraints.NotBlank;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowVolumeDTO extends GenericDTO<FlowVolume> {

    @NotBlank(message = "volume is required")
    private double volume;

    private LocalDate measurementDate;

    @NotNull(message = "Measurement Hour ID is required")
    private Long measurementHourId;

    @NotNull(message = "Pipeline is required")
    private Long pipelineId;
    
    private MeasurementHourDTO measurementHour;
    
    private PipelineDTO pipeline;

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
