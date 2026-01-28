/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: DataSourceController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.DataSourceDTO;
import dz.sh.trc.hyflo.flow.common.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for DataSource entity.
 * Provides CRUD endpoints and search functionality for data source management.
 */
@RestController
@RequestMapping("/flow/common/dataSource")
@Tag(name = "Data Source", description = "Flow data source management API")
@Slf4j
public class DataSourceController extends GenericController<DataSourceDTO, Long> {

    private final DataSourceService dataSourceService;
    
    public DataSourceController(DataSourceService dataSourceService) {
        super(dataSourceService, "DataSource");
        this.dataSourceService = dataSourceService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get data source by ID", description = "Retrieve a single data source by its unique identifier")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<DataSourceDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all data sources (paginated)", description = "Retrieve all data sources with pagination and sorting")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Page<DataSourceDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all data sources (no pagination)", description = "Retrieve all data sources sorted by code")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<List<DataSourceDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create data source", description = "Create a new data source with unique code and French designation validation")
    @PreAuthorize("hasAuthority('DATA_SOURCE:MANAGE')")
    public ResponseEntity<DataSourceDTO> create(@Valid @RequestBody DataSourceDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update data source", description = "Update an existing data source by ID")
    @PreAuthorize("hasAuthority('DATA_SOURCE:MANAGE')")
    public ResponseEntity<DataSourceDTO> update(@PathVariable Long id, @Valid @RequestBody DataSourceDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete data source", description = "Delete a data source by ID")
    @PreAuthorize("hasAuthority('DATA_SOURCE:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search data sources", description = "Search data sources across all fields with pagination")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Page<DataSourceDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if data source exists", description = "Check if a data source exists by ID")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count data sources", description = "Get total count of data sources")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get data source by code", description = "Find data source by its unique code (SCADA, MANUAL, etc.)")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<DataSourceDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/common/datasource/code/{} - Getting data source by code", code);
        return ResponseEntity.ok(dataSourceService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get data source by French designation", description = "Find data source by its unique French designation")
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<DataSourceDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/datasource/designation/{} - Getting data source by French designation", designationFr);
        return ResponseEntity.ok(dataSourceService.findByDesignationFr(designationFr));
    }
}
