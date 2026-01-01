/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ProviderRepresentatorDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Provider
 *
 **/

package dz.mdn.iaas.business.provider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.configuration.template.GenericDTO;
import dz.mdn.iaas.business.provider.model.ProviderRepresentator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * ProviderRepresentator Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderRepresentatorDTO extends GenericDTO<ProviderRepresentator> {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstname;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastname;
    
    @Size(max = 200, message = "Birth date must not exceed 200 characters")
    private String birthDate;
    
    @Size(max = 100, message = "Birth place must not exceed 100 characters")
    private String birthPlace;
    
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;
    
    @Size(max = 50, message = "Job title must not exceed 50 characters")
    private String jobTitle;
    
    @Size(max = 100, message = "Mobile phone number must not exceed 100 characters")
    private String mobilePhoneNumber;
    
    @Size(max = 100, message = "Fix phone number must not exceed 100 characters")
    private String fixPhoneNumber;
    
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String mail;
    
    @NotNull(message = "Provider is required")
    private Long providerId;

    @Override
    public ProviderRepresentator toEntity() {
        ProviderRepresentator entity = new ProviderRepresentator();
        entity.setId(this.getId());
        entity.setFirstname(this.firstname);
        entity.setLastname(this.lastname);
        entity.setBirthDate(this.birthDate);
        entity.setBirthPlace(this.birthPlace);
        entity.setAddress(this.address);
        entity.setJobTitle(this.jobTitle);
        entity.setMobilePhoneNumber(this.mobilePhoneNumber);
        entity.setFixPhoneNumber(this.fixPhoneNumber);
        entity.setMail(this.mail);
        return entity;
    }

    @Override
    public void updateEntity(ProviderRepresentator entity) {
        if (this.firstname != null) entity.setFirstname(this.firstname);
        if (this.lastname != null) entity.setLastname(this.lastname);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
        if (this.birthPlace != null) entity.setBirthPlace(this.birthPlace);
        if (this.address != null) entity.setAddress(this.address);
        if (this.jobTitle != null) entity.setJobTitle(this.jobTitle);
        if (this.mobilePhoneNumber != null) entity.setMobilePhoneNumber(this.mobilePhoneNumber);
        if (this.fixPhoneNumber != null) entity.setFixPhoneNumber(this.fixPhoneNumber);
        if (this.mail != null) entity.setMail(this.mail);
    }

    public static ProviderRepresentatorDTO fromEntity(ProviderRepresentator entity) {
        if (entity == null) return null;
        
        return ProviderRepresentatorDTO.builder()
                .id(entity.getId())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .birthDate(entity.getBirthDate())
                .birthPlace(entity.getBirthPlace())
                .address(entity.getAddress())
                .jobTitle(entity.getJobTitle())
                .mobilePhoneNumber(entity.getMobilePhoneNumber())
                .fixPhoneNumber(entity.getFixPhoneNumber())
                .mail(entity.getMail())
                .providerId(entity.getProvider() != null ? entity.getProvider().getId() : null)
                .build();
    }
}
