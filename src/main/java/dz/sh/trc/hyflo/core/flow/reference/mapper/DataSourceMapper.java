package dz.sh.trc.hyflo.core.flow.reference.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.DataSource;
import dz.sh.trc.hyflo.platform.kernel.BaseMapper;

@Mapper(componentModel = "spring")
public interface DataSourceMapper extends BaseMapper<CreateDataSourceRequest, UpdateDataSourceRequest, DataSourceResponse, DataSourceSummary, DataSource> {
    @Override
    @Mapping(target = "sourceNatureId", source = "sourceNature.id")
    @Mapping(target = "sourceNatureDesignationFr", source = "sourceNature.designationFr")
    DataSourceResponse toResponse(DataSource entity);
    
    @Override
    @Mapping(target = "sourceNatureDesignationFr", source = "sourceNature.designationFr")
    DataSourceSummary toSummary(DataSource entity);
}