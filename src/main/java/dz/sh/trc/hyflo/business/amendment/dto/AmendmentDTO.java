/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Amendment
 *
 **/

package dz.mdn.iaas.business.amendment.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.business.amendment.model.Amendment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Amendment Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmendmentDTO extends GenericDTO<Amendment> {

    private int internalId;
    private String reference;
    private String designationAr;
    private String designationEn;
    
    @NotNull(message = "French designation is required")
    private String designationFr;
    
    private double amount;
    private double transferableAmount;
    private Date startDate;
    private Date approvalDate;
    private Date notifyDate;
    private String observation;
    
    @NotNull(message = "Contract is required")
    private Long contractId;
    
    @NotNull(message = "Amendment type is required")
    private Long amendmentTypeId;
    
    @NotNull(message = "Procurement status is required")
    private Long procurementStatusId;
    
    @NotNull(message = "Amendment step is required")
    private Long amendmentStepId;
    
    private Long approvalStatusId;
    
    @NotNull(message = "Currency is required")
    private Long currencyId;

    @Override
    public Amendment toEntity() {
        Amendment entity = new Amendment();
        entity.setId(getId());
        entity.setInternalId(this.internalId);
        entity.setReference(this.reference);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setAmount(this.amount);
        entity.setTransferableAmount(this.transferableAmount);
        entity.setStartDate(this.startDate);
        entity.setApprovalDate(this.approvalDate);
        entity.setNotifyDate(this.notifyDate);
        entity.setObservation(this.observation);
        return entity;
    }

    @Override
    public void updateEntity(Amendment entity) {
        if (this.reference != null) entity.setReference(this.reference);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        entity.setAmount(this.amount);
        entity.setTransferableAmount(this.transferableAmount);
        if (this.startDate != null) entity.setStartDate(this.startDate);
        if (this.approvalDate != null) entity.setApprovalDate(this.approvalDate);
        if (this.notifyDate != null) entity.setNotifyDate(this.notifyDate);
        if (this.observation != null) entity.setObservation(this.observation);
    }

    public static AmendmentDTO fromEntity(Amendment entity) {
        if (entity == null) return null;
        
        return AmendmentDTO.builder()
                .id(entity.getId())
                .internalId(entity.getInternalId())
                .reference(entity.getReference())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .amount(entity.getAmount())
                .transferableAmount(entity.getTransferableAmount())
                .startDate(entity.getStartDate())
                .approvalDate(entity.getApprovalDate())
                .notifyDate(entity.getNotifyDate())
                .observation(entity.getObservation())
                .build();
    }

}
