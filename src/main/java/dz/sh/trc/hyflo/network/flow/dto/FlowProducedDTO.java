/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowProducedDTO
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
import dz.sh.trc.hyflo.network.core.dto.ProcessingPlantDTO;
import dz.sh.trc.hyflo.network.core.model.ProcessingPlant;
import dz.sh.trc.hyflo.network.flow.model.FlowProduced;
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
public class FlowProducedDTO extends GenericDTO<FlowProduced> {

    @NotBlank(message = "Estimated volume is required")
    private double volumeEstimated;

    private double volumeProduced;

    private LocalDate measurementDate;

    @NotNull(message = "Pipeline is required")
    private Long processingPlantId;
    
    private ProcessingPlantDTO processingPlant;

    @Override
    public FlowProduced toEntity() {
    	FlowProduced flow = new FlowProduced();
        flow.setId(getId());
        flow.setVolumeEstimated(this.volumeEstimated);
        flow.setVolumeProduced(this.volumeProduced);
        flow.setMeasurementDate(this.measurementDate);
        
        if (this.processingPlantId != null) {
        	ProcessingPlant processingPlant = new ProcessingPlant();
        	processingPlant.setId(this.processingPlantId);
            flow.setProcessingPlant(processingPlant);
        }
        
        return flow;
    }

    @Override
    public void updateEntity(FlowProduced flow) {
        if (this.volumeEstimated != 0) flow.setVolumeEstimated(this.volumeEstimated);
        if (this.volumeProduced != 0) flow.setVolumeProduced(this.volumeProduced);
        if (this.measurementDate != null) flow.setMeasurementDate(this.measurementDate);
        
        if (this.processingPlantId != null) {
        	ProcessingPlant processingPlant = new ProcessingPlant();
        	processingPlant.setId(this.processingPlantId);
            flow.setProcessingPlant(processingPlant);
        }
    }

    public static FlowProducedDTO fromEntity(FlowProduced flow) {
        if (flow == null) return null;
        
        return FlowProducedDTO.builder()
                .id(flow.getId())
                .volumeEstimated(flow.getVolumeEstimated())
                .volumeProduced(flow.getVolumeProduced())
                .measurementDate(flow.getMeasurementDate())
                .processingPlantId(flow.getProcessingPlant() != null ? flow.getProcessingPlant().getId() : null)
                
                .processingPlant(flow.getProcessingPlant() != null ? ProcessingPlantDTO.fromEntity(flow.getProcessingPlant()) : null)
                .build();
    }
}
