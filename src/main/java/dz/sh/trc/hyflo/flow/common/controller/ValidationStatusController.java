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
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.configuration.template.GenericController;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.service.ValidationStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
