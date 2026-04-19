package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateAlertStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.AlertStatusSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface AlertStatusService extends BaseService<CreateAlertStatusRequest, UpdateAlertStatusRequest, AlertStatusResponse, AlertStatusSummary> {
}