package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.ValidationStatus;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ValidationStatusMapper extends BaseMapper<CreateValidationStatusRequest, UpdateValidationStatusRequest, ValidationStatusResponse, ValidationStatusSummary, ValidationStatus> {
}