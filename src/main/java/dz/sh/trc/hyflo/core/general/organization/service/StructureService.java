package dz.sh.trc.hyflo.core.general.organization.service;

import dz.sh.trc.hyflo.core.general.organization.dto.request.CreateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.request.UpdateStructureRequest;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureResponse;
import dz.sh.trc.hyflo.core.general.organization.dto.response.StructureSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface StructureService extends BaseService<CreateStructureRequest, UpdateStructureRequest, StructureResponse, StructureSummary> {
}
