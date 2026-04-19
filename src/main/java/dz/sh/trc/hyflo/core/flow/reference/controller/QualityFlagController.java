package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateQualityFlagRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.QualityFlagSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.QualityFlag;
import dz.sh.trc.hyflo.core.flow.reference.service.QualityFlagService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/quality-flags")
@Tag(name = "QualityFlag API", description = "Endpoints for managing QualityFlag")
public class QualityFlagController extends BaseController<CreateQualityFlagRequest, UpdateQualityFlagRequest, QualityFlagResponse, QualityFlagSummary, QualityFlag, Long> {

    public QualityFlagController(QualityFlagService service) {
        super(service);
    }

    @Override
    protected Page<QualityFlagSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}