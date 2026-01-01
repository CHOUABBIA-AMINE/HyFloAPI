/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PersonDTO
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.iaas.common.administration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.common.administration.model.Person;
import dz.mdn.iaas.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Person Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDTO extends GenericDTO<Person> {

    @NotBlank(message = "Arabic last name is required")
    @Size(max = 100, message = "Arabic last name must not exceed 100 characters")
    private String lastNameAr;

    @NotBlank(message = "Arabic first name is required")
    @Size(max = 100, message = "Arabic first name must not exceed 100 characters")
    private String firstNameAr;

    @NotBlank(message = "Latin last name is required")
    @Size(max = 100, message = "Latin last name must not exceed 100 characters")
    private String lastNameLt;

    @NotBlank(message = "Latin first name is required")
    @Size(max = 100, message = "Latin first name must not exceed 100 characters")
    private String firstNameLt;

    private Date birthDate;

    @Size(max = 100, message = "Birth place must not exceed 100 characters")
    private String birthPlace;

    private Long countryId;

    @Override
    public Person toEntity() {
        Person entity = new Person();
        entity.setId(this.getId());
        entity.setLastNameAr(this.lastNameAr);
        entity.setFirstNameAr(this.firstNameAr);
        entity.setLastNameLt(this.lastNameLt);
        entity.setFirstNameLt(this.firstNameLt);
        entity.setBirthDate(this.birthDate);
        entity.setBirthPlace(this.birthPlace);
        return entity;
    }

    @Override
    public void updateEntity(Person entity) {
        if (this.lastNameAr != null) entity.setLastNameAr(this.lastNameAr);
        if (this.firstNameAr != null) entity.setFirstNameAr(this.firstNameAr);
        if (this.lastNameLt != null) entity.setLastNameLt(this.lastNameLt);
        if (this.firstNameLt != null) entity.setFirstNameLt(this.firstNameLt);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
        if (this.birthPlace != null) entity.setBirthPlace(this.birthPlace);
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
                .birthPlace(entity.getBirthPlace())
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .build();
    }
}