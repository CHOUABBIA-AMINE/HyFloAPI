package dz.sh.trc.hyflo.core.system.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceSummary;
import dz.sh.trc.hyflo.core.system.security.model.Resource;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper extends BaseMapper<CreateResourceRequest, UpdateResourceRequest, ResourceResponse, ResourceSummary, Resource> {

    @Override
    @Mapping(target = "resourceTypeName", source = "resourceType.name")
    ResourceResponse toResponse(Resource entity);

    @Override
    @Mapping(target = "resourceType", ignore = true)
    Resource toEntity(CreateResourceRequest request);

    @Override
    @Mapping(target = "resourceType", ignore = true)
    void updateEntityFromRequest(UpdateResourceRequest request, @MappingTarget Resource entity);
}
