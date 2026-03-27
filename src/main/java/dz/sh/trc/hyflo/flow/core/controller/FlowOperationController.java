/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowOperationV2Controller
 *  @CreatedOn  : Phase 4 — Commit 31
 *  @UpdatedOn  : Phase 4/5 bridge — Commit 37
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Core
 *
 *  @Description: v2 REST controller for flow operations.
 *                Implements full CQRS split:
 *                  FlowOperationCommandService  — writes (create, update, delete, approve, reject)
 *                  FlowOperationQueryService    — reads  (all query methods)
 *                No entity exposure. No fat DTO in v2 write paths.
 *                Command body: FlowOperationCommandDTO (Commit 37 migration).
 *                Response body: FlowOperationReadDTO exclusively.
 *
 *  Phase 4 — Commit 31
 *  Commit 37  — @RequestBody migrated from FlowOperationDTO to
 *                FlowOperationCommandDTO on POST and PUT endpoints.
 *
 *  Service method signatures (Commit 37):
 *
 *  Command:
 *    create(FlowOperationCommandDTO)                        -> FlowOperationReadDTO
 *    update(Long, FlowOperationCommandDTO)                  -> FlowOperationReadDTO
 *    delete(Long)                                            -> void
 *    approve(Long id, Long validatorId)                     -> FlowOperationReadDTO
 *    reject(Long id, Long validatorId, String reason)       -> FlowOperationReadDTO
 *
 *  Query:
 *    findById(Long)                                          -> FlowOperationReadDTO
 *    findAll(Pageable)                                       -> Page<FlowOperationReadDTO>
 *    findByDate(LocalDate)                                   -> List<FlowOperationReadDTO>
 *    findByDateRange(LocalDate, LocalDate)                   -> List<FlowOperationReadDTO>
 *    findByInfrastructure(Long)                              -> List<FlowOperationReadDTO>
 *    findByProduct(Long)                                     -> List<FlowOperationReadDTO>
 *    findByType(Long)                                        -> List<FlowOperationReadDTO>
 *    findByValidationStatus(Long)                            -> List<FlowOperationReadDTO>
 *    findByInfrastructureAndDateRange(Long, LocalDate, LocalDate, Pageable)
 *    findByProductAndTypeAndDateRange(Long, Long, LocalDate, LocalDate, Pageable)
 *    findByValidationStatusAndDateRange(Long, LocalDate, LocalDate, Pageable)
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
@RequestMapping("/api/v2/flow/operations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flow Operations v2",
     description = "Hydrocarbon transport flow operations — CQRS v2 API (create, update, delete, approve, reject + full query surface)")
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
        log.debug("GET /api/v2/flow/operations/{}", id);
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
        log.debug("GET /api/v2/flow/operations/date/{}", date);
        return ResponseEntity.ok(queryService.findByDate(date));
    }

    @GetMapping("/range")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations within a date range")
    public ResponseEntity<List<FlowOperationReadDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.debug("GET /api/v2/flow/operations/range from={} to={}", from, to);
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
    public ResponseEntity<List<FlowOperationReadDTO>> getByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(queryService.findByProduct(productId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('FLOW:READ')")
    @Operation(summary = "Get operations by operation type")
    public ResponseEntity<List<FlowOperationReadDTO>> getByType(
            @PathVariable Long typeId) {
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
    // COMMAND ENDPOINTS
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Create a new flow operation",
               description = "Records a new hydrocarbon transport operation. Enforces date+infrastructure+product+type uniqueness. "
                           + "PENDING validation status is assigned server-side.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Operation created"),
        @ApiResponse(responseCode = "400", description = "Invalid data or uniqueness violation"),
        @ApiResponse(responseCode = "404", description = "Referenced infrastructure, product or type not found")
    })
    public ResponseEntity<FlowOperationReadDTO> create(
            @Valid @RequestBody FlowOperationCommandDTO dto) {
        log.debug("POST /api/v2/flow/operations - create");
        return ResponseEntity.status(HttpStatus.CREATED).body(commandService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('FLOW:WRITE')")
    @Operation(summary = "Update a flow operation",
               description = "Updates an existing PENDING operation. Only pre-validated operations may be modified.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation updated"),
        @ApiResponse(responseCode = "409", description = "Operation is not in a modifiable state")
    })
    public ResponseEntity<FlowOperationReadDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FlowOperationCommandDTO dto) {
        log.debug("PUT /api/v2/flow/operations/{}", id);
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
        log.debug("DELETE /api/v2/flow/operations/{}", id);
        commandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Approve a pending flow operation",
               description = "Transitions operation status from PENDING to VALIDATED. Updates linked WorkflowInstance.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation approved"),
        @ApiResponse(responseCode = "409", description = "Operation is not in PENDING state"),
        @ApiResponse(responseCode = "404", description = "Operation or validator employee not found")
    })
    public ResponseEntity<FlowOperationReadDTO> approve(
            @PathVariable Long id,
            @Parameter(description = "Employee ID performing the approval")
            @RequestParam Long validatorId) {
        log.info("POST /api/v2/flow/operations/{}/approve validatorId={}", id, validatorId);
        return ResponseEntity.ok(commandService.approve(id, validatorId));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "Reject a pending flow operation",
               description = "Transitions operation status from PENDING to REJECTED. Rejection reason is appended to audit notes.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operation rejected"),
        @ApiResponse(responseCode = "409", description = "Operation is not in a rejectable state"),
        @ApiResponse(responseCode = "404", description = "Operation or validator employee not found")
    })
    public ResponseEntity<FlowOperationReadDTO> reject(
            @PathVariable Long id,
            @Parameter(description = "Employee ID performing the rejection")
            @RequestParam Long validatorId,
            @Parameter(description = "Mandatory rejection reason (appended to audit notes)")
            @RequestParam String reason) {
        log.info("POST /api/v2/flow/operations/{}/reject validatorId={}", id, validatorId);
        return ResponseEntity.ok(commandService.reject(id, validatorId, reason));
    }
}
