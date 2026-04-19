package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateDataSourceRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.DataSourceSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.DataSource;
import dz.sh.trc.hyflo.core.flow.reference.service.DataSourceService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/data-sources")
@Tag(name = "DataSource API", description = "Endpoints for managing DataSource")
public class DataSourceController extends BaseController<CreateDataSourceRequest, UpdateDataSourceRequest, DataSourceResponse, DataSourceSummary, DataSource, Long> {
    public DataSourceController(DataSourceService service) { super(service); }
    @Override
    protected Page<DataSourceSummary> performSearch(String search, Pageable pageable) { throw new UnsupportedOperationException("Search not implemented"); }
}