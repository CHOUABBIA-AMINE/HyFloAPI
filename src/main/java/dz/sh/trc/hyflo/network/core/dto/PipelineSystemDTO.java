/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineSystemDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.common.dto.OperationalStatusDTO;
import dz.sh.trc.hyflo.network.common.dto.ProductDTO;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import dz.sh.trc.hyflo.network.organization.dto.RegionDTO;
import dz.sh.trc.hyflo.network.organization.model.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * PipelineSystem Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - code (F_01) - unique, required
 * - designationAr (F_02) - required
 * - designationEn (F_03) - required
 * - designationFr (F_04) - required
 * - productId (F_05) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSystemDTO extends GenericDTO<PipelineSystem> {

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "OperationalStatus is required")
    private Long operationalStatusId;

    @NotNull(message = "Region is required")
    private Long regionId;
    
    private ProductDTO product;
    
    private OperationalStatusDTO operationalStatus;
    
    private RegionDTO region;

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
        
        if (this.regionId != null) {
        	Region region = new Region();
        	region.setId(this.regionId);
            entity.setRegion(region);
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
        
        if (this.regionId != null) {
        	Region region = new Region();
        	region.setId(this.regionId);
            entity.setRegion(region);
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
                .regionId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                
                .product(entity.getProduct() != null ? ProductDTO.fromEntity(entity.getProduct()) : null)
                .operationalStatus(entity.getOperationalStatus() != null ? OperationalStatusDTO.fromEntity(entity.getOperationalStatus()) : null)
                .region(entity.getRegion() != null ? RegionDTO.fromEntity(entity.getRegion()) : null)
                .build();
    }
}
