/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : FlowOperationController
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026
 * 	@UpdatedOn	: 03-28-2026 — refactor(v2): removed approve/reject endpoints.
 *                             Workflow transitions moved to:
 *                             flow.workflow.controller.FlowOperationWorkflowController
 *                             POST /flow/workflow/operation/{id}/approve
 *                             POST /flow/workflow/operation/{id}/reject
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: CRUD and query endpoints for FlowOperation.
 *                Approve/reject lifecycle endpoints are now in
 *                FlowOperationWorkflowController (flow.workflow package).
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowOperationReadDTO;
import dz.sh.trc.hyflo.flow.core.dto.command.FlowOperationCommandDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowOperationCommandService;
import dz.sh.trc.hyflo.flow.core.service.FlowOperationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flow/operation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Operations v2",
        description = "Hydrocarbon transport flow operations — CRUD + query. "
                + "Workflow transitions (approve/reject) are at /flow/workflow/operation/{id}/approve|reject")
@SecurityRequirement(name = "bearer-auth")
public class FlowOperationController {

    private final FlowOperationCommandService commandService;
    private final FlowOperationQueryService   queryService;

    // =========================================================
    // QUERY ENDPOINTS
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get flow operation by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation found"),
            @ApiResponse(responseCode = "404", description = "Operation not found")
    })
    public ResponseEntity<FlowOperationReadDTO> getById(
            @Parameter(description = "Flow operation ID") @PathVariable Long id) {
        log.debug("GET /flow/operation/{}", id);
        return ResponseEntity.ok(queryService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "List all flow operations (paginated)")
    public ResponseEntity<Page<FlowOperationReadDTO>> getAll(
            @PageableDefault(size = 20, sort = "operationDate") Pageable pageable) {
        return ResponseEntity.ok(queryService.findAll(pageable));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations for a specific date")
    public ResponseEntity<List<FlowOperationReadDTO>> getByDate(
            @Parameter(description = "Operation date, format: yyyy-MM-dd")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.debug("GET /flow/operation/date/{}", date);
        return ResponseEntity.ok(queryService.findByDate(date));
    }

    @GetMapping("/range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations within a date range")
    public ResponseEntity<List<FlowOperationReadDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /flow/operation/range from={} to={}", from, to);
        return ResponseEntity.ok(queryService.findByDateRange(from, to));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations by infrastructure")
    public ResponseEntity<List<FlowOperationReadDTO>> getByInfrastructure(
            @PathVariable Long infrastructureId) {
        return ResponseEntity.ok(queryService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations by product")
    public ResponseEntity<List<FlowOperationReadDTO>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(queryService.findByProduct(productId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations by operation type")
    public ResponseEntity<List<FlowOperationReadDTO>> getByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(queryService.findByType(typeId));
    }

    @GetMapping("/status/{validationStatusId}")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Get operations by validation status")
    public ResponseEntity<List<FlowOperationReadDTO>> getByValidationStatus(
            @PathVariable Long validationStatusId) {
        return ResponseEntity.ok(queryService.findByValidationStatus(validationStatusId));
    }

    @GetMapping("/infrastructure/{infrastructureId}/range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations by infrastructure and date range (paginated)")
    public ResponseEntity<Page<FlowOperationReadDTO>> getByInfrastructureAndDateRange(
            @PathVariable Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                queryService.findByInfrastructureAndDateRange(infrastructureId, from, to, pageable));
    }

    @GetMapping("/product/{productId}/type/{typeId}/range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations by product, type and date range (paginated)")
    public ResponseEntity<Page<FlowOperationReadDTO>> getByProductAndTypeAndDateRange(
            @PathVariable Long productId,
            @PathVariable Long typeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                queryService.findByProductAndTypeAndDateRange(productId, typeId, from, to, pageable));
    }

    @GetMapping("/status/{statusId}/range")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Get operations by validation status and date range (paginated)")
    public ResponseEntity<Page<FlowOperationReadDTO>> getByValidationStatusAndDateRange(
            @PathVariable Long statusId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                queryService.findByValidationStatusAndDateRange(statusId, from, to, pageable));
    }

    // =========================================================
    // COMMAND ENDPOINTS (CRUD only — no workflow transitions)
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Create a new flow operation",
               description = "Records a new hydrocarbon transport operation. "
                       + "PENDING validation status is assigned server-side.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operation created"),
            @ApiResponse(responseCode = "400", description = "Invalid data or uniqueness violation"),
            @ApiResponse(responseCode = "404", description = "Referenced infrastructure, product or type not found")
    })
    public ResponseEntity<FlowOperationReadDTO> create(
            @Valid @RequestBody FlowOperationCommandDTO dto) {
        log.debug("POST /flow/operation - create");
        return ResponseEntity.status(HttpStatus.CREATED).body(commandService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Update a flow operation",
               description = "Updates an existing PENDING operation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation updated"),
            @ApiResponse(responseCode = "409", description = "Operation is not in a modifiable state")
    })
    public ResponseEntity<FlowOperationReadDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FlowOperationCommandDTO dto) {
        log.debug("PUT /flow/operation/{}", id);
        return ResponseEntity.ok(commandService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:DELETE')")
    @Operation(summary = "Delete a flow operation",
               description = "Deletes a PENDING operation. Validated or approved operations cannot be deleted.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operation deleted"),
            @ApiResponse(responseCode = "409", description = "Operation cannot be deleted in current state")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("DELETE /flow/operation/{}", id);
        commandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
