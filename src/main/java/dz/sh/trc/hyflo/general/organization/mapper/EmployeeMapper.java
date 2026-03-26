/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : EmployeeMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Fix Role import: general.organization.model.Role →
 *                             system.security.model.Role
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.mapper;

import dz.sh.trc.hyflo.general.organization.dto.command.EmployeeCommandDto;
import dz.sh.trc.hyflo.general.organization.dto.query.EmployeeReadDto;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.general.organization.model.Structure;
import dz.sh.trc.hyflo.system.security.model.Role;

public final class EmployeeMapper {

    private EmployeeMapper() {}

    public static EmployeeReadDto toReadDto(Employee entity) {
        if (entity == null) return null;

        String fullName = buildFullName(entity);

        return EmployeeReadDto.builder()
                .id(entity.getId())
                .registrationNumber(entity.getRegistrationNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .fullName(fullName)
                .email(entity.getEmail())
                .jobTitle(entity.getJobTitle())
                .structureId(entity.getStructure() != null ? entity.getStructure().getId() : null)
                .structureName(entity.getStructure() != null ? entity.getStructure().getName() : null)
                .roleId(entity.getRole() != null ? entity.getRole().getId() : null)
                .roleCode(entity.getRole() != null ? entity.getRole().getName() : null)
                .active(entity.getActive())
                .build();
    }

    public static Employee toEntity(EmployeeCommandDto dto) {
        if (dto == null) return null;

        Employee entity = new Employee();
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setJobTitle(dto.getJobTitle());
        entity.setActive(dto.getActive() != null ? dto.getActive() : Boolean.TRUE);

        if (dto.getStructureId() != null) {
            Structure s = new Structure();
            s.setId(dto.getStructureId());
            entity.setStructure(s);
        }
        if (dto.getRoleId() != null) {
            Role r = new Role();
            r.setId(dto.getRoleId());
            entity.setRole(r);
        }

        return entity;
    }

    public static void updateEntity(EmployeeCommandDto dto, Employee entity) {
        if (dto == null || entity == null) return;

        if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)  entity.setLastName(dto.getLastName());
        if (dto.getEmail() != null)     entity.setEmail(dto.getEmail());
        if (dto.getJobTitle() != null)  entity.setJobTitle(dto.getJobTitle());
        if (dto.getActive() != null)    entity.setActive(dto.getActive());

        if (dto.getStructureId() != null) {
            Structure s = new Structure();
            s.setId(dto.getStructureId());
            entity.setStructure(s);
        }
        if (dto.getRoleId() != null) {
            Role r = new Role();
            r.setId(dto.getRoleId());
            entity.setRole(r);
        }
    }

    /**
     * Public static utility for cross-mapper fullName resolution.
     * Used by WorkflowInstanceMapper, IncidentMapper, IncidentImpactMapper.
     */
    public static String buildFullName(Employee employee) {
        if (employee == null) return null;
        String first = employee.getFirstNameLt() != null ? employee.getFirstNameLt() : "";
        String last  = employee.getLastNameLt()  != null ? employee.getLastNameLt()  : "";
        return (first + " " + last).trim();
    }
}
