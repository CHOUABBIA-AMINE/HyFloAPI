package dz.sh.trc.hyflo.core.general.organization.dto.response;

import java.util.Date;

public record EmployeeResponse(
        Long id,
        String lastNameAr,
        String firstNameAr,
        String lastNameLt,
        String firstNameLt,
        String fullNameAr,
        String fullNameLt,
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
        Long jobId,
        String jobDesignationFr,
        Long roleId,
        String roleName
) {}
