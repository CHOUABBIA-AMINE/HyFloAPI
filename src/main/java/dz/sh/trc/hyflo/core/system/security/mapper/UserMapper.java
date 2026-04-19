package dz.sh.trc.hyflo.core.system.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateUserRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.UserSummary;
import dz.sh.trc.hyflo.core.system.security.model.User;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, GroupMapper.class})
public interface UserMapper extends BaseMapper<CreateUserRequest, UpdateUserRequest, UserResponse, UserSummary, User> {
    
    @Override
    @Mapping(target = "employeeFullName", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getFullNameLt() : null)")
    UserResponse toResponse(User entity);

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "groups", ignore = true)
    User toEntity(CreateUserRequest request);

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "groups", ignore = true)
    void updateEntityFromRequest(UpdateUserRequest request, @MappingTarget User entity);
}
