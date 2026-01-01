/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ApprovalStatusDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Data Transfer Object
 *	@Layer		: Business / Core / DTO
 *	@Package	: Business / Core / DTO
 *
 **/

package dz.sh.trc.hyflo.business.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.business.core.model.ApprovalStatus;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ApprovalStatus Data Transfer Object - Extends GenericDTO
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
public class ApprovalStatusDTO extends GenericDTO<ApprovalStatus> {

    @Size(max = 200, message = "Arabic designation must not exceed 200 characters")
    private String designationAr;

    @Size(max = 200, message = "English designation must not exceed 200 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 200, message = "French designation must not exceed 200 characters")
    private String designationFr;

    // ========== GENERIC DTO IMPLEMENTATION ==========

    @Override
    public ApprovalStatus toEntity() {
        ApprovalStatus approvalStatus = new ApprovalStatus();
        approvalStatus.setId(getId());
        approvalStatus.setDesignationAr(this.designationAr);
        approvalStatus.setDesignationEn(this.designationEn);
        approvalStatus.setDesignationFr(this.designationFr);
        return approvalStatus;
    }

    @Override
    public void updateEntity(ApprovalStatus approvalStatus) {
        if (this.designationAr != null) {
            approvalStatus.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            approvalStatus.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            approvalStatus.setDesignationFr(this.designationFr);
        }
    }

    // ========== FACTORY METHOD ==========

    /**
     * Create DTO from entity
     */
    public static ApprovalStatusDTO fromEntity(ApprovalStatus approvalStatus) {
        if (approvalStatus == null) return null;
        
        return ApprovalStatusDTO.builder()
                .id(approvalStatus.getId())
                .designationAr(approvalStatus.getDesignationAr())
                .designationEn(approvalStatus.getDesignationEn())
                .designationFr(approvalStatus.getDesignationFr())
                .build();
    }
}
