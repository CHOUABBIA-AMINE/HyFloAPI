package dz.sh.trc.hyflo.core.system.security.mapper;

import org.mapstruct.Mapper;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateResourceTypeRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ResourceTypeSummary;
import dz.sh.trc.hyflo.core.system.security.model.ResourceType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ResourceTypeMapper extends BaseMapper<CreateResourceTypeRequest, UpdateResourceTypeRequest, ResourceTypeResponse, ResourceTypeSummary, ResourceType> {
}
