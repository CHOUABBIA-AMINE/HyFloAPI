package dz.sh.trc.hyflo.core.system.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdatePermissionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.PermissionSummary;
import dz.sh.trc.hyflo.core.system.security.model.Permission;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", uses = {ResourceMapper.class, ActionMapper.class})
public interface PermissionMapper extends BaseMapper<CreatePermissionRequest, UpdatePermissionRequest, PermissionResponse, PermissionSummary, Permission> {

    @Override
    @Mapping(target = "resourceCode", source = "resource.code")
    @Mapping(target = "actionName", source = "action.name")
    PermissionResponse toResponse(Permission entity);

    @Override
    @Mapping(target = "resource", ignore = true)
    @Mapping(target = "action", ignore = true)
    Permission toEntity(CreatePermissionRequest request);

    @Override
    @Mapping(target = "resource", ignore = true)
    @Mapping(target = "action", ignore = true)
    void updateEntityFromRequest(UpdatePermissionRequest request, @MappingTarget Permission entity);
}
