/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CountryController
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
import dz.sh.trc.hyflo.general.localization.dto.CountryDTO;
import dz.sh.trc.hyflo.general.localization.service.CountryService;
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
@RequestMapping("/general/localization/country")
@Slf4j
@Tag(name = "Country Management", description = "APIs for managing countries and their details")
@SecurityRequirement(name = "bearer-auth")
public class CountryController extends GenericController<CountryDTO, Long> {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        super(countryService, "Country");
        this.countryService = countryService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Get country by ID", description = "Retrieves a single country by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Country found", content = @Content(schema = @Schema(implementation = CountryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CountryDTO> getById(
            @Parameter(description = "Country ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Get all countries (paginated)", description = "Retrieves a paginated list of all countries")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Countries retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CountryDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Get all countries (unpaginated)", description = "Retrieves all countries without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Countries retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CountryDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:MANAGE')")
    @Operation(summary = "Create new country", description = "Creates a new country with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Country created successfully", content = @Content(schema = @Schema(implementation = CountryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Country name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CountryDTO> create(
            @Parameter(description = "Country data", required = true) 
            @Valid @RequestBody CountryDTO dto) {
        CountryDTO created = countryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:MANAGE')")
    @Operation(summary = "Update country", description = "Updates an existing country")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Country updated successfully", content = @Content(schema = @Schema(implementation = CountryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "409", description = "Country name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CountryDTO> update(
            @Parameter(description = "Country ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated country data", required = true) @Valid @RequestBody CountryDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:MANAGE')")
    @Operation(summary = "Delete country", description = "Deletes a country permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Country deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete country - has dependencies (states, locations)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Country ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Search countries", description = "Searches countries by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CountryDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Check if country exists", description = "Checks if a country with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Country ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Count countries", description = "Returns the total number of countries in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Country count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<CountryDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return countryService.getAll(pageable);
        }
        return countryService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all countries as list (non-paginated)
     * GET /country/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('COUNTRY:READ')")
    @Operation(summary = "Get all countries as list", description = "Retrieves all countries as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Country list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COUNTRY:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CountryDTO>> getAllList() {
        log.debug("GET /country/list - Getting all countries as list");
        List<CountryDTO> countries = countryService.getAll();
        return success(countries);
    }
}
