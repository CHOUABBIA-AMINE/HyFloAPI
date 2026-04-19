package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSlot;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ReadingSlotMapper extends BaseMapper<CreateReadingSlotRequest, UpdateReadingSlotRequest, ReadingSlotResponse, ReadingSlotSummary, ReadingSlot> {
}