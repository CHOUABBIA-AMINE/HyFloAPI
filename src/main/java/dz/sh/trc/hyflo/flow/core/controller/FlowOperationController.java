/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperationController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-31-2026 - Added validate and reject endpoints
 * 	@UpdatedOn	: 02-16-2026 - Added comprehensive OpenAPI documentation
 * 	@UpdatedOn	: 03-26-2026 - H10: Marked @Deprecated(forRemoval=true)
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 *  @Deprecated Phase 4 — H10
 *  This controller is superseded by FlowOperationV2Controller at /api/v2/flow/operations.
 *  Retained only for transitional compile safety and legacy client backward-compatibility.
 *  Scheduled for removal: Phase 8 cleanup.
 *  DO NOT add new endpoints here.
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.core.dto.FlowOperationDTO;
import dz.sh.trc.hyflo.flow.core.service.FlowOperationService;
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

/**
 * @deprecated Superseded by FlowOperationV2Controller (/api/v2/flow/operations).
 * Retained for transitional compile safety. Scheduled for removal in Phase 8 cleanup.
 */
@Deprecated(since = "v2-phase4", forRemoval = true)
@RestController
@RequestMapping("/flow/core/operation")
@Tag(name = "Flow Operation Management (Legacy)", description = "[DEPRECATED] Use /api/v2/flow/operations instead.")
@SecurityRequirement(name = "bearer-auth")
@Slf4j
public class FlowOperationController extends GenericController<FlowOperationDTO, Long> {

    private final FlowOperationService flowOperationService;

    public FlowOperationController(FlowOperationService flowOperationService) {
        super(flowOperationService, "FlowOperation");
        this.flowOperationService = flowOperationService;
    }

    @Override
    @Operation(summary = "[DEPRECATED] Get flow operation by ID — use /api/v2/flow/operations/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Flow operation found", content = @Content(schema = @Schema(implementation = FlowOperationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Flow operation not found"),
        @ApiResponse(responseCode = "403", description = "Access denied - requires FLOW_OPERATION:READ authority")
    })
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<FlowOperationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Get all flow operations (paginated) — use /api/v2/flow/operations")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Page<FlowOperationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Get all flow operations (unpaginated)")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<List<FlowOperationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "[DEPRECATED] Create flow operation — use /api/v2/flow/operations")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:WRITE')")
    public ResponseEntity<FlowOperationDTO> create(@Valid @RequestBody FlowOperationDTO dto) {
        FlowOperationDTO created = flowOperationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Update flow operation — use /api/v2/flow/operations/{id}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:WRITE')")
    public ResponseEntity<FlowOperationDTO> update(@PathVariable Long id, @Valid @RequestBody FlowOperationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Delete flow operation — use /api/v2/flow/operations/{id}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Search flow operations")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Page<FlowOperationDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Check if flow operation exists")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "[DEPRECATED] Count flow operations")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by date")
    public ResponseEntity<List<FlowOperationDTO>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.debug("GET /flow/core/operation/date/{} - legacy endpoint", date);
        return ResponseEntity.ok(flowOperationService.findByDate(date));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by date range")
    public ResponseEntity<List<FlowOperationDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.debug("GET /flow/core/operation/date-range - legacy endpoint");
        return ResponseEntity.ok(flowOperationService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by infrastructure")
    public ResponseEntity<List<FlowOperationDTO>> getByInfrastructure(@PathVariable Long infrastructureId) {
        log.debug("GET /flow/core/operation/infrastructure/{} - legacy endpoint", infrastructureId);
        return ResponseEntity.ok(flowOperationService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by product")
    public ResponseEntity<List<FlowOperationDTO>> getByProduct(@PathVariable Long productId) {
        log.debug("GET /flow/core/operation/product/{} - legacy endpoint", productId);
        return ResponseEntity.ok(flowOperationService.findByProduct(productId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by type")
    public ResponseEntity<List<FlowOperationDTO>> getByType(@PathVariable Long typeId) {
        log.debug("GET /flow/core/operation/type/{} - legacy endpoint", typeId);
        return ResponseEntity.ok(flowOperationService.findByType(typeId));
    }

    @GetMapping("/validation-status/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by validation status")
    public ResponseEntity<List<FlowOperationDTO>> getByValidationStatus(@PathVariable Long statusId) {
        log.debug("GET /flow/core/operation/validation-status/{} - legacy endpoint", statusId);
        return ResponseEntity.ok(flowOperationService.findByValidationStatus(statusId));
    }

    @GetMapping("/infrastructure/{infrastructureId}/date-range")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "[DEPRECATED] Get operations by infrastructure and date range")
    public ResponseEntity<Page<FlowOperationDTO>> getByInfrastructureAndDateRange(
            @PathVariable Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.debug("GET /flow/core/operation/infrastructure/{}/date-range - legacy endpoint", infrastructureId);
        return ResponseEntity.ok(flowOperationService.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:MANAGE')")
    @Operation(summary = "[DEPRECATED] Validate a flow operation")
    public ResponseEntity<FlowOperationDTO> validate(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request) {
        Long validatorId = request.get("validatedById");
        if (validatorId == null) {
            throw new IllegalArgumentException("validatorId is required in request body");
        }
        log.debug("POST /flow/core/operation/{}/validate - legacy endpoint", id);
        FlowOperationDTO validated = flowOperationService.validate(id, validatorId);
        return ResponseEntity.ok(validated);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:VALIDATE')")
    @Operation(summary = "[DEPRECATED] Reject a flow operation")
    public ResponseEntity<FlowOperationDTO> reject(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Long validatorId = Long.parseLong(request.get("validatorId"));
        String rejectionReason = request.get("rejectionReason");
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new IllegalArgumentException("rejectionReason is required in request body");
        }
        log.debug("POST /flow/core/operation/{}/reject - legacy endpoint", id);
        FlowOperationDTO rejected = flowOperationService.reject(id, validatorId, rejectionReason);
        return ResponseEntity.ok(rejected);
    }
}
