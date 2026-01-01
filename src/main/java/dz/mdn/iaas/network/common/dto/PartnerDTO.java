/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PartnerDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Common
 *
 **/

package dz.mdn.iaas.network.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.mdn.iaas.common.administration.dto.CountryDTO;
import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.network.common.model.Partner;
import dz.mdn.iaas.network.type.dto.PartnerTypeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Partner Data Transfer Object - Extends GenericDTO
 * Maps Partner entity extending GenericModel
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - name (F_01) - optional name
 * - shortName (F_02) - unique, required identifier
 * - partnerTypeId (F_03) - required foreign key to PartnerType
 * - countryId (F_04) - required foreign key to Country
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerDTO extends GenericDTO<Partner> {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Short name is required")
    @Size(min = 2, max = 20, message = "Short name must be between 2 and 20 characters")
    private String shortName;

    @NotNull(message = "Partner type ID is required")
    private Long partnerTypeId;

    @NotNull(message = "Country ID is required")
    private Long countryId;
    
    private PartnerTypeDTO partnerType;
    
    private CountryDTO country;

    @Override
    public Partner toEntity() {
        Partner entity = new Partner();
        entity.setId(getId());
        entity.setName(this.name);
        entity.setShortName(this.shortName);
        
        if (this.partnerTypeId != null) {
            dz.mdn.iaas.network.type.model.PartnerType type = 
                new dz.mdn.iaas.network.type.model.PartnerType();
            type.setId(this.partnerTypeId);
            entity.setPartnerType(type);
        }
        
        if (this.countryId != null) {
            dz.mdn.iaas.common.administration.model.Country country = 
                new dz.mdn.iaas.common.administration.model.Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Partner entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.shortName != null) entity.setShortName(this.shortName);
        
        if (this.partnerTypeId != null) {
            dz.mdn.iaas.network.type.model.PartnerType type = 
                new dz.mdn.iaas.network.type.model.PartnerType();
            type.setId(this.partnerTypeId);
            entity.setPartnerType(type);
        }
        
        if (this.countryId != null) {
            dz.mdn.iaas.common.administration.model.Country country = 
                new dz.mdn.iaas.common.administration.model.Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
    }

    public static PartnerDTO fromEntity(Partner entity) {
        if (entity == null) return null;
        
        return PartnerDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .shortName(entity.getShortName())
                .partnerTypeId(entity.getPartnerType() != null ? entity.getPartnerType().getId() : null)
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                
                .partnerType(entity.getPartnerType() != null ? PartnerTypeDTO.fromEntity(entity.getPartnerType()) : null)
                .country(entity.getCountry() != null ? CountryDTO.fromEntity(entity.getCountry()) : null)
                .build();
    }
}
