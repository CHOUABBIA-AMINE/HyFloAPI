package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSourceNatureRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSourceNatureSummary;
import dz.sh.trc.hyflo.core.flow.reference.model.ReadingSourceNature;
import dz.sh.trc.hyflo.core.flow.reference.service.ReadingSourceNatureService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/reading-source-natures")
@Tag(name = "ReadingSourceNature API", description = "Endpoints for managing ReadingSourceNature")
public class ReadingSourceNatureController extends BaseController<CreateReadingSourceNatureRequest, UpdateReadingSourceNatureRequest, ReadingSourceNatureResponse, ReadingSourceNatureSummary, ReadingSourceNature, Long> {

    public ReadingSourceNatureController(ReadingSourceNatureService service) {
        super(service);
    }

    @Override
    protected Page<ReadingSourceNatureSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}