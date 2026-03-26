/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Phase A: Remove non-existent field access.
 *                             User entity has no createdAt / lastLoginAt.
 *                             GenericModel only provides 'id'.
 *                             Role has 'name' field only — no getCode();
 *                             UserMapper already used getCode(); fixed to getName().
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : System / Security
 *
 *  Entity source-of-truth:
 *    GenericModel : id
 *    User         : username, email, password, accountNonExpired,
 *                   accountNonLocked, credentialsNonExpired, enabled,
 *                   employee (Employee), roles (Set<Role>), groups (Set<Group>)
 *                   — NO createdAt, NO lastLoginAt
 *    Role         : name, description, permissions   — NO getCode()
 **/

package dz.sh.trc.hyflo.system.security.mapper;

import dz.sh.trc.hyflo.general.organization.mapper.EmployeeMapper;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.system.security.dto.command.UserCommandDto;
import dz.sh.trc.hyflo.system.security.dto.query.UserReadDto;
import dz.sh.trc.hyflo.system.security.model.User;

public final class UserMapper {

    private UserMapper() {}

    // =====================================================================
    // entity → UserReadDto
    // =====================================================================

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
                // Role resolved through Employee.role (system.security.model.Role).
                // Role has 'name' field only — no getCode().
                .roleCode(employee != null && employee.getRole() != null
                        ? employee.getRole().getName() : null)
                // createdAt / lastLoginAt: NOT on User entity (GenericModel only has id).
                // Fields left null; UserReadDto is @JsonInclude(NON_NULL) so they are omitted.
                .build();
        // NOTE: password is INTENTIONALLY not mapped — never expose credentials.
    }

    // =====================================================================
    // UserCommandDto → new User entity
    // =====================================================================

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

    // =====================================================================
    // UserCommandDto → update existing User entity (patch)
    // =====================================================================

    public static void updateEntity(UserCommandDto dto, User entity) {
        if (dto == null || entity == null) return;

        if (dto.getEmail() != null)   entity.setEmail(dto.getEmail());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        // username changes require dedicated security validation — handled in service
    }
}
