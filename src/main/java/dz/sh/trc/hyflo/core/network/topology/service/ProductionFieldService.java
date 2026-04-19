package dz.sh.trc.hyflo.core.network.topology.service;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface ProductionFieldService extends BaseService<CreateProductionFieldRequest, UpdateProductionFieldRequest, ProductionFieldResponse, ProductionFieldSummary> {
}