/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EmployeeDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-15-2026 - Added comprehensive @Schema documentation
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
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Job;
import dz.sh.trc.hyflo.system.utility.dto.FileDTO;
import dz.sh.trc.hyflo.system.utility.model.File;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "Data Transfer Object for SONATRACH employee with complete personal information, job assignment, and registration details")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO extends GenericDTO<Employee> {

    @Schema(
        description = "Last name in Arabic script",
        example = "شعبية",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic last name must not exceed 100 characters")
    private String lastNameAr;

    @Schema(
        description = "First name in Arabic script",
        example = "أمين",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic first name must not exceed 100 characters")
    private String firstNameAr;

    @Schema(
        description = "Last name in Latin script (required)",
        example = "CHOUABBIA",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Latin last name is required")
    @Size(max = 100, message = "Latin last name must not exceed 100 characters")
    private String lastNameLt;

    @Schema(
        description = "First name in Latin script (required)",
        example = "Amine",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Latin first name is required")
    @Size(max = 100, message = "Latin first name must not exceed 100 characters")
    private String firstNameLt;

    @Schema(
        description = "Date of birth (must be in the past)",
        example = "1990-05-15",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Past(message = "Birth date must be in the past")
    private Date birthDate;

    @Schema(
        description = "Place of birth in Arabic script",
        example = "الجزائر العاصمة",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Birth place must not exceed 200 characters")
    private String birthPlaceAr;

    @Schema(
        description = "Place of birth in Latin script",
        example = "Alger",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Birth place must not exceed 200 characters")
    private String birthPlaceLt;

    @Schema(
        description = "Physical address in Arabic script",
        example = "شارع ديدوش مراد 15، الجزائر",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String addressAr;

    @Schema(
        description = "Physical address in Latin script",
        example = "15 Rue Didouche Mourad, Alger",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String addressLt;

    @Schema(
        description = "Employee registration number or matricule SONATRACH",
        example = "EMP-2025-001234",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String registrationNumber;

    @Schema(
        description = "ID of the locality where the employee was born",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long birthLocalityId;

    @Schema(
        description = "ID of the locality of current residential address",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long addressLocalityId;

    @Schema(
        description = "ID of the country of citizenship or nationality",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long countryId;

    @Schema(
        description = "ID of the profile picture or photo identification",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long pictureId;

    @Schema(
        description = "ID of the job position assigned to this employee",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long jobId;

    @Schema(
        description = "Birth locality details (populated when fetching with locality information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalityDTO birthLocality;

    @Schema(
        description = "Address locality details (populated when fetching with locality information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalityDTO addressLocality;

    @Schema(
        description = "Country details (populated when fetching with country information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private CountryDTO country;

    @Schema(
        description = "Profile picture file details (populated when fetching with file information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private FileDTO picture;

    @Schema(
        description = "Job position details (populated when fetching with job information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private JobDTO job;

    @Override
    public Employee toEntity() {
        Employee entity = new Employee();
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
        entity.setRegistrationNumber(this.registrationNumber);
        
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
        
        if (this.jobId != null) {
        	Job job = new Job();
        	job.setId(this.jobId);
            entity.setJob(job);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Employee entity) {
        if (this.lastNameAr != null) entity.setLastNameAr(this.lastNameAr);
        if (this.firstNameAr != null) entity.setFirstNameAr(this.firstNameAr);
        if (this.lastNameLt != null) entity.setLastNameLt(this.lastNameLt);
        if (this.firstNameLt != null) entity.setFirstNameLt(this.firstNameLt);
        if (this.birthDate != null) entity.setBirthDate(this.birthDate);
        if (this.birthPlaceAr != null) entity.setBirthPlaceAr(this.birthPlaceAr);
        if (this.birthPlaceLt != null) entity.setBirthPlaceLt(this.birthPlaceLt);
        if (this.addressAr != null) entity.setAddressAr(this.addressAr);
        if (this.addressLt != null) entity.setAddressLt(this.addressLt);
        if (this.registrationNumber != null) entity.setRegistrationNumber(this.registrationNumber);
        
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
		
		if (this.jobId != null) {
			Job job = new Job();
			job.setId(this.jobId);
		    entity.setJob(job);
		}
    }

    public static EmployeeDTO fromEntity(Employee entity) {
        if (entity == null) return null;
        return EmployeeDTO.builder()
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
                .registrationNumber(entity.getRegistrationNumber())
                .birthLocalityId(entity.getBirthLocality() != null ? entity.getBirthLocality().getId() : null)
                .addressLocalityId(entity.getAddressLocality() != null ? entity.getAddressLocality().getId() : null)
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .pictureId(entity.getPicture() != null ? entity.getPicture().getId() : null)
                .jobId(entity.getJob() != null ? entity.getJob().getId() : null)
                
                .birthLocality(entity.getBirthLocality() != null ? LocalityDTO.fromEntity(entity.getBirthLocality()) : null)
                .addressLocality(entity.getAddressLocality() != null ? LocalityDTO.fromEntity(entity.getAddressLocality()) : null)
                .country(entity.getCountry() != null ? CountryDTO.fromEntity(entity.getCountry()) : null)
                .picture(entity.getPicture() != null ? FileDTO.fromEntity(entity.getPicture()) : null)
                .job(entity.getJob() != null ? JobDTO.fromEntity(entity.getJob()) : null)
                .build();
    }
}