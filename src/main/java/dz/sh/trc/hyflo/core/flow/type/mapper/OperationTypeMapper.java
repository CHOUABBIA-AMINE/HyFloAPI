package dz.sh.trc.hyflo.core.flow.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.type.dto.request.CreateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.request.UpdateOperationTypeRequest;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeResponse;
import dz.sh.trc.hyflo.core.flow.type.dto.response.OperationTypeSummary;
import dz.sh.trc.hyflo.core.flow.type.model.OperationType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface OperationTypeMapper extends BaseMapper<CreateOperationTypeRequest, UpdateOperationTypeRequest, OperationTypeResponse, OperationTypeSummary, OperationType> {
}