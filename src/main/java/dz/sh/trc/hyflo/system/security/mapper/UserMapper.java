/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserMapper
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.mapper;

import dz.sh.trc.hyflo.general.organization.mapper.EmployeeMapper;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.system.security.dto.command.UserCommandDto;
import dz.sh.trc.hyflo.system.security.dto.query.UserReadDto;
import dz.sh.trc.hyflo.system.security.model.User;

public final class UserMapper {

    private UserMapper() {}

    public static UserReadDto toReadDto(User entity) {
        if (entity == null) return null;

        Employee employee = entity.getEmployee();

        return UserReadDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .enabled(entity.getEnabled())
                .employeeId(employee != null ? employee.getId() : null)
                .employeeFullName(employee != null ? EmployeeMapper.buildFullName(employee) : null)
                .roleCode(employee != null && employee.getRole() != null
                        ? employee.getRole().getCode() : null)
                .createdAt(entity.getCreatedAt())
                .lastLoginAt(entity.getLastLoginAt())
                .build();
        // NOTE: password is INTENTIONALLY not mapped — never expose credentials.
    }

    public static User toEntity(UserCommandDto dto) {
        if (dto == null) return null;

        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : Boolean.TRUE);
        // password encoding is NOT performed here — belongs to UserCommandService

        if (dto.getEmployeeId() != null) {
            Employee emp = new Employee();
            emp.setId(dto.getEmployeeId());
            entity.setEmployee(emp);
        }

        return entity;
    }

    public static void updateEntity(UserCommandDto dto, User entity) {
        if (dto == null || entity == null) return;

        if (dto.getEmail() != null)   entity.setEmail(dto.getEmail());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        // username changes require dedicated security validation — handled in service
    }
}
