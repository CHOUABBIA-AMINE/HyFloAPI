/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingWorkflowController
 *  @CreatedOn  : (legacy)
 *
 *  @Type       : Class
 *  @Layer      : Controller
 *  @Package    : Flow / Workflow
 *
 *  @Deprecated Phase 4 — Commit 29
 *  This controller is superseded by ReadingWorkflowV2Controller at /api/v2/flow/workflow.
 *  Active v2 endpoints are served from ReadingWorkflowV2Controller.
 *  This class is retained for transitional compile safety.
 *  Scheduled for removal: Phase 8 cleanup.
 *  DO NOT add new endpoints here.
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.controller;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.workflow.dto.ReadingSubmitRequestDTO;
import dz.sh.trc.hyflo.flow.workflow.dto.ReadingValidationRequestDTO;
import dz.sh.trc.hyflo.flow.workflow.service.ReadingWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @deprecated Superseded by ReadingWorkflowV2Controller.
 * Retained for transitional compile safety.
 * Scheduled for removal in Phase 8 cleanup.
 */
@Deprecated(since = "v2-phase4", forRemoval = true)
@RestController
@RequestMapping("/flow/workflow/reading")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reading Workflow (Legacy)", description = "[DEPRECATED] Use /api/v2/flow/workflow instead.")
@SecurityRequirement(name = "bearer-auth")
public class ReadingWorkflowController {

    private final ReadingWorkflowService workflowService;

    /**
     * @deprecated Phase 4. Delegates to deprecated validate() stub in ReadingWorkflowService.
     * Use POST /api/v2/flow/workflow/readings/{id}/approve instead.
     */
    @Deprecated(since = "v2-phase4", forRemoval = true)
    @PostMapping("/validate")
    @PreAuthorize("hasAuthority('FLOW:VALIDATE')")
    @Operation(summary = "[DEPRECATED] Validate reading — use /api/v2/flow/workflow/readings/{id}/approve")
    public ResponseEntity<FlowReadingDTO> validateReading(
            @Valid @RequestBody ReadingValidationRequestDTO request) {
        log.warn("DEPRECATED: POST /flow/workflow/reading/validate called. Migrate to v2 endpoint.");
        FlowReadingDTO result = workflowService.validate(request.getReadingId(), request.getEmployeeId());
        return ResponseEntity.ok(result);
    }
}
