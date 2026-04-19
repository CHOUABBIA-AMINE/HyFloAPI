package dz.sh.trc.hyflo.core.network.common.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.common.dto.request.CreateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.request.UpdateAlloyRequest;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloyResponse;
import dz.sh.trc.hyflo.core.network.common.dto.response.AlloySummary;
import dz.sh.trc.hyflo.core.network.common.model.Alloy;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface AlloyMapper extends BaseMapper<CreateAlloyRequest, UpdateAlloyRequest, AlloyResponse, AlloySummary, Alloy> {
}
