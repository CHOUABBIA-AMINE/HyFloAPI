package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateInfrastructureRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.InfrastructureSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Infrastructure;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface InfrastructureMapper extends BaseMapper<CreateInfrastructureRequest, UpdateInfrastructureRequest, InfrastructureResponse, InfrastructureSummary, Infrastructure> {
}
