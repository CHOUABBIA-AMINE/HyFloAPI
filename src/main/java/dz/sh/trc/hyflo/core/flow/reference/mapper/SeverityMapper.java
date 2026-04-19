package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateSeverityRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeverityResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.SeveritySummary;
import dz.sh.trc.hyflo.core.flow.reference.model.Severity;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface SeverityMapper extends BaseMapper<CreateSeverityRequest, UpdateSeverityRequest, SeverityResponse, SeveritySummary, Severity> {
}