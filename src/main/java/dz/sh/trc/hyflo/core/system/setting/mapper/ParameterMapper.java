package dz.sh.trc.hyflo.core.system.setting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import dz.sh.trc.hyflo.core.system.setting.dto.request.CreateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.request.UpdateParameterRequest;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterResponse;
import dz.sh.trc.hyflo.core.system.setting.dto.response.ParameterSummary;
import dz.sh.trc.hyflo.core.system.setting.model.Parameter;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ParameterMapper extends BaseMapper<CreateParameterRequest, UpdateParameterRequest, ParameterResponse, ParameterSummary, Parameter> {
    
    @Override
    void updateEntityFromRequest(UpdateParameterRequest dto, @MappingTarget Parameter entity);
}
