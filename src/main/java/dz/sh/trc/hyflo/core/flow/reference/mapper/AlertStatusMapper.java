package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.AlertStatus;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface AlertStatusMapper extends BaseMapper<CreateAlertStatusRequest, UpdateAlertStatusRequest, AlertStatusResponse, AlertStatusSummary, AlertStatus> {
}