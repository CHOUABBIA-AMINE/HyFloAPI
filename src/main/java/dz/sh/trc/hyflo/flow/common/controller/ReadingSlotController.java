/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSlotController
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
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.flow.common.service.ReadingSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/common/readingSlot")
@Tag(name = "Reading Slot", description = "Flow reading slot  management API")
@Slf4j
public class ReadingSlotController extends GenericController<ReadingSlotDTO, Long> {

    private final ReadingSlotService readingSlotService;
    
    public ReadingSlotController(ReadingSlotService readingSlotService) {
        super(readingSlotService, "ReadingSlot");
        this.readingSlotService = readingSlotService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get reading slot  by ID", description = "Retrieve a single reading slot  by its unique identifier")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<ReadingSlotDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all reading slot es (paginated)", description = "Retrieve all reading slot es with pagination and sorting")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Page<ReadingSlotDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all reading slot es (no pagination)", description = "Retrieve all reading slot es sorted by code")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<List<ReadingSlotDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create reading slot ", description = "Create a new reading slot  with unique code and French designation validation")
    @PreAuthorize("hasAuthority('READING_SLOT:ADMIN')")
    public ResponseEntity<ReadingSlotDTO> create(@Valid @RequestBody ReadingSlotDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update reading slot ", description = "Update an existing reading slot  by ID")
    @PreAuthorize("hasAuthority('READING_SLOT:ADMIN')")
    public ResponseEntity<ReadingSlotDTO> update(@PathVariable Long id, @Valid @RequestBody ReadingSlotDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete reading slot ", description = "Delete a reading slot  by ID")
    @PreAuthorize("hasAuthority('READING_SLOT:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search reading slot es", description = "Search reading slot es across all fields with pagination")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Page<ReadingSlotDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if reading slot  exists", description = "Check if a reading slot  exists by ID")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count reading slot es", description = "Get total count of reading slot es")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get reading slot  by code", description = "Find reading slot  by its unique code (VALIDATED, PENDING, etc.)")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<ReadingSlotDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/common/readingSlot /code/{} - Getting reading slot  by code", code);
        return ResponseEntity.ok(readingSlotService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get reading slot  by French designation", description = "Find reading slot  by its unique French designation")
    @PreAuthorize("hasAuthority('READING_SLOT:READ')")
    public ResponseEntity<ReadingSlotDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/readingSlot /designation/{} - Getting reading slot  by French designation", designationFr);
        return ResponseEntity.ok(readingSlotService.findByDesignationFr(designationFr));
    }
}
