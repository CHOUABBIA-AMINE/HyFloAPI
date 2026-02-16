/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PersonController
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 02-16-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.general.organization.dto.PersonDTO;
import dz.sh.trc.hyflo.general.organization.service.PersonService;
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

/**
 * Person REST Controller - Extends GenericController
 * Provides standard CRUD endpoints for Person management
 */
@RestController
@RequestMapping("/general/organization/person")
@Slf4j
@Tag(name = "Person Management", description = "APIs for managing individual persons and their personal information")
@SecurityRequirement(name = "bearer-auth")
public class PersonController extends GenericController<PersonDTO, Long> {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        super(personService, "Person");
        this.personService = personService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Get person by ID", description = "Retrieves a single person by their unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Person found", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(responseCode = "404", description = "Person not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PersonDTO> getById(
            @Parameter(description = "Person ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Get all persons (paginated)", description = "Retrieves a paginated list of all persons")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persons retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PersonDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Get all persons (unpaginated)", description = "Retrieves all persons without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persons retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PersonDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:MANAGE')")
    @Operation(summary = "Create new person", description = "Creates a new person with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Person created successfully", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Person identifier already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PersonDTO> create(
            @Parameter(description = "Person data", required = true) 
            @Valid @RequestBody PersonDTO dto) {
        PersonDTO created = personService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:MANAGE')")
    @Operation(summary = "Update person", description = "Updates an existing person")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Person updated successfully", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Person not found"),
        @ApiResponse(responseCode = "409", description = "Person identifier already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PersonDTO> update(
            @Parameter(description = "Person ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated person data", required = true) @Valid @RequestBody PersonDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:MANAGE')")
    @Operation(summary = "Delete person", description = "Deletes a person permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Person not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete person - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Person ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Search persons", description = "Searches persons by name, identifier, or contact information (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PersonDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Check if person exists", description = "Checks if a person with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Person ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Count persons", description = "Returns the total number of persons in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Person count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== IMPLEMENT SEARCH ==========

    @Override
    protected Page<PersonDTO> searchByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return personService.getAll(pageable);
        }
        return personService.globalSearch(query, pageable);
    }

    // ========== CUSTOM ENDPOINTS ==========

    /**
     * Get all persons as list (non-paginated)
     * GET /person/list
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('PERSON:READ')")
    @Operation(summary = "Get all persons as list", description = "Retrieves all persons as a simple list without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Person list retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PERSON:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PersonDTO>> getAllList() {
        log.debug("GET /person/list - Getting all persons as list");
        List<PersonDTO> persons = personService.getAll();
        return success(persons);
    }
}
