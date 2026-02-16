/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StateController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.localization.dto.StateDTO;
import dz.sh.trc.hyflo.general.localization.service.StateService;
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
@RequestMapping("/general/localization/state")
@Slf4j
@Tag(name = "State Management", description = "APIs for managing states, provinces, or regional administrative divisions")
@SecurityRequirement(name = "bearer-auth")
public class StateController extends GenericController<StateDTO, Long> {

    private final StateService stateService;

    public StateController(StateService stateService) {
        super(stateService, "State");
        this.stateService = stateService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Get state by ID", description = "Retrieves a single state by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "State found", content = @Content(schema = @Schema(implementation = StateDTO.class))),
        @ApiResponse(responseCode = "404", description = "State not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StateDTO> getById(
            @Parameter(description = "State ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Get all states (paginated)", description = "Retrieves a paginated list of all states")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "States retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StateDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Get all states (unpaginated)", description = "Retrieves all states without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "States retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StateDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:MANAGE')")
    @Operation(summary = "Create new state", description = "Creates a new state with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "State created successfully", content = @Content(schema = @Schema(implementation = StateDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "State name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StateDTO> create(
            @Parameter(description = "State data", required = true) 
            @Valid @RequestBody StateDTO dto) {
        StateDTO created = stateService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:MANAGE')")
    @Operation(summary = "Update state", description = "Updates an existing state")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "State updated successfully", content = @Content(schema = @Schema(implementation = StateDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "State not found"),
        @ApiResponse(responseCode = "409", description = "State name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StateDTO> update(
            @Parameter(description = "State ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated state data", required = true) @Valid @RequestBody StateDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:MANAGE')")
    @Operation(summary = "Delete state", description = "Deletes a state permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "State deleted successfully"),
        @ApiResponse(responseCode = "404", description = "State not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete state - has dependencies (districts)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "State ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Search states", description = "Searches states by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StateDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Check if state exists", description = "Checks if a state with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "State ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Count states", description = "Returns the total number of states in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "State count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<StateDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return stateService.getAll(pageable);
        }
        return stateService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all states as list (non-paginated)
     * GET /state/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('STATE:READ')")
    @Operation(summary = "Get all states as list", description = "Retrieves all states as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "State list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires STATE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StateDTO>> getAllList() {
        log.debug("GET /state/list - Getting all states as list");
        List<StateDTO> states = stateService.getAll();
        return success(states);
    }
}
