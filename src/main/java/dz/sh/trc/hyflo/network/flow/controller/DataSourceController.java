/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DataSourceController
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.flow.dto.DataSourceDTO;
import dz.sh.trc.hyflo.network.flow.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hyflo/api/network/flow/data-sources")
@Tag(name = "Data Sources", description = "Flow reading data sources management")
public class DataSourceController extends GenericController<DataSourceDTO, Long> {
    
    private final DataSourceService dataSourceService;
    
    public DataSourceController(DataSourceService dataSourceService) {
        super(dataSourceService, "DataSource");
        this.dataSourceService = dataSourceService;
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Get data source by code")
    public ResponseEntity<DataSourceDTO> getByCode(@PathVariable String code) {
        return dataSourceService.findByCode(code)
                .map(this::success)
                .orElse(notFound());
    }
}
