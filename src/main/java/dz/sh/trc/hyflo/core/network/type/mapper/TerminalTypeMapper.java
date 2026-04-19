package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.TerminalType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface TerminalTypeMapper extends BaseMapper<CreateTerminalTypeRequest, UpdateTerminalTypeRequest, TerminalTypeResponse, TerminalTypeSummary, TerminalType> {
}