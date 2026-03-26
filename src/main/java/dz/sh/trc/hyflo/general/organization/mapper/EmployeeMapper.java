/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Fix Role import: general.organization.model.Role →
 *                             system.security.model.Role
 *  @UpdatedOn  : 03-26-2026 - Phase A: Align all entity field access to actual
 *                             Person/Employee model (no firstName/lastName/email/
 *                             jobTitle/structure/active on entity).
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : General / Organization
 *
 *  Entity source-of-truth:
 *    Person  : lastNameAr, firstNameAr, lastNameLt, firstNameLt, birthDate,
 *              birthPlaceAr, birthPlaceLt, addressAr, addressLt,
 *              birthLocality, addressLocality, country, picture
 *    Employee: registrationNumber, job (type Job), role (type Role)
 *    Job     : code, designationAr, designationEn, designationFr, structure
 *    Role    : name, description, permissions   (NO getCode())
 *
 *  Fields present ONLY in EmployeeReadDto (DTO-level, NOT on entity):
 *    firstName, lastName, email, jobTitle, structureId, structureName, active
 *  These are derived/projected below; they are NEVER read from entity directly.
 **/

package dz.sh.trc.hyflo.general.organization.mapper;

import dz.sh.trc.hyflo.general.organization.dto.command.EmployeeCommandDto;
import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDto;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Job;
import dz.sh.trc.hyflo.system.security.model.Role;

public final class EmployeeMapper {

    private EmployeeMapper() {}

    // =====================================================================
    // entity → EmployeeReadDto
    // =====================================================================

    public static EmployeeReadDto toReadDto(Employee entity) {
        if (entity == null) return null;

        Job job = entity.getJob();

        return EmployeeReadDto.builder()
                .id(entity.getId())
                .registrationNumber(entity.getRegistrationNumber())
                // Person fields — Latin script is the canonical API name
                .firstName(entity.getFirstNameLt())
                .lastName(entity.getLastNameLt())
                .fullName(buildFullName(entity))
                // Job → projected as jobTitle (DesignationFr is SONATRACH canonical)
                .jobTitle(job != null ? job.getDesignationFr() : null)
                // Job.structure → projected as structureId / structureName
                .structureId(job != null && job.getStructure() != null
                        ? job.getStructure().getId() : null)
                .structureName(job != null && job.getStructure() != null
                        ? job.getStructure().getDesignationFr() : null)
                // Role — Role has 'name' field only (no getCode())
                .roleId(entity.getRole() != null ? entity.getRole().getId() : null)
                .roleCode(entity.getRole() != null ? entity.getRole().getName() : null)
                // email / active : not on entity — left null (DTO accepts NON_NULL)
                .build();
    }

    // =====================================================================
    // EmployeeCommandDto → new Employee entity
    // =====================================================================

    public static Employee toEntity(EmployeeCommandDto dto) {
        if (dto == null) return null;

        Employee entity = new Employee();
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setFirstNameLt(dto.getFirstName());
        entity.setLastNameLt(dto.getLastName());
        // email / active / structure do NOT exist on Employee entity;
        // they are DTO convenience fields only.

        // Job: resolved by service using dto.getStructureId() + dto.getJobTitle();
        // mapper creates a proxy with the ID supplied via DTO (structureId doubles
        // as the job-resolution key at service layer).
        if (dto.getStructureId() != null) {
            Job j = new Job();
            j.setId(dto.getStructureId()); // proxy — service must load real Job
            entity.setJob(j);
        }
        if (dto.getRoleId() != null) {
            Role r = new Role();
            r.setId(dto.getRoleId());
            entity.setRole(r);
        }

        return entity;
    }

    // =====================================================================
    // EmployeeCommandDto → update existing Employee entity (patch)
    // =====================================================================

    public static void updateEntity(EmployeeCommandDto dto, Employee entity) {
        if (dto == null || entity == null) return;

        if (dto.getFirstName() != null) entity.setFirstNameLt(dto.getFirstName());
        if (dto.getLastName()  != null) entity.setLastNameLt(dto.getLastName());
        // email / active / structure — not on entity; no-op

        if (dto.getStructureId() != null) {
            Job j = new Job();
            j.setId(dto.getStructureId());
            entity.setJob(j);
        }
        if (dto.getRoleId() != null) {
            Role r = new Role();
            r.setId(dto.getRoleId());
            entity.setRole(r);
        }
    }

    // =====================================================================
    // Public utility — used by UserMapper, WorkflowInstanceMapper, etc.
    // =====================================================================

    /**
     * Resolves Latin full name from Person fields.
     * Used by cross-mapper fullName projection (UserMapper, IncidentMapper, etc.).
     */
    public static String buildFullName(Employee employee) {
        if (employee == null) return null;
        String first = employee.getFirstNameLt() != null ? employee.getFirstNameLt() : "";
        String last  = employee.getLastNameLt()  != null ? employee.getLastNameLt()  : "";
        return (first + " " + last).trim();
    }
}
