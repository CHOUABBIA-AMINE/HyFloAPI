/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: TerminalController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.core.dto.TerminalDTO;
import dz.sh.trc.hyflo.network.core.service.TerminalService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/network/core/terminal")
@Slf4j
@Tag(name = "Terminal Management", description = "APIs for managing storage and export terminals")
@SecurityRequirement(name = "bearer-auth")
public class TerminalController extends GenericController<TerminalDTO, Long> {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        super(terminalService, "Terminal");
        this.terminalService = terminalService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    @Operation(summary = "Get terminal by ID", description = "Retrieves a single terminal by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal found", content = @Content(schema = @Schema(implementation = TerminalDTO.class))),
        @ApiResponse(responseCode = "404", description = "Terminal not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TerminalDTO> getById(
            @Parameter(description = "Terminal ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    @Operation(summary = "Get all terminals (paginated)", description = "Retrieves a paginated list of all terminals")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminals retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<TerminalDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    @Operation(summary = "Get all terminals (unpaginated)", description = "Retrieves all terminals without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminals retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TerminalDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:MANAGE')")
    @Operation(summary = "Create new terminal", description = "Creates a new terminal with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Terminal created successfully", content = @Content(schema = @Schema(implementation = TerminalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Terminal name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TerminalDTO> create(
            @Parameter(description = "Terminal data", required = true) 
            @Valid @RequestBody TerminalDTO dto) {
        TerminalDTO created = terminalService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:MANAGE')")
    @Operation(summary = "Update terminal", description = "Updates an existing terminal")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal updated successfully", content = @Content(schema = @Schema(implementation = TerminalDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Terminal not found"),
        @ApiResponse(responseCode = "409", description = "Terminal name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TerminalDTO> update(
            @Parameter(description = "Terminal ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated terminal data", required = true) @Valid @RequestBody TerminalDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:MANAGE')")
    @Operation(summary = "Delete terminal", description = "Deletes a terminal permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Terminal deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Terminal not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete terminal - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Terminal ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    @Operation(summary = "Search terminals", description = "Searches terminals by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<TerminalDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<TerminalDTO> searchByQuery(String query, Pageable pageable) {
        return terminalService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    @Operation(summary = "Check if terminal exists", description = "Checks if a terminal with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Terminal ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL:READ')")
    @Operation(summary = "Count terminals", description = "Returns the total number of terminals in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
