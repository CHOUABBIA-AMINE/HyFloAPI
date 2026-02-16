/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: VendorTypeController
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
import dz.sh.trc.hyflo.network.type.dto.VendorTypeDTO;
import dz.sh.trc.hyflo.network.type.service.VendorTypeService;
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
@RequestMapping("/network/type/vendor")
@Slf4j
@Tag(name = "Vendor Type Management", description = "APIs for managing vendor type classifications")
@SecurityRequirement(name = "bearer-auth")
public class VendorTypeController extends GenericController<VendorTypeDTO, Long> {

    private final VendorTypeService vendorTypeService;

    public VendorTypeController(VendorTypeService vendorTypeService) {
        super(vendorTypeService, "VendorType");
        this.vendorTypeService = vendorTypeService;
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    @Operation(summary = "Get vendor type by ID", description = "Retrieves a single vendor type by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor type found", content = @Content(schema = @Schema(implementation = VendorTypeDTO.class))),
        @ApiResponse(responseCode = "404", description = "Vendor type not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VendorTypeDTO> getById(
            @Parameter(description = "Vendor type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    @Operation(summary = "Get all vendor types (paginated)", description = "Retrieves a paginated list of all vendor types")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor types retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<VendorTypeDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    @Operation(summary = "Get all vendor types (unpaginated)", description = "Retrieves all vendor types without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor types retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<VendorTypeDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:MANAGE')")
    @Operation(summary = "Create new vendor type", description = "Creates a new vendor type with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vendor type created successfully", content = @Content(schema = @Schema(implementation = VendorTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Vendor type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VendorTypeDTO> create(
            @Parameter(description = "Vendor type data", required = true) 
            @Valid @RequestBody VendorTypeDTO dto) {
        VendorTypeDTO created = vendorTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:MANAGE')")
    @Operation(summary = "Update vendor type", description = "Updates an existing vendor type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor type updated successfully", content = @Content(schema = @Schema(implementation = VendorTypeDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Vendor type not found"),
        @ApiResponse(responseCode = "409", description = "Vendor type name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VendorTypeDTO> update(
            @Parameter(description = "Vendor type ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated vendor type data", required = true) @Valid @RequestBody VendorTypeDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:MANAGE')")
    @Operation(summary = "Delete vendor type", description = "Deletes a vendor type permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vendor type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Vendor type not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete vendor type - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Vendor type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    @Operation(summary = "Search vendor types", description = "Searches vendor types by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<VendorTypeDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<VendorTypeDTO> searchByQuery(String query, Pageable pageable) {
        return vendorTypeService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    @Operation(summary = "Check if vendor type exists", description = "Checks if a vendor type with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Vendor type ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('VENDOR_TYPE:READ')")
    @Operation(summary = "Count vendor types", description = "Returns the total number of vendor types in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor type count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires VENDOR_TYPE:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
