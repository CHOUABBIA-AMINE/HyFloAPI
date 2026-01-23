/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: AlertStatusController
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
import dz.sh.trc.hyflo.flow.common.dto.AlertStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.AlertStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/flow/common/alertstatus")
@Tag(name = "Alert Status", description = "Flow alert status management API")
@Slf4j
public class AlertStatusController extends GenericController<AlertStatusDTO, Long> {

    private final AlertStatusService alertStatusService;
    
    public AlertStatusController(AlertStatusService alertStatusService) {
        super(alertStatusService, "AlertStatus");
        this.alertStatusService = alertStatusService;
    }

    // ========== SECURED CRUD OPERATIONS ==========

    @Override
    @Operation(summary = "Get alert status by ID", description = "Retrieve a single alert status by its unique identifier")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<AlertStatusDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @Override
    @Operation(summary = "Get all alert statuses (paginated)", description = "Retrieve all alert statuses with pagination and sorting")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Page<AlertStatusDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.getAll(page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Get all alert statuses (no pagination)", description = "Retrieve all alert statuses sorted by code")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<List<AlertStatusDTO>> getAll() {
        return super.getAll();
    }

    @Override
    @Operation(summary = "Create alert status", description = "Create a new alert status with unique code and French designation validation")
    @PreAuthorize("hasAuthority('ALERT_STATUS:ADMIN')")
    public ResponseEntity<AlertStatusDTO> create(@Valid @RequestBody AlertStatusDTO dto) {
        return super.create(dto);
    }

    @Override
    @Operation(summary = "Update alert status", description = "Update an existing alert status by ID")
    @PreAuthorize("hasAuthority('ALERT_STATUS:ADMIN')")
    public ResponseEntity<AlertStatusDTO> update(@PathVariable Long id, @Valid @RequestBody AlertStatusDTO dto) {
        return super.update(id, dto);
    }

    @Override
    @Operation(summary = "Delete alert status", description = "Delete an alert status by ID")
    @PreAuthorize("hasAuthority('ALERT_STATUS:ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @Override
    @Operation(summary = "Search alert statuses", description = "Search alert statuses across all fields with pagination")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Page<AlertStatusDTO>> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return super.search(q, page, size, sortBy, sortDir);
    }

    @Override
    @Operation(summary = "Check if alert status exists", description = "Check if an alert status exists by ID")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return super.exists(id);
    }

    @Override
    @Operation(summary = "Count alert statuses", description = "Get total count of alert statuses")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<Long> count() {
        return super.count();
    }

    // ========== CUSTOM ENDPOINTS ==========

    @GetMapping("/code/{code}")
    @Operation(summary = "Get alert status by code", description = "Find alert status by its unique code (ACTIVE, ACKNOWLEDGED, etc.)")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<AlertStatusDTO> getByCode(@PathVariable String code) {
        log.info("GET /flow/common/alertstatus/code/{} - Getting alert status by code", code);
        return ResponseEntity.ok(alertStatusService.findByCode(code));
    }

    @GetMapping("/designation/{designationFr}")
    @Operation(summary = "Get alert status by French designation", description = "Find alert status by its unique French designation")
    @PreAuthorize("hasAuthority('ALERT_STATUS:READ')")
    public ResponseEntity<AlertStatusDTO> getByDesignationFr(@PathVariable String designationFr) {
        log.info("GET /flow/common/alertstatus/designation/{} - Getting alert status by French designation", designationFr);
        return ResponseEntity.ok(alertStatusService.findByDesignationFr(designationFr));
    }
}
