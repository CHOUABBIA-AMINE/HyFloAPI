/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProcurementDirectorDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Data Transfer Object
 *	@Layer		: Business / Core / DTO
 *	@Package	: Business / Core / DTO
 *
 **/

package dz.mdn.iaas.business.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.business.core.model.ProcurementDirector;
import dz.mdn.iaas.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ProcurementDirector Data Transfer Object - Extends GenericDTO
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
public class ProcurementDirectorDTO extends GenericDTO<ProcurementDirector> {

    @Size(max = 300, message = "Arabic designation must not exceed 300 characters")
    private String designationAr;

    @Size(max = 300, message = "English designation must not exceed 300 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 300, message = "French designation must not exceed 300 characters")
    private String designationFr;

    // ========== GENERIC DTO IMPLEMENTATION ==========

    @Override
    public ProcurementDirector toEntity() {
        ProcurementDirector procurementDirector = new ProcurementDirector();
        procurementDirector.setId(getId());
        procurementDirector.setDesignationAr(this.designationAr);
        procurementDirector.setDesignationEn(this.designationEn);
        procurementDirector.setDesignationFr(this.designationFr);
        return procurementDirector;
    }

    @Override
    public void updateEntity(ProcurementDirector procurementDirector) {
        if (this.designationAr != null) {
            procurementDirector.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            procurementDirector.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            procurementDirector.setDesignationFr(this.designationFr);
        }
    }

    // ========== FACTORY METHOD ==========

    /**
     * Create DTO from entity
     */
    public static ProcurementDirectorDTO fromEntity(ProcurementDirector procurementDirector) {
        if (procurementDirector == null) return null;
        
        return ProcurementDirectorDTO.builder()
                .id(procurementDirector.getId())
                .designationAr(procurementDirector.getDesignationAr())
                .designationEn(procurementDirector.getDesignationEn())
                .designationFr(procurementDirector.getDesignationFr())
                .build();
    }
}
