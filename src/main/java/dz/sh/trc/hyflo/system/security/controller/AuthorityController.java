/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthorityController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.system.security.dto.AuthorityDTO;
import dz.sh.trc.hyflo.system.security.service.AuthorityService;
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

@RestController
@RequestMapping("/system/security/authority")
@Slf4j
@Tag(name = "Authority Management", description = "APIs for managing system authorities and authorization types")
@SecurityRequirement(name = "bearer-auth")
public class AuthorityController extends GenericController<AuthorityDTO, Long> {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        super(authorityService, "Authority");
        this.authorityService = authorityService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Get authority by ID", description = "Retrieves a single authority by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authority found", content = @Content(schema = @Schema(implementation = AuthorityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Authority not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorityDTO> getById(
            @Parameter(description = "Authority ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Get all authorities (paginated)", description = "Retrieves a paginated list of all authorities")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authorities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuthorityDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Get all authorities (unpaginated)", description = "Retrieves all authorities without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authorities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AuthorityDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:MANAGE')")
    @Operation(summary = "Create a new authority", description = "Creates a new authority with validation for unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Authority created successfully", content = @Content(schema = @Schema(implementation = AuthorityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Authority name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorityDTO> create(
            @Parameter(description = "Authority data", required = true) 
            @Valid @RequestBody AuthorityDTO dto) {
        AuthorityDTO created = authorityService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:MANAGE')")
    @Operation(summary = "Update authority", description = "Updates an existing authority")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authority updated successfully", content = @Content(schema = @Schema(implementation = AuthorityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Authority not found"),
        @ApiResponse(responseCode = "409", description = "Authority name already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorityDTO> update(
            @Parameter(description = "Authority ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated authority data", required = true) @Valid @RequestBody AuthorityDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:MANAGE')")
    @Operation(summary = "Delete authority", description = "Deletes an authority permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Authority deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Authority not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete authority - in use by system"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Authority ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Search authorities", description = "Searches authorities by name or type (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AuthorityDTO>> search(
            @Parameter(description = "Search query", example = "admin") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Check if authority exists", description = "Checks if an authority with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Authority ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Count authorities", description = "Returns the total number of authorities in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authority count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY OPERATIONS ==========

    @Override
    protected Page<AuthorityDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return authorityService.getAll(pageable);
        }
        log.debug("GET /search?q={} - Searching", query);
        return authorityService.globalSearch(query, pageable);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Get authority by name", description = "Retrieves an authority by its unique name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authority found", content = @Content(schema = @Schema(implementation = AuthorityDTO.class))),
        @ApiResponse(responseCode = "404", description = "Authority not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthorityDTO> getByName(
            @Parameter(description = "Authority name", required = true, example = "SYSTEM_ADMIN") 
            @PathVariable String name) {
        log.info("REST request to get Authority by name: {}", name);
        return ResponseEntity.ok(authorityService.findByName(name));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Get authorities by type", description = "Retrieves all authorities of a specific type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authorities retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AuthorityDTO>> getByType(
            @Parameter(description = "Authority type", required = true, example = "SYSTEM") 
            @PathVariable String type) {
        log.info("REST request to get Authorities by type: {}", type);
        return ResponseEntity.ok(authorityService.findByType(type));
    }

    @GetMapping("/exists/{name}")
    @PreAuthorize("hasAuthority('AUTHORITY:READ')")
    @Operation(summary = "Check if authority name exists", description = "Checks if an authority with the given name already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check result returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires AUTHORITY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Boolean>> checkExists(
            @Parameter(description = "Authority name", required = true, example = "SYSTEM_ADMIN") 
            @PathVariable String name) {
        log.info("REST request to check if Authority exists: {}", name);
        boolean exists = authorityService.existsByName(name);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
