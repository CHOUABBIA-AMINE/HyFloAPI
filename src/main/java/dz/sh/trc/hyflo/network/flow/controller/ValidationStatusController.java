/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ValidationStatusController
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Controller
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.controller;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.network.flow.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.network.flow.service.ValidationStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hyflo/api/network/flow/validation-statuses")
@Tag(name = "Validation Statuses", description = "Flow reading validation statuses management")
public class ValidationStatusController extends GenericController<ValidationStatusDTO, Long> {
    
    private final ValidationStatusService validationStatusService;
    
    public ValidationStatusController(ValidationStatusService validationStatusService) {
        super(validationStatusService, "ValidationStatus");
        this.validationStatusService = validationStatusService;
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Get validation status by code")
    public ResponseEntity<ValidationStatusDTO> getByCode(@PathVariable String code) {
        return validationStatusService.findByCode(code)
                .map(this::success)
                .orElse(notFound());
    }
}
