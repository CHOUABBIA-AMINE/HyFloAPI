/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeDTO
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Administration
 *
 **/

package dz.sh.trc.hyflo.common.administration.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.common.administration.model.Country;
import dz.sh.trc.hyflo.common.administration.model.Employee;
import dz.sh.trc.hyflo.common.administration.model.Job;
import dz.sh.trc.hyflo.common.administration.model.MilitaryRank;
import dz.sh.trc.hyflo.common.administration.model.Structure;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Employee Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO extends GenericDTO<Employee> {

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

    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String registrationNumber;

    private Long jobId;

    private Long structureId;

    private Long militaryRankId;

    @Override
    public Employee toEntity() {
        Employee entity = new Employee();
        entity.setId(this.getId());
        entity.setLastNameAr(this.lastNameAr);
        entity.setFirstNameAr(this.firstNameAr);
        entity.setLastNameLt(this.lastNameLt);
        entity.setFirstNameLt(this.firstNameLt);
        entity.setBirthDate(this.birthDate);
        entity.setBirthPlace(this.birthPlace);
        entity.setRegistrationNumber(this.registrationNumber);
                
        if (this.countryId != null) {
        	Country country = new Country();
        	country.setId(this.countryId);
            entity.setCountry(country);
        }
        
        if (this.jobId != null) {
        	Job job = new Job();
        	job.setId(this.jobId);
            entity.setJob(job);
        }
        
        if (this.structureId != null) {
            Structure structure = new Structure();
            structure.setId(this.structureId);
            entity.setStructure(structure);
        }
        
        if (this.militaryRankId != null) {
        	MilitaryRank rank = new MilitaryRank();
        	rank.setId(this.militaryRankId);
            entity.setMilitaryRank(rank);
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
        if (this.birthPlace != null) entity.setBirthPlace(this.birthPlace);
        if (this.registrationNumber != null) entity.setRegistrationNumber(this.registrationNumber);
        
		if (this.countryId != null) {
			Country country = new Country();
			country.setId(this.countryId);
		    entity.setCountry(country);
		}
		
		if (this.jobId != null) {
			Job job = new Job();
			job.setId(this.jobId);
		    entity.setJob(job);
		}
		
		if (this.structureId != null) {
		    Structure structure = new Structure();
		    structure.setId(this.structureId);
		    entity.setStructure(structure);
		}
		
		if (this.militaryRankId != null) {
			MilitaryRank rank = new MilitaryRank();
			rank.setId(this.militaryRankId);
		    entity.setMilitaryRank(rank);
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
                .birthPlace(entity.getBirthPlace())
                .registrationNumber(entity.getRegistrationNumber())
                .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
                .jobId(entity.getJob() != null ? entity.getJob().getId() : null)
                .structureId(entity.getStructure() != null ? entity.getStructure().getId() : null)
                .militaryRankId(entity.getMilitaryRank() != null ? entity.getMilitaryRank().getId() : null)
                .build();
    }
}