package dz.sh.trc.hyflo.core.flow.reference.service;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface DataSourceService extends BaseService<CreateDataSourceRequest, UpdateDataSourceRequest, DataSourceResponse, DataSourceSummary> {
}