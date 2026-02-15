/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSystemDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added comprehensive @Schema documentation
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.organization.dto.StructureDTO;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for pipeline systems representing logical groupings of pipelines transporting specific products")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSystemDTO extends GenericDTO<PipelineSystem> {

    @Schema(
        description = "Unique identification code for the pipeline system",
        example = "PS-OLZ-001",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Schema(
        description = "Official name of the pipeline system",
        example = "Hassi Messaoud Crude Oil Transport System",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "ID of the product transported by this pipeline system (required)",
        example = "2",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Product is required")
    private Long productId;

    @Schema(
        description = "ID of the current operational status (required)",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Operational status is required")
    private Long operationalStatusId;

    @Schema(
        description = "ID of the organizational structure managing this pipeline system (required)",
        example = "7",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Structure is required")
    private Long structureId;
    
    @Schema(description = "Product transported by this pipeline system")
    private ProductDTO product;
    
    @Schema(description = "Current operational status of the pipeline system")
    private OperationalStatusDTO operationalStatus;
    
    @Schema(description = "Organizational structure managing this pipeline system")
    private StructureDTO structure;

    @Override
    public PipelineSystem toEntity() {
        PipelineSystem entity = new PipelineSystem();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setName(this.name);
        
        if (this.productId != null) {
            Product product = new Product();
            product.setId(this.productId);
            entity.setProduct(product);
        }
        
        if (this.operationalStatusId != null) {
        	OperationalStatus operationalStatus = new OperationalStatus();
        	operationalStatus.setId(this.operationalStatusId);
            entity.setOperationalStatus(operationalStatus);
        }
        
        if (this.structureId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.structureId);
            entity.setStructure(structure);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(PipelineSystem entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.name != null) entity.setName(this.name);
        
        if (this.productId != null) {
            Product product = new Product();
            product.setId(this.productId);
            entity.setProduct(product);
        }
        
        if (this.operationalStatusId != null) {
        	OperationalStatus operationalStatus = new OperationalStatus();
        	operationalStatus.setId(this.operationalStatusId);
            entity.setOperationalStatus(operationalStatus);
        }
        
        if (this.structureId != null) {
        	Structure structure = new Structure();
        	structure.setId(this.structureId);
            entity.setStructure(structure);
        }
    }

    public static PipelineSystemDTO fromEntity(PipelineSystem entity) {
        if (entity == null) return null;
        
        return PipelineSystemDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .operationalStatusId(entity.getOperationalStatus() != null ? entity.getOperationalStatus().getId() : null)
                .structureId(entity.getStructure() != null ? entity.getStructure().getId() : null)
                
                .product(entity.getProduct() != null ? ProductDTO.fromEntity(entity.getProduct()) : null)
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .structure(entity.getStructure() != null ? StructureDTO.fromEntity(entity.getStructure()) : null)
                .build();
    }
}
