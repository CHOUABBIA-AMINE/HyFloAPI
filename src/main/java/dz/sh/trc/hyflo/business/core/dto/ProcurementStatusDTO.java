/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProcurementStatusDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Data Transfer Object
 *	@Layer		: Business / Core / DTO
 *	@Package	: Business / Core / DTO
 *
 **/

package dz.sh.trc.hyflo.business.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.business.core.model.ProcurementStatus;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ProcurementStatus Data Transfer Object - Extends GenericDTO
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
public class ProcurementStatusDTO extends GenericDTO<ProcurementStatus> {

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr;

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr;

    // ========== GENERIC DTO IMPLEMENTATION ==========

    @Override
    public ProcurementStatus toEntity() {
        ProcurementStatus procurementStatus = new ProcurementStatus();
        procurementStatus.setId(getId());
        procurementStatus.setDesignationAr(this.designationAr);
        procurementStatus.setDesignationEn(this.designationEn);
        procurementStatus.setDesignationFr(this.designationFr);
        return procurementStatus;
    }

    @Override
    public void updateEntity(ProcurementStatus procurementStatus) {
        if (this.designationAr != null) {
            procurementStatus.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            procurementStatus.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            procurementStatus.setDesignationFr(this.designationFr);
        }
    }

    // ========== FACTORY METHOD ==========

    /**
     * Create DTO from entity
     */
    public static ProcurementStatusDTO fromEntity(ProcurementStatus procurementStatus) {
        if (procurementStatus == null) return null;
        
        return ProcurementStatusDTO.builder()
                .id(procurementStatus.getId())
                .designationAr(procurementStatus.getDesignationAr())
                .designationEn(procurementStatus.getDesignationEn())
                .designationFr(procurementStatus.getDesignationFr())
                .build();
    }
}
