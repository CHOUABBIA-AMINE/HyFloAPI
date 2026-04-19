package dz.sh.trc.hyflo.core.flow.type.service;

import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface OperationTypeService extends BaseService<CreateOperationTypeRequest, UpdateOperationTypeRequest, OperationTypeResponse, OperationTypeSummary> {
}