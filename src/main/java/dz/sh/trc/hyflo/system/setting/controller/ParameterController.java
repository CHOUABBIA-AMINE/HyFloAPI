/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ParameterController
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : System / Setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.setting.dto.ParameterDTO;
import dz.sh.trc.hyflo.system.setting.service.ParameterService;
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

import java.util.Optional;

/**
 * REST Controller for system configuration parameter management.
 * Supports key-value configuration storage, retrieval, and runtime updates.
 */
@Tag(
    name = "System Parameters",
    description = "API for managing system configuration parameters and application settings"
)
@SecurityRequirement(name = "bearer-auth")
@RestController
@RequestMapping("/system/setting/parameters")
@Slf4j
public class ParameterController extends GenericController<ParameterDTO, Long> {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        super(parameterService, "Parameter");
        this.parameterService = parameterService;
    }

    @Operation(
        summary = "Get parameter by ID",
        description = "Retrieve a specific configuration parameter by its unique identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parameter found", content = @Content(schema = @Schema(implementation = ParameterDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Parameter not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('SETTING:READ')")
    public ResponseEntity<ParameterDTO> getById(
        @Parameter(description = "Parameter ID", required = true, example = "1") @PathVariable Long id
    ) {
        return super.getById(id);
    }

    @Operation(
        summary = "Get all parameters",
        description = "Retrieve paginated list of all system configuration parameters."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parameters retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('SETTING:READ')")
    public ResponseEntity<Page<ParameterDTO>> getAll(
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Sort field", example = "key") @RequestParam(defaultValue = "key") String sortBy,
        @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Operation(
        summary = "Get parameter by key",
        description = "Retrieve a configuration parameter by its unique key identifier. More efficient than ID lookup for configuration access."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parameter found", content = @Content(schema = @Schema(implementation = ParameterDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Parameter not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/by-key/{key}")
    @PreAuthorize("hasAuthority('SETTING:READ')")
    public ResponseEntity<ParameterDTO> getByKey(
        @Parameter(description = "Parameter key", required = true, example = "flow_reading_validator") @PathVariable String key
    ) {
        log.debug("Getting parameter by key: {}", key);
        Optional<ParameterDTO> parameter = parameterService.findByKey(key);
        return parameter.map(this::success)
                .orElseThrow(() -> new ResourceNotFoundException("Parameter not found with key: " + key));
    }

    @Operation(
        summary = "Get parameters by type",
        description = "Retrieve all configuration parameters of a specific type for grouped configuration management."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parameters retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid type or pagination", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('SETTING:READ')")
    public ResponseEntity<Page<ParameterDTO>> getByType(
        @Parameter(description = "Parameter type", required = true, example = "FOREIGN_KEY") @PathVariable String type,
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("Getting parameters by type: {}", type);
        Page<ParameterDTO> parameters = parameterService.findByType(type, buildPageable(page, size, "key", "asc"));
        return success(parameters);
    }

    @Operation(
        summary = "Search parameters",
        description = "Search configuration parameters across all fields (key, value, description) with pagination support."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search query or pagination", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/search/parameter")
    @PreAuthorize("hasAuthority('SETTING:READ')")
    public ResponseEntity<Page<ParameterDTO>> search(
        @Parameter(description = "Search query", required = true, example = "validator") @RequestParam String query,
        @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("Searching parameters with query: {}", query);
        Page<ParameterDTO> results = parameterService.search(query, buildPageable(page, size, "key", "asc"));
        return success(results);
    }

    @Operation(
        summary = "Create parameter",
        description = "Create a new system configuration parameter. Key must be unique."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Parameter created", content = @Content(schema = @Schema(implementation = ParameterDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameter data or duplicate key", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('SETTING:MANAGE')")
    public ResponseEntity<ParameterDTO> create(@Valid @RequestBody ParameterDTO dto) {
        return super.create(dto);
    }

    @Operation(
        summary = "Update parameter",
        description = "Update an existing configuration parameter. Changes take effect immediately and may require application restart depending on parameter usage."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parameter updated", content = @Content(schema = @Schema(implementation = ParameterDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameter data", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Parameter not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('SETTING:MANAGE')")
    public ResponseEntity<ParameterDTO> update(@PathVariable Long id, @Valid @RequestBody ParameterDTO dto) {
        return super.update(id, dto);
    }

    @Operation(
        summary = "Delete parameter",
        description = "Delete a configuration parameter. WARNING: Deleting system-critical parameters may cause application malfunction."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Parameter deleted", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Parameter not found", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    @PreAuthorize("hasAuthority('SETTING:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }
}
