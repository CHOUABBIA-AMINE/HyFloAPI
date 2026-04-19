package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateTerminalTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.TerminalTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface TerminalTypeService extends BaseService<CreateTerminalTypeRequest, UpdateTerminalTypeRequest, TerminalTypeResponse, TerminalTypeSummary> {
}