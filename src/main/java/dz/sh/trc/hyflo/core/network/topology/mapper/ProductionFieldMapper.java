package dz.sh.trc.hyflo.core.network.topology.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateProductionFieldRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.ProductionFieldSummary;
import dz.sh.trc.hyflo.core.network.topology.model.ProductionField;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring", builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ProductionFieldMapper extends BaseMapper<CreateProductionFieldRequest, UpdateProductionFieldRequest, ProductionFieldResponse, ProductionFieldSummary, ProductionField> {
}