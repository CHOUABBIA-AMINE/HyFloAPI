package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.QualityFlag;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface QualityFlagMapper extends BaseMapper<CreateQualityFlagRequest, UpdateQualityFlagRequest, QualityFlagResponse, QualityFlagSummary, QualityFlag> {
}