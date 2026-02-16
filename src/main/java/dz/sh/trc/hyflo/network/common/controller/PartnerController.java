/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PartnerController
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
import dz.sh.trc.hyflo.network.common.dto.PartnerDTO;
import dz.sh.trc.hyflo.network.common.service.PartnerService;
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
@RequestMapping("/network/common/partner")
@Slf4j
@Tag(name = "Partner Management", description = "APIs for managing business partners and stakeholders")
@SecurityRequirement(name = "bearer-auth")
public class PartnerController extends GenericController<PartnerDTO, Long> {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        super(partnerService, "Partner");
        this.partnerService = partnerService;
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    @Operation(summary = "Get partner by ID", description = "Retrieves a single partner by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner found", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Partner not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PartnerDTO> getById(
            @Parameter(description = "Partner ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    @Operation(summary = "Get all partners (paginated)", description = "Retrieves a paginated list of all partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PartnerDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    @Operation(summary = "Get all partners (unpaginated)", description = "Retrieves all partners without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PartnerDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:MANAGE')")
    @Operation(summary = "Create new partner", description = "Creates a new partner with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Partner created successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Partner name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PartnerDTO> create(
            @Parameter(description = "Partner data", required = true) 
            @Valid @RequestBody PartnerDTO dto) {
        PartnerDTO created = partnerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:MANAGE')")
    @Operation(summary = "Update partner", description = "Updates an existing partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner updated successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Partner not found"),
        @ApiResponse(responseCode = "409", description = "Partner name/code already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PartnerDTO> update(
            @Parameter(description = "Partner ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated partner data", required = true) @Valid @RequestBody PartnerDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:MANAGE')")
    @Operation(summary = "Delete partner", description = "Deletes a partner permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Partner deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Partner not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete partner - has dependencies"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Partner ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    @Operation(summary = "Search partners", description = "Searches partners by name, code, or description (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<PartnerDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    protected Page<PartnerDTO> searchByQuery(String query, Pageable pageable) {
        return partnerService.globalSearch(query, pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    @Operation(summary = "Check if partner exists", description = "Checks if a partner with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Partner ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('PARTNER:READ')")
    @Operation(summary = "Count partners", description = "Returns the total number of partners in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partner count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires PARTNER:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> count() {
        return super.count();
    }
}
