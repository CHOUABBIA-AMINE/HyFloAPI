package dz.sh.trc.hyflo.core.general.type.service;

import dz.sh.trc.hyflo.core.general.type.dto.request.CreateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.request.UpdateStructureTypeRequest;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeResponse;
import dz.sh.trc.hyflo.core.general.type.dto.response.StructureTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface StructureTypeService extends BaseService<CreateStructureTypeRequest, UpdateStructureTypeRequest, StructureTypeResponse, StructureTypeSummary> {
}
