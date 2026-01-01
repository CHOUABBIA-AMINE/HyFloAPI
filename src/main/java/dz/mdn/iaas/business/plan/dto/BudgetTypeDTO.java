/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: BudgetTypeDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.business.plan.model.BudgetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * BudgetType Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetTypeDTO extends GenericDTO<BudgetType> {

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr;

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr;
    
    @Size(max = 20, message = "Arabic acronym must not exceed 20 characters")
    private String acronymAr;
    
    @Size(max = 20, message = "English acronym must not exceed 20 characters")
    private String acronymEn;
    
    @NotBlank(message = "French acronym is required")
    @Size(max = 20, message = "French acronym must not exceed 20 characters")
    private String acronymFr;

    @Override
    public BudgetType toEntity() {
        BudgetType entity = new BudgetType();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setAcronymAr(this.acronymAr);
        entity.setAcronymEn(this.acronymEn);
        entity.setAcronymFr(this.acronymFr);
        return entity;
    }

    @Override
    public void updateEntity(BudgetType entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.acronymAr != null) entity.setAcronymAr(this.acronymAr);
        if (this.acronymEn != null) entity.setAcronymEn(this.acronymEn);
        if (this.acronymFr != null) entity.setAcronymFr(this.acronymFr);
    }

    public static BudgetTypeDTO fromEntity(BudgetType entity) {
        if (entity == null) return null;
        return BudgetTypeDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .acronymAr(entity.getAcronymAr())
                .acronymEn(entity.getAcronymEn())
                .acronymFr(entity.getAcronymFr())
                .build();
    }
}
