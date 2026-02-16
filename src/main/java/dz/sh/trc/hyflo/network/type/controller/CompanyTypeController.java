/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: CompanyTypeController
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
import dz.sh.trc.hyflo.network.type.dto.CompanyTypeDTO;
import dz.sh.trc.hyflo.network.type.service.CompanyTypeService;
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
@RequestMapping("/network/type/company")
@Slf4j
@Tag(name = "Company Type Management", description = "APIs for managing company type classifications")
@SecurityRequirement(name = "bearer-auth")
public class CompanyTypeController extends GenericController<CompanyTypeDTO, Long> {

    private final CompanyTypeService companyTypeService;

    public CompanyTypeController(CompanyTypeService companyTypeService) {
        super(companyTypeService, "CompanyType");
        this.companyTypeService = companyTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    @Operation(summary = "Get company type by ID", description = "Retrieves a single company type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company type found", content = @Content(schema = @Schema(implementation = CompanyTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Company type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CompanyTypeDTO> getById(
            @Parameter(description = "Company type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    @Operation(summary = "Get all company types (paginated)", description = "Retrieves a paginated list of all company types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CompanyTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    @Operation(summary = "Get all company types (unpaginated)", description = "Retrieves all company types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CompanyTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:MANAGE')")
    @Operation(summary = "Create new company type", description = "Creates a new company type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Company type created successfully", content = @Content(schema = @Schema(implementation = CompanyTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Company type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CompanyTypeDTO> create(
            @Parameter(description = "Company type data", required = true) 
            @Valid @RequestBody CompanyTypeDTO dto) {
        CompanyTypeDTO created = companyTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:MANAGE')")
    @Operation(summary = "Update company type", description = "Updates an existing company type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company type updated successfully", content = @Content(schema = @Schema(implementation = CompanyTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Company type not found"),
        @ApiResponse(responseCode = "409", description = "Company type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CompanyTypeDTO> update(
            @Parameter(description = "Company type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated company type data", required = true) @Valid @RequestBody CompanyTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:MANAGE')")
    @Operation(summary = "Delete company type", description = "Deletes a company type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Company type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Company type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete company type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Company type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    @Operation(summary = "Search company types", description = "Searches company types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CompanyTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    protected Page<CompanyTypeDTO> searchByQuery(String query, Pageable pageable) {
        return companyTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    @Operation(summary = "Check if company type exists", description = "Checks if a company type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Company type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('COMPANY_TYPE:READ')")
    @Operation(summary = "Count company types", description = "Returns the total number of company types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Company type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires COMPANY_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
