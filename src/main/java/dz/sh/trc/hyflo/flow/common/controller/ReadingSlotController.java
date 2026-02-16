/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSlotController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.service.ReadingSlotService;
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
@RequestMapping("/flow/common/readingSlot")
@Tag(name = "Reading Slot Management", description = "APIs for managing time slots for flow readings")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class ReadingSlotController extends GenericController<ReadingSlotDTO, Long> {

    private final ReadingSlotService readingSlotService;
    
    public ReadingSlotController(ReadingSlotService readingSlotService) {
        super(readingSlotService, "ReadingSlot");
        this.readingSlotService = readingSlotService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get reading slot by ID", description = "Retrieves a single reading slot by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slot found", content = @Content(schema = @Schema(implementation = ReadingSlotDTO.class))),
        @ApiResponse(responseCode = "404", description = "Reading slot not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<ReadingSlotDTO> getById(
            @Parameter(description = "Reading slot ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all reading slots (paginated)", description = "Retrieves a paginated list of all reading slots")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slots retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Page<ReadingSlotDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all reading slots (unpaginated)", description = "Retrieves all reading slots without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slots retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<List<ReadingSlotDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create reading slot", description = "Creates a new reading slot with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reading slot created successfully", content = @Content(schema = @Schema(implementation = ReadingSlotDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Reading slot code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:MANAGE')")
    public ResponseEntity<ReadingSlotDTO> create(
            @Parameter(description = "Reading slot data", required = true) 
            @Valid @RequestBody ReadingSlotDTO dto) {
        ReadingSlotDTO created = readingSlotService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update reading slot", description = "Updates an existing reading slot")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slot updated successfully", content = @Content(schema = @Schema(implementation = ReadingSlotDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Reading slot not found"),
        @ApiResponse(responseCode = "409", description = "Reading slot code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:MANAGE')")
    public ResponseEntity<ReadingSlotDTO> update(
            @Parameter(description = "Reading slot ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated reading slot data", required = true) @Valid @RequestBody ReadingSlotDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete reading slot", description = "Deletes a reading slot permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reading slot deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Reading slot not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete reading slot - has dependencies (readings using this slot)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Reading slot ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search reading slots", description = "Searches reading slots by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Page<ReadingSlotDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if reading slot exists", description = "Checks if a reading slot with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Reading slot ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count reading slots", description = "Returns the total number of reading slots in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slot count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get reading slot by code", description = "Retrieves a reading slot by its unique code (e.g., HOURLY, DAILY, MONTHLY)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slot found", content = @Content(schema = @Schema(implementation = ReadingSlotDTO.class))),
        @ApiResponse(responseCode = "404", description = "Reading slot not found with given code"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<ReadingSlotDTO> getByCode(
            @Parameter(description = "Reading slot code", required = true, example = "HOURLY") 
            @PathVariable String code) {
        log.info("GET /flow/common/readingslot/code/{} - Getting reading slot by code", code);
        return ResponseEntity.ok(readingSlotService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get reading slot by French designation", description = "Retrieves a reading slot by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reading slot found", content = @Content(schema = @Schema(implementation = ReadingSlotDTO.class))),
        @ApiResponse(responseCode = "404", description = "Reading slot not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires READING_SLOT:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<ReadingSlotDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "Horaire") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/readingslot/designation/{} - Getting reading slot by French designation", designationFr);
        return ResponseEntity.ok(readingSlotService.findByDesignationFr(designationFr));
    }
}
