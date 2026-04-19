package dz.sh.trc.hyflo.core.network.type.service;

import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ProductionFieldTypeService extends BaseService<CreateProductionFieldTypeRequest, UpdateProductionFieldTypeRequest, ProductionFieldTypeResponse, ProductionFieldTypeSummary> {
}