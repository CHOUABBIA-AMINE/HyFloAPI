/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FinancialOperationDTO
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
import dz.mdn.iaas.business.plan.model.FinancialOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * FinancialOperation Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialOperationDTO extends GenericDTO<FinancialOperation> {

    @NotBlank(message = "Operation is required")
    @Size(max = 200, message = "Operation must not exceed 200 characters")
    private String operation;
    
    @NotBlank(message = "Budget year is required")
    @Size(max = 4, message = "Budget year must not exceed 4 characters")
    private String budgetYear;
    
    @NotNull(message = "Budget type is required")
    private Long budgetTypeId;

    @Override
    public FinancialOperation toEntity() {
        FinancialOperation entity = new FinancialOperation();
        entity.setId(this.getId());
        entity.setOperation(this.operation);
        entity.setBudgetYear(this.budgetYear);
        return entity;
    }

    @Override
    public void updateEntity(FinancialOperation entity) {
        if (this.operation != null) entity.setOperation(this.operation);
        if (this.budgetYear != null) entity.setBudgetYear(this.budgetYear);
    }

    public static FinancialOperationDTO fromEntity(FinancialOperation entity) {
        if (entity == null) return null;
        return FinancialOperationDTO.builder()
                .id(entity.getId())
                .operation(entity.getOperation())
                .budgetYear(entity.getBudgetYear())
                .budgetTypeId(entity.getBudgetType() != null ? entity.getBudgetType().getId() : null)
                .build();
    }
}
