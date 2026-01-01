/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineSegmentDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Core
 *
 **/

package dz.mdn.iaas.network.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.network.core.model.Pipeline;
import dz.mdn.iaas.network.core.model.PipelineSegment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * PipelineSegment Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - code (F_01) - required
 * - pipelineId (F_02) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSegmentDTO extends GenericDTO<PipelineSegment> {

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @NotNull(message = "Pipeline is required")
    private Long pipelineId;

    private PipelineDTO pipeline;

    @Override
    public PipelineSegment toEntity() {
        PipelineSegment entity = new PipelineSegment();
        entity.setId(getId());
        entity.setCode(this.code);
        
        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(PipelineSegment entity) {
        if (this.code != null) entity.setCode(this.code);
        
        if (this.pipelineId != null) {
            Pipeline pipeline = new Pipeline();
            pipeline.setId(this.pipelineId);
            entity.setPipeline(pipeline);
        }
    }

    public static PipelineSegmentDTO fromEntity(PipelineSegment entity) {
        if (entity == null) return null;
        
        return PipelineSegmentDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .pipelineId(entity.getPipeline() != null ? entity.getPipeline().getId() : null)
                
                .pipeline(entity.getPipeline() != null ? PipelineDTO.fromEntity(entity.getPipeline()) : null)
                .build();
    }
}
