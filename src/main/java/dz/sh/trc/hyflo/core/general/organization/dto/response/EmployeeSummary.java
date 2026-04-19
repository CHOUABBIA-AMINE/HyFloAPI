package dz.sh.trc.hyflo.core.general.organization.dto.response;

public record EmployeeSummary(
        Long id,
        String fullNameLt,
        String registrationNumber,
        String jobDesignationFr
) {}
