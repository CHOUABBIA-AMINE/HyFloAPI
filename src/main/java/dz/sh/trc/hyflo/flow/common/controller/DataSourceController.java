/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: DataSourceController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.DataSourceDTO;
import dz.sh.trc.hyflo.flow.common.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for DataSource entity.
 * Provides CRUD endpoints and search functionality for data source management.
 */
@RestController
@RequestMapping("/flow/common/dataSource")
@Tag(name = "Data Source Management", description = "APIs for managing flow data sources (SCADA, Manual, etc.)")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class DataSourceController extends GenericController<DataSourceDTO, Long> {

    private final DataSourceService dataSourceService;
    
    public DataSourceController(DataSourceService dataSourceService) {
        super(dataSourceService, "DataSource");
        this.dataSourceService = dataSourceService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get data source by ID", description = "Retrieves a single data source by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data source found", content = @Content(schema = @Schema(implementation = DataSourceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Data source not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<DataSourceDTO> getById(
            @Parameter(description = "Data source ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all data sources (paginated)", description = "Retrieves a paginated list of all data sources")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data sources retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Page<DataSourceDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all data sources (unpaginated)", description = "Retrieves all data sources without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data sources retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<List<DataSourceDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create data source", description = "Creates a new data source with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Data source created successfully", content = @Content(schema = @Schema(implementation = DataSourceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Data source code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:MANAGE')")
    public ResponseEntity<DataSourceDTO> create(
            @Parameter(description = "Data source data", required = true) 
            @Valid @RequestBody DataSourceDTO dto) {
        DataSourceDTO created = dataSourceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update data source", description = "Updates an existing data source")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data source updated successfully", content = @Content(schema = @Schema(implementation = DataSourceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Data source not found"),
        @ApiResponse(responseCode = "409", description = "Data source code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:MANAGE')")
    public ResponseEntity<DataSourceDTO> update(
            @Parameter(description = "Data source ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated data source data", required = true) @Valid @RequestBody DataSourceDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete data source", description = "Deletes a data source permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Data source deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Data source not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete data source - has dependencies (readings using this source)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Data source ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search data sources", description = "Searches data sources by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Page<DataSourceDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if data source exists", description = "Checks if a data source with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Data source ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count data sources", description = "Returns the total number of data sources in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data source count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get data source by code", description = "Retrieves a data source by its unique code (e.g., SCADA, MANUAL, API)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data source found", content = @Content(schema = @Schema(implementation = DataSourceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Data source not found with given code"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<DataSourceDTO> getByCode(
            @Parameter(description = "Data source code", required = true, example = "SCADA") 
            @PathVariable String code) {
        log.info("GET /flow/common/datasource/code/{} - Getting data source by code", code);
        return ResponseEntity.ok(dataSourceService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get data source by French designation", description = "Retrieves a data source by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data source found", content = @Content(schema = @Schema(implementation = DataSourceDTO.class))),
        @ApiResponse(responseCode = "404", description = "Data source not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires DATA_SOURCE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('DATA_SOURCE:READ')")
    public ResponseEntity<DataSourceDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "SCADA") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/datasource/designation/{} - Getting data source by French designation", designationFr);
        return ResponseEntity.ok(dataSourceService.findByDesignationFr(designationFr));
    }
}
