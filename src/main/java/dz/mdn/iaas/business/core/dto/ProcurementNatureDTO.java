/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProcurementNatureDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Data Transfer Object
 *	@Layer		: Business / Core / DTO
 *	@Package	: Business / Core / DTO
 *
 **/

package dz.mdn.iaas.business.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.business.core.model.ProcurementNature;
import dz.mdn.iaas.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ProcurementNature Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - designationAr (F_01) - optional
 * - designationEn (F_02) - optional
 * - designationFr (F_03) - unique, required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcurementNatureDTO extends GenericDTO<ProcurementNature> {

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr;

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr;

    // ========== GENERIC DTO IMPLEMENTATION ==========

    @Override
    public ProcurementNature toEntity() {
        ProcurementNature procurementNature = new ProcurementNature();
        procurementNature.setId(getId());
        procurementNature.setDesignationAr(this.designationAr);
        procurementNature.setDesignationEn(this.designationEn);
        procurementNature.setDesignationFr(this.designationFr);
        return procurementNature;
    }

    @Override
    public void updateEntity(ProcurementNature procurementNature) {
        if (this.designationAr != null) {
            procurementNature.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            procurementNature.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            procurementNature.setDesignationFr(this.designationFr);
        }
    }

    // ========== FACTORY METHOD ==========

    /**
     * Create DTO from entity
     */
    public static ProcurementNatureDTO fromEntity(ProcurementNature procurementNature) {
        if (procurementNature == null) return null;
        
        return ProcurementNatureDTO.builder()
                .id(procurementNature.getId())
                .designationAr(procurementNature.getDesignationAr())
                .designationEn(procurementNature.getDesignationEn())
                .designationFr(procurementNature.getDesignationFr())
                .build();
    }
}
