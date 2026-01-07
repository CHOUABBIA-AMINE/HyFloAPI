/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowConsumedDTO
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
import dz.sh.trc.hyflo.network.core.dto.TerminalDTO;
import dz.sh.trc.hyflo.network.core.model.Terminal;
import dz.sh.trc.hyflo.network.flow.model.FlowConsumed;
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
public class FlowConsumedDTO extends GenericDTO<FlowConsumed> {

    @NotBlank(message = "Estimated volume is required")
    private double volumeEstimated;

    private double volumeConsumed;

    private LocalDate measurementDate;

    @NotNull(message = "Pipeline is required")
    private Long terminalId;
    
    private TerminalDTO terminal;

    @Override
    public FlowConsumed toEntity() {
    	FlowConsumed flow = new FlowConsumed();
        flow.setId(getId());
        flow.setVolumeEstimated(this.volumeEstimated);
        flow.setVolumeConsumed(this.volumeConsumed);
        flow.setMeasurementDate(this.measurementDate);
        
        if (this.terminalId != null) {
        	Terminal terminal = new Terminal();
        	terminal.setId(this.terminalId);
            flow.setTerminal(terminal);
        }
        
        return flow;
    }

    @Override
    public void updateEntity(FlowConsumed flow) {
        if (this.volumeEstimated != 0) flow.setVolumeEstimated(this.volumeEstimated);
        if (this.volumeConsumed != 0) flow.setVolumeConsumed(this.volumeConsumed);
        if (this.measurementDate != null) flow.setMeasurementDate(this.measurementDate);
        
        if (this.terminalId != null) {
        	Terminal terminal = new Terminal();
        	terminal.setId(this.terminalId);
            flow.setTerminal(terminal);
        }
    }

    public static FlowConsumedDTO fromEntity(FlowConsumed flow) {
        if (flow == null) return null;
        
        return FlowConsumedDTO.builder()
                .id(flow.getId())
                .volumeEstimated(flow.getVolumeEstimated())
                .volumeConsumed(flow.getVolumeConsumed())
                .measurementDate(flow.getMeasurementDate())
                .terminalId(flow.getTerminal() != null ? flow.getTerminal().getId() : null)
                
                .terminal(flow.getTerminal() != null ? TerminalDTO.fromEntity(flow.getTerminal()) : null)
                .build();
    }
}
