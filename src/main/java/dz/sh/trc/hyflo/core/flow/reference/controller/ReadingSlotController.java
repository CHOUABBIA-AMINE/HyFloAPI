package dz.sh.trc.hyflo.core.flow.reference.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.flow.reference.dto.request.CreateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.request.UpdateReadingSlotRequest;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotResponse;
import dz.sh.trc.hyflo.core.flow.reference.dto.response.ReadingSlotSummary;
import dz.sh.trc.hyflo.core.flow.reference.service.ReadingSlotService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/flow/reading-slots")
@Tag(name = "ReadingSlot API", description = "Endpoints for managing ReadingSlot")
public class ReadingSlotController extends BaseController<CreateReadingSlotRequest, UpdateReadingSlotRequest, ReadingSlotResponse, ReadingSlotSummary> {

    public ReadingSlotController(ReadingSlotService service) {
        super(service);
    }

    @Override
    protected Page<ReadingSlotSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}