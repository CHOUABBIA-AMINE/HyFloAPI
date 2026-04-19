package dz.sh.trc.hyflo.core.network.type.mapper;

import org.mapstruct.Mapper;
import dz.sh.trc.hyflo.core.network.type.dto.request.CreateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.request.UpdateProductionFieldTypeRequest;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeResponse;
import dz.sh.trc.hyflo.core.network.type.dto.response.ProductionFieldTypeSummary;
import dz.sh.trc.hyflo.core.network.type.model.ProductionFieldType;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface ProductionFieldTypeMapper extends BaseMapper<CreateProductionFieldTypeRequest, UpdateProductionFieldTypeRequest, ProductionFieldTypeResponse, ProductionFieldTypeSummary, ProductionFieldType> {
}