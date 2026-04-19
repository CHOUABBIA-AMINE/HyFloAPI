package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Terminal;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface TerminalMapper extends BaseMapper<CreateTerminalRequest, UpdateTerminalRequest, TerminalResponse, TerminalSummary, Terminal> {
}