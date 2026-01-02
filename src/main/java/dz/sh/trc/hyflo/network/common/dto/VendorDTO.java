/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: VendorDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.dto.CountryDTO;
import dz.sh.trc.hyflo.general.localization.model.Country;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.type.dto.VendorTypeDTO;
import dz.sh.trc.hyflo.network.type.model.VendorType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorDTO extends GenericDTO<Vendor> {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(min = 2, max = 20, message = "Short name must be between 2 and 20 characters")
    private String shortName;

    @NotNull(message = "Vendor type ID is required")
    private Long vendorTypeId;

    @NotNull(message = "Country ID is required")
    private Long countryId;
    
    private VendorTypeDTO vendorType;
    
    private CountryDTO country;

    @Override
    public Vendor toEntity() {
        Vendor entity = new Vendor();
        entity.setId(getId());
        entity.setName(this.name);
        entity.setShortName(this.shortName);
        
        if (this.vendorTypeId != null) {
            VendorType type = new VendorType();
            type.setId(this.vendorTypeId);
            entity.setVendorType(type);
        }
        
        if (this.countryId != null) {
            Country country = new Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Vendor entity) {
        if (this.name != null) entity.setName(this.name);
        if (this.shortName != null) entity.setShortName(this.shortName);
        
        if (this.vendorTypeId != null) {
            VendorType type = new VendorType();
            type.setId(this.vendorTypeId);
            entity.setVendorType(type);
        }
        
        if (this.countryId != null) {
            Country country = new Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
    }

    public static VendorDTO fromEntity(Vendor entity) {
        if (entity == null) return null;
        
        return VendorDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .shortName(entity.getShortName())
                .vendorTypeId(entity.getVendorType() != null ? entity.getVendorType().getId() : null)
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                
                .vendorType(entity.getVendorType() != null ? VendorTypeDTO.fromEntity(entity.getVendorType()) : null)
                .country(entity.getCountry() != null ? CountryDTO.fromEntity(entity.getCountry()) : null)
                .build();
    }
}
