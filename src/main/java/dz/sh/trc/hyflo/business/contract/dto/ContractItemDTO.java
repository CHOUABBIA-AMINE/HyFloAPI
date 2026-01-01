/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractItemDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Contract
 *
 **/

package dz.sh.trc.hyflo.business.contract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.business.contract.model.ContractItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ContractItem Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractItemDTO extends GenericDTO<ContractItem> {

    @NotBlank(message = "Designation is required")
    private String designation;
    
    @NotBlank(message = "Reference is required")
    private String reference;
    
    private double quantity;
    
    private double unitPrice;
    
    @Size(max = 500, message = "Observation must not exceed 500 characters")
    private String observation;
    
    @NotNull(message = "Contract is required")
    private Long contractId;

    @Override
    public ContractItem toEntity() {
        ContractItem entity = new ContractItem();
        entity.setId(this.getId());
        entity.setDesignation(this.designation);
        entity.setReference(this.reference);
        entity.setQuantity(this.quantity);
        entity.setUnitPrice(this.unitPrice);
        entity.setObservation(this.observation);
        return entity;
    }

    @Override
    public void updateEntity(ContractItem entity) {
        if (this.designation != null) entity.setDesignation(this.designation);
        if (this.reference != null) entity.setReference(this.reference);
        entity.setQuantity(this.quantity);
        entity.setUnitPrice(this.unitPrice);
        if (this.observation != null) entity.setObservation(this.observation);
    }

    public static ContractItemDTO fromEntity(ContractItem entity) {
        if (entity == null) return null;
        return ContractItemDTO.builder()
                .id(entity.getId())
                .designation(entity.getDesignation())
                .reference(entity.getReference())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .observation(entity.getObservation())
                .contractId(entity.getContract() != null ? entity.getContract().getId() : null)
                .build();
    }
}
