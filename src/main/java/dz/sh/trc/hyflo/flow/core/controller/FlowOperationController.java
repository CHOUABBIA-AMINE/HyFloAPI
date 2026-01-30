/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowOperationController
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-31-2026 - Added validate and reject endpoints
 *
 * 	@Type		: Class
 * 	@Layer		: Controller
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/core/operation")
@Tag(name = "Flow Operations", description = "API for managing flow operations (production, transportation, consumption)")
@Slf4j
public class FlowOperationController extends GenericController<FlowOperationDTO, Long> {

    private final FlowOperationService flowOperationService;
    
    public FlowOperationController(FlowOperationService flowOperationService) {
        super(flowOperationService, "FlowOperation");
        this.flowOperationService = flowOperationService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<FlowOperationDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Page<FlowOperationDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<List<FlowOperationDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:WRITE')")
    public ResponseEntity<FlowOperationDTO> create(@Valid @RequestBody FlowOperationDTO dto) {
        return super.create(dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:WRITE')")
    public ResponseEntity<FlowOperationDTO> update(@PathVariable Long id, @Valid @RequestBody FlowOperationDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
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
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM QUERY ENDPOINTS ==========

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by date", description = "Retrieve all flow operations for a specific date")
    public ResponseEntity<List<FlowOperationDTO>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET /flow/core/operation/date/{} - Getting operations by date", date);
        return ResponseEntity.ok(flowOperationService.findByDate(date));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by date range", description = "Retrieve flow operations within a date range")
    public ResponseEntity<List<FlowOperationDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /flow/core/operation/date-range - Getting operations from {} to {}", startDate, endDate);
        return ResponseEntity.ok(flowOperationService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/infrastructure/{infrastructureId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by infrastructure", description = "Retrieve all flow operations for a specific infrastructure")
    public ResponseEntity<List<FlowOperationDTO>> getByInfrastructure(@PathVariable Long infrastructureId) {
        log.info("GET /flow/core/operation/infrastructure/{} - Getting operations by infrastructure", infrastructureId);
        return ResponseEntity.ok(flowOperationService.findByInfrastructure(infrastructureId));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by product", description = "Retrieve all flow operations for a specific product")
    public ResponseEntity<List<FlowOperationDTO>> getByProduct(@PathVariable Long productId) {
        log.info("GET /flow/core/operation/product/{} - Getting operations by product", productId);
        return ResponseEntity.ok(flowOperationService.findByProduct(productId));
    }

    @GetMapping("/type/{typeId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by type", description = "Retrieve all flow operations of a specific type")
    public ResponseEntity<List<FlowOperationDTO>> getByType(@PathVariable Long typeId) {
        log.info("GET /flow/core/operation/type/{} - Getting operations by type", typeId);
        return ResponseEntity.ok(flowOperationService.findByType(typeId));
    }

    @GetMapping("/validation-status/{statusId}")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by validation status", description = "Retrieve all flow operations with a specific validation status")
    public ResponseEntity<List<FlowOperationDTO>> getByValidationStatus(@PathVariable Long statusId) {
        log.info("GET /flow/core/operation/validation-status/{} - Getting operations by validation status", statusId);
        return ResponseEntity.ok(flowOperationService.findByValidationStatus(statusId));
    }

    @GetMapping("/infrastructure/{infrastructureId}/date-range")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:READ')")
    @Operation(summary = "Get operations by infrastructure and date range", 
               description = "Retrieve paginated flow operations for specific infrastructure within date range")
    public ResponseEntity<Page<FlowOperationDTO>> getByInfrastructureAndDateRange(
            @PathVariable Long infrastructureId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("GET /flow/core/operation/infrastructure/{}/date-range - Getting operations from {} to {}", 
                 infrastructureId, startDate, endDate);
        return ResponseEntity.ok(flowOperationService.findByInfrastructureAndDateRange(
                infrastructureId, startDate, endDate, buildPageable(page, size, sortBy, sortDir)));
    }

    // ========== VALIDATION ENDPOINTS ==========

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:VALIDATE')")
    @Operation(
        summary = "Validate a flow operation",
        description = "Approve a PENDING flow operation, changing its status to VALIDATED. " +
                      "Only operations in PENDING status can be validated."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Operation validated successfully",
            content = @Content(schema = @Schema(implementation = FlowOperationDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - operation is not in PENDING status"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Operation or validator not found"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Insufficient permissions to validate operations"
        )
    })
    public ResponseEntity<FlowOperationDTO> validate(
            @Parameter(description = "ID of the flow operation to validate", required = true)
            @PathVariable Long id,
            @Parameter(description = "Request body containing validator employee ID", required = true)
            @RequestBody Map<String, Long> request) {
        
        Long validatorId = request.get("validatorId");
        if (validatorId == null) {
            throw new IllegalArgumentException("validatorId is required in request body");
        }
        
        log.info("POST /flow/core/operation/{}/validate - Validating operation, validatorId={}", id, validatorId);
        FlowOperationDTO validated = flowOperationService.validate(id, validatorId);
        return ResponseEntity.ok(validated);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('FLOW_OPERATION:VALIDATE')")
    @Operation(
        summary = "Reject a flow operation",
        description = "Reject a PENDING flow operation with a reason, changing its status to REJECTED. " +
                      "Only operations in PENDING status can be rejected. " +
                      "Rejection reason is appended to operation notes."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Operation rejected successfully",
            content = @Content(schema = @Schema(implementation = FlowOperationDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - operation is not in PENDING status or rejection reason is missing"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Operation or validator not found"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Insufficient permissions to reject operations"
        )
    })
    public ResponseEntity<FlowOperationDTO> reject(
            @Parameter(description = "ID of the flow operation to reject", required = true)
            @PathVariable Long id,
            @Parameter(description = "Request body containing validator ID and rejection reason", required = true)
            @RequestBody Map<String, String> request) {
        
        Long validatorId = Long.parseLong(request.get("validatorId"));
        String rejectionReason = request.get("rejectionReason");
        
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new IllegalArgumentException("rejectionReason is required in request body");
        }
        
        log.info("POST /flow/core/operation/{}/reject - Rejecting operation, validatorId={}", id, validatorId);
        FlowOperationDTO rejected = flowOperationService.reject(id, validatorId, rejectionReason);
        return ResponseEntity.ok(rejected);
    }
}
