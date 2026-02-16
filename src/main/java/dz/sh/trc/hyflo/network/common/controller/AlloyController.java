/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AlloyController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Common 
 *
 **/

package dz.sh.trc.hyflo.network.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.common.dto.AlloyDTO;
import dz.sh.trc.hyflo.network.common.service.AlloyService;
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

@RestController
@RequestMapping("/network/common/alloy")
@Slf4j
@Tag(name = "Alloy Management", description = "APIs for managing metal alloys and materials")
@SecurityRequirement(name = "bearer-auth")
public class AlloyController extends GenericController<AlloyDTO, Long> {

    @SuppressWarnings("unused")
	private final AlloyService alloyService;

    public AlloyController(AlloyService alloyService) {
        super(alloyService, "Alloy");
        this.alloyService = alloyService;
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    @Operation(summary = "Get alloy by ID", description = "Retrieves a single alloy by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alloy found", content = @Content(schema = @Schema(implementation = AlloyDTO.class))),
        @ApiResponse(responseCode = "404", description = "Alloy not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AlloyDTO> getById(
            @Parameter(description = "Alloy ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    @Operation(summary = "Get all alloys (paginated)", description = "Retrieves a paginated list of all alloys")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alloys retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AlloyDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    @Operation(summary = "Get all alloys (unpaginated)", description = "Retrieves all alloys without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alloys retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<AlloyDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:MANAGE')")
    @Operation(summary = "Create new alloy", description = "Creates a new alloy with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alloy created successfully", content = @Content(schema = @Schema(implementation = AlloyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Alloy name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AlloyDTO> create(
            @Parameter(description = "Alloy data", required = true) 
            @Valid @RequestBody AlloyDTO dto) {
        AlloyDTO created = alloyService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:MANAGE')")
    @Operation(summary = "Update alloy", description = "Updates an existing alloy")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alloy updated successfully", content = @Content(schema = @Schema(implementation = AlloyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Alloy not found"),
        @ApiResponse(responseCode = "409", description = "Alloy name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AlloyDTO> update(
            @Parameter(description = "Alloy ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated alloy data", required = true) @Valid @RequestBody AlloyDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:MANAGE')")
    @Operation(summary = "Delete alloy", description = "Deletes an alloy permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Alloy deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Alloy not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete alloy - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Alloy ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    @Operation(summary = "Search alloys", description = "Searches alloys by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<AlloyDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    @Operation(summary = "Check if alloy exists", description = "Checks if an alloy with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Alloy ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ALLOY:READ')")
    @Operation(summary = "Count alloys", description = "Returns the total number of alloys in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alloy count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires ALLOY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
