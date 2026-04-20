package dz.sh.trc.hyflo.core.network.topology.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.core.network.topology.dto.request.CreateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.request.UpdateEquipmentRequest;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentResponse;
import dz.sh.trc.hyflo.core.network.topology.dto.response.EquipmentSummary;
import dz.sh.trc.hyflo.core.network.topology.model.Equipment;
import dz.sh.trc.hyflo.core.network.topology.service.EquipmentService;
import dz.sh.trc.hyflo.platform.kernel.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/network/equipments")
@Tag(name = "Equipment API", description = "Endpoints for managing Equipment")
public class EquipmentController extends BaseController<CreateEquipmentRequest, UpdateEquipmentRequest, EquipmentResponse, EquipmentSummary> {

    public EquipmentController(EquipmentService service) {
        super(service);
    }

    @Override
    protected Page<EquipmentSummary> performSearch(String search, Pageable pageable) {
        throw new UnsupportedOperationException("Search not implemented");
    }
}