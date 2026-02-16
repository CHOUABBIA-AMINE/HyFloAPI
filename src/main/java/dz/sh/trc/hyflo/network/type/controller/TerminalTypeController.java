/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: TerminalTypeController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.type.dto.TerminalTypeDTO;
import dz.sh.trc.hyflo.network.type.service.TerminalTypeService;
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
@RequestMapping("/network/type/terminal")
@Slf4j
@Tag(name = "Terminal Type Management", description = "APIs for managing terminal type classifications")
@SecurityRequirement(name = "bearer-auth")
public class TerminalTypeController extends GenericController<TerminalTypeDTO, Long> {

    private final TerminalTypeService terminalTypeService;

    public TerminalTypeController(TerminalTypeService terminalTypeService) {
        super(terminalTypeService, "TerminalType");
        this.terminalTypeService = terminalTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    @Operation(summary = "Get terminal type by ID", description = "Retrieves a single terminal type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal type found", content = @Content(schema = @Schema(implementation = TerminalTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Terminal type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TerminalTypeDTO> getById(
            @Parameter(description = "Terminal type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    @Operation(summary = "Get all terminal types (paginated)", description = "Retrieves a paginated list of all terminal types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<TerminalTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    @Operation(summary = "Get all terminal types (unpaginated)", description = "Retrieves all terminal types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TerminalTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:MANAGE')")
    @Operation(summary = "Create new terminal type", description = "Creates a new terminal type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Terminal type created successfully", content = @Content(schema = @Schema(implementation = TerminalTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Terminal type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TerminalTypeDTO> create(
            @Parameter(description = "Terminal type data", required = true) 
            @Valid @RequestBody TerminalTypeDTO dto) {
        TerminalTypeDTO created = terminalTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:MANAGE')")
    @Operation(summary = "Update terminal type", description = "Updates an existing terminal type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal type updated successfully", content = @Content(schema = @Schema(implementation = TerminalTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Terminal type not found"),
        @ApiResponse(responseCode = "409", description = "Terminal type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TerminalTypeDTO> update(
            @Parameter(description = "Terminal type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated terminal type data", required = true) @Valid @RequestBody TerminalTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:MANAGE')")
    @Operation(summary = "Delete terminal type", description = "Deletes a terminal type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Terminal type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Terminal type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete terminal type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Terminal type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    @Operation(summary = "Search terminal types", description = "Searches terminal types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<TerminalTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<TerminalTypeDTO> searchByQuery(String query, Pageable pageable) {
        return terminalTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    @Operation(summary = "Check if terminal type exists", description = "Checks if a terminal type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Terminal type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('TERMINAL_TYPE:READ')")
    @Operation(summary = "Count terminal types", description = "Returns the total number of terminal types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Terminal type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires TERMINAL_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
