/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PartnerTypeController
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
import dz.sh.trc.hyflo.network.type.dto.PartnerTypeDTO;
import dz.sh.trc.hyflo.network.type.service.PartnerTypeService;
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
@RequestMapping("/network/type/partner")
@Slf4j
@Tag(name = "Partner Type Management", description = "APIs for managing partner type classifications")
@SecurityRequirement(name = "bearer-auth")
public class PartnerTypeController extends GenericController<PartnerTypeDTO, Long> {

    private final PartnerTypeService partnerTypeService;

    public PartnerTypeController(PartnerTypeService partnerTypeService) {
        super(partnerTypeService, "PartnerType");
        this.partnerTypeService = partnerTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    @Operation(summary = "Get partner type by ID", description = "Retrieves a single partner type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner type found", content = @Content(schema = @Schema(implementation = PartnerTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Partner type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PartnerTypeDTO> getById(
            @Parameter(description = "Partner type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    @Operation(summary = "Get all partner types (paginated)", description = "Retrieves a paginated list of all partner types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PartnerTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    @Operation(summary = "Get all partner types (unpaginated)", description = "Retrieves all partner types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PartnerTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:MANAGE')")
    @Operation(summary = "Create new partner type", description = "Creates a new partner type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Partner type created successfully", content = @Content(schema = @Schema(implementation = PartnerTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Partner type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PartnerTypeDTO> create(
            @Parameter(description = "Partner type data", required = true) 
            @Valid @RequestBody PartnerTypeDTO dto) {
        PartnerTypeDTO created = partnerTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:MANAGE')")
    @Operation(summary = "Update partner type", description = "Updates an existing partner type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner type updated successfully", content = @Content(schema = @Schema(implementation = PartnerTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Partner type not found"),
        @ApiResponse(responseCode = "409", description = "Partner type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PartnerTypeDTO> update(
            @Parameter(description = "Partner type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated partner type data", required = true) @Valid @RequestBody PartnerTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:MANAGE')")
    @Operation(summary = "Delete partner type", description = "Deletes a partner type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Partner type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Partner type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete partner type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Partner type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    @Operation(summary = "Search partner types", description = "Searches partner types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PartnerTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<PartnerTypeDTO> searchByQuery(String query, Pageable pageable) {
        return partnerTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    @Operation(summary = "Check if partner type exists", description = "Checks if a partner type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Partner type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER_TYPE:READ')")
    @Operation(summary = "Count partner types", description = "Returns the total number of partner types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
