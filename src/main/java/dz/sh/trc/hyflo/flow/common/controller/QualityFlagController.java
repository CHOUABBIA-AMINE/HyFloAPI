/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: QualityFlagController
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
import dz.sh.trc.hyflo.flow.common.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.flow.common.service.QualityFlagService;
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
@RequestMapping("/flow/common/qualityFlag")
@Tag(name = "Quality Flag Management", description = "APIs for managing data quality flags and indicators")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class QualityFlagController extends GenericController<QualityFlagDTO, Long> {

    private final QualityFlagService qualityFlagService;
    
    public QualityFlagController(QualityFlagService qualityFlagService) {
        super(qualityFlagService, "QualityFlag");
        this.qualityFlagService = qualityFlagService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get quality flag by ID", description = "Retrieves a single quality flag by its unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flag found", content = @Content(schema = @Schema(implementation = QualityFlagDTO.class))),
        @ApiResponse(responseCode = "404", description = "Quality flag not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<QualityFlagDTO> getById(
            @Parameter(description = "Quality flag ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all quality flags (paginated)", description = "Retrieves a paginated list of all quality flags")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flags retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Page<QualityFlagDTO>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all quality flags (unpaginated)", description = "Retrieves all quality flags without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flags retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<List<QualityFlagDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create quality flag", description = "Creates a new quality flag with validation")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Quality flag created successfully", content = @Content(schema = @Schema(implementation = QualityFlagDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "409", description = "Quality flag code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:MANAGE')")
    public ResponseEntity<QualityFlagDTO> create(
            @Parameter(description = "Quality flag data", required = true) 
            @Valid @RequestBody QualityFlagDTO dto) {
        QualityFlagDTO created = qualityFlagService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "Update quality flag", description = "Updates an existing quality flag")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flag updated successfully", content = @Content(schema = @Schema(implementation = QualityFlagDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
        @ApiResponse(responseCode = "404", description = "Quality flag not found"),
        @ApiResponse(responseCode = "409", description = "Quality flag code/designation already exists"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:MANAGE')")
    public ResponseEntity<QualityFlagDTO> update(
            @Parameter(description = "Quality flag ID", required = true, example = "1") @PathVariable Long id, 
            @Parameter(description = "Updated quality flag data", required = true) @Valid @RequestBody QualityFlagDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete quality flag", description = "Deletes a quality flag permanently")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Quality flag deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Quality flag not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete quality flag - has dependencies (readings using this flag)"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:MANAGE authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:MANAGE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Quality flag ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search quality flags", description = "Searches quality flags by code or designation (case-insensitive partial match)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results returned"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Page<QualityFlagDTO>> search(
            @Parameter(description = "Search query") @RequestParam(required = false) String q,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if quality flag exists", description = "Checks if a quality flag with the given ID exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Existence check result"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Boolean> exists(
            @Parameter(description = "Quality flag ID", required = true, example = "1") 
            @PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count quality flags", description = "Returns the total number of quality flags in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flag count returned"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get quality flag by code", description = "Retrieves a quality flag by its unique code (e.g., GOOD, SUSPECT, BAD)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flag found", content = @Content(schema = @Schema(implementation = QualityFlagDTO.class))),
        @ApiResponse(responseCode = "404", description = "Quality flag not found with given code"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<QualityFlagDTO> getByCode(
            @Parameter(description = "Quality flag code", required = true, example = "GOOD") 
            @PathVariable String code) {
        log.info("GET /flow/common/qualityflag/code/{} - Getting quality flag by code", code);
        return ResponseEntity.ok(qualityFlagService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get quality flag by French designation", description = "Retrieves a quality flag by its unique French designation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quality flag found", content = @Content(schema = @Schema(implementation = QualityFlagDTO.class))),
        @ApiResponse(responseCode = "404", description = "Quality flag not found with given designation"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires QUALITY_FLAG:READ authority"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<QualityFlagDTO> getByDesignationFr(
            @Parameter(description = "French designation", required = true, example = "Bonne") 
            @PathVariable String designationFr) {
        log.info("GET /flow/common/qualityflag/designation/{} - Getting quality flag by French designation", designationFr);
        return ResponseEntity.ok(qualityFlagService.findByDesignationFr(designationFr));
    }
}
