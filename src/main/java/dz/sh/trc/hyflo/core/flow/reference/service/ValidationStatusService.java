package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ValidationStatusService extends BaseService<CreateValidationStatusRequest, UpdateValidationStatusRequest, ValidationStatusResponse, ValidationStatusSummary> {
}