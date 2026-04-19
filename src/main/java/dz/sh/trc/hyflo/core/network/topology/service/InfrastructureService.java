package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface InfrastructureService extends BaseService<CreateInfrastructureRequest, UpdateInfrastructureRequest, InfrastructureResponse, InfrastructureSummary> {
}
