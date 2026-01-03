/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowTransportedDTO
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
import dz.sh.trc.hyflo.network.flow.model.FlowTransported;
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
public class FlowTransportedDTO extends GenericDTO<FlowTransported> {

    @NotBlank(message = "Estimated volume is required")
    private double volumeEstimated;

    private double volumeTransported;

    private LocalDate measurementDate;

    @NotNull(message = "Pipeline is required")
    private Long pipelineId;
    
    private PipelineDTO pipeline;

    @Override
    public FlowTransported toEntity() {
    	FlowTransported flow = new FlowTransported();
        flow.setId(getId());
        flow.setVolumeEstimated(this.volumeEstimated);
        flow.setVolumeTransported(this.volumeTransported);
        flow.setMeasurementDate(this.measurementDate);
        
        if (this.pipelineId != null) {
        	Pipeline pipeline = new Pipeline();
        	pipeline.setId(this.pipelineId);
            flow.setPipeline(pipeline);
        }
        
        return flow;
    }

    @Override
    public void updateEntity(FlowTransported flow) {
        if (this.volumeEstimated != 0) flow.setVolumeEstimated(this.volumeEstimated);
        if (this.volumeTransported != 0) flow.setVolumeTransported(this.volumeTransported);
        if (this.measurementDate != null) flow.setMeasurementDate(this.measurementDate);
        
        if (this.pipelineId != null) {
        	Pipeline pipeline = new Pipeline();
        	pipeline.setId(this.pipelineId);
            flow.setPipeline(pipeline);
        }
    }

    public static FlowTransportedDTO fromEntity(FlowTransported flow) {
        if (flow == null) return null;
        
        return FlowTransportedDTO.builder()
                .id(flow.getId())
                .volumeEstimated(flow.getVolumeEstimated())
                .volumeTransported(flow.getVolumeTransported())
                .measurementDate(flow.getMeasurementDate())
                .pipelineId(flow.getPipeline() != null ? flow.getPipeline().getId() : null)
                
                .pipeline(flow.getPipeline() != null ? PipelineDTO.fromEntity(flow.getPipeline()) : null)
                .build();
    }
}
