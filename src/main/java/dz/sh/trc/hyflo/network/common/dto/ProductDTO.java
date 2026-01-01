/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProductDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.common.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Product Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - name (F_01) - required
 * - code (F_02) - unique, required
 * - density (F_03) - required
 * - viscosity (F_04) - required
 * - flashPoint (F_05) - required
 * - sulfurContent (F_06) - required
 * - isHazardous (F_07) - required
 * - description (F_08) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO extends GenericDTO<Product> {

	@NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;
    
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @NotNull(message = "Density is required")
    private Double density;

    @NotNull(message = "Viscosity is required")
    private Double viscosity;

    @NotNull(message = "Flash point is required")
    private Double flashPoint;

    @NotNull(message = "Sulfur content is required")
    private Double sulfurContent;

    @NotNull(message = "Hazardous flag is required")
    private Boolean isHazardous;

    @Override
    public Product toEntity() {
        Product entity = new Product();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDensity(this.density);
        entity.setViscosity(this.viscosity);
        entity.setFlashPoint(this.flashPoint);
        entity.setSulfurContent(this.sulfurContent);
        entity.setIsHazardous(this.isHazardous);
        return entity;
    }

    @Override
    public void updateEntity(Product entity) {
    	if (this.code != null) { entity.setCode(this.code); }
        if (this.designationAr != null) { entity.setDesignationAr(this.designationAr); }
        if (this.designationEn != null) { entity.setDesignationEn(this.designationEn); }
        if (this.designationFr != null) { entity.setDesignationFr(this.designationFr); }
        if (this.density != null) entity.setDensity(this.density);
        if (this.viscosity != null) entity.setViscosity(this.viscosity);
        if (this.flashPoint != null) entity.setFlashPoint(this.flashPoint);
        if (this.sulfurContent != null) entity.setSulfurContent(this.sulfurContent);
        if (this.isHazardous != null) entity.setIsHazardous(this.isHazardous);
    }

    public static ProductDTO fromEntity(Product entity) {
        if (entity == null) return null;
        
        return ProductDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .density(entity.getDensity())
                .viscosity(entity.getViscosity())
                .flashPoint(entity.getFlashPoint())
                .sulfurContent(entity.getSulfurContent())
                .isHazardous(entity.getIsHazardous())
                .build();
    }
}
