/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: QualityFlagController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.QualityFlagDTO;
import dz.sh.trc.hyflo.flow.common.service.QualityFlagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/common/qualityFlag")
@Tag(name = "Quality Flag", description = "Flow quality flag management API")
@Slf4j
public class QualityFlagController extends GenericController<QualityFlagDTO, Long> {

    private final QualityFlagService qualityFlagService;
    
    public QualityFlagController(QualityFlagService qualityFlagService) {
        super(qualityFlagService, "QualityFlag");
        this.qualityFlagService = qualityFlagService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get quality flag by ID", description = "Retrieve a single quality flag by its unique identifier")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<QualityFlagDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all quality flags (paginated)", description = "Retrieve all quality flags with pagination and sorting")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Page<QualityFlagDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all quality flags (no pagination)", description = "Retrieve all quality flags sorted by code")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<List<QualityFlagDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create quality flag", description = "Create a new quality flag with unique code and French designation validation")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:MANAGE')")
    public ResponseEntity<QualityFlagDTO> create(@Valid @RequestBody QualityFlagDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update quality flag", description = "Update an existing quality flag by ID")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:MANAGE')")
    public ResponseEntity<QualityFlagDTO> update(@PathVariable Long id, @Valid @RequestBody QualityFlagDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete quality flag", description = "Delete a quality flag by ID")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search quality flags", description = "Search quality flags across all fields with pagination")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Page<QualityFlagDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if quality flag exists", description = "Check if a quality flag exists by ID")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count quality flags", description = "Get total count of quality flags")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get quality flag by code", description = "Find quality flag by its unique code (GOOD, SUSPECT, BAD, etc.)")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<QualityFlagDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/common/qualityflag/code/{} - Getting quality flag by code", code);
        return ResponseEntity.ok(qualityFlagService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get quality flag by French designation", description = "Find quality flag by its unique French designation")
    @PreAuthorize("hasAuthority('QUALITY_FLAG:READ')")
    public ResponseEntity<QualityFlagDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/qualityflag/designation/{} - Getting quality flag by French designation", designationFr);
        return ResponseEntity.ok(qualityFlagService.findByDesignationFr(designationFr));
    }
}
