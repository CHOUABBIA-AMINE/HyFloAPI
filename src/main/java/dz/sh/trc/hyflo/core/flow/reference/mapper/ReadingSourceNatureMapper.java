package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSourceNature;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ReadingSourceNatureMapper extends BaseMapper<CreateReadingSourceNatureRequest, UpdateReadingSourceNatureRequest, ReadingSourceNatureResponse, ReadingSourceNatureSummary, ReadingSourceNature> {
}