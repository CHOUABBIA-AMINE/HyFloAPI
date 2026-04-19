package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateTerminalRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.TerminalSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface TerminalService extends BaseService<CreateTerminalRequest, UpdateTerminalRequest, TerminalResponse, TerminalSummary> {
}