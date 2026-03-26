/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : StructureTypeController
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : General / Type
 *
 **/

package dz.sh.trc.hyflo.general.type.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.general.type.dto.query.StructureTypeReadDto;
import dz.sh.trc.hyflo.general.type.service.IStructureTypeQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/general/type/structure-type")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Structure Types", description = "Reference APIs for organizational structure type classifications")
@SecurityRequirement(name = "bearer-auth")
public class StructureTypeController {

    private final IStructureTypeQueryService structureTypeService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GENERAL:READ')")
    @Operation(summary = "Get structure type by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Structure type found",
                content = @Content(schema = @Schema(implementation = StructureTypeReadDto.class))),
        @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<StructureTypeReadDto> getById(
            @Parameter(description = "Structure type ID", required = true) @PathVariable Long id) {
        log.debug("GET /general/type/structure-type/{}", id);
        return ResponseEntity.ok(structureTypeService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GENERAL:READ')")
    @Operation(summary = "Get all structure types (paginated)")
    public ResponseEntity<Page<StructureTypeReadDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /general/type/structure-type?page={}&size={}", page, size);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(structureTypeService.getAll(pageable));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('GENERAL:READ')")
    @Operation(summary = "Get all structure types (unpaginated)")
    public ResponseEntity<List<StructureTypeReadDto>> getAllUnpaginated() {
        log.debug("GET /general/type/structure-type/all");
        return ResponseEntity.ok(structureTypeService.getAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('GENERAL:READ')")
    @Operation(summary = "Search structure types by designation (FR / EN / AR)")
    public ResponseEntity<Page<StructureTypeReadDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "designationFr") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.debug("GET /general/type/structure-type/search?q={}", q);
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(structureTypeService.searchByQuery(q, pageable));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('GENERAL:READ')")
    @Operation(summary = "Count all structure types")
    public ResponseEntity<Long> count() {
        log.debug("GET /general/type/structure-type/count");
        return ResponseEntity.ok(structureTypeService.count());
    }
}
