/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingSourceNatureController
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import dz.sh.trc.hyflo.flow.common.dto.ReadingSourceNatureDTO;
import dz.sh.trc.hyflo.flow.common.service.ReadingSourceNatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flow/common/reading-source-natures")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reading Source Nature", description = "Reference data for reading source natures (DIRECT, DERIVED, AI_ASSISTED)")
public class ReadingSourceNatureController {

    private final ReadingSourceNatureService readingSourceNatureService;

    @GetMapping
    @Operation(summary = "Get all reading source natures (paginated)")
    public ResponseEntity<Page<ReadingSourceNatureDTO>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(readingSourceNatureService.getAll(pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all reading source natures (list)")
    public ResponseEntity<List<ReadingSourceNatureDTO>> getAllList() {
        return ResponseEntity.ok(readingSourceNatureService.getAll());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active reading source natures")
    public ResponseEntity<List<ReadingSourceNatureDTO>> getAllActive() {
        return ResponseEntity.ok(readingSourceNatureService.getAllActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reading source nature by ID")
    public ResponseEntity<ReadingSourceNatureDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(readingSourceNatureService.getById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get reading source nature by code")
    public ResponseEntity<ReadingSourceNatureDTO> getByCode(@PathVariable String code) {
        return readingSourceNatureService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Global search across reading source natures")
    public ResponseEntity<Page<ReadingSourceNatureDTO>> search(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(readingSourceNatureService.globalSearch(query, pageable));
    }

    @PostMapping
    @Operation(summary = "Create a new reading source nature")
    public ResponseEntity<ReadingSourceNatureDTO> create(
            @Valid @RequestBody ReadingSourceNatureDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readingSourceNatureService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing reading source nature")
    public ResponseEntity<ReadingSourceNatureDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ReadingSourceNatureDTO dto) {
        return ResponseEntity.ok(readingSourceNatureService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reading source nature")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        readingSourceNatureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
