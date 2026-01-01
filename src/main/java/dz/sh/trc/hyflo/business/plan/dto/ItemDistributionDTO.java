/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemDistributionDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Plan
 *
 **/

package dz.sh.trc.hyflo.business.plan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.business.plan.model.ItemDistribution;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ItemDistribution Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDistributionDTO extends GenericDTO<ItemDistribution> {

    private float quantity;
    
    @NotNull(message = "Planned item is required")
    private Long plannedItemId;
    
    @NotNull(message = "Structure is required")
    private Long structureId;

    @Override
    public ItemDistribution toEntity() {
        ItemDistribution entity = new ItemDistribution();
        entity.setId(this.getId());
        entity.setQuantity(this.quantity);
        return entity;
    }

    @Override
    public void updateEntity(ItemDistribution entity) {
        entity.setQuantity(this.quantity);
    }

    public static ItemDistributionDTO fromEntity(ItemDistribution entity) {
        if (entity == null) return null;
        return ItemDistributionDTO.builder()
                .id(entity.getId())
                .quantity(entity.getQuantity())
                .plannedItemId(entity.getPlannedItem() != null ? entity.getPlannedItem().getId() : null)
                .structureId(entity.getStructure() != null ? entity.getStructure().getId() : null)
                .build();
    }
}
