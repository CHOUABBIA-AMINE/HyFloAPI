package dz.sh.trc.hyflo.core.system.security.mapper;

import org.mapstruct.Mapper;

import dz.sh.trc.hyflo.core.system.security.dto.request.CreateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.request.UpdateActionRequest;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionResponse;
import dz.sh.trc.hyflo.core.system.security.dto.response.ActionSummary;
import dz.sh.trc.hyflo.core.system.security.model.Action;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ActionMapper extends BaseMapper<CreateActionRequest, UpdateActionRequest, ActionResponse, ActionSummary, Action> {
}
