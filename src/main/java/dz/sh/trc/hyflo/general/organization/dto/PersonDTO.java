/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PersonDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.dto.CountryDTO;
import dz.sh.trc.hyflo.general.localization.dto.LocalityDTO;
import dz.sh.trc.hyflo.general.localization.model.Country;
import dz.sh.trc.hyflo.general.localization.model.Locality;
import dz.sh.trc.hyflo.general.organization.model.Person;
import dz.sh.trc.hyflo.system.utility.dto.FileDTO;
import dz.sh.trc.hyflo.system.utility.model.File;
import jakarta.validation.constraints.NotBlank;
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
public class PersonDTO extends GenericDTO<Person> {

    @Size(max = 100, message = "Arabic last name must not exceed 100 characters")
    private String lastNameAr;

    @Size(max = 100, message = "Arabic first name must not exceed 100 characters")
    private String firstNameAr;

    @NotBlank(message = "Latin last name is required")
    @Size(max = 100, message = "Latin last name must not exceed 100 characters")
    private String lastNameLt;

    @NotBlank(message = "Latin first name is required")
    @Size(max = 100, message = "Latin first name must not exceed 100 characters")
    private String firstNameLt;

    private Date birthDate;

    @Size(max = 200, message = "Birth place must not exceed 200 characters")
    private String birthPlaceAr;

    @Size(max = 200, message = "Birth place must not exceed 200 characters")
    private String birthPlaceLt;

    @Size(max = 200, message = "Birth place must not exceed 200 characters")
    private String addressAr;

    @Size(max = 200, message = "Birth place must not exceed 200 characters")
    private String addressLt;

    private Long birthLocalityId;

    private Long addressLocalityId;

    private Long countryId;

    private Long pictureId;

    private LocalityDTO birthLocality;

    private LocalityDTO addressLocality;

    private FileDTO picture;

    private CountryDTO country;

    @Override
    public Person toEntity() {
        Person entity = new Person();
        entity.setId(this.getId());
        entity.setLastNameAr(this.lastNameAr);
        entity.setFirstNameAr(this.firstNameAr);
        entity.setLastNameLt(this.lastNameLt);
        entity.setFirstNameLt(this.firstNameLt);
        entity.setBirthDate(this.birthDate);
        entity.setBirthPlaceAr(this.birthPlaceAr);
        entity.setBirthPlaceLt(this.birthPlaceLt);
        entity.setAddressAr(this.addressAr);
        entity.setAddressLt(this.addressLt);
        
        if (this.birthLocalityId != null) {
            Locality birthLocality = new Locality();
            birthLocality.setId(this.birthLocalityId);
            entity.setBirthLocality(birthLocality);
        }
        
        if (this.addressLocalityId != null) {
            Locality addressLocality = new Locality();
            addressLocality.setId(this.addressLocalityId);
            entity.setAddressLocality(addressLocality);
        }
        
        if (this.countryId != null) {
        	Country country = new Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
        
        if (this.pictureId != null) {
            File picture = new File();
            picture.setId(this.pictureId);
            entity.setPicture(picture);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Person entity) {
        if (this.lastNameAr != null) entity.setLastNameAr(this.lastNameAr);
        if (this.firstNameAr != null) entity.setFirstNameAr(this.firstNameAr);
        if (this.lastNameLt != null) entity.setLastNameLt(this.lastNameLt);
        if (this.firstNameLt != null) entity.setFirstNameLt(this.firstNameLt);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
        if (this.birthPlaceAr != null) entity.setBirthPlaceAr(this.birthPlaceAr);
        if (this.birthPlaceLt != null) entity.setBirthPlaceLt(this.birthPlaceLt);
        if (this.addressAr != null) entity.setAddressAr(this.addressAr);
        if (this.addressLt != null) entity.setAddressLt(this.addressLt);
        
        if (this.birthLocalityId != null) {
            Locality birthLocality = new Locality();
            birthLocality.setId(this.birthLocalityId);
            entity.setBirthLocality(birthLocality);
        }
        
        if (this.addressLocalityId != null) {
            Locality addressLocality = new Locality();
            addressLocality.setId(this.addressLocalityId);
            entity.setAddressLocality(addressLocality);
        }
        
        if (this.countryId != null) {
        	Country country = new Country();
            country.setId(this.countryId);
            entity.setCountry(country);
        }
        
        if (this.pictureId != null) {
            File picture = new File();
            picture.setId(this.pictureId);
            entity.setPicture(picture);
        }
    }

    public static PersonDTO fromEntity(Person entity) {
        if (entity == null) return null;
        return PersonDTO.builder()
                .id(entity.getId())
                .lastNameAr(entity.getLastNameAr())
                .firstNameAr(entity.getFirstNameAr())
                .lastNameLt(entity.getLastNameLt())
                .firstNameLt(entity.getFirstNameLt())
                .birthDate(entity.getBirthDate())
                .birthPlaceAr(entity.getBirthPlaceAr())
                .birthPlaceLt(entity.getBirthPlaceLt())
                .addressAr(entity.getAddressAr())
                .addressLt(entity.getAddressLt())
                .birthLocalityId(entity.getBirthLocality() != null ? entity.getBirthLocality().getId() : null)
                .addressLocalityId(entity.getAddressLocality() != null ? entity.getAddressLocality().getId() : null)
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .pictureId(entity.getPicture() != null ? entity.getPicture().getId() : null)
                
                .birthLocality(entity.getBirthLocality() != null ? LocalityDTO.fromEntity(entity.getBirthLocality()) : null)
                .addressLocality(entity.getAddressLocality() != null ? LocalityDTO.fromEntity(entity.getAddressLocality()) : null)
                .country(entity.getCountry() != null ? CountryDTO.fromEntity(entity.getCountry()) : null)
                .picture(entity.getPicture() != null ? FileDTO.fromEntity(entity.getPicture()) : null)
                .build();
    }
}