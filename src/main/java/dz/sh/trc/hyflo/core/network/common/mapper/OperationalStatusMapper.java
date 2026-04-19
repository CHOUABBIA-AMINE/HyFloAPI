package dz.sh.trc.hyflo.core.network.common.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateOperationalStatusRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.OperationalStatusSummary;
import dz.sh.trc.hyflo.core.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface OperationalStatusMapper extends BaseMapper<CreateOperationalStatusRequest, UpdateOperationalStatusRequest, OperationalStatusResponse, OperationalStatusSummary, OperationalStatus> {
}
