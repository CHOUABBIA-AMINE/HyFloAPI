package dz.sh.trc.hyflo.core.general.organization.dto.request;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record UpdateEmployeeRequest(
        String lastNameAr,
        String firstNameAr,
        
        @NotBlank(message = "Latin last name is mandatory")
        String lastNameLt,
        
        @NotBlank(message = "Latin first name is mandatory")
        String firstNameLt,
        
        @Past(message = "Birth date must be in the past")
        Date birthDate,
        
        String birthPlaceAr,
        String birthPlaceLt,
        String addressAr,
        String addressLt,
        
        Long birthLocalityId,
        Long addressLocalityId,
        Long countryId,
        Long pictureId,
        
        String registrationNumber,
        
        @NotNull(message = "Job is mandatory")
        Long jobId,
        
        Long roleId
) {}
