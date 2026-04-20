package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateValidationStatusRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ValidationStatusSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.ValidationStatus;
import dz.sh.trc.hyflo.core.flow.reference.service.ValidationStatusService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/validation-statuses")
@Tag(name = "ValidationStatus API", description = "Endpoints for managing ValidationStatus")
public class ValidationStatusController extends BaseController<CreateValidationStatusRequest, UpdateValidationStatusRequest, ValidationStatusResponse, ValidationStatusSummary> {

    public ValidationStatusController(ValidationStatusService service) {
        super(service);
    }

    @Override
    protected Page<ValidationStatusSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}